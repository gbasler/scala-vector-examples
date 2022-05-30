package example

import jdk.incubator.vector.DoubleVector
import jdk.incubator.vector.VectorOperators
import jdk.incubator.vector.VectorSpecies
import org.openjdk.jmh.annotations._
import java.util.Objects
import java.util.Random
import java.util.concurrent.TimeUnit
import java.util.function.IntUnaryOperator

// jmh:run .*BlackScholesDouble.*

@BenchmarkMode(Array(Mode.Throughput))
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Warmup(iterations = 3, time = 5)
@Measurement(iterations = 3, time = 5)
@Fork(value = 1, jvmArgsPrepend = Array("--add-modules=jdk.incubator.vector"))
class BlackScholesScalaDouble {
  private[this] val Y = 0.2316419
  private[this] val A1 = 0.31938153
  private[this] val A2 = -0.356563782
  private[this] val A3 = 1.781477937
  private[this] val A4 = -1.821255978
  private[this] val A5 = 1.330274429
  private[this] val PI = Math.PI
  private[this] val fsp = DoubleVector.SPECIES_PREFERRED

  @Param(Array("1024"))
  private[this] var size = 0

  private[this] var s0: Array[Double] = _ // Stock Price

  private[this] var x: Array[Double] = _ // Strike Price

  private[this] var t: Array[Double] = _ // Maturity

  private[this] var call: Array[Double] = _
  private[this] var put: Array[Double] = _
  private[this] var r: Double = .0 // risk-neutrality

  private[this] var sig: Double = .0 // volatility

  private[this] var rand: Random = _

  private[this] def randDouble(low: Double, high: Double) = {
    val `val` = rand.nextDouble
    (1.0f - `val`) * low + `val` * high
  }

  private[this] def fillRandom(low: Double, high: Double) = {
    val array = new Array[Double](size)
    var i = 0
    while(i < array.length) {
      array(i) = randDouble(low, high)
      i += 1
    }
    array
  }

  @Setup def init(): Unit = {
    rand = new Random
    s0 = fillRandom(5.0, 30.0)
    x = fillRandom(1.0, 100.0)
    t = fillRandom(0.25, 10.0)
    r = 0.02
    sig = 0.30
    call = new Array[Double](size)
    put = new Array[Double](size)
  }

  private[this] def cdf(inp: Double) = {
    var x = inp
    if (inp < 0.0) x = -inp
    val term = 1.0 / (1.0 + (Y * x))
    val term_pow2 = term * term
    val term_pow3 = term_pow2 * term
    val term_pow4 = term_pow2 * term_pow2
    val term_pow5 = term_pow2 * term_pow3
    val part1 = (1.0 / Math.sqrt(2.0 * PI)) * Math.exp((-x * x) * 0.5)
    val part2 = (A1 * term) + (A2 * term_pow2) + (A3 * term_pow3) + (A4 * term_pow4) + (A5 * term_pow5)
    if (inp >= 0.0) 1.0 - part1 * part2
    else part1 * part2
  }

  def scalar_black_scholes_kernel(off: Int): Unit = {
    val sig_sq_by2 = 0.5f * sig * sig
    var i = off
    while(i < size) {
      val log_s0byx = Math.log(s0(i) / x(i))
      val sig_sqrt_t = sig * Math.sqrt(t(i))
      val exp_neg_rt = Math.exp(-r * t(i))
      val d1 = (log_s0byx + (r + sig_sq_by2) * t(i)) / sig_sqrt_t
      val d2 = d1 - sig_sqrt_t
      call(i) = s0(i) * cdf(d1) - exp_neg_rt * x(i) * cdf(d2)
      put(i) = call(i) + exp_neg_rt - s0(i)
      i += 1
    }
  }

  @Benchmark def scalar_black_scholes(): Unit = {
    scalar_black_scholes_kernel(0)
  }

  private[this] def vcdf(vinp: DoubleVector) = {
    val vx = vinp.abs
    val vone = DoubleVector.broadcast(fsp, 1.0)
    val vtwo = DoubleVector.broadcast(fsp, 2.0)
    val vterm = vone.div(vone.add(vx.mul(Y)))
    val vterm_pow2 = vterm.mul(vterm)
    val vterm_pow3 = vterm_pow2.mul(vterm)
    val vterm_pow4 = vterm_pow2.mul(vterm_pow2)
    val vterm_pow5 = vterm_pow2.mul(vterm_pow3)
    val vpart1 = vone.div(vtwo.mul(PI).lanewise(VectorOperators.SQRT)).mul(vx.mul(vx).neg.lanewise(VectorOperators.EXP).mul(0.5))
    val vpart2 = vterm.mul(A1).add(vterm_pow2.mul(A2)).add(vterm_pow3.mul(A3)).add(vterm_pow4.mul(A4)).add(vterm_pow5.mul(A5))
    val vmask = vinp.compare(VectorOperators.GT, 0.0)
    val vresult1 = vpart1.mul(vpart2)
    val vresult2 = vresult1.neg.add(vone)
    val vresult = vresult1.blend(vresult2, vmask)
    vresult
  }

  def vector_black_scholes_kernel: Int = {
    var i = 0
    val vsig = DoubleVector.broadcast(fsp, sig)
    val vsig_sq_by2 = vsig.mul(vsig).mul(0.5)
    val vr = DoubleVector.broadcast(fsp, r)
    val vnegr = DoubleVector.broadcast(fsp, -r)

    while(i < x.length - fsp.length) {
      val vx = DoubleVector.fromArray(fsp, x, i)
      val vs0 = DoubleVector.fromArray(fsp, s0, i)
      val vt = DoubleVector.fromArray(fsp, t, i)
      val vlog_s0byx = vs0.div(vx).lanewise(VectorOperators.LOG)
      val vsig_sqrt_t = vt.lanewise(VectorOperators.SQRT).mul(vsig)
      val vexp_neg_rt = vt.mul(vnegr).lanewise(VectorOperators.EXP)
      val vd1 = vsig_sq_by2.add(vr).mul(vt).add(vlog_s0byx).div(vsig_sqrt_t)
      val vd2 = vd1.sub(vsig_sqrt_t)
      val vcall = vs0.mul(vcdf(vd1)).sub(vx.mul(vexp_neg_rt).mul(vcdf(vd2)))
      val vput = vcall.add(vexp_neg_rt).sub(vs0)
      vcall.intoArray(call, i)
      vput.intoArray(put, i)

      i += fsp.length
    }
    i
  }

  @Benchmark def vector_black_scholes(): Unit = {
    val processed = vector_black_scholes_kernel
    if (processed < size) scalar_black_scholes_kernel(processed)
  }
}

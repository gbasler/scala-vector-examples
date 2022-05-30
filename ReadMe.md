````
OpenJDK 19-ea: Build 24 & i7 8700K:
[info] Benchmark                                     (size)   Mode  Cnt       Score        Error  Units
[info] BlackScholes.scalar_black_scholes               1024  thrpt    3   17906.254 ±    156.083  ops/s
[info] BlackScholes.vector_black_scholes               1024  thrpt    3  130799.335 ± 103914.998  ops/s
[info] BlackScholesJavaDouble.scalar_black_scholes     1024  thrpt    3   22177.672 ±     32.099  ops/s
[info] BlackScholesJavaDouble.vector_black_scholes     1024  thrpt    3   59092.188 ±  27213.164  ops/s
[info] BlackScholesScalaDouble.scalar_black_scholes    1024  thrpt    3   21637.594 ±   1843.349  ops/s
[info] BlackScholesScalaDouble.vector_black_scholes    1024  thrpt    3   33615.223 ±   7743.492  ops/s
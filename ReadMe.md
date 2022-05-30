````
OpenJDK 19-ea: Build 24 & i7 8700K:
[info] Benchmark                                (size)   Mode  Cnt       Score        Error  Units
[info] BlackScholes.scalar_black_scholes          1024  thrpt    3   18090.776 ±   1420.474  ops/s
[info] BlackScholes.vector_black_scholes          1024  thrpt    3  122222.904 ± 154456.168  ops/s
[info] BlackScholesDouble.scalar_black_scholes    1024  thrpt    3   21720.336 ±   2968.612  ops/s
[info] BlackScholesDouble.vector_black_scholes    1024  thrpt    3   34959.203 ±   5605.386  ops/s

Azul Systems, Inc. Java 17.0.1 & i7 8700K:
[info] Benchmark                                (size)   Mode  Cnt       Score       Error  Units
[info] BlackScholes.scalar_black_scholes          1024  thrpt    3   16722.118 ±  7324.678  ops/s
[info] BlackScholes.vector_black_scholes          1024  thrpt    3  126314.048 ± 23425.337  ops/s
[info] BlackScholesDouble.scalar_black_scholes    1024  thrpt    3   20652.024 ±  1876.097  ops/s
[info] BlackScholesDouble.vector_black_scholes    1024  thrpt    3   35123.654 ±  1482.909  ops/s

Azul Systems, Inc. Java 18.0.1 & i7 8700K:
[info] Benchmark                                (size)   Mode  Cnt       Score       Error  Units
[info] BlackScholes.scalar_black_scholes          1024  thrpt    3   16937.713 ±  4322.945  ops/s
[info] BlackScholes.vector_black_scholes          1024  thrpt    3  141390.153 ± 96289.543  ops/s
[info] BlackScholesDouble.scalar_black_scholes    1024  thrpt    3   21926.126 ±   330.043  ops/s
[info] BlackScholesDouble.vector_black_scholes    1024  thrpt    3   35826.052 ±  1170.350  ops/s

GraalVM Community Java 17.0.3 & i7 8700K:
[info] Benchmark                                (size)   Mode  Cnt      Score      Error  Units
[info] BlackScholes.scalar_black_scholes          1024  thrpt    3  19482.251 ±  827.275  ops/s
[info] BlackScholes.vector_black_scholes          1024  thrpt    3   4258.228 ±  541.321  ops/s
[info] BlackScholesDouble.scalar_black_scholes    1024  thrpt    3  21496.256 ± 1121.358  ops/s
[info] BlackScholesDouble.vector_black_scholes    1024  thrpt    3   3942.520 ±  309.107  ops/s

GraalVM Community Java 17.0.3 & Intel Gold 6238:
[info] Benchmark                                (size)   Mode  Cnt      Score      Error  Units
[info] BlackScholes.scalar_black_scholes          1024  thrpt    3  15211.035 ± 2280.045  ops/s
[info] BlackScholes.vector_black_scholes          1024  thrpt    3   4631.071 ±  414.479  ops/s
[info] BlackScholesDouble.scalar_black_scholes    1024  thrpt    3  16562.472 ±  433.358  ops/s
[info] BlackScholesDouble.vector_black_scholes    1024  thrpt    3   3895.225 ±  865.861  ops/s
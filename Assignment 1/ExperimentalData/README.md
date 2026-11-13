# Experimental data
`lookup_benchmark_results.csv` contains measured Java execution times from the included `LookupBenchmark.java` program. It compares worst-case linear search in a String array with HashMap lookup for registration IDs at five dataset sizes. Each measurement is averaged over 20,000 operations after three warm-up passes for the linear search.

These are one-run local JVM measurements, not universal performance guarantees. Nanosecond timings are sensitive to JVM warm-up, CPU load, runtime version and hardware. Repeat the experiment several times and report medians for stronger evidence. The benchmark supports the Task 11 alternative data-structure discussion; it is not a measurement of the full parking application.

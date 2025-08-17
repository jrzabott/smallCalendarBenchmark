**Project Description**

This repository contains a Java benchmarking project using JMH (Java Microbenchmark Harness) to compare different approaches for creating objects based on calendar and date/time APIs. The benchmarks evaluate performance differences between legacy `Calendar`, the modern Java Time API, and a memoization-based approach. The project is built with Gradle and includes JMH integration for accurate microbenchmarking, as well as sample benchmarks and profiling support.

**Key Features:**
- JMH-based microbenchmarks for Java date/time object creation
- Comparison of legacy and modern Java APIs
- Memoization technique benchmarking
- Gradle build with proper annotation processing for JMH
- Ready-to-run benchmarks with profiling support

**How to Run:**
1. Clone the repository.
2. Run `./gradlew jmh` to execute the benchmarks.
3. Review the results in the generated reports.

**Requirements:**
- Java 17 or higher
- Gradle 8.x or higher

This project is intended for developers interested in Java performance analysis and benchmarking best practices.

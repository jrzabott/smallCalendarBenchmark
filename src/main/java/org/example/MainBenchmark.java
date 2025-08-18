package org.example;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.profile.JavaFlightRecorderProfiler;
import org.openjdk.jmh.profile.MemPoolProfiler;
import org.openjdk.jmh.profile.PausesProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
@BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SampleTime})
@State(Scope.Benchmark)
@OutputTimeUnit(java.util.concurrent.TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, batchSize = 5_000)
@Fork(value = 2)
public class MainBenchmark {

    int number;
    int year;
    int month;

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .addProfiler(JavaFlightRecorderProfiler.class)
                .addProfiler(GCProfiler.class)
                .addProfiler(PausesProfiler.class)
                .addProfiler(MemPoolProfiler.class)
                .include(MainBenchmark.class.getName())
                .build();
        new Runner(options).run();
    }

    @Setup(Level.Invocation)
    public void setup() {
        number = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
        year = ThreadLocalRandom.current().nextInt(1970, 2100);
        month = ThreadLocalRandom.current().nextInt(1, 13);
    }

    @Benchmark
    public void benchmarkCreateAnObjViaCalendar(Blackhole blackhole) {
        blackhole.consume(Main.createAnObjViaCalendar(number, year, month));
    }

    @Benchmark
    public void benchmarkCreateAnObjViaNewJavaTimeAPI(Blackhole blackhole) {
        blackhole.consume(Main.createAnObjViaNewJavaTimeAPI(number, year, month));
    }

    @Benchmark
    public void benchmarkCreateAndObjUsingMemoization(Blackhole blackhole) {
        blackhole.consume(Main.createAndObjUsingMemoization(number, year, month));
    }

    @Benchmark
    public void benchmarkCreateAnObjNewJavaTimeAPIMinusNano(Blackhole blackhole) {
        blackhole.consume(Main.createAnObjNewJavaTimeAPIMinusNano(number, year, month));
    }
}

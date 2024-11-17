package src.main;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkRunner {
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(InvertedIndexBenchmark.class.getSimpleName())  // Incluir solo nuestro benchmark
                .forks(1)  // Número de forks (recomendado al menos 1 para resultados confiables)
                .warmupIterations(2)  // Iteraciones de calentamiento para estabilizar el rendimiento
                .measurementIterations(10)  // Número de iteraciones de medición
                .build();

        new Runner(opt).run();  // Ejecuta el benchmark con las opciones configuradas
    }
}
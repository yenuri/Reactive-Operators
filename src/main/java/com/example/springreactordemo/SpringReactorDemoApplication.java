package com.example.springreactordemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringReactorDemoApplication implements CommandLineRunner {

    public static final Logger log = LoggerFactory.getLogger(SpringReactorDemoApplication.class);

    public static List<String> plates = new ArrayList<>();

    public static void main(String[] args) {
        plates.add("Burger");
        plates.add("Pizza");
        plates.add("Soda");
        SpringApplication.run(SpringReactorDemoApplication.class, args);
    }

    public void createMono() {
        Mono.just(31).subscribe(e -> log.info("Number: " + e));
    }

    public void createFlux() {
        Flux.fromIterable(plates)
                .collectList()
                .subscribe(lista -> log.info(lista.toString()));
    }

    public void m1doOnNext() {
        Flux<String> fxPlates = Flux.fromIterable(plates);
        fxPlates
                .doOnNext(p -> log.info(p))
                .subscribe();
    }

    public void m2map() {
        Flux<String> fxPlates = Flux.fromIterable(plates);
        fxPlates.map(p -> "PLate: " + p)
                .subscribe(p -> log.info(p));
    }

    public void m3flatMap() {
        Mono.just("Yenuri")
                .flatMap(e -> Mono.just(27))
                .subscribe(p -> log.info("age: " + p));
    }

    public void m4range() {
        Flux<Integer> fx1 = Flux.range(0, 10);
        fx1.map(x -> x + 1)
                .subscribe(x -> log.info(x.toString()));
    }

    public void m5delayElements() throws InterruptedException {
        Flux.range(0, 10)
                .delayElements(Duration.ofSeconds(19))
                .doOnNext(p -> log.info(p.toString()))
                .subscribe();

        Thread.sleep(20000);
    }

    public void m6zipWith() {
        List<String> customers = new ArrayList<>();
        customers.add("Yenuri");
        customers.add("Nayra");
        //customers.add("Betty");
        Flux<String> fxPlatos = Flux.fromIterable(plates);
        Flux<String> fxCustomers = Flux.fromIterable(customers);
        fxPlatos
                .zipWith(fxCustomers, (p, c) -> String.format("Flux1: %s, Flux2: %s", p, c))
                .subscribe(x -> log.info(x));
    }

    public void m7merge() {
        List<String> customers = new ArrayList<>();
        customers.add("Yenuri");
        customers.add("Nayra");
        Flux<String> fxPlatos = Flux.fromIterable(plates);
        Flux<String> fxCustomers = Flux.fromIterable(customers);

        Flux.merge(fxPlatos, fxCustomers, fxPlatos)
                .subscribe(x -> log.info(x));
    }

    public void m8filter() {
        Flux<String> fxPlates = Flux.fromIterable(plates);
        fxPlates
                .filter(p -> p.startsWith("B"))
                .subscribe(p -> log.info(p));
    }

    public void m9takeLast() {
        Flux<String> fxPlates = Flux.fromIterable(plates);
        fxPlates
                .takeLast(4)
                .subscribe(p -> log.info(p));
    }

    public void m10take() {
        Flux<String> fxPlates = Flux.fromIterable(plates);
        fxPlates
                .take(2)
                .subscribe(p -> log.info(p));
    }

    public void m11defaultIfEmpty() {
        plates = new ArrayList<>();
        Flux<String> fxPlates = Flux.fromIterable(plates);
        fxPlates
                .defaultIfEmpty("EMPTY LIST")
                .subscribe(p -> log.info(p));

    }

    public void m12onErrorMap() {
        Flux<String> fxPlates = Flux.fromIterable(plates);
        fxPlates
                .doOnNext(p -> {
                    throw new ArithmeticException("WRONG CALCULATION");
                })
                .onErrorMap(ex -> new Exception(ex.getMessage()))
                .subscribe(x -> log.info(x));
    }

    public void m13retry() {
        Flux<String> fxPlates = Flux.fromIterable(plates);
        fxPlates
                .doOnNext(p -> {
                    log.info("Trying...");
                    throw new ArithmeticException("WRONG CALCULATION");
                })
                .retry(3)
                .onErrorReturn("ERROR")
                .subscribe(x -> log.info(x));
    }

    @Override
    public void run(String... args) throws Exception {
        m13retry();
    }
}

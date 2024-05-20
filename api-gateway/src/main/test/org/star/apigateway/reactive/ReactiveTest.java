package org.star.apigateway.reactive;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class ReactiveTest {
    @Test
    public void test() {
        Flux<String> fluxColors = Flux.just("red", "green", "blue");
        Flux<Character> fluxChars = fluxColors.map((color) -> {
            return color.charAt(0);
        });
        fluxChars.subscribe((character -> {
        }));
    }

    @Test
    void zipExample() {
        Flux<String> fluxFruits = Flux.just("apple", "pear", "plum");
        Flux<String> fluxColors = Flux.just("red", "green", "blue");
        Flux<Integer> fluxAmounts = Flux.just(10, 20, 30);
        Flux.zip(fluxFruits, fluxColors, fluxAmounts).subscribe(pub -> {

        });
    }
    @Test
    public void publishSubscribeExample() {
        Scheduler schedulerA = Schedulers.newParallel("Scheduler A");
        Scheduler schedulerB = Schedulers.newParallel("Scheduler B");
        Scheduler schedulerC = Schedulers.newParallel("Scheduler C");

        Flux.just(1)
                .map(i -> {
                    return i;
                })
                .subscribeOn(schedulerA)
                .map(i -> {
                    return i;
                })
                .publishOn(schedulerB)
                .map(i -> {
                    return i;
                })
                .subscribeOn(schedulerC)
                .map(i -> {
                    return i;
                })
                .publishOn(schedulerA)
                .map(i -> {
                    return i;
                })
                .blockLast();
    }

}

package com.example.demo;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadTest {
    private static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(100);

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/hello1/{idx}";
//        String url = "http://localhost:8080/hello2/{idx}";
//        String url = "http://localhost:8080/hello3/{idx}";

        CyclicBarrier barrier = new CyclicBarrier(100);

        StopWatch main = new StopWatch();
        main.start();

        for (int i = 0; i < 100; i++) {
            executor.submit(() -> {
                int idx = counter.addAndGet(1);

                barrier.await();

                log.info("Thread {}", idx);

                StopWatch sw = new StopWatch();
                sw.start();

                String res = restTemplate.getForObject(url, String.class, idx);

                sw.stop();
                log.info("Elapsed: {} {} {}", idx, sw.getTotalTimeSeconds(), res);
                return null;
            });
        }

        executor.shutdown();
        executor.awaitTermination(100, TimeUnit.SECONDS);

        main.stop();
        log.info("Total: {}", main.getTotalTimeSeconds());

    }
}

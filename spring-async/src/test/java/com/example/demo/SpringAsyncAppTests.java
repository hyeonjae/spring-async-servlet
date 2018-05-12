package com.example.demo;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAsyncAppTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void loadTest1() throws InterruptedException {
		String url = "http://localhost:8080/hello1/{idx}";
		load(url, 100);
	}

	@Test
	public void loadTest2() throws InterruptedException {
		String url = "http://localhost:8080/hello2/{idx}";
		load(url, 100);
	}

	@Test
	public void loadTest3() throws InterruptedException {
		String url = "http://localhost:8080/hello3/{idx}";
		load(url, 100);
	}

	private void load(String url, final int num) throws InterruptedException {
		AtomicInteger counter = new AtomicInteger(0);

		ExecutorService executor = Executors.newFixedThreadPool(num);

		RestTemplate restTemplate = new RestTemplate();

		CyclicBarrier barrier = new CyclicBarrier(num);

		StopWatch main = new StopWatch();
		main.start();

		for (int i = 0; i < num; i++) {
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

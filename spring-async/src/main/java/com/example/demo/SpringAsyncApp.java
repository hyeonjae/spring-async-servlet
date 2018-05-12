package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.Netty4ClientHttpRequestFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class SpringAsyncApp {
    @RestController
    public static class MyController {
		@GetMapping("/hello1/{index}")
		public String hello1(@PathVariable int index) {
			log.info("/hello1/{}", index);

			return "hello " + index;
		}


        private RestTemplate restTemplate = new RestTemplate();

        @GetMapping("/hello2/{index}")
        public String hello2(@PathVariable int index) {
            log.info("/hello2/{}", index);

            restTemplate.getForObject(
                    "http://localhost:8081/service?req={req}", String.class, "hello " + index);
            return "hello " + index;
        }


        private AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate(
                new Netty4ClientHttpRequestFactory(
                        new NioEventLoopGroup(1)));

        @GetMapping("/hello3/{index}")
        public ListenableFuture<ResponseEntity<String>> hello3(@PathVariable int index) {
            log.info("/hello3/{}", index);

            ListenableFuture<ResponseEntity<String>> result = asyncRestTemplate.getForEntity(
                    "http://localhost:8081/service?req={req}", String.class, "hello " + index);
            return result;
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringAsyncApp.class, args);
    }
}

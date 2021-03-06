# spring-async-servlet

## 프로젝트 구성

<img src="/image1.png" width="400">

 - spring-async
    - 8080포트
    - LoadTest 실행하여 100개의 동시 접속 실행
    - tomcat threads: 1개
 - spring-async-remote
    - 8081포트
    - tomcat threads: 512개


## 실행

```bash
# spring-async running 8080 port tomcat server
$ cd spring-async && mvn spring-boot:run
```

```bash
# spring-async-remote running 8081 port tomcat server
$ cd spring-async-remote && mvn spring-boot:run
```

```bash
# loadtest #1
$ cd spring-async && mvn -Dtest=SpringAsyncAppTests#loadTest1 test

# loadtest #2
$ cd spring-async && mvn -Dtest=SpringAsyncAppTests#loadTest2 test

# loadtest #3
$ cd spring-async && mvn -Dtest=SpringAsyncAppTests#loadTest3 test
```


## 참고자료
 - 토비의 봄 TV: https://www.youtube.com/channel/UCcqH2RV1-9ebRBhmN_uaSNg/videos
 - 토비의 봄 TV 9회 스프링 리액티브 프로그래밍 (5) 비동기 RestTemplate과 비동기 MVC/Serlvet: https://www.youtube.com/watch?v=ExUfZkh7Puk

package org.dr.callablefuture.controller;

import org.dr.callablefuture.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
public class TestController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    MyService service;

    @GetMapping("/mytest")
    public String myTest(){
        long start = System.currentTimeMillis()/1000;
        for(int i = 0; i < 100; i++){
            restTemplate.getForObject("https://jsonplaceholder.typicode.com/todos", String.class);
        }
        long end = System.currentTimeMillis()/1000;
        return "success in : " + (end - start) + " s";
    }

    @GetMapping("/myTest")
    public String myCallable() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis()/1000;

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<String>> futureList = new ArrayList<>();

        for(int i = 0; i < 100; i++){
            Future<String> future = executorService.submit(service);
            futureList.add(future);
        }
        for(Future<String> future: futureList){
            future.get();
        }
        long end = System.currentTimeMillis()/1000;
        return "success in : " + (end - start) + " s";
    }
}

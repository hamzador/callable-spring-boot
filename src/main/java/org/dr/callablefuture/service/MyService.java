package org.dr.callablefuture.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

@Component
public class MyService implements Callable<String> {

    @Autowired
    public RestTemplate restTemplate;

    @Override
    public String call() throws Exception {
        return restTemplate.getForObject("https://jsonplaceholder.typicode.com/todos", String.class);
    }
}

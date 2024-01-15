package com.cos.security1;

import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest
public class UserTest {

    @LocalServerPort
    private int port;

    //@Autowired
    //private TestRestTemplate restTemplate;


}

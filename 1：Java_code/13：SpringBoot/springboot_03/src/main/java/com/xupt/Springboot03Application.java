package com.xupt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Springboot03Application {

    public static void main(String[] args)
    {
        SpringApplication ap=new SpringApplication(Springboot03Application.class);
        ap.run(args);

//        SpringApplication.run(Springboot03Application.class, args);
    }

}


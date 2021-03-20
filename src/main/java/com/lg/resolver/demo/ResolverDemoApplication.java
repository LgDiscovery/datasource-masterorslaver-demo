package com.lg.resolver.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class ResolverDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResolverDemoApplication.class, args);
    }

}

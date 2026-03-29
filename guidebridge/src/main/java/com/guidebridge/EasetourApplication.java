package com.Easetour;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Easetour - Tourist Guide Management System
 * Java PBL Project - Spring Boot Application Entry Point
 */
@SpringBootApplication
public class EasetourApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasetourApplication.class, args);
        System.out.println("===========================================");
        System.out.println("  Easetour is running!");
        System.out.println("  Open: http://localhost:8080");
        System.out.println("===========================================");
    }
}

package com.omnia.admin;

import lombok.extern.log4j.Log4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static java.util.Objects.isNull;

@Log4j
@EnableTransactionManagement
@SpringBootApplication
public class AdminApplication {

    public static String configurationFile;

    public static void main(String[] args) {
        if (isNull(args) || args.length != 1) {
            throw new IllegalStateException("Wrong application's arguments");
        }
        configurationFile = args[0];
        SpringApplication.run(AdminApplication.class, args);
    }
}
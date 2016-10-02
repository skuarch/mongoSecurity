package com.example;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MongoSecurityApplication {

    //==========================================================================
    public static void main(String[] args) {
        SpringApplication.run(MongoSecurityApplication.class, args);
    }

    //==========================================================================
    @Bean
    public Jongo jongo() {
        DB db;
        try {
            db = new MongoClient("127.0.0.1", 27017).getDB("test");
        } catch (Exception e) {
            throw new MongoException("Connection error : ", e);
        }
        return new Jongo(db);
    }

    //==========================================================================
    @Bean
    public MongoCollection users() {
        return jongo().getCollection("users");
    }
}

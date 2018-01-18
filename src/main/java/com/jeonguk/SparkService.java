package com.jeonguk;

import static spark.Spark.get;
import static spark.Spark.post;
import spark.Request;
import spark.Response;
import spark.Route;

public class SparkService {

    public static void main(String[] args) {
        get("/posts", (req, rest) -> {
            return "Hello Sparkingly World!";
        });
    }
    
}

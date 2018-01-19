package com.jeonguk;

import com.jeonguk.controller.PostController;

public class SparkService {

    private static PostController postController;

    public static void main(String[] args) {
        init();
    }
    
    private static void init() {
        setPostController(new PostController());
    }

    public static PostController getPostontroller() {
        return postController;
    }

    public static void setPostController(PostController postController) {
        SparkService.postController = postController;
    }
    
}
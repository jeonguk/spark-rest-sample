package com.jeonguk.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.jeonguk.model.Post;

public class PostService {
    
    public static Map<String, Post> posts = new HashMap<>();
    private static final AtomicInteger count = new AtomicInteger(0);
 
    public Post findById(String id) {
        return posts.get(id);
    }
 
    public Post add(String title, String content, List<String> categories) {
        Integer currentId = count.incrementAndGet();
        Post post = new Post(currentId, title, content, categories);
        posts.put(String.valueOf(currentId), post);
        return post;
    }
 
    public Post update(String id, String title, String content, List<String> categories) {
 
        Post post = posts.get(id);
        if (title != null) {
            post.setTitle(title);
        }
 
        if (content != null) {
            post.setContent(content);
        }
        
        if (categories != null) {
            post.setCategories(categories);
        }
        
        posts.put(id, post);
 
        return post;
 
    }
 
    public void delete(String id) {
        posts.remove(id);
    }
 
    public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }
 
    public PostService() {
    }
    
}

package com.jeonguk;

import static spark.Spark.get;
import static spark.Spark.post;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.Data;

public class SparkService {

    private static final int HTTP_BAD_REQUEST = 400;
    
    interface Validable {
        boolean isValid();
    }
    
    @Data
    static class NewPostPayload {
        
        private String title;
        private List<String> categories = new LinkedList<>();
        private String content;
        
        public boolean isValid() {
            return title != null && !title.isEmpty() && !categories.isEmpty();
        }
        
    }
    
    // Using DB
    public static class Model {
        
        private int nextId = 1;
        private Map<Integer, Post> posts = new HashMap<>();
        
        @Data
        class Post {
            private int id;
            private String title;
            private List<String> categories;
            private String content;
        }
        
        public int createPost(String title, String content, List<String> categories) {
            int id = nextId++;
            Post post = new Post();
            post.setId(id);
            post.setTitle(title);
            post.setContent(content);
            post.setCategories(categories);
            posts.put(id, post);
            return id;
        }
        
        public List<Post> getAllPosts() {
            return posts.keySet().stream().sorted().map((id) ->
                                                        posts.get(id)).collect(Collectors.toList());
        }
        
    }
    
    public static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e) {
            throw new RuntimeException("IOException from a StringWriter");
        }
    }
    
    
    public static void main(String[] args) {
        
        Model model = new Model();
               
        get("/posts", (req, res) -> {
            res.status(200);
            res.type("application/json");
            return dataToJson(model.getAllPosts());
        });

        post("/posts", (req, res) -> {
           try {
               ObjectMapper mapper = new ObjectMapper();
               NewPostPayload creation = mapper.readValue(req.body(), NewPostPayload.class);
               if (!creation.isValid()) {
                   res.status(HTTP_BAD_REQUEST);
                   return "";
               }
               int id = model.createPost(creation.getTitle(), creation.getContent(), creation.getCategories());
               res.status(200);
               res.type("application/json");
               return id;
           } catch (JsonParseException jpe) {
               res.status(HTTP_BAD_REQUEST);
               return "";
           }
        });
    }
    
}

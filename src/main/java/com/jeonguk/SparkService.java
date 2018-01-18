package com.jeonguk;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;

import java.io.IOException;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jeonguk.model.Post;
import com.jeonguk.service.PostService;

import lombok.Data;

public class SparkService {

    private static final int HTTP_BAD_REQUEST = 400;
    
    interface Validable {
        boolean isValid();
    }
    
    @Data
    static class NewPostPayload {
        
        private String title;
        private String content;
        private List<String> categories = new LinkedList<>();
        
        public boolean isValid() {
            return title != null && !title.isEmpty() && !categories.isEmpty();
        }
        
    }
    
//    // Using DB
//    public static class Model {
//        
//        private int nextId = 1;
//        private Map<Integer, Post> posts = new HashMap<>();
//        
//        @Data
//        class Post {
//            private int id;
//            private String title;
//            private List<String> categories;
//            private String content;
//        }
//        
//        public int createPost(String title, String content, List<String> categories) {
//            int id = nextId++;
//            Post post = new Post();
//            post.setId(id);
//            post.setTitle(title);
//            post.setContent(content);
//            post.setCategories(categories);
//            posts.put(id, post);
//            return id;
//        }
//        
//        public List<Post> getAllPosts() {
//            return posts.keySet().stream().sorted().map((id) ->
//                                                        posts.get(id)).collect(Collectors.toList());
//        }
//        
//    }
    
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
        
        final PostService postService = new PostService();
        
        // Start embedded server at this port
        port(8080);
 
        // Main Page, welcome
        get("/", (request, response) -> "Welcome");
 
        // POST - Add an post
        post("/posts", (request, response) -> {
            ObjectMapper mapper = new ObjectMapper();
            NewPostPayload creation = mapper.readValue(request.body(), NewPostPayload.class);
            if (!creation.isValid()) {
                response.status(HTTP_BAD_REQUEST);
                return "";
            }
            Post user = postService.add(creation.getTitle(), creation.getContent(), creation.getCategories());
            response.status(201); // 201 Created
            response.type("application/json");
            return dataToJson(user);
 
        });
 
        // GET - Give me post with this id
        get("/posts/:id", (request, response) -> {
            Post post = postService.findById(request.params(":id"));
            if (post != null) {
                return dataToJson(post);
            } else {
                response.status(404); // 404 Not found
                return dataToJson("post not found");
            }
        });
 
        // Get - Give me all posts
        get("/posts", (request, response) -> {
            List<Post> result = postService.findAll();
            if (result.isEmpty()) {
                return dataToJson("user not found");
            } else {
                return dataToJson(postService.findAll());
            }
        });
 
        // PUT - Update post
        put("/posts/:id", (request, response) -> {
            String id = request.params(":id");
            Post post = postService.findById(id);
            if (post != null) {
                ObjectMapper mapper = new ObjectMapper();
                NewPostPayload modify = mapper.readValue(request.body(), NewPostPayload.class);
                if (!modify.isValid()) {
                    response.status(HTTP_BAD_REQUEST);
                    return "";
                }
                postService.update(id, modify.getTitle(), modify.getContent(), modify.getCategories());
                return dataToJson("post with id " + id + " is updated!");
            } else {
                response.status(404);
                return dataToJson("post not found");
            }
        });
 
        // DELETE - delete post
        delete("/posts/:id", (request, response) -> {
            String id = request.params(":id");
            Post post = postService.findById(id);
            if (post != null) {
                postService.delete(id);
                return dataToJson("post with id " + id + " is deleted!");
            } else {
                response.status(404);
                return dataToJson("post not found");
            }
        });
 
    }
    
}

package com.jeonguk.controller;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jeonguk.error.ResponseError;
import com.jeonguk.model.Post;
import com.jeonguk.service.impl.PostService;

import lombok.Data;

public class PostController extends AbstractController {

    private static final int HTTP_BAD_REQUEST = 400;
    
    private PostService postService;

    public PostController() {
        postService = new PostService();
        initRoutes();
    }
    
    @Override
    protected void initRoutes() {
        port(8080);
//        
//        // Get - Give me all posts
//        get("/posts", (request, response) -> {
//            List<Post> result = postService.findAll();
//            if (result.isEmpty()) {
//                return dataToJson("user not found");
//            } else {
//                return dataToJson(postService.findAll());
//            }
//        });
// 
        
        // GET - Give me post with this id
        get("/posts/:id", (request, response) -> {
            final String id = request.params(":id");
            if (id == null || !Pattern.matches("[0-9]+", id)) {
                response.status(400);
                return dataToJson(new ResponseError("Bad Request!"));
            }
            Post post = postService.findById(Integer.valueOf(request.params(":id")));
            if (post != null) {
                return dataToJson(post);
            } else {
                response.status(404); // 404 Not found
                return dataToJson("post not found");
            }
        });
        
        // POST - Add an post
        post("/posts", (request, response) -> {
            ObjectMapper mapper = new ObjectMapper();
            NewPostPayload creation = mapper.readValue(request.body(), NewPostPayload.class);
            if (!creation.isValid()) {
                response.status(HTTP_BAD_REQUEST);
                return "";
            }
            Post newPost = new Post();
            newPost.setTitle(creation.getTitle());
            newPost.setContent(creation.getContent());
            newPost.setCreatedAt(new Date());
            Post user = postService.create(newPost);
            response.status(201); // 201 Created
            response.type("application/json");
            return dataToJson(user);
 
        });
        
        // PUT - Update post
        put("/posts/:id", (request, response) -> {
            final String id = request.params(":id");
            if (id == null || !Pattern.matches("[0-9]+", id)) {
                response.status(400);
                return dataToJson(new ResponseError("Bad Request!"));
            }    
            Post post = postService.findById(Integer.valueOf(id));
            if (post != null) {
                ObjectMapper mapper = new ObjectMapper();
                NewPostPayload modify = mapper.readValue(request.body(), NewPostPayload.class);
                if (!modify.isValid()) {
                    response.status(HTTP_BAD_REQUEST);
                    return "";
                }
                post.setTitle(modify.getTitle());
                post.setContent(modify.getContent());
                postService.update(post);
                return dataToJson("post with id " + id + " is updated!");
            } else {
                response.status(404);
                return dataToJson("post not found");
            }
        });
       
        // DELETE - delete post
        delete("/posts/:id", (request, response) -> {
            String id = request.params(":id");
            if (id == null || !Pattern.matches("[0-9]+", id)) {
                response.status(400);
                return dataToJson(new ResponseError("Bad Request!"));
            }               
            Post post = postService.findById(Integer.valueOf(id));
            if (post != null) {
                postService.delete(post);
                return dataToJson("post with id " + id + " is deleted!");
            } else {
                response.status(404);
                return dataToJson("post not found");
            }
        });
    }
    
    @Data
    static class NewPostPayload {
        
        private String title;
        private String content;

        public boolean isValid() {
            return title != null && !title.isEmpty();
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
    
}

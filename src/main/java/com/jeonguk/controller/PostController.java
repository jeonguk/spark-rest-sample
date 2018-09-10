package com.jeonguk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jeonguk.error.ResponseError;
import com.jeonguk.model.Post;
import com.jeonguk.service.impl.PostService;
import lombok.Data;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

import static spark.Spark.*;

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
            final Optional<Post> post = Optional.ofNullable(postService.findById(Integer.valueOf(request.params(":id"))));
            if(post.isPresent()) {
                return dataToJson(post.get());
            } else {
                response.status(404); // 404 Not found
                return dataToJson("post not found");
            }
        });
        
        // POST - Add an post
        post("/posts", (request, response) -> {
            ObjectMapper mapper = new ObjectMapper();
            final NewPostPayload creation = mapper.readValue(request.body(), NewPostPayload.class);
            if (!creation.isValid()) {
                response.status(HTTP_BAD_REQUEST);
                return "";
            }
            final Post newPost = new Post();
            newPost.setTitle(creation.getTitle());
            newPost.setContent(creation.getContent());
            newPost.setCreatedAt(new Date());
            final Post post = postService.create(newPost);
            response.status(201); // 201 Created
            response.type("application/json");
            return dataToJson(post);
 
        });
        
        // PUT - Update post
        put("/posts/:id", (request, response) -> {
            final String id = request.params(":id");
            if (id == null || !Pattern.matches("[0-9]+", id)) {
                response.status(400);
                return dataToJson(new ResponseError("Bad Request!"));
            }    
            final Optional<Post> post = Optional.ofNullable(postService.findById(Integer.valueOf(id)));
            if (post.isPresent()) {
                final ObjectMapper mapper = new ObjectMapper();
                final NewPostPayload modify = mapper.readValue(request.body(), NewPostPayload.class);
                if (!modify.isValid()) {
                    response.status(HTTP_BAD_REQUEST);
                    return "";
                }
                post.get().setTitle(modify.getTitle());
                post.get().setContent(modify.getContent());
                postService.update(post.get());
                return dataToJson("post with id " + id + " is updated!");
            } else {
                response.status(404);
                return dataToJson("post not found");
            }
        });
       
        // DELETE - delete post
        delete("/posts/:id", (request, response) -> {
            final String id = request.params(":id");
            if (id == null || !Pattern.matches("[0-9]+", id)) {
                response.status(400);
                return dataToJson(new ResponseError("Bad Request!"));
            }               
            final Optional<Post> post = Optional.ofNullable(postService.findById(Integer.valueOf(id)));
            if (post.isPresent()) {
                postService.delete(post.get());
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

        boolean isValid() {
            return title != null && !title.isEmpty();
        }
        
    }
    
    private static String dataToJson(Object data) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            final StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e) {
            throw new RuntimeException("IOException from a StringWriter");
        }
    }
    
}

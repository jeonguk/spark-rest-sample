package com.jeonguk.service.impl;

import com.jeonguk.model.Post;
import com.jeonguk.service.CrudService;

import dao.PostRepository;

public class PostService implements CrudService<Post, Integer> {
    
    private PostRepository postRepository;

    public PostService() {
        postRepository = new PostRepository();
    }

    @Override
    public Post create(Post t) {
        return postRepository.create(t);
    }

    @Override
    public Post update(Post t) {
        return postRepository.update(t);
    }

    @Override
    public Post findById(Integer id) {
        return postRepository.findById(id);
    }

    @Override
    public void delete(Post t) {
        postRepository.delete(t);
    }
    
}

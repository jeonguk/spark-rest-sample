package com.jeonguk.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Post {
    private Integer id;
    private String title;
    private String content;
    private List<String> categories;
}

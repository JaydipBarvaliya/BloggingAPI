package com.blogging.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlogDTO {
    private Long id;

    private String author;
    private String category;
    private String content;
    private String image;
    private String summary;
    private String title;
}

package com.blogging.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlogDTO {
    private Long id;
    private String slug;
    private String title;
    private String summary;
    private String content;
    private byte[] image;
    private String author;
    private String category;
}

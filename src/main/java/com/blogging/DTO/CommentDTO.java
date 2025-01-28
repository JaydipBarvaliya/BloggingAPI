package com.blogging.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private String content;
    private String name;
    private String timestamp; // String for JSON compatibility
    private Long userId;
    private Long blogId;
}

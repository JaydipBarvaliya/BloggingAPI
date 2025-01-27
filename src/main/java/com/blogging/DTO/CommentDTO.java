package com.blogging.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDTO {
    private Long blogId;
    private String content;
    private Long userId;
    private String name; // Full name of the user
    private String timestamp; // String for JSON compatibility
}

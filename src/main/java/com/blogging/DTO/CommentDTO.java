package com.blogging.DTO;

public class CommentDTO {
    private Long id;
    private String name;
    private String comment;

    // Constructors
    public CommentDTO(Long id, String name, String comment) {
        this.id = id;
        this.name = name;
        this.comment = comment;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

package com.example.graduationproject.community.model;

public class PostImageDTO {
    private Integer _id;
    private Integer post_id;
    private String filename;

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}

package com.example.graduationproject.community.model;

public class GroupDTO {
    private Integer _id;
    private String title;
    private String description;
    private String category;
    private Integer authority;
    private String author;
    private Integer member_cnt;
    private String cover;
    private String groupPassword;

    public Integer get_id() {
        return _id;
    }
    public void set_id(Integer _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getAuthority() {
        return authority;
    }
    public void setAuthority(Integer authority) {
        this.authority = authority;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getMember_cnt() {
        return member_cnt;
    }
    public void setMember_cnt(Integer member_cnt) {
        this.member_cnt = member_cnt;
    }

    public String getCover() {
        return cover;
    }
    public void setCover(String cover) {
        this.cover = cover;
    }
}

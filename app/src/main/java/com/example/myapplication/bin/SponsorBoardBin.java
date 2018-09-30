package com.example.myapplication.bin;

public class SponsorBoardBin {
    private int num;
    private int category;
    private String writer;
    private String title;
    private String content;
    private String sthumbnail_uri;
    private String contents_uri;


    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getSthumbnail_uri() {
        return sthumbnail_uri;
    }

    public void setSthumbnail_uri(String sthumbnail_uri) {
        this.sthumbnail_uri = sthumbnail_uri;
    }

    public String getContents_uri() {
        return contents_uri;
    }

    public void setContents_uri(String contents_uri) {
        this.contents_uri = contents_uri;
    }

}

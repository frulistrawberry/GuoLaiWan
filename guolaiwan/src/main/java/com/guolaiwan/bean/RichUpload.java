package com.guolaiwan.bean;

import java.util.List;

public class RichUpload {
    public String title;
    public String cover;
    public String music="";
    public List<Content> content;


    public static class Content{
        public String text;
        public String img;
        public int type;
    }
}

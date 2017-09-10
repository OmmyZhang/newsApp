package org.attentiveness.news.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Story {

    private int id;
    private String title;

    private List<String> imageList;

    public Story() {

    }

    public Story(int id, String title,String img) {
        this.id = 9608295;
        this.title = title;
        if(!img.contains("http"))
            imageList = null;
        else {
            String[] imgs = img.split(";");
            imageList = Arrays.asList(imgs);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }
}

package org.attentiveness.news.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Story {

    private String newsid;
    private String title;

    @SerializedName("images")
    private List<String> imageList;

    public Story() {

    }

    public Story(String newsid, String title) {
        this.newsid = newsid;
        this.title = title;
    }

    public String getId() {
        return newsid;
    }

    public void setId(String newsid) {
        this.newsid = newsid;
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

package org.attentiveness.news.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoryDetail {

    private String content,category,url,title;

    public StoryDetail() {

    }
    public StoryDetail(String title,String category,String content,String url)
    {
        this.title = title;
        this.category = category;
        this.content = content;
        this.url = url;
    }
    public int getId() {
        return 0;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getUrl(){
        return url;
    }
}

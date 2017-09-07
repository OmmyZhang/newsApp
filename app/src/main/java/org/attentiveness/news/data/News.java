package org.attentiveness.news.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class News {

    @SerializedName("date")
    private String date;

    @SerializedName("stories")
    private List<Story> storyList;

    @SerializedName("top_stories")
    private List<Story> topStoryList;

    public News() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Story> getStoryList() {
        ArrayList<Story> sL = new ArrayList<Story>(storyList);
        sL.add(0,new Story(0,"zyn_test1"));
        sL.add(0,new Story(1,"zyn_test2"));
        return sL;
        //return storyList;
    }

    public void setStoryList(List<Story> storyList) {
        this.storyList = storyList;
    }

    public List<Story> getTopStoryList() {
        return topStoryList;
    }

    public void setTopStoryList(List<Story> topStoryList) {
        this.topStoryList = topStoryList;
    }

}

package org.attentiveness.news.data;

import com.google.gson.annotations.SerializedName;

import org.attentiveness.news.net.GetNews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class News {

    @SerializedName("date")
    private String date;

    @SerializedName("stories")
    private List<Story> storyList;

    @SerializedName("top_stories")
    private List<Story> topStoryList;

    GetNews getnews;

    public News() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Story> getStoryList() throws Exception {
        ArrayList<Story> storylist = new ArrayList<Story>(storyList);
        HashSet<String> tags = new HashSet<>();
        tags.add("科技");
        tags.add("教育");
        tags.add("文化");
        HashSet<String> notshow = new HashSet<>();
        int pagesize = 10;
        //GetNews getnews = new GetNews(tags, notshow, pagesize);
        //getnews.getInstance(tags, notshow, pagesize);
        ArrayList<HashMap> detaillist = getnews.getInstance(tags, notshow, pagesize).getMore();
        int i = 0;
        for (HashMap map : detaillist) {
            storylist.add(0, new Story(i, (String) map.get("news_Title")));
            System.out.println("storymap:" + map.get("news_Title"));
            i++;
        }
        return storylist;
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

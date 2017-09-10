package org.attentiveness.news.data;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.attentiveness.news.net.GetNews;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
        try {
            GetNews gn = GetNews.getINSTANCE();


            ArrayList<HashMap> list = gn.getMore();

            List<Story> sL = new ArrayList<Story>();

            for (HashMap e : list) {
                sL.add(new Story(0, (String) e.get("news_Title") , (String) e.get("news_Pictures")));
            }
            return sL;
        }
        catch (Exception e)
        {
            System.out.print("Error: " + e);
            return new ArrayList<Story>();
        }
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

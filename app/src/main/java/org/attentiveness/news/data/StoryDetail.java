package org.attentiveness.news.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoryDetail {

    private String content, category, url, title, segList;

    public StoryDetail() {

    }

    public StoryDetail(String title, String category, String content, String url, String segList) {
        this.title = title;
        this.category = category;
        this.content = content;
        this.url = url;
        this.segList = segList;
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

    public String getUrl() {
        return url;
    }

    public Pattern getNames() {
        String ss = "";
        Pattern name = Pattern.compile("([^\\s]*)/(LOC|PER)");
        Matcher m = name.matcher(segList);
        while (m.find())
            ss = ss + "|" + m.group(1);


        //System.out.println("rex: " + ss.substring(1));

        if (ss.length() > 0)
            return Pattern.compile(ss.substring(1));
        else
            return Pattern.compile("NoNoNo");
    }
}

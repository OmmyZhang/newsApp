package org.attentiveness.news.data;

import com.google.gson.annotations.SerializedName;

import org.attentiveness.news.net.GetNews;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Story {

    private String id;
    private String title;
    private String intro;

    private List<String> imageList;
    private String firstImage;

    public Story() {

    }

    public Story(String id, String title,String img,String intro) {
        this.id = id;
        this.title = title;
        this.intro = intro;
        if(!img.contains("http")) {
            imageList = new ArrayList<String>();
            firstImage = "baidu::"+title;
        }
        else {
            String[] imgs = img.split(";| ");
            imageList = Arrays.asList(imgs);
            firstImage = imageList.get(0);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro(){
        return intro;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public  String getImage(){
        return firstImage;
    }

    public void setImage(String img) {
        this.firstImage = img;
    }
}

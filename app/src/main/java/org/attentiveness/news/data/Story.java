package org.attentiveness.news.data;

import java.util.Arrays;
import java.util.List;

public class Story {

    private String id;
    private String title;
    private String intro;

    private List<String> imageList;

    public Story() {

    }

    public Story(String id, String title,String img,String intro) {
        this.id = id;
        this.title = title;
        this.intro = intro;
        if(!img.contains("http"))
            imageList = null;
        else {
            String[] imgs = img.split(";");
            imageList = Arrays.asList(imgs);
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

    public void setIntro(String intro) { this.intro = intro; }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }
}

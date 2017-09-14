package com.java.group11.data;

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

        firstImage = "baidu::" + title;

        if (img.contains("http")) {
            String[] imgs = img.split(";| ");
            imageList = Arrays.asList(imgs);

            for (String tmp : imageList)
                if (tmp.contains("http") && !(tmp.toLowerCase().contains("logo") || tmp.toLowerCase().contains("ico") || tmp.toLowerCase().contains("qrcode") || tmp.toLowerCase().contains("tang.jpg"))) {
                    firstImage = tmp;
                    break;
                }
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

    public  String getImage(){
        return firstImage;
    }

    public void setImage(String img) {
        this.firstImage = img;
    }
}

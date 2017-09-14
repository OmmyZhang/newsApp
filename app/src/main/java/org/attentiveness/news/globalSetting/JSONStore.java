package org.attentiveness.news.globalSetting;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.attentiveness.news.base.BaseActivity;
import org.attentiveness.news.data.Story;
import org.attentiveness.news.data.StoryDetail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by shols on 2017/9/13.
 */

public class JSONStore extends BaseActivity{

    private Context mContext;

    private String DATA_PATH = "user_config.txt";
    private GlobalSetting mGlobalSetting;
    private String mData;
    private GsonBuilder mBuilder;
    private Gson gson;

    public JSONStore(Context context){
        mBuilder = new GsonBuilder();
        gson = mBuilder.create();
        mContext = context;
        mGlobalSetting.getINSTANCE();
    }

    public void saveSettings() {
        mData = gson.toJson(mGlobalSetting.getINSTANCE(), GlobalSetting.class);
//        System.out.println("save mData: "+mData);
        FileOutputStream outputStream;
        try {
            outputStream = mContext.openFileOutput(DATA_PATH, Context.MODE_PRIVATE);
            byte[] bytes = mData.getBytes();
            outputStream.write(bytes);
            outputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public GlobalSetting loadSettings() {
        try {
            FileInputStream inputStream;
            inputStream = mContext.openFileInput(DATA_PATH);
            int len = inputStream.available();
            byte[] bytes = new byte[len];
            inputStream.read(bytes);
            mData = new String(bytes);
            System.out.println("load mData: "+mData);
            inputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return gson.fromJson(mData, GlobalSetting.class);
    }

    public void saveNews (StoryDetail storyDetail) {
        FileOutputStream outputStream;
        try {
            outputStream = mContext.openFileOutput(storyDetail.getId(), Context.MODE_PRIVATE);
            String detailJson = gson.toJson(storyDetail, StoryDetail.class);
//            System.out.println("news data: "+detailJson);
            byte[] bytes = detailJson.getBytes();
            outputStream.write(bytes);
            outputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveList (List<Story> storyList) {
        FileOutputStream outputStream;
        try {
            outputStream = mContext.openFileOutput("story_list.txt", Context.MODE_PRIVATE);
            String detailJson = gson.toJson(storyList, List.class);
            System.out.println("list save: "+detailJson);
            byte[] bytes = detailJson.getBytes();
            outputStream.write(bytes);
            outputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Story> loadList () {
        FileInputStream inputStream;
        String detail = "";
        try {
            inputStream = mContext.openFileInput("story_list.txt");
            int len = inputStream.available();
            byte[] bytes = new byte[len];
            inputStream.read(bytes);
            detail = new String(bytes);
            inputStream.close();
            System.out.println("list load: "+detail);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        List<Story> storyList;
        storyList = gson.fromJson(detail, new TypeToken<List<Story>>(){}.getType());
        System.out.println("data check:"+storyList.get(0));
        Story test = storyList.get(0);
        System.out.println("test check: "+test+test.getClass().toString());
        return storyList;
    }

    public StoryDetail loadNews (String storyId) {
        FileInputStream inputStream;
        String detail = "";
        try {
            inputStream = mContext.openFileInput(storyId);
            int len = inputStream.available();
            byte[] bytes = new byte[len];
            inputStream.read(bytes);
            detail = new String(bytes);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return gson.fromJson(detail, StoryDetail.class);
    }

    public void deleteSettings() {
        FileInputStream inputStream;
        try {
            inputStream = mContext.openFileInput(DATA_PATH);
            System.out.println("data reset"+inputStream.toString());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

}

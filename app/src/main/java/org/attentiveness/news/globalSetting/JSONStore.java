package org.attentiveness.news.globalSetting;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.attentiveness.news.base.BaseActivity;
import org.attentiveness.news.data.StoryDetail;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by shols on 2017/9/13.
 */

public class JSONStore extends BaseActivity{

    private Context mContext;

    private String DATA_PATH = "user_config.txt";
    private GlobalSetting mGlobalSetting;
    private JSONObject jsonObject;
    private String mData;
    private GsonBuilder mBuilder;
    private Gson gson;
    private File path;

    public JSONStore(Context context){
        mBuilder = new GsonBuilder();
        gson = mBuilder.create();
        mContext = context;
        mGlobalSetting.getINSTANCE();
    }

    public void saveSettings() {
        mData = gson.toJson(mGlobalSetting.getINSTANCE(), GlobalSetting.class);
        System.out.println("save mData: "+mData);
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
            System.out.println("news data: "+detailJson);
            byte[] bytes = detailJson.getBytes();
            outputStream.write(bytes);
            outputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
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

}

package org.attentiveness.news.globalSetting;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.attentiveness.news.base.BaseActivity;
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

    private String DATA_PATH = "/data/data/BigNews/user_config.txt";
    private GlobalSetting mGlobalSetting;
    private JSONObject jsonObject;
    private String mData;
    private GsonBuilder mBuilder;
    private Gson gson;
    private File path;

    public JSONStore(GlobalSetting globalSetting){
        mBuilder = new GsonBuilder();
        gson = mBuilder.create();
        path = new File(DATA_PATH);
        mGlobalSetting = globalSetting;
    }

    public void saveToLocal() {
        mData = gson.toJson(mGlobalSetting, GlobalSetting.class);
        System.out.println(mData);
        FileOutputStream outputStream;
        try {
            outputStream = this.openFileOutput(DATA_PATH, Context.MODE_PRIVATE);
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
            inputStream = this.openFileInput(DATA_PATH);
            int len = inputStream.available();
            byte[] bytes = new byte[len];
            inputStream.read(bytes);
            mData = new String(bytes);
            System.out.println(mData);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return gson.fromJson(mData, GlobalSetting.class);
    }

}

package org.attentiveness.news.globalSetting;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangyn on 17-9-12.
 */

public class GlobalSetting {

    private GlobalSetting INSTANCE;

    private boolean allowBackstageVoice = true;
    private boolean noPicture = false;
    private boolean nightMode = false;
    private Map<String,Double>  readRecord;
    private Set<String> notShow;
    private Set<String> interestedClass;

    private GlobalSetting()
    {
        readRecord = new HashMap<String,Double>();

    }
    public GlobalSetting getINSTANCE()
    {
        if(INSTANCE == null)
            INSTANCE = new GlobalSetting();
        return INSTANCE;
    }

    public void resetReadRecord()
    {
        readRecord = new HashMap<String,Double>();
    }

    public  Set<String> getNotShow()
    {
        return notShow;
    }
    public void setNotShow(Set<String> notShow)
    {
        this.notShow = notShow;
    }

    public void updateReadRecord(String word,double w)
    {
        if(readRecord.containsKey(word))
            readRecord.put(word,readRecord.get(word) + w);
        else
            readRecord.put(word,w);
    }

    public void setNoPicture()
    {
        noPicture = ! noPicture;
        //
    }

    public void setNightMode()
    {
        nightMode = !nightMode;
        //
    }

}
package org.attentiveness.news.globalSetting;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangyn on 17-9-12.
 */

public class GlobalSetting {

    static private GlobalSetting INSTANCE = null;


    private boolean allowBackstageVoice = true;
    private boolean noPicture = false;
    private boolean nightMode = false;
    private boolean allowOtherClass = true;

    private Map<String,Double>  readRecord;
    private Set<String> notShow;
    private Set<String> interestedClass;

    private Set<String> readToday;

    private GlobalSetting()
    {
        readRecord = new HashMap<String,Double>();
        readRecord.put("澳洲",100.0);
        readRecord.put("李克强",100.0);

        readToday = new HashSet<String>();
    }

    public boolean ifRead(String id)
    {
        return readToday.contains(id);
    }

    public void readIt(String id)
    {
        readToday.add(id);
    }

    public static GlobalSetting getINSTANCE() {
        if(INSTANCE == null)
            INSTANCE = new GlobalSetting();
        return INSTANCE;
    }

    public static void setINSTANCE(GlobalSetting loader) {
        INSTANCE = loader;
    }

    public boolean isAllowOtherClass(){
        return allowOtherClass;
    }

    public void resetReadRecord()
    {
        readRecord = new HashMap<String,Double>();
    }

    public Map<String, Double> getReadRecord() { return readRecord; }

    public Set<String> getNotShow()
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

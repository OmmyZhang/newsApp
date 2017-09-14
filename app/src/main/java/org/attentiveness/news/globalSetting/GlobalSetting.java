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
    private boolean allowOtherClass = true;
    private boolean autoRefreshMayLike = false;

    private Map<String, Double> readRecord;
    private Set<String> notShow;
    private Set<String> interestedClass;

    private Set<String> readToday;

    private Set<String> favStoryId;

    private GlobalSetting() {
        readRecord = new HashMap<String, Double>();

        readToday = new HashSet<String>();

        interestedClass = new HashSet<String>();
        interestedClass.add("科技");
        interestedClass.add("教育");
        interestedClass.add("军事");
//            ct.add("国内");
//            ct.add("国际");
//            ct.add("文化");

        notShow = new HashSet<>();
        favStoryId = new HashSet<>();
    }

    public boolean ifRead(String id) {
        return readToday.contains(id);
    }

    public void readIt(String id) {
        readToday.add(id);
    }

    public Set<String> getReadToday () { return readToday; }

    public static GlobalSetting getINSTANCE() {
        if (INSTANCE == null)
            INSTANCE = new GlobalSetting();
        return INSTANCE;
    }

    public static void setINSTANCE(GlobalSetting loader) {
        INSTANCE = loader;
    }

    public boolean isAllowOtherClass() {
        return allowOtherClass;
    }

    public void setAllowOtherClass(boolean b){
        allowOtherClass = b;
    }

    public boolean isAllowBackstageVoice() {
        return allowBackstageVoice;
    }

    public void setAllowBackstageVoice(boolean b) {
        allowBackstageVoice = b;
    }

    public void clearReadRecord() {
        readRecord = new HashMap<String, Double>();
        readToday = new HashSet<String>();
    }

    public Map<String, Double> getReadRecord() {
        return readRecord;
    }

    public Set<String> getInterestedClass() {
        return interestedClass;
    }

    public Set<String> getNotShow() {
        return notShow;
    }

    public void setNotShow(Set<String> notShow) {
        this.notShow = notShow;
    }

    public void updateReadRecord(String word, double w) {
        if (readRecord.containsKey(word))
            readRecord.put(word, readRecord.get(word) + w);
        else
            readRecord.put(word, w);
    }

    public void setNoPicture(boolean b) {
        noPicture = b;
    }

    public boolean isNoPicture() {
        return noPicture;
    }

    public boolean isAutoRefreshMayLike() {
        return autoRefreshMayLike;
    }

    public void setAutoRefreshMayLike(boolean b) {
        autoRefreshMayLike = b;
    }

    public boolean checkClassTag(String classTag) {
        return interestedClass.contains(classTag);
    }

    public void addClassTag(String classTag) {
        interestedClass.add(classTag);
    }

    public void delClassTag(String classTag) {
        interestedClass.remove(classTag);
    }

    public boolean isFav(String sId) {
        return favStoryId.contains(sId);
    }

    public void addFavStoryId(String sId) {
        favStoryId.add(sId);
    }

    public void delFavStoryId(String sId) {
        favStoryId.remove(sId);
    }

}

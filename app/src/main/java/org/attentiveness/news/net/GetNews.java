package org.attentiveness.news.net;

/**
 * Created by zhangyn on 17-9-7.
 */

import org.attentiveness.news.data.StoryDetail;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.attentiveness.news.data.StoryDetail;
import org.attentiveness.news.globalSetting.GlobalSetting;
import org.json.*;

public class GetNews {
    final private static String BASE_URL = "http://166.111.68.66:2042/news/action/query/";
    static private Set<String> classTag;
    static private Set<String> notShow;
    static private GetNews INSTANCE;

    private int pageSize, currNo;
    private int searchNo;
    private String searchKey;


    static public void newINSTANCE(Set<String> cT, Set<String> nS, int pS) {
        INSTANCE = new GetNews(cT, nS, pS);
    }

    static public GetNews getINSTANCE() {
        return INSTANCE;
    }

    private GetNews(Set<String> cT, Set<String> nS, int pS) {
        classTag = cT;
        notShow = nS;
        pageSize = pS;
        currNo = 0;
    }

    public List<HashMap> getMore() throws Exception {
        ++currNo;
        JSONArray result = latest(pageSize, currNo);

        ArrayList<HashMap> ans = new ArrayList<HashMap>();

        if (result.length() == 0) return null;


        for (int i = 0; i < result.length(); ++i) {
            JSONObject e = result.getJSONObject(i);
            if (!classTag.contains(e.getString("newsClassTag")) &&
                    ( Math.random() > 0.05 || !GlobalSetting.getINSTANCE().isAllowOtherClass()))
                continue;
            HashMap<String, String> news = new HashMap<String, String>();
            boolean nono = false;

            for (Iterator<String> it = e.keys(); it.hasNext(); ) {
                String k = it.next();
                String v = e.getString(k);
                for (String bad : notShow)
                    if (v.contains(bad)) {
                        nono = true;
                        break;
                    }
                if (nono) break;
                news.put(k, v);
            }
            if (!nono)
                ans.add(news);
        }
        return ans;
    }

    public List<HashMap> mayLike(Map<String, Double> readRecord) throws Exception {
        System.out.println("MayLike START!");
        JSONArray stories = latest(300, (int) (Math.random() * 200 + 1));
        System.out.println("MayLike GETDATA!");
        ArrayList<HashMap> ans = new ArrayList<HashMap>();

        for (int i = 0; i < stories.length(); ++i) {
            JSONObject story = stories.getJSONObject(i);
            HashMap<String, Object> news = new HashMap<String, Object>();
            boolean nono = false;
            double weight = 0.0;
            for (Iterator<String> it = story.keys(); it.hasNext(); ) {
                String k = it.next();
                String v = story.getString(k);
                for (String bad : notShow)
                    if (v.contains(bad)) {
                        nono = true;
                        break;
                    }
                for (String word : readRecord.keySet())
                    if (v.contains(word)) {
                        weight += readRecord.get(word);
//                        System.out.println("Have!" + word);
                    }
                if (nono) break;
                news.put(k, v);
            }
            if (!nono) {
                news.put("weight", weight);
                ans.add(news);
            }

        }

        Collections.sort(ans, new Comparator<HashMap>() {
            @Override
            public int compare(HashMap h1, HashMap h2) {
                if (Math.abs((double) h2.get("weight") - (double) h1.get("weight")) < 1e-7)
                    return 0;
                return ((double) h2.get("weight") > (double) h1.get("weight")) ? 1 : -1;
            }
        });

        System.out.println("MayLike Finish!");
        return ans.subList(0, Math.min(pageSize / 2, ans.size()));
    }


    public StoryDetail getDetail(String id) throws Exception {
        URL cs = new URL(BASE_URL + "detail?newsId=" + id);

        Scanner w = new Scanner(cs.openStream());

        String data = "";
        while (w.hasNextLine())
            data = data + w.nextLine();

        JSONObject jo = new JSONObject(data);

        try
        {
            JSONArray keywords = jo.getJSONArray("Keywords");
            for(int i=0;i<keywords.length();++i) {
                String word = keywords.getJSONObject(i).getString("word");
                double weight = keywords.getJSONObject(i).getDouble("score");

                GlobalSetting.getINSTANCE().updateReadRecord(word,weight);
            }
        }catch (Exception e)
        {
            System.out.println("keywords " + e);
        }

        if (jo.has("news_Category"))
            return new StoryDetail(
                    jo.getString("news_Title"),
                    jo.getString("news_Category"),
                    jo.getString("news_Content"),
                    jo.getString("news_URL"),
                    jo.getJSONArray("seggedPListOfContent").getString(0));
        else
            return new StoryDetail(
                    jo.getString("news_Title"),
                    "",
                    jo.getString("news_Content"),
                    jo.getString("news_URL"),
                    jo.getJSONArray("seggedPListOfContent").getString(0));
    }

    public void search(String key) {
        searchKey = key;
        searchNo = 0;

    }

    public ArrayList<HashMap> searchMore() throws Exception {
        ++searchNo;
        JSONArray result = find(searchKey, pageSize, searchNo);

        ArrayList<HashMap> ans = new ArrayList<HashMap>();

        if (result.length() == 0) return null;

        for (int i = 0; i < result.length(); ++i) {
            JSONObject e = result.getJSONObject(i);
            HashMap<String, String> news = new HashMap<String, String>();
            boolean nono = false;

            for (Iterator<String> it = e.keys(); it.hasNext(); ) {
                String k = it.next();
                String v = e.getString(k);
                for (String bad : notShow)
                    if (v.contains(bad)) {
                        nono = true;
                        break;
                    }
                if (nono) break;
                news.put(k, v);
            }
            if (!nono)
                ans.add(news);
        }
        return ans;
    }

    JSONArray latest(int pageSize, int pageNo) throws Exception {
        return urlQuest(String.format("latest?pageSize=%d&pageNo=%d", pageSize, pageNo));
    }

    JSONArray find(String key, int pageSize, int pageNo) throws Exception {
        return urlQuest(String.format("search?keyword=%s&pageSize=%d&pageNo=%d", key, pageSize, pageNo));
    }

    JSONArray urlQuest(String s) throws Exception {
        URL cs = new URL(BASE_URL + s);
        Scanner w = new Scanner(cs.openStream());

        String data = "";
        while (w.hasNextLine())
            data = data + w.nextLine();
        try {
            System.out.println("Request URL: " + s);
            JSONObject jo = new JSONObject(data);
            return jo.getJSONArray("list");
        } catch (Exception e) {
            System.out.println("urlQuest : " + e);
            return urlQuest(s);
        }
    }

    public String findPicture(String s) throws Exception {
        URL cs = new URL("http://image.baidu.com/search/flip?tn=baiduimage&ipn=r&ct=201326592&cl=2&lm=-1&st=-1&fm=result&fr=&sf=1&fmq=1460997499750_R&pv=&ic=0&nc=1&z=&se=1&showtab=0&fb=0&width=&height=&face=0&istype=2&ie=utf-8&word=" + s);

        HttpURLConnection con = (HttpURLConnection) cs.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.2) AppleWebKit/525.13 (KHTML, like Gecko) Chrome/0.2.149.27 Safari/525.13");
        System.setProperty("http.agent", "");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();

        String html = response.toString();

        Pattern pp = Pattern.compile("\"objURL\":\"(http.*?)\"");
        Matcher mm = pp.matcher(html);

        String s1 = mm.find() ? mm.group(1) : "no";
        String s2 = mm.find() ? mm.group(1) : "no";

        return  "baidu;"+ s1 + ";" + s2;
    }
}

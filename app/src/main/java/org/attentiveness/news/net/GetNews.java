package org.attentiveness.news.net;

/**
 * Created by zhangyn on 17-9-7.
 */

import java.net.*;
import java.util.*;

import org.attentiveness.news.data.StoryDetail;
import org.json.*;

public class GetNews
{
    final private static String BASE_URL = "http://166.111.68.66:2042/news/action/query/";
    static private HashSet<String> classTag;
    static private HashSet<String> notShow;
    static private GetNews INSTANTCE;

    private int pageSize,currNo;
    private int searchNo;
    private String searchKey;

    static public void newINSTANCE(HashSet<String> cT,HashSet<String> nS,int pS)
    {
        INSTANTCE = new GetNews(cT,nS,pS);
    }
    static public  GetNews getINSTANCE()
    {
        return INSTANTCE;
    }

    private GetNews(HashSet<String> cT,HashSet<String> nS,int pS)
    {
        classTag = cT;
        notShow = nS;
        pageSize = pS;
        currNo = 0;
    }

    public ArrayList<HashMap> getMore() throws Exception
    {
        ++currNo;
        JSONArray result = latest(pageSize , currNo);

        ArrayList<HashMap> ans = new ArrayList<HashMap>();

        if(result.length() == 0) return null;


        for(int i=0;i<result.length();++i)
        {
            JSONObject e = result.getJSONObject(i);
            if(!classTag.contains(e.getString("newsClassTag")) && Math.random() > 0.05 )
                continue;
            HashMap<String,String> news = new HashMap<String,String>();
            boolean nono = false;

            for(Iterator<String> it=e.keys();it.hasNext();)
            {
                String k = it.next();
                String v = e.getString(k);
                for(String bad:notShow)
                    if(v.contains(bad))
                    {
                        nono = true;
                        break;
                    }
                if(nono) break;
                news.put(k,v);
            }
            if(!nono)
                ans.add(news);
        }
        return ans;
    }

    public StoryDetail getDetail(String id) throws Exception {
        URL cs = new URL(BASE_URL + "detail?newsId=" + id);

        Scanner w = new Scanner(cs.openStream());

        String data = "";
        while (w.hasNextLine())
            data = data + w.nextLine();

        JSONObject jo = new JSONObject(data);

        return new StoryDetail(
                jo.getString("news_Title"),
                jo.getString("news_Category"),
                jo.getString("news_Content"),
                jo.getString("news_URL"));

    }

    public void search(String key)
    {
        searchKey = key;
        searchNo = 0;

    }

    public ArrayList<HashMap> searchMore() throws Exception
    {
        ++searchNo;
        JSONArray result = find(searchKey , pageSize , searchNo);

        ArrayList<HashMap> ans = new ArrayList<HashMap>();

        if(result.length() == 0) return null;

        for(int i=0;i<result.length();++i)
        {
            JSONObject e = result.getJSONObject(i);
            HashMap<String,String> news = new HashMap<String,String>();
            boolean nono = false;

            for(Iterator<String> it=e.keys();it.hasNext();)
            {
                String k = it.next();
                String v = e.getString(k);
                for(String bad:notShow)
                    if(v.contains(bad))
                    {
                        nono = true;
                        break;
                    }
                if(nono) break;
                news.put(k,v);
            }
            if(!nono)
                ans.add(news);
        }
        return ans;
    }

    JSONArray latest(int pageSize , int pageNo) throws Exception
    {
        return urlQuest( String.format("latest?pageSize=%d&pageNo=%d",pageSize,pageNo));
    }

    JSONArray find(String key,int pageSize,int pageNo) throws Exception
    {
        return urlQuest( String.format("search?keyword=%s&pageSize=%d&pageNo=%d",key,pageSize,pageNo));
    }

    JSONArray urlQuest(String s) throws Exception
    {
        URL cs = new URL(BASE_URL +s);
        Scanner w = new Scanner(cs.openStream());

        String data = "";
        while(w.hasNextLine())
            data = data + w.nextLine();
        try
        {
            JSONObject jo = new JSONObject(data);
            return  jo.getJSONArray("list");
        }
        catch(Exception e)
        {
            System.out.println(s);
            return new JSONArray();
        }
    }

}

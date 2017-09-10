package org.attentiveness.news.net;

/**
 * Created by zhangyn on 17-9-7.
 */

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class GetNews
{
    final private static String BASE_URL = "http://166.111.68.66:2042/news/action/query/";
    private HashSet<String> classTag;
    private HashSet<String> notShow;
    private int pageSize,currNo;
    private int searchNo;
    private String searchKey;

    private static GetNews INSTANCE = null;

    public static GetNews getInstance(HashSet<String> cT,HashSet<String> nS,int pS) {
        if (INSTANCE == null)
            INSTANCE = new GetNews(cT, nS, pS);
        return INSTANCE;
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
            if(!classTag.contains(e.getString("newsClassTag")))
                break;
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
            if(!classTag.contains(e.getString("newsClassTag")))
                break;
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
        URL cs = new URL(BASE_URL + String.format("latest?pageSize=%d&pageNo=%d",pageSize,pageNo));
        Scanner w = new Scanner(cs.openStream());

        String data = "";
        while(w.hasNextLine())
            data = data + w.nextLine();
        try
        {
            JSONObject jo = new JSONObject(data);
            JSONArray newsList  = jo.getJSONArray("list");

            return newsList;
        }
        catch(Exception e)
        {
            System.out.println("["+data+"]");
            System.out.println(pageNo);
            return new JSONArray();
        }
    }

    JSONArray find(String key,int pageSize,int pageNo)
            throws Exception
    {
        URL cs = new URL(BASE_URL + String.format("search?keyword=%s&pageSize=%d&pageNo=%d",key,pageSize,pageNo));
        Scanner w = new Scanner(cs.openStream());

        String data = "";
        while(w.hasNextLine())
            data = data + w.nextLine();
        try
        {
            JSONObject jo = new JSONObject(data);
            JSONArray newsList  = jo.getJSONArray("list");

            return newsList;
        }
        catch(Exception e)
        {
            System.out.println("["+data+"]");
            System.out.println(pageNo);
            return new JSONArray();
        }
    }

    public void clear() {
        this.INSTANCE = null;
    }
}

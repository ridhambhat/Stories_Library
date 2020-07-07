package com.example.storieslibrary;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class status_handler {

    private static final String SHARED_PREFS_FILE = "com.example.storieslibrary.storycache";
    private Context ctx;
    private SharedPreferences shared;
    private int fragmentId;
    private FragmentManager fm;
    private ArrayList<String> fromList = new ArrayList<>();
    private ArrayList<String> linkList = new ArrayList<>();
    private ArrayList<String> timeList= new ArrayList<>();

    public status_handler(Context ctx, int fragmentId, FragmentManager fm){
        this.ctx = ctx;
        this.fragmentId = fragmentId;
        this.fm = fm;
        shared = ctx.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        processStory(false);
        addStoryToFrame();
    }

    public void addStory(String from, String link) {
        fromList.add(from+link);
        linkList.add(link);
        timeList.add("nill "+link);
        processStory(true);
        addStoryToFrame();
    }

    public void addStory(String from, String link, String time){
        if(shouldStoryBeAlive(time))
        {
            fromList.add(from+link);
            linkList.add(link);
            timeList.add(time);
        }

        processStory(true);
        addStoryToFrame();

    }

    private void processStory(boolean storyAdded){

        if(!storyAdded)
        loadStoryState();
//        Log.d("STORY_PROCESS", "timesize => "+timeList.size()+" - fromsize => "+fromList.size()+" - linksize => "+linkList.size());
//        Log.d("STORY_PROCESS", "from => "+fromList);
//        Log.d("STORY_PROCESS", "link => "+linkList);
//        Log.d("STORY_PROCESS", "time => "+timeList);

        ArrayList<String> templink = new ArrayList<>();
        ArrayList<String> tempfrom = new ArrayList<>();
        ArrayList<String> temptime = new ArrayList<>();
        for(int i = 0; i<fromList.size(); i++)
        {
            if (!timeList.get(i).startsWith("nill"))
            {
                if (shouldStoryBeAlive(timeList.get(i)))
                {
                    tempfrom.add(fromList.get(i));
                    templink.add(linkList.get(i));
                    temptime.add(timeList.get(i));
                }
            }
            else
            {
                tempfrom.add(fromList.get(i));
                templink.add(linkList.get(i));
                temptime.add(timeList.get(i));
            }
        }
        fromList = tempfrom;
        timeList = temptime;
        linkList = templink;

        for(int i = 0; i<linkList.size(); i++)
        {
            Picasso.with(ctx)
                    .load(linkList.get(i));
        }

        saveStoryState();

    }

    private boolean shouldStoryBeAlive(String time)
    {
        DateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date1 = new Date();
        Date date2 = null;
        try {
            date2 = date.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Log.d("TIME", "timenow => "+date1+" timeStory => "+date2);
        long diff = date1.getTime() - date2.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        //Log.d("TIME", "diff => "+hours);
        if(hours<24)
            return true;
        return false;
    }

    private void addStoryToFrame()
    {
        String TAG = "STORY ADDED";
//        Log.d(TAG, "processStory: from "+fromList);
//        Log.d(TAG, "processStory: link "+linkList);
//        Log.d(TAG, "processStory: time "+timeList);
        final horizontal_status_fragment hs = horizontal_status_fragment.newInstance(linkList, fromList, timeList);
//        horizontal_status hs = new horizontal_status();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(fragmentId, hs, "tag");
        ft.commit();
    }

    private void saveStoryState()
    {
        saveArrayList(fromList, "STORY_FROM");
        saveArrayList(timeList, "STORY_TIME");
        saveArrayList(linkList, "STORY_LINK");
    }

    private void loadStoryState()
    {
        linkList = new ArrayList<>();
        if (getArrayList("STORY_LINK") != null)
            linkList = getArrayList("STORY_LINK");

        fromList = new ArrayList<>();
        if (getArrayList("STORY_FROM") != null)
            fromList = getArrayList("STORY_FROM");

        timeList = new ArrayList<>();
        if (getArrayList("STORY_TIME") != null)
            timeList = getArrayList("STORY_TIME");

    }

    public void saveArrayList(ArrayList<String> list, String key)
    {
        SharedPreferences.Editor editor = shared.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<String> getArrayList(String key)
    {
        Gson gson = new Gson();
        String json = shared.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }

}

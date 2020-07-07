package com.example.storieslibrary;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class horizontal_status_fragment extends Fragment {

    private View view;
    private Context ctx;
    private ArrayList<String> allFrom = new ArrayList<>();
    private HashMap<String, String> allTime = new HashMap<>();
    private HashMap<String, ArrayList<String>> categorisedLinks = new HashMap<>();

    private ArrayList<ArrayList<String>> storyLink = new ArrayList<>();
    private ArrayList<String> storyFrom = new ArrayList<>();
    private ArrayList<ArrayList<String>> storyTime = new ArrayList<>();
    private int[] storyPosition;
    private ArrayList<Integer> storyState = new ArrayList<>();
    private RecyclerView rv;

    private static final String SHARED_PREFS_FILE = "com.example.storieslibrary.storycache";
    private static SharedPreferences shared;

    public static horizontal_status_fragment newInstance(ArrayList<String> links, ArrayList<String> from, ArrayList<String> time){
        horizontal_status_fragment fragmentFirst = new horizontal_status_fragment();
        Bundle args = new Bundle();
        args.putStringArrayList("links",links);
        args.putStringArrayList("from",from);
        args.putStringArrayList("time",time);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        allTime = new HashMap<>();
//        categorisedLinks = new HashMap<>();
//        allFrom = new ArrayList<>();
//        storyState = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_horizontal_status, container, false);
        ctx=view.getContext();

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        rv = view.findViewById(R.id.recyclerForStatus);

        ArrayList<String> links = getArguments().getStringArrayList("links");
        ArrayList<String> froms = getArguments().getStringArrayList("from");
        ArrayList<String> times = getArguments().getStringArrayList("time");

        for (int i = 0; i<links.size(); i++)
        {
            if (times.get(i).startsWith("nill"))
            {
                int n1 = links.get(i).length();
                int n2 = froms.get(i).length();
                addStory(froms.get(i).substring(0,(n2-n1)), links.get(i));
            }
            else
            {
                int n1 = links.get(i).length();
                int n2 = froms.get(i).length();
                addStory(froms.get(i).substring(0,(n2-n1)), times.get(i),links.get(i));
            }
        }

        prepareStoryContent();
        setRecyclerView();

        prepareStoryContent();
        setRecyclerView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        prepareStoryContent();
        setRecyclerView();
    }

    public void addStory(String from, String link)
    {
        addStory(from, " ", link);
    }

    public void addStory(String from, String time, String link)
    {
        if(categorisedLinks.containsKey(from))
        {
            if(!categorisedLinks.get(from).contains(link))
            {
                ArrayList<String> ele = categorisedLinks.get(from);
                ele.add(link);
                allTime.put(link, time);
                categorisedLinks.remove(from);
                categorisedLinks.put(from, ele);
            }
        }
        else
        {
            allFrom.add(from);
            ArrayList<String> ele = new ArrayList<>();
            ele.add(link);
            allTime.put(link, time);
            categorisedLinks.put(from, ele);
        }
    }

    private void prepareStoryContent()
    {
        storyFrom = new ArrayList<>();
        storyLink = new ArrayList<>();
        storyTime = new ArrayList<>();
        storyState = new ArrayList<>();

        ArrayList<String> allLinksVisited = new ArrayList<>();
        shared = ctx.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        String rec = shared.getString("VISITED_LINKS", "");
//        Log.d("STATE => ","stored links => "+rec);
        StringTokenizer vl = new StringTokenizer(rec);
        int n = vl.countTokens();
        for(int i = 0; i<n; i++)
        {
            String l = vl.nextToken();
            if(!allLinksVisited.contains(l))
            {
                allLinksVisited.add(l);
            }
        }

//        Log.d("STATE => ","stored links arraylist => "+allLinksVisited);

        storyPosition = new int[allFrom.size()];

        ArrayList<ArrayList<String>> templinks = new ArrayList<>();
        ArrayList<ArrayList<String>> temptime = new ArrayList<>();
        ArrayList<Integer> tempstate = new ArrayList<>();
        int[] temppos = new int[allFrom.size()];

        for(int i = 0; i < allFrom.size(); i++)
        {
            ArrayList<String> tlink = new ArrayList<>();
            ArrayList<String> ttime = new ArrayList<>();
            int numOfStories = categorisedLinks.get(allFrom.get(i)).size();
            boolean isAnyUnread = false;

            for(int j = 0; j < numOfStories; j++)
            {
                String link = categorisedLinks.get(allFrom.get(i)).get(j);

                if(!isAnyUnread && !allLinksVisited.contains(link))
                {
                    isAnyUnread = true;
                    temppos[i] = j;
                }

                tlink.add(link);
                ttime.add(allTime.get(link));

            }


//            Log.d("STATE => "," for i = "+i +" state = "+ isAnyUnread);
            templinks.add(tlink);
            temptime.add(ttime);
            if(isAnyUnread)
            {
                tempstate.add(1);
            }
            else
            {
                tempstate.add(0);
                temppos[i] = 0;
            }

        }


        int ctr = 0;
        for (int i = 0; i< allFrom.size(); i++)
        {
            if(tempstate.get(i)==1)
            {
                storyFrom.add(allFrom.get(i));
                storyLink.add(templinks.get(i));
                storyTime.add(temptime.get(i));
                storyPosition[ctr] = temppos[i];
                storyState.add(1);
                ctr++;
            }
        }

        for (int i = 0; i< allFrom.size(); i++)
        {
            if(tempstate.get(i)==0)
            {
                storyFrom.add(allFrom.get(i));
                storyLink.add(templinks.get(i));
                storyTime.add(temptime.get(i));
                storyPosition[ctr] = temppos[i];
                storyState.add(0);
                ctr++;
            }
        }

//        Log.d("STATE => ","temppos => "+ Arrays.toString(temppos));
//        Log.d("STATE => " ,"storyPos => "+Arrays.toString(storyPosition));

//        String TAG = "PrepareStoryContent";
//        Log.d(TAG, "story links : "+storyLink.toString());
//        Log.d(TAG, "story from : "+storyFrom.toString());
//        Log.d(TAG, "story Time : "+storyTime.toString());

    }


    private void storyProcessor(int positionToStart){

        Intent it=new Intent(ctx, stories_main.class);
        String TAG = "STORY";
//        Log.d(TAG, "story links : "+storyLink.toString());
//        Log.d(TAG, "story from : "+storyFrom.toString());
//        Log.d(TAG, "story Time : "+storyTime.toString());
        it.putExtra("url",storyLink);
        it.putExtra("allIndex",storyPosition);
        it.putExtra("index", positionToStart);
        it.putExtra("from",storyFrom);
        it.putExtra("time",storyTime);

        startActivityForResult(it,0);

    }

    private void setRecyclerView()
    {
//        Log.d("SRV", "setRecyclerView: ");
        rv.setLayoutManager(new LinearLayoutManager(ctx,LinearLayoutManager.HORIZONTAL, false));
        ArrayList<String> showlink = new ArrayList<>();
        for(int i = 0; i<storyFrom.size(); i++)
            showlink.add(storyLink.get(i).get(0));
        rv.setAdapter(new RoundStoryAdapter(ctx,storyFrom, storyState, showlink));
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(ctx, rv, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        storyProcessor(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );
    }

}

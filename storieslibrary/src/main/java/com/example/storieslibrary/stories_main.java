package com.example.storieslibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class stories_main extends AppCompatActivity {

    static int current_index;
    static ArrayList<ArrayList<String>> links;
    static ArrayList<ArrayList<String>> time;
    static ArrayList<String> from;
    static fragment_story allPages[];
    static FragmentManager fm;
    static  int allIndex[];
    static Activity activity;
    static Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories_main);
        Intent intent = getIntent();
        current_index = intent.getIntExtra("index", 0);
        current_index--;
        ctx=this;
        links = (ArrayList<ArrayList<String>>) intent.getSerializableExtra("url");
        time = (ArrayList<ArrayList<String>>) intent.getSerializableExtra("time");
        from=(ArrayList<String>)intent.getSerializableExtra("from");
//        time=(ArrayList<ArrayList<String>>) intent.getSerializableExtra("time");
        allIndex =intent.getIntArrayExtra("allIndex");
        Log.d("Array", "Size = " + links.size() + " cur in " + current_index);
        for (int i = 0; i < links.size(); i++) {
            Log.d("Links", links.get(i).toString());
        }
        fm=getSupportFragmentManager();
        goNextFrame(0);
        activity=this;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        activity.finish();
    }

    public static void goNextFrame(int previndex){



        if(current_index>=0&&current_index<links.size())
            allIndex[current_index]=previndex;
        current_index++;
        if(current_index>=links.size()){
            activity.finish();
        }
        else {
            //current_index = links.size() - 1;
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            //ft.replace(R.id.frame,allPages[current_index]);
            final fragment_story fr = fragment_story.newInstance(links.size(), current_index, allIndex[current_index], links.get(current_index),from.get(current_index),time.get(current_index));
            ft.replace(R.id.frame, fr,"tag");
            ft.commit();

//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    // Do something after 5s = 5000ms
//                    fr.start();
//                }
//            }, 1000);
            Log.d("Page", "Change in Page -- next");
        }


    }

    public static void goPrevFrame(int previndex){
        if(current_index>=0&&current_index<links.size())
            allIndex[current_index]=previndex;
        current_index--;
        if(current_index<0){
            activity.finish();
        }
        else {
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            //ft.replace(R.id.frame,allPages[current_index]);
            final fragment_story fr = fragment_story.newInstance(links.size(), current_index, allIndex[current_index], links.get(current_index),from.get(current_index),time.get(current_index));
            ft.replace(R.id.frame, fr);
            ft.commit();
//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    // Do something after 5s = 5000ms
//                    fr.start();
//                }
//            }, 1000);
            Log.d("Page", "Change in Page -- prev");
        }

    }

}

package com.example.stories_library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.storieslibrary.stories_main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = this;

        Button go = findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                storyProcessor();
            }
        });

    }

    private void storyProcessor(){

        ArrayList<ArrayList<String>> alllinks=new ArrayList<>();//Get value of all links from database
        ArrayList<String> from=new ArrayList<>();//Get value from database
        ArrayList<ArrayList<String>> time=new ArrayList<>();//Format - "dd-mm-yyyy hh:ss"//24hrs system
        ArrayList<Boolean> state=new ArrayList<>();//True if there is new story from a specific club
        ArrayList<Integer> position=new ArrayList<>();//Stores position from where stories have to be started

        //SampleData
        from.add("Page One");
        from.add("Page Two");
        from.add("Page Three");


        ArrayList<String> pp=new ArrayList<>();
        pp.add("https://cdn.pixabay.com/photo/2020/03/07/05/18/coffee-4908764_960_720.jpg");
        pp.add("https://cdn.pixabay.com/photo/2020/03/04/05/57/key-4900643_960_720.jpg");
        pp.add("https://cdn.pixabay.com/photo/2020/06/29/21/13/city-5354477_960_720.jpg");
        alllinks.add(pp);
        ArrayList<String> bb=new ArrayList<>();
        bb.add("https://cdn.pixabay.com/photo/2019/01/07/18/07/flowers-3919634_960_720.jpg");
        bb.add("https://cdn.pixabay.com/photo/2020/06/23/12/22/forest-5332379_960_720.jpg");
        alllinks.add(bb);
        ArrayList<String> cc=new ArrayList<>();
        cc.add("https://cdn.pixabay.com/photo/2020/06/28/21/41/cat-5350785_1280.jpg");
        cc.add("https://cdn.pixabay.com/photo/2020/06/28/14/45/butterfly-5349558_960_720.jpg");
        cc.add("https://cdn.pixabay.com/photo/2020/03/01/17/50/monalisa-4893660_960_720.jpg");
        alllinks.add(cc);


        String timearr[]=new String[8];
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        for(int i=0;i<8;i++){
            int hr=Integer.parseInt(currentTime.substring(0,2));
            if((hr-18+(2*i))<0)
                if (((hr - 18 + (2 * i)) + 24)<12)
                    timearr[i] = "Yesterday, " + ((hr - 18 + (2 * i)) + 24)+":00 a.m.";
                else
                    timearr[i] = "Yesterday, " + ((hr - 18 + (2 * i)) + 24 - 12)+":00 p.m.";
            else
            if (((hr - 18 + (2 * i)))<12)
                timearr[i] = "Today, " + ((hr - 18 + (2 * i)) )+":00 a.m.";
            else
                timearr[i] = "Today, " + ((hr - 18 + (2 * i)) -12)+":00 p.m.";
        }


        pp=new ArrayList<>();
        for(int i=0;i<3;i++)
            pp.add(timearr[i]);
        time.add(pp);
        pp=new ArrayList<>();
        for(int i=2;i<4;i++)
            pp.add(timearr[i]);
        time.add(pp);
        pp=new ArrayList<>();
        for(int i=3;i<6;i++)
            pp.add(timearr[i]);
        time.add(pp);

        int[] sortedPosition =new int[alllinks.size()];

        Intent it=new Intent(ctx, stories_main.class);
        it.putExtra("url",alllinks);
        it.putExtra("allIndex",sortedPosition);
        it.putExtra("index",position);
        it.putExtra("from",from);
        it.putExtra("time",time);

        startActivityForResult(it,0);

    }
}

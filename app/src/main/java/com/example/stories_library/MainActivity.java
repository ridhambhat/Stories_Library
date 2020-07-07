package com.example.stories_library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.storieslibrary.status_handler;
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

        //We have 2 methods of using the library
        //We can either just use the story page that will display a story and then vanish off
        //Or we can use a custom horizontal recycler view housed in a fragment which will display story as in instagram
        //and will delete stories after a day it was posted if provided with time

        //METHOD 1

        //First we will make that fragment
        //In the layout xml, we have an empty frame layout with id fl
        //To that fragment, we will attach an object of story_library

        FragmentManager fm = getSupportFragmentManager();   // A fragment manager [it is an parameter for the story library]

        //status_handler is a custom class in the library.
        //It takes the context, fragment id and the fragment manager as input parameters
        //This object created, then can be used to add stories
        //Those stories will be handled automatically.
        //i.e. they will be displayed in a horizontal recycler view and will act just as instagram stories

        final status_handler sh = new status_handler(ctx, R.id.fl, fm);

        //The stories added in this method are stored even after the app is closed
        //So you don't need to add the same story twice on every start-up
        //Even if you do, it won't cause any errors or unwanted behaviour

        //The only condition for the stories to work perfectly is that the link of the image for each story should be different

        //They work exactly as a normal instagram story would work

        //Now, we can add stories in 2 ways.

        //First we can either provide a time
        //Then the story will die and won't be showed a day after.
        //If the time is provided, it will also be shown when the story is viewed

        //Important note:
        //If time is given it should be in the format => "dd/MM/yyyy HH:mm:ss"
        //and as a string
        //If the format is different, it will throw a Parse Exception

        sh.addStory("Page 1", "https://cdn.pixabay.com/photo/2020/06/28/14/45/butterfly-5349558_960_720.jpg", "06/07/2020 22:02:12");
        sh.addStory("Page 2", "https://cdn.pixabay.com/photo/2020/06/29/21/13/city-5354477_960_720.jpg", "06/07/2020 22:05:12" );
        sh.addStory("Page 3", "https://cdn.pixabay.com/photo/2020/06/23/12/22/forest-5332379_960_720.jpg", "06/07/2020 22:03:12");

        //If we don't want to add a time and want the story to last forever
        //We can do that too

        sh.addStory("Page 1", "https://cdn.pixabay.com/photo/2020/06/28/21/41/cat-5350785_1280.jpg");
        sh.addStory("Page 1", "https://cdn.pixabay.com/photo/2020/06/30/22/18/cat-5357759_1280.jpg");
        sh.addStory("Page 2", "https://cdn.pixabay.com/photo/2020/03/07/05/18/coffee-4908764_960_720.jpg");
        sh.addStory("Page 3", "https://cdn.pixabay.com/photo/2020/03/04/05/57/key-4900643_960_720.jpg");


        //METHOD 2
        //If you don't want to use the provided recycler view
        //You can create your own and attach our story module to it
        //To do that you can do as follows
        //In this example, we have made a button
        //Which when clicked launches a group of stories.

        //Details of how the data is to be passed is given in the function storyProcessor

        Button go = findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                storyProcessor();
            }
        });

        //The METHOD 1 is more encapsulated and gives you more functionality
        //But if you need more control over what is going on, you have the option of METHOD 2

        //If there are any doubts regarding the library,
        //Hit me up on ridham.bhat@gmail.com

    }

    private void storyProcessor(){

        ArrayList<ArrayList<String>> alllinks=new ArrayList<>();
        ArrayList<String> from=new ArrayList<>();
        ArrayList<ArrayList<String>> time=new ArrayList<>();
        int position = 0; //Define which page should be opened [each page contains all stories from a particular sender]

        //SampleData
        //-------------------------------------------------------------------------------------------------------------------------
        from.add("Page One");
        from.add("Page Two");
        from.add("Page Three");

        ArrayList<String> pp = new ArrayList<>();
        pp.add("https://cdn.pixabay.com/photo/2020/06/29/21/13/city-5354477_960_720.jpg");
        pp.add("https://cdn.pixabay.com/photo/2020/06/23/12/22/forest-5332379_960_720.jpg");
        pp.add("https://cdn.pixabay.com/photo/2020/06/28/21/41/cat-5350785_1280.jpg");
        alllinks.add(pp);

        pp = new ArrayList<>();
        pp.add("https://cdn.pixabay.com/photo/2020/03/07/05/18/coffee-4908764_960_720.jpg");
        pp.add("https://cdn.pixabay.com/photo/2020/03/04/05/57/key-4900643_960_720.jpg");
        alllinks.add(pp);

        pp = new ArrayList<>();
        pp.add("https://cdn.pixabay.com/photo/2020/06/17/17/01/underwater-5310424_960_720.jpg");
        pp.add("https://cdn.pixabay.com/photo/2020/06/30/22/18/cat-5357759_1280.jpg");
        pp.add("https://cdn.pixabay.com/photo/2020/07/02/08/01/stork-5362181_960_720.jpg");
        alllinks.add(pp);


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

        //SampleData
        //-------------------------------------------------------------------------------------------------------------------------

        //First we make a new intent that will start a new Activity which will display all the stories
        Intent it=new Intent(ctx, stories_main.class);

        //To launch the story module
        //We have a few parameters

        // Parameter 1 with label "url"
        //It is an ArrayList<ArrayList<String>>
        //These contain the links of all stories categorically
        //The inner ArrayList contains link of stories from the same sender
        //And the outer arraylist is just for multiple senders
        //for example if "Person1" has 2 stories with "link1" and "link2"
        //and "Person2" has 3 stories with "link3", "link4" and "link5"
        //then the arraylist will look like
        //< <"link1", "link2"> , <"link3", "link4", "link5"> >
        it.putExtra("url",alllinks);

        //Parameter 2 is with label "allIndex"
        //It stores that an individual from page should start from which story number
        //If we take the above example
        //and the stories with "link1", "link3", "link4" are already viewed
        //Then if the stories are viewed the next time, they should start from "link2" and "link5" respectively
        //So, "allIndex" expects an array of the size same as the from list
        //Which contains the starting points
        //SO in above example an array like [1,2] should be passed
        //INDEXING STARTS FROM ZERO
        //THAT IS WHY AN ARRAY OF ZEROS ARE PASSES IN GIVEN SAMPLE AS STATES ARE NOT STORED
        it.putExtra("allIndex",sortedPosition);

        //Parameter 3 is with the label "position"
        //It stores the value which signifies which page sgould be opened first
        //If you want that the stories should open from page of "person 2"
        //Then value of "position" should be 1 //as indexing starts from 0
        it.putExtra("index",position);

        //Parameter 4 is with the label "from"
        //It is an arraylist that will contain all the labels of senders
        //Do note that they should be in the same order in which links are
        //i.e. if the links of "person 1" are placed first in the "url" parameter
        //then, "person 1" should be first in the "from" parameter too
        it.putExtra("from",from);

        //Parameter 5 is with the label "time"
        //It is an Arrayist<ArrayList<String>> which runs parallel to the "url" parameter
        //The times of the story with corresponding links are placed in this Arrayist<ArrayList<String>>
        //Do remember, you are just passing a string and it will be shown as it is
        //No calculations of time are performed on it and stories won't die even after a day has passed
        it.putExtra("time",time);

        //With all parameters loaded, you can start the story activity now.
        startActivityForResult(it,0);

    }
}

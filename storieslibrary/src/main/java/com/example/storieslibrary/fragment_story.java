package com.example.storieslibrary;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class fragment_story extends Fragment {

    int count;
    int pageId;
    static ArrayList<String> links;
    static ArrayList<String> timelist;
    static int progress=0;
    static int currentindex;
    static ObjectAnimator pillanim[];
    static ProgressBar pills[];
    static ImageView image;
    private boolean isSpeakButtonLongPressed = false;
    static RelativeLayout layout;
    static boolean wasInFocus=false;
    static boolean isInFocus=true;
    static View view;
    float oldX,oldY,newX,newY;
    static Context ctx;
    static boolean record=false;
    static int maxId;
    static TextView from;
    static TextView time;
    static LinearLayout progressBar;



    public static fragment_story newInstance(int maxId, int pageId, int currentIndex, ArrayList<String> links,String from,ArrayList<String> time) {
        fragment_story fragmentFirst = new fragment_story();
        Bundle args = new Bundle();
        args.putInt("currentIndex", currentIndex);
        args.putInt("pageId",pageId);
        args.putStringArrayList("links",links);
        args.putInt("maxId",maxId);
        args.putString("from",from);
        args.putStringArrayList("time",time);
        fragmentFirst.setArguments(args);

        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //view holds the

        view= inflater.inflate(R.layout.fragment_story, container, false);
        ctx=view.getContext();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i("TAG", "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.i("TAG", "onKey Back listener is working!!!");
                    //getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    pause();
                    getActivity().finish();
                    return true;
                }
                //if (keyCode==KeyEvent.K)
                return false;
            }
        });
        View touch=view.findViewById(R.id.touch);
        currentindex=getArguments().getInt("currentIndex");
        links=getArguments().getStringArrayList("links");
        timelist=getArguments().getStringArrayList("time");
        pageId=getArguments().getInt("pageId");
        maxId=getArguments().getInt("maxId");
        //links= getIntent().getStringArrayListExtra("url");
        //currentindex=getIntent().getIntExtra("index",0);
        count=links.size();
        from=view.findViewById(R.id.from);
        time=view.findViewById(R.id.time);
        from.setText(getArguments().getString("from"));
        progressBar=view.findViewById(R.id.progress_bar_2);
        image=view.findViewById(R.id.image_view_2);
        image.setImageResource(R.drawable.loading);
        LayoutInflater li=LayoutInflater.from(ctx);
        pills=new ProgressBar[count];
        pillanim=new ObjectAnimator[count];
        for(int i=0;i<count;i++){
            View view2=li.inflate(R.layout.story_pill,progressBar,false);
            pills[i]=(ProgressBar)view2;
            if(i<(currentindex))
                pills[i].setProgress(100);
            progressBar.addView(view2);
            pillanim[i]=anim(pills[i]);
        }

        touch.setOnLongClickListener(speakHoldListener);
        touch.setOnTouchListener(TouchListener);

        start();


        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void start(){
        //imageLoader.displayImage(links.get(currentindex),image);
        String t=(links.get(currentindex).substring(22));
        if(links.get(currentindex).substring(0,8).equals("file:///")){
            AssetManager assetManager = ctx.getAssets();
            try (
                    //declaration of inputStream in try-with-resources statement will automatically close inputStream
                    // ==> no explicit inputStream.close() in additional block finally {...} necessary
                    InputStream inputStream = assetManager.open(links.get(currentindex).substring(22));
            ) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(bitmap);
            } catch (IOException ex) {
                //Toast.makeText(ctx,"path - "+t,Toast.LENGTH_SHORT).show();
            }
        }
        else
        Picasso.with(ctx)
                .load(links.get(currentindex))
                .placeholder(R.drawable.loading)
                .into(image);
        pillanim[currentindex].start();
        time.setText(timelist.get(currentindex));
//        String aa= SaveSharedPreference.getVisitedlinks(ctx);
//        aa=aa+" "+links.get(currentindex);
//        SaveSharedPreference.setVisitedlinks(ctx,aa);
        Log.d("Pic","Piccasso");
    }



    public static void pause(){
        pillanim[currentindex].cancel();
        pills[currentindex].setProgress(0);
    }

    public static void Hide(){
        progressBar.setVisibility(View.INVISIBLE);
        from.setVisibility(View.INVISIBLE);
        time.setVisibility(View.INVISIBLE);
    }

    public static void Visible(){
        progressBar.setVisibility(View.VISIBLE);
        from.setVisibility(View.VISIBLE);
        time.setVisibility(View.VISIBLE);
    }


    ObjectAnimator anim(final ProgressBar pb){
        final ObjectAnimator animation = ObjectAnimator.ofInt(pb, "progress", 0, 100);
        //Log.d("progress","pg = "+pb.getProgress());
        animation.setDuration(5000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) { }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onAnimationEnd(Animator animator) {

                if(pills[currentindex].getProgress()==100){
                    currentindex++;
                    Gonext();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                //pillanim[currentindex]=anim(pb);
            }

            @Override
            public void onAnimationRepeat(Animator animator) { }
        });
        return  animation;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void Gonext(){
        if(currentindex<count){
            start();
        }
        else
        {
            for(int i=0;i<count;i++){
                pills[i].setProgress(0);
            }
            currentindex=0;
            Log.d("kill","kill");
            stories_main.goNextFrame(currentindex);
        }
    }

    private View.OnLongClickListener speakHoldListener = new View.OnLongClickListener() {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean onLongClick(View pView) {
            // Do something when your hold starts here.
            pillanim[currentindex].pause();
            Hide();
            isSpeakButtonLongPressed = true;
            return true;
        }
    };


    private View.OnTouchListener TouchListener = new View.OnTouchListener() {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean onTouch(View pView, MotionEvent pEvent) {
            pView.onTouchEvent(pEvent);
            // We're only interested in when the button is released.
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int height = size.y;

            if (pEvent.getAction() == MotionEvent.ACTION_DOWN){
                if (!record){
                    oldX=pEvent.getX();
                    oldY=pEvent.getY();
                    record=true;
                }

                //Log.d("Swipes","Motion Down");
            }

            if (pEvent.getAction() == MotionEvent.ACTION_UP
            ) {
                record=false;
                newX=pEvent.getX();
                newY=pEvent.getY();
                Log.d("up","UUPP");

                if (isSpeakButtonLongPressed) {
                    // Do something when the button is released.
                    pillanim[currentindex].resume();
                    Visible();
                    isSpeakButtonLongPressed = false;
                }
                else if(Math.abs(newX-oldX)>50){
                    if(oldX<newX) {
                        goLeftPage();
                        //Toast.makeText(ctx, "ToLeftPage", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        //Toast.makeText(ctx, "ToRightPage", Toast.LENGTH_SHORT).show();
                        goRightPage();
                    }
                }
                else{

                    float xpoint=pEvent.getRawX();
                    if((width/2)>=xpoint){
                        goLeft();
                    }
                    else {
                        goRight();
                    }
                }
            }
//            if (pEvent.getAction()==MotionEvent.ACTION_CANCEL){
//                Log.d("cancel","cancel");
//                record=false;
//                if(oldX<(width/2)){
//                    goRightPage();
//                }
//                else{
//                    goLeftPage();
//                }
//            }
            return false;
        }
    };

    private void goLeftPage(){
        pause();
        stories_main.goPrevFrame(currentindex);
    }

    private void goRightPage(){
        Log.d("Array","Right");
        pause();
        stories_main.goNextFrame(currentindex);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void goLeft(){
        pillanim[currentindex].cancel();
        pills[currentindex].setProgress(0);
        currentindex--;
        if(currentindex<0) {
            currentindex = 0;
            //pillanim[currentindex].start();
            goLeftPage();
        }
        else{
            pills[currentindex].setProgress(0);
            start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void goRight(){
        pillanim[currentindex].cancel();
        pills[currentindex].setProgress(100);
        currentindex++;
        if(currentindex<count)
            Gonext();
        else{
            for(int i=0;i<count;i++){
                pills[i].setProgress(0);
            }
            currentindex=0;
            Log.d("kill","kill");
            //getActivity().finish();
            goRightPage();
        }
            //go to next page
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStop() {
        super.onStop();
        pillanim[currentindex].pause();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();
        pillanim[currentindex].resume();
        Visible();
    }


}

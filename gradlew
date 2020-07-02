package com.example.turuf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class game extends AppCompatActivity {

    static boolean cardSelected=false;
    static View selectedView;
    static int selectedCardId;
    static int selectedCardPos;
    static RecyclerView myCards;
    static List<Integer> my_card_ids;
    static String color="";
    static ImageView current_cards[];
    static Button playCard,removeSelection;
    static Context ctx;
    static String myPartner="";
    static int currentPlayerId;
    static int currentGameStatus;
    static String originalPlayerOrder[];
    static String playerOrder[];
    static int playerOrderViaInt[];
    static String deck;
    static String playerRelativePos[];
    static TextView game_message;
    static String myUsername;
    static String host;
    static String suits[]={"spades","hearts","clubs","diamonds"};
    static int color_card_id;
    static int myPos;
    static int currentMoveNumber=0;
    static int opponentScore=0;
    static int ourScore=0;
    static RelativeLayout playerLayout[];
    static TextView playerRelNameView[];
    static ArrayList<Integer> playedCards;
    static ArrayList<Integer> remainingCards;
    static boolean isMoveDone[];
    static boolean shouldTaskBeRepeated=false;
    static boolean shouldUHandlerRun=false;
    static boolean shouldNewGameBeSet=false;
    static ImageView playerCardsToRemove[][];

    ImageView colorCard,colorSuit;
    TextView teamScore;

    private int mInterval = 3000; // 5 seconds by default, can be changed later
    private Handler mHandler;
    private Handler uHandler;
    private View decorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx=this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        decorView=getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility==0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        setContentView(R.layout.activity_game);
        game_message=findViewById(R.id.game_message);
        currentPlayerId=0;
        host=SaveSharedPreference.getHost(ctx);
        removeSelection=findViewById(R.id.remove_selection);
        playCard=findViewById(R.id.play_card);
        playerRelativePos=new String[4];
        teamScore=findViewById(R.id.your_team_score_value);
        teamScore.setText("0");
        playerCardsToRemove=new ImageView[5][13];
        playerCardsToRemove[2][0]=findViewById(R.id.p2c13);
        playerCardsToRemove[2][1]=findViewById(R.id.p2c1);
        playerCardsToRemove[2][2]=findViewById(R.id.p2c12);
        playerCardsToRemove[2][3]=findViewById(R.id.p2c2);
        playerCardsToRemove[2][4]=findViewById(R.id.p2c11);
        playerCardsToRemove[2][5]=findViewById(R.id.p2c3);
        playerCardsToRemove[2][6]=findViewById(R.id.p2c10);
        playerCardsToRemove[2][7]=findViewById(R.id.p2c4);
        playerCardsToRemove[2][8]=findViewById(R.id.p2c9);
        playerCardsToRemove[2][9]=findViewById(R.id.p2c5);
        playerCardsToRemove[2
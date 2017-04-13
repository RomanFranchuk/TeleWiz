package com.arcticwolf.telewiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_main;

    private Toolbar toolbar;
    private String nameOfChannel;
    private int logo;
    private int logoForTable;
    private String channelLink;
    private DrawerLayout drawerLayout;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private String channelTitle;
    private int postn;
    private Boolean nextWeek;
    private TextView userNameView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreference();
        setContentView(LAYOUT);


        initUI();

        gridViewListen();

        initNavigationView();

        connection();

    }

    private void initUI(){
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();

        gridView = (GridView) findViewById(R.id.gridView);
        AsynMain asynMain = new AsynMain();
        asynMain.execute();
    }

    private void gridViewListen() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);

                switch (position) {
                    case 0:
                        if (connection()) {
                            logo = R.drawable.notification_oneplusone;
                            logoForTable = R.drawable.channel_oneplusone;
                            nextWeek = true;
                            nameOfChannel = "1+1";
                            postn = 0;
                            channelLink = "http://1plus1.ua";
                            channelTitle = "1+1";

                            intent.putExtra("logo", logo);
                            intent.putExtra("logoForTable", logoForTable);
                            intent.putExtra("nextWeek", nextWeek);
                            intent.putExtra("nameOfChannel", nameOfChannel);
                            //channel position for asyncTask
                            intent.putExtra("postn", postn);
                            intent.putExtra("channelLink", channelLink);
                            intent.putExtra("channelTitle", channelTitle);
                            startActivity(intent);
                        }
                        break;
                    case 1:
                        if (connection()) {
                            logo = R.drawable.notification_tet;
                            logoForTable = R.drawable.channel_tet;
                            nextWeek = false;
                            nameOfChannel = "tet";
                            postn = 1;
                            channelLink = "http://tet.tv/uk";
                            channelTitle = "TET";

                            intent.putExtra("logo", logo);
                            intent.putExtra("logoForTable", logoForTable);
                            intent.putExtra("nextWeek", nextWeek);
                            intent.putExtra("nameOfChannel", nameOfChannel);
                            //channel position for asyncTask
                            intent.putExtra("postn", postn);
                            intent.putExtra("channelLink", channelLink);
                            intent.putExtra("channelTitle", channelTitle);
                            startActivity(intent);
                        }
                        break;
                    case 2:
                        if (connection()) {
                            logo = R.drawable.notification_nlo;
                            logoForTable = R.drawable.channel_nlo;
                            nextWeek = true;
                            nameOfChannel = "nloTv";
                            postn = 2;
                            channelLink = "http://nlotv.com";
                            channelTitle = "НЛО ТВ";

                            intent.putExtra("logo", logo);
                            intent.putExtra("logoForTable", logoForTable);
                            intent.putExtra("nextWeek", nextWeek);
                            intent.putExtra("nameOfChannel", nameOfChannel);
                            //channel position for asyncTask
                            intent.putExtra("postn", postn);
                            intent.putExtra("channelLink", channelLink);
                            intent.putExtra("channelTitle", channelTitle);
                            startActivity(intent);
                        }
                        break;
                    case 3:
                        if (connection()) {
                            logo = R.drawable.notification_novyi;
                            logoForTable = R.drawable.channel_novyi;
                            nextWeek = false;
                            nameOfChannel = "novyTv";
                            postn = 3;
                            channelLink = "http://novy.tv/ua";
                            channelTitle = "Новий канал";

                            intent.putExtra("logo", logo);
                            intent.putExtra("logoForTable", logoForTable);
                            intent.putExtra("nextWeek", nextWeek);
                            intent.putExtra("nameOfChannel", nameOfChannel);
                            //channel position for asyncTask
                            intent.putExtra("postn", postn);
                            intent.putExtra("channelLink", channelLink);
                            intent.putExtra("channelTitle", channelTitle);
                            startActivity(intent);
                        }

                        break;
                    case 4:
                        if (connection()) {
                            logo = R.drawable.notification_twoplustwo;
                            logoForTable = R.drawable.channel_twoplustwo;
                            nextWeek = true;
                            nameOfChannel = "2+2";
                            postn = 4;
                            channelLink = "http://2plus2.ua/";
                            channelTitle = "2+2";

                            intent.putExtra("logo", logo);
                            intent.putExtra("logoForTable", logoForTable);
                            intent.putExtra("nextWeek", nextWeek);
                            intent.putExtra("nameOfChannel", nameOfChannel);
                            //channel position for asyncTask
                            intent.putExtra("postn", postn);
                            intent.putExtra("channelLink", channelLink);
                            intent.putExtra("channelTitle", channelTitle);
                            startActivity(intent);
                        }
                        break;
                    case 5:
                        if (connection()) {
                            logo = R.drawable.notification_k1;
                            logoForTable = R.drawable.channel_k_one;
                            nextWeek = true;
                            nameOfChannel = "k1";
                            postn = 5;
                            channelLink = "http://k1.ua";
                            channelTitle = "K1";

                            intent.putExtra("logo", logo);
                            intent.putExtra("logoForTable", logoForTable);
                            intent.putExtra("nextWeek", nextWeek);
                            intent.putExtra("nameOfChannel", nameOfChannel);
                            //channel position for asyncTask
                            intent.putExtra("postn", postn);
                            intent.putExtra("channelLink", channelLink);
                            intent.putExtra("channelTitle", channelTitle);
                            startActivity(intent);
                        }
                        break;
                    default:
                        break;
                }
            }

        });
    }

    private void getPreference(){
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
        String themeName = pref.getString("theme", "Grey and Darker Grey");
        String language = pref.getString("languages", "English");
        if (themeName.equals("Grey and Darker Grey")){
            setTheme(R.style.Grey);
        } else if (themeName.equals("Red and Claret")) {
            setTheme(R.style.RedTheme);
        }else if (themeName.equals("Violet and Yankees Blue")) {
            setTheme(R.style.VioletTheme);
        }

        LanguageHelper languageHelper = new LanguageHelper();
        languageHelper.changeLocal(this.getResources(), language);
    }


    private ArrayList<Channel> getData() {
        final ArrayList<Channel> arrayChannels = new ArrayList<>();
        String  name;
        Channel channel;

        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        TypedArray imgNames = getResources().obtainTypedArray(R.array.channels_name);

        for (int i = 0; i < imgs.length(); i++) {

            name = imgNames.getString(i);
            channel = new Channel(i, name);

            arrayChannels.add(channel);
        }
        imgs.recycle();
        imgNames.recycle();
        return arrayChannels;
    }

    private boolean connection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if ((connectivityManager.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED) ||
            connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
                return true;
            }else if(
                    connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                            connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

                Toast.makeText(this, R.string.no_connection, Toast.LENGTH_LONG).show();
                return false;
        }
        return false;
    }


    private void initNavigationView() {

        String[] ItemMenu;
        ListView drawerList;
        ItemMenu = getResources().getStringArray(R.array.menu_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();

        View listHeaderView = inflater.inflate(R.layout.nav_header, null, false);

        CircleImageView circleImageView = (CircleImageView) listHeaderView.findViewById(R.id.header_user_image);

        userNameView = (TextView) listHeaderView.findViewById(R.id.userName);
        TextView userEmailView = (TextView) listHeaderView.findViewById(R.id.userEmail);
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user!= null){
            userEmailView.setText(firebaseAuth.getCurrentUser().getEmail());
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }
            });
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User s = dataSnapshot.child("users").child(user.getUid()).getValue(User.class);

                userNameView.setText(s.getName());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        drawerList.addHeaderView(listHeaderView);

        ActionBarDrawerToggle barDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open, R.string.close);
        barDrawerToggle.syncState();

        barDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(barDrawerToggle);

        drawerList.setAdapter(new NavigationView(this, R.layout.navigation_item_layout,ItemMenu));

        drawerList.setOnItemClickListener(new DrawerItemClickListener());

    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }
    @Override
    public void onBackPressed(){
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }



    private void selectItem(int position) {

        switch (position){
            case 1:
                Intent intentMain = new Intent(this, MainActivity.class);
                startActivity(intentMain);
                break;
            case 2:
                Intent intentFavorite = new Intent(this, FavoriteChannels.class);
                startActivity(intentFavorite);
                break;
            case 3:
                Intent intentSelected = new Intent(this, SelectedShows.class);
                startActivity(intentSelected);
                break;
            case 4:
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


        private class AsynMain extends AsyncTask <Void,Void,Void>{

            @Override
            protected Void doInBackground(Void... params) {

                gridAdapter = new GridViewAdapter(MainActivity.this, R.layout.grid_item_layout, getData());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                gridView.setAdapter(gridAdapter);
            }
        }


}

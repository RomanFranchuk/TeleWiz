package com.arcticwolf.telewiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class FavoriteChannels extends AppCompatActivity {

    ArrayList<Channel> favoriteList = new ArrayList<Channel>();
    private ArrayList<Channel> arrayChannels = new ArrayList<>();
    private ArrayList<Channel> confirmRemove = new ArrayList<>();
    private ArrayList<Channel> confirmAdd = new ArrayList<>();
    private String channelLink;
    private Toolbar toolbar;
    private int logo;
    private int logoForTable;
    private String nameOfChannel;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private View imageView;
    private Button buttonConfirm;
    private Button cancelButton;
    private static final int LAYOUT = R.layout.activity_favorite;
    private String channelTitle;
    private int postn;
    private Boolean nextWeek;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreference();
        setContentView(LAYOUT);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.favorite_channels);

        setSupportActionBar(toolbar);



        imageView = (ImageView) findViewById(R.id.image);
        buttonConfirm = (Button) findViewById(R.id.button_confirmation);
        cancelButton = (Button) findViewById(R.id.buttton_cancel);
        gridView = (GridView) findViewById(R.id.gridViewFavorite);

        loadFavoriteChannels();

        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, favoriteList);
        gridView.setAdapter(gridAdapter);
        gridViewListen();

        initNavigationView();

    }



    private void gridViewListen(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Channel channel = favoriteList.get(position);
                Intent intent = new Intent(FavoriteChannels.this, ScheduleActivity.class);
                int channelId = channel.getId();
                switch (channelId) {
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


    private ArrayList<Channel> getData() {
        String  name;
        int id;

        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        TypedArray imgNames = getResources().obtainTypedArray(R.array.channels_name);
        for (int i = 0; i < imgs.length(); i++) {
            id = i;

            name = imgNames.getString(i);
            Channel channel = new Channel(id, name);

            arrayChannels.add(channel);
        }

        return arrayChannels;
    }

    public void onClickButtonConfirm(View view){
        if (!confirmRemove.isEmpty()){
            removeConfirmChannelsFB(confirmRemove);
        }else{
            addConfirmChannelsFB(confirmAdd);
        }
        final Intent intent = new Intent(this, FavoriteChannels.class);
        intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //remove channels from the phone
    private void removeConfirm(ArrayList<Channel> arrayList){
        favoriteList.removeAll(arrayList);
        SharedPreferences sharedPreferences = getSharedPreferences("favorite", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String favList = gson.toJson(favoriteList);

        editor.putString("favList", favList);

        editor.apply();
    }

    private void removeConfirmChannelsFB(ArrayList<Channel> arrayList){

        for (int i = 0; i<=arrayList.size() -1; i++){

            Channel channel = arrayList.get(i);
            int id = channel.getId();

        Query applesQuery = databaseReference.child("users").child(user.getUid()).child("channels").orderByChild("id").equalTo(id);

        applesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot showSnapshot : dataSnapshot.getChildren()) {

                    showSnapshot.getRef().removeValue();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }

    }

    //save channels to the phone
    private void addConfirm(ArrayList<Channel> arrayList){
        favoriteList.addAll(arrayList);
        SharedPreferences sharedPreferences = getSharedPreferences("favorite", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String favList = gson.toJson(favoriteList);

        editor.putString("favList", favList);

        editor.apply();
    }

    private void addConfirmChannelsFB(ArrayList<Channel> arrayList){

        for(int i = 0; i <= arrayList.size()-1; i++){
            Channel channel = arrayList.get(i);
            String id = databaseReference.push().getKey();
                databaseReference.child("users").child(user.getUid()).child("channels").child(id).setValue(channel);
        }


    }
    private void removeFavorite(int position){
        Channel channel =  favoriteList.get(position);

        confirmRemove.add(channel);

    }

    private void addFavorite(int position){
        Channel channel = arrayChannels.get(position);
        if (!favoriteList.contains(channel)){
            confirmAdd.add(channel);
        }
    }

    public void cancelButton(View view){
        Intent intent = new Intent(this, FavoriteChannels.class);
        startActivity(intent);
    }

    //load favorite channel from phone
    private void loadFavorite(){

        SharedPreferences preferences = getSharedPreferences("favorite", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        String favList = preferences.getString("favList", "");
        if (!favList.isEmpty()){
        Type type = new TypeToken<List<Channel>>(){}.getType();
        favoriteList = gson.fromJson(favList, type);
        }
    }

    private void loadFavoriteChannels(){

        databaseReference.child("users").child(user.getUid()).child("channels").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot showSnapshot : dataSnapshot.getChildren()) {
                    Channel ch = showSnapshot.getValue(Channel.class);

                    favoriteList.add(ch);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void initNavigationView() {

        String[] ItemMenu;
        ListView drawerList;
        ItemMenu = getResources().getStringArray(R.array.menu_items);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();

        View listHeaderView = inflater.inflate(R.layout.nav_header, null, false);

        CircleImageView circleImageView = (CircleImageView) listHeaderView.findViewById(R.id.header_user_image);

        final TextView userNameView = (TextView) listHeaderView.findViewById(R.id.userName);
        TextView userEmailView = (TextView) listHeaderView.findViewById(R.id.userEmail);
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user!= null){
            userEmailView.setText(firebaseAuth.getCurrentUser().getEmail());
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(FavoriteChannels.this, ProfileActivity.class));
                }
            });
        }

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


    private void getPreference(){
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
        String themeName = pref.getString("theme", "Grey and Darker Grey");
        String language = pref.getString("languages", "English" );
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_favorite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){
            case R.id.add_button:

                gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, getData());
                gridView.setAdapter(gridAdapter);
                buttonConfirm.setText(R.string.add);
                buttonConfirm.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position){
                            case 0:
                                gridView.setPressed(true);
                                imageView = gridView.getChildAt(position);
                                imageView.setVisibility(View.INVISIBLE);
                                addFavorite(position);
                                break;
                            case 1:
                                gridView.setPressed(true);
                                imageView = gridView.getChildAt(position);
                                imageView.setVisibility(View.INVISIBLE);
                                addFavorite(position);
                                break;
                            case 2:
                                gridView.setPressed(true);
                                imageView = gridView.getChildAt(position);
                                imageView.setVisibility(View.INVISIBLE);
                                addFavorite(position);
                                break;
                            case 3:
                                gridView.setPressed(true);
                                imageView = gridView.getChildAt(position);
                                imageView.setVisibility(View.INVISIBLE);
                                addFavorite(position);
                                break;
                            case 4:
                                gridView.setPressed(true);
                                imageView = gridView.getChildAt(position);
                                imageView.setVisibility(View.INVISIBLE);
                                addFavorite(position);
                                break;
                            case 5:
                                gridView.setPressed(true);
                                imageView = gridView.getChildAt(position);
                                imageView.setVisibility(View.INVISIBLE);
                                addFavorite(position);
                                break;
                            default:
                                break;
                        }
                    }
                });
                break;
            case R.id.remove_button:
                gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, favoriteList);
                gridView.setAdapter(gridAdapter);
                buttonConfirm.setText(R.string.remove);
                buttonConfirm.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                gridView.setPressed(true);
                                imageView = gridView.getChildAt(position);
                                imageView.setVisibility(View.INVISIBLE);
                                removeFavorite(position);
                                break;
                            case 1:
                                gridView.setPressed(true);
                                imageView = gridView.getChildAt(position);
                                imageView.setVisibility(View.INVISIBLE);
                                removeFavorite(position);
                                break;
                            case 2:
                                gridView.setPressed(true);
                                imageView = gridView.getChildAt(position);
                                imageView.setVisibility(View.INVISIBLE);
                                removeFavorite(position);
                                break;
                            case 3:
                                gridView.setPressed(true);
                                imageView = gridView.getChildAt(position);
                                imageView.setVisibility(View.INVISIBLE);
                                removeFavorite(position);
                                break;
                            case 4:
                                gridView.setPressed(true);
                                imageView = gridView.getChildAt(position);
                                imageView.setVisibility(View.INVISIBLE);
                                removeFavorite(position);
                                break;
                            case 5:
                                gridView.setPressed(true);
                                imageView = gridView.getChildAt(position);
                                imageView.setVisibility(View.INVISIBLE);
                                removeFavorite(position);
                                break;
                            default:
                                break;
                        }
                    }
                });
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {
        switch (position){
            case 1:
                Intent intentMain = new Intent(this, MainActivity.class);
                startActivity(intentMain);
                break;
            case 2:
                Intent intentFavChan = new Intent(this, FavoriteChannels.class);
                startActivity(intentFavChan);
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

}




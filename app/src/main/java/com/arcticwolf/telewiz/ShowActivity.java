package com.arcticwolf.telewiz;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.arcticwolf.telewiz.parse.AsyncKOnePicture;
import com.arcticwolf.telewiz.parse.AsyncNloPicture;
import com.arcticwolf.telewiz.parse.AsyncNovyPicture;
import com.arcticwolf.telewiz.parse.AsyncPlusOnePicture;
import com.arcticwolf.telewiz.parse.AsyncPlusTwoPicture;
import com.arcticwolf.telewiz.parse.AsyncTetPicture;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.show_activity;

    private ImageView imageView;
    private ImageView logoView;
    private ProgressBar progressBar;
    private String showTime;
    private Spinner spinner;
    private Bitmap image;
    public int logo;
    private int logoForTable;
    private ImageButton imageButton;
    private ArrayList<ShowInfo> showList = new ArrayList<ShowInfo>();
    private String showTitle;
    private String showDate;
    private String description;
    private String imgUrl;
    private String url;
    private String showUrl;
    private String channelLink;
    private String channelTitle;
    private Toolbar toolbar;
    private long remindInMili = 1800000;
    private String dateToParse;
    private int t;
    private TextView textViewDesc;
    private int postn;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private TextView userNameView;
    private DatabaseReference databaseReference;
    private boolean isPlay = true;



    public boolean checkForSaved;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreference();
        setContentView(LAYOUT);


        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.show_info);
        setSupportActionBar(toolbar);

        TextView textView = (TextView) findViewById(R.id.textTitle);
        TextView textViewTime = (TextView) findViewById(R.id.textViewTime);
        spinner = (Spinner) findViewById(R.id.spinner);
        imageButton = (ImageButton) findViewById(R.id.buttomSave);
        imageView = (ImageView) findViewById(R.id.imageView);
        logoView = (ImageView) findViewById(R.id.logo_channel);
        textViewDesc = (TextView) findViewById(R.id.description);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);

        progressBar.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.INVISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        getExtras();

        textViewTime.setText(showTime);
        textView.setText(showTitle);
        logoView.setImageResource(logoForTable);

        channel(postn);

        initSpinner();
        initNavigationView();
    }

    private void getExtras(){
        logo = getIntent().getExtras().getInt("logo");
        logoForTable = getIntent().getExtras().getInt("logoForTable");
        showTitle = getIntent().getExtras().getString("title");
        showTime = getIntent().getExtras().getString("time");
        description = getIntent().getExtras().getString("description");
        imgUrl = getIntent().getExtras().getString("imgUrl");
        showDate = getIntent().getExtras().getString("showDate");
        dateToParse = getIntent().getExtras().getString("dateToParse");
        channelLink = getIntent().getExtras().getString("channelLink");
        channelTitle = getIntent().getExtras().getString("channelTitle");
        showUrl = getIntent().getExtras().getString("showUrl");
        //?checkForSaved
        checkForSaved = getIntent().getExtras().getBoolean("checkForSaved");
        postn = getIntent().getExtras().getInt("postn");
    }

    private void initSpinner(){
        String[] timeRemind = getApplicationContext().getResources().getStringArray(R.array.timer);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_item,  R.id.spinner_text, timeRemind);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        remindInMili = 1800000;
                        break;
                    case 1:
                        remindInMili = 3600000;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void channel(int position){
        switch (position){
            case 0:
                AsyncPlusOnePicture plusOnePicture = new AsyncPlusOnePicture(this, showUrl);
                url = channelLink.concat(showUrl);
                plusOnePicture.execute();
                break;
            case 1:
                AsyncTetPicture tetPicture = new AsyncTetPicture(this,showUrl);
                url = channelLink.concat(showUrl);
                tetPicture.execute();
                break;
            case 2:
                AsyncNloPicture nloPicture = new AsyncNloPicture(this,showUrl);
                url = channelLink.concat(showUrl);
                nloPicture.execute();
                break;
            case 3:
                AsyncNovyPicture novyPicture = new AsyncNovyPicture(this, showUrl);
                url = showUrl;
                novyPicture.execute();
                break;
            case 4:
                AsyncPlusTwoPicture twoPicture = new AsyncPlusTwoPicture(this, showUrl);
                url = showUrl;
                twoPicture.execute();
                break;
            case 5:
                AsyncKOnePicture kOnePicture = new AsyncKOnePicture(this,showUrl);
                url = channelLink.concat(showUrl);
                kOnePicture.execute();
                break;
            default:
                break;
        }
    }


    public void openWebsite(View view){
        Uri uri = Uri.parse(url);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    public void parsePicture(Bitmap bitmap, String desc){

        image = bitmap;

        if (!desc.equals("")){
            description = desc;
        }else if (description.equals("")){
            description = getResources().getString(R.string.not_available);
        }

        textViewDesc.setText(description);

        imageView.setImageBitmap(image);
        imageView.setVisibility(View.VISIBLE);

        progressBar.setVisibility(View.GONE);

        onClickButton();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_bar, menu);
        MenuItem menuItem = menu.findItem(R.id.data);
        menuItem.setTitle(showDate);
        return true;

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (image !=null) {
            image.recycle();
        }
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
        }else if (themeName.equals("Violet and Yankees BlueContext context")) {
            setTheme(R.style.VioletTheme);
        }

        LanguageHelper languageHelper = new LanguageHelper();
        languageHelper.changeLocal(this.getResources(), language);
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

        userNameView = (TextView) listHeaderView.findViewById(R.id.userName);
        TextView userEmailView = (TextView) listHeaderView.findViewById(R.id.userEmail);

        if(user!= null){
            userEmailView.setText(firebaseAuth.getCurrentUser().getEmail());
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ShowActivity.this, ProfileActivity.class));
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
                Intent intentSetting = new Intent(this, SettingsActivity.class);
                intentSetting.setFlags(intentSetting.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intentSetting);
                break;
            default:
                break;
        }
    }

    //load show from the phone
//    private void loadShowInfoSaved(){
//
//        SharedPreferences preferences = getSharedPreferences("saveShowList", Context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        String showListFromString = preferences.getString("showList", "");
//        Type type = new TypeToken<List<ShowInfo>>(){}.getType();
//        showList = gson.fromJson(showListFromString, type);
//        if (showList == null){
//            showList = new ArrayList<ShowInfo>();
//        }
//    }


    private void loadShowFromFB(){

        databaseReference.child("users").child(user.getUid()).child("shows").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot showSnapshot : dataSnapshot.getChildren()) {
                    ShowInfo ch = showSnapshot.getValue(ShowInfo.class);

                    showList.add(ch);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private static void saveFile(Context context, Bitmap b, String picName){
        File rootPath = context.getExternalFilesDir("Telewiz");
        File path = new File(rootPath.toString());

        boolean pathBoolean = path.exists();
        FileOutputStream fos;
        try {

            if (!pathBoolean){
                pathBoolean = path.mkdirs();
            }

            if (pathBoolean){
                File file = new File(path,picName);
                fos = new FileOutputStream(file);
                b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            }

        }
        catch (FileNotFoundException e) {
            Log.d("TAG", "file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d("TAG", "io exception");
            e.printStackTrace();
        }

    }

    public void searchButton(View view) throws ParseException {

        Uri uri = Uri.parse("http://www.google.com/#q=" + showTitle);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


    public void onClickButton(){

        final ShowInfo showInfo = new ShowInfo();
        showInfo.setTitle(showTitle);
        if (showTime.equals("ЗАРАЗ")){
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            showTime = dateFormat.format(calendar.getTime());

        }
        showInfo.setTime(showTime);
        showInfo.setDescription(description);
        showInfo.setImgUrl(imgUrl);
        showInfo.setDateParse(dateToParse);
        showInfo.setDateShow(showDate);
        showInfo.setShowLink(showUrl);
        showInfo.setChannelLink(channelLink);
        showInfo.setChannelTitle(channelTitle);
        showInfo.setPosnt(postn);
        showInfo.setCheckForSaved(true);
        showInfo.setLogoForTablePath(logoForTable);
        scheduleNotification(getNotification(showTitle));
        showInfo.setUniqueInt(t);

        loadShowFromFB();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlay){
                    v.setBackgroundResource(R.drawable.active_add_button);
                    Toast.makeText(ShowActivity.this, "Save", Toast.LENGTH_LONG).show();
                    if (image != null) {
                        saveFile(getApplicationContext(), image, String.valueOf(t));
                    }
                    saveShowInFB(showInfo);

                }else{
                    v.setBackgroundResource(R.drawable.noactivated_add_button);
                    Toast.makeText(ShowActivity.this, "ReMOVE", Toast.LENGTH_LONG).show();
                    removeShowFromFB(showInfo);
                }

                isPlay = !isPlay;
            }
        });

    }



    private void removeShowFromFB(ShowInfo showInfo){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        int removeId = showInfo.getUniqueInt();
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, removeId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        removePicture(this, String.valueOf(removeId));
            databaseReference.child("users").child(user.getUid()).child("shows").child(String.valueOf(removeId)).removeValue();

    }

    //remove show from the phone
//    public void removeShow(ShowInfo showInfo){
//
//        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
//
//        int t = showInfo.getUniqueInt();
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, t, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        SharedPreferences preferences = getSharedPreferences("saveShowList", Context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        removePicture(this, String.valueOf(t));
//
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmManager.cancel(pendingIntent);
//
//        SharedPreferences.Editor editor = preferences.edit();
//
//        showList.remove(showInfo);
//        String showInfoToString = gson.toJson(showList);
//
//        editor.putString("showList", showInfoToString);
//
//        editor.apply();
//
//    }


    private void removePicture(Context context, String picName){

        File rootPath = context.getExternalFilesDir("Telewiz");
        File path = new File(rootPath.toString());

        File file = new File(path, picName);
        file.delete();
    }

    //save show to the phone
//    private void saveShows(ShowInfo showInfo){
//        SharedPreferences sharedPreferences = getSharedPreferences("saveShowList", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        if (showList == null){
//            showList = new ArrayList<ShowInfo>();
//        }
//        Gson gson = new Gson();
//        if (!showList.contains(showInfo)){
//            showList.add(showInfo);
//        }
//
//        String showListToString = gson.toJson(showList);
//
//        editor.putString("showList", showListToString);
//
//        editor.apply();
//    }


    private void saveShowInFB(ShowInfo showInfo){

        int unique = showInfo.getUniqueInt();

        databaseReference.child("users").child(user.getUid()).child("shows").child(String.valueOf(unique)).setValue(showInfo);

    }



    private void scheduleNotification(Notification notification) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);

        Calendar calendar = Calendar.getInstance();
        try {
            Date dateMinutes = new SimpleDateFormat("HH:mm").parse(showTime);
            Date dateMonth = new SimpleDateFormat("yyyy-MM-dd").parse(dateToParse);
            int myHour = dateMinutes.getHours();
            int myMinutes = dateMinutes.getMinutes();

            calendar.setTime(dateMonth);
            calendar.set(Calendar.HOUR_OF_DAY, myHour);
            calendar.set(Calendar.MINUTE, myMinutes);
            calendar.set(Calendar.SECOND, 0);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        long futureInMillis = calendar.getTimeInMillis() - remindInMili;

        long timeId = calendar.getTimeInMillis();
        t = (int) (timeId / 1000);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, t);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, t, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);


    }


    private Notification getNotification(String name){

        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentTitle(showTime + " " + name);
        String text = getApplicationContext().getResources().getString(R.string.enjoy);
        builder.setContentText(text);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        Bitmap d;
        d = BitmapFactory.decodeResource(getApplicationContext().getResources(), logo);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            builder.setSmallIcon(R.drawable.ic_app_logo);
            builder.setLargeIcon(d);
        }else {
            builder.setSmallIcon(logo);
            builder.setLargeIcon(d);

        }

        return builder.build();
    }

}

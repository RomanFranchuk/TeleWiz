package com.arcticwolf.telewiz;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class SelectedShows extends AppCompatActivity {

    private static final int LAYOUT =  R.layout.selected_shows_activity;
    ArrayList<ShowInfo>  showList = new ArrayList<ShowInfo>();
    ArrayList<ShowInfo> arrayListShow = new ArrayList<>();
    private ArrayList<String> arrayList;
    private MyDates myDates = new MyDates();
    private ArrayList<ShowInfo> listFirstSection = new ArrayList<ShowInfo>();
    ShowsAdapter showsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private String todayDay;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private TextView userNameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreference();
        setContentView(LAYOUT);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        getTodayFullDate();

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle(R.string.selected_shows);

        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }


        loadShowFromFB();

        initNavigationView();


    }

    private void getTodayFullDate(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        todayDay = dateFormat.format(calendar.getTime());
        todayDay = todayDay.replace("-", "");
    }

    private void initSwipeListHeader(){
        StickyListHeadersListView listHeadersListView = (StickyListHeadersListView) findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        setSupportActionBar(toolbar);
        listHeadersListView.setAdapter(showsAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                addOlderShows(showList);
                showList.removeAll(listFirstSection);
                if (showsAdapter != null) {
                    showsAdapter.notifyDataSetChanged();
                    showsAdapter.closeAllItems();
                }


            }
        });

        listHeadersListView.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                Toast.makeText(getApplicationContext(), String.valueOf(itemPosition), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_selected, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.clear_button:
                clearList();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void clearList(){
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);

        for (int i=0; i <= listFirstSection.size() -1; i++){
            ShowInfo show = listFirstSection.get(i);
            int unique = show.getUniqueInt();


            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, unique, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            databaseReference.child("users").child(user.getUid()).child("shows").child(String.valueOf(unique)).removeValue();
        }
    }

//    remove all saved show from the phone
//    private void clearList(){
//
//        SharedPreferences preferences = getSharedPreferences("saveShowList", Context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        SharedPreferences.Editor editor = preferences.edit();
//
//        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
//        Iterator<ShowInfo> iterator = listFirstSection.iterator();
//        while(iterator.hasNext()){
//            ShowInfo showInfo = iterator.next();
//            int t = showInfo.getUniqueInt();
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, t, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            alarmManager.cancel(pendingIntent);
//        }
//
//        listFirstSection.clear();
//        showsAdapter.notifyDataSetChanged();
//
//
//        String showInfoToString = gson.toJson(listFirstSection);
//
//        editor.putString("showList", showInfoToString);
//        editor.apply();
//
//
//    }

    //remove items from listFirstSection if it would be saved on phone
//    public void removeItem(int positionItem){
//
//        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
//
//        ShowInfo showInfo = listFirstSection.get(positionItem);
//
//        int t = showInfo.getUniqueInt();
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, t, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        SharedPreferences preferences = getSharedPreferences("saveShowList", Context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        removePicture(this, String.valueOf(t));
//        listFirstSection.remove(positionItem);
//        showsAdapter.notifyDataSetChanged();
//
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarmManager.cancel(pendingIntent);
//
//        SharedPreferences.Editor editor = preferences.edit();
//
//        String showInfoToString = gson.toJson(listFirstSection);
//
//        editor.putString("showList", showInfoToString);
//
//        editor.apply();
//
//    }

    public void removeItem(int positionItem){
        ShowInfo show = listFirstSection.get(positionItem);

        int unique = show.getUniqueInt();

        removePicture(this, String.valueOf(unique));

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, unique, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        databaseReference.child("users").child(user.getUid()).child("shows").child(String.valueOf(unique)).removeValue();
        listFirstSection.remove(positionItem);
        showsAdapter.notifyDataSetChanged();

    }

    private void getPreference(){
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
        String themeName = pref.getString("theme", "Theme Default");
        String language = pref.getString("languages", "English");
        if (themeName.equals("Grey and Darker Grey")) {
            setTheme(R.style.Grey);
        } else if (themeName.equals("Red and Claret")) {
            setTheme(R.style.RedTheme);
        }else if (themeName.equals("Violet and Yankees Blue")) {
            setTheme(R.style.VioletTheme);
        }
        LanguageHelper languageHelper = new LanguageHelper();
        languageHelper.changeLocal(this.getResources(), language);
    }


    private void loadShowFromFB(){

        databaseReference.child("users").child(user.getUid()).child("shows").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listFirstSection.clear();
                for (DataSnapshot showSnapshot : dataSnapshot.getChildren()) {
                    ShowInfo ch = showSnapshot.getValue(ShowInfo.class);

                    showList.add(ch);

                }

                if(!showList.isEmpty()){
                    Collections.sort(showList, new Comparator<ShowInfo>() {

                        @Override
                        public int compare(ShowInfo lhs, ShowInfo rhs) {

                            DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Date date = null;
                            Date date1 = null;
                            try {
                                date = f.parse(lhs.getDateParse() + " " + lhs.getTime());
                                date1 = f.parse(rhs.getDateParse() + " " + rhs.getTime());

                            } catch (java.text.ParseException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                            Log.e("NOT NULL", showList.toString());
                            return date.compareTo(date1);
                        }
                    });

                    arrayListShow = methodsSplit(showList);
                    showsAdapter = new ShowsAdapter(SelectedShows.this, arrayListShow);

                }
                initSwipeListHeader();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
//    Load saved shows from phone
//    private void loadSaved(){
//
//        SharedPreferences preferences = getSharedPreferences("saveShowList", Context.MODE_PRIVATE);
//        Gson gson = new Gson();
//        String showListFromString = preferences.getString("showList", "");
//        if (!showListFromString.isEmpty()){
//            Type type = new TypeToken<List<ShowInfo>>(){}.getType();
//            showList = gson.fromJson(showListFromString, type);
//
//        }
//
//        if(!showList.isEmpty()){
//        Collections.sort(showList, new Comparator<ShowInfo>() {
//
//            @Override
//            public int compare(ShowInfo lhs, ShowInfo rhs) {
//
//                DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                Date date = null;
//                Date date1 = null;
//                try {
//                    date = f.parse(lhs.getDateParse() + " " + lhs.getTime());
//                    date1 = f.parse(rhs.getDateParse() + " " + rhs.getTime());
//
//                } catch (java.text.ParseException e) {
//                    e.printStackTrace();
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                }
//
//                return date.compareTo(date1);
//            }
//        });
//
//        arrayListShow = methodsSplit(showList);
//        }
//
//        showsAdapter = new ShowsAdapter(this, arrayListShow);
//
//    }


    private ArrayList<ShowInfo> methodsSplit(ArrayList<ShowInfo> arrayList){
        Date dataFromList;
        Date dateNowDate;
        String dataParse;

        for(int i = 0; i<=arrayList.size() - 1; i++){

            ShowInfo showInfo = arrayList.get(i);
            dataParse = showInfo.getDateParse();
            try {
                dataFromList = new SimpleDateFormat("yyyy-MM-dd").parse(dataParse);
                Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("EET"));
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                Date date = calendar.getTime();
                String dateNow = new SimpleDateFormat("yyyy-MM-dd").format(date);

                dateNowDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateNow);
                if (dataFromList.getTime() >= dateNowDate.getTime()){

                    listFirstSection.add(showInfo);
                }

            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }



        }
        arrayList.removeAll(listFirstSection);
        return listFirstSection;

    }

   private void addOlderShows(ArrayList<ShowInfo> arrayList){
       List<ShowInfo> olderList;
       if (arrayList.size() >= 7){
           olderList = arrayList.subList(arrayList.size() -7 , arrayList.size());
           listFirstSection.addAll(0, olderList);

       }else {
           olderList = arrayList.subList(0 , arrayList.size());
           listFirstSection.addAll(0, olderList);
       }

       swipeRefreshLayout.setRefreshing(false);

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
                    startActivity(new Intent(SelectedShows.this, ProfileActivity.class));
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


    public class DrawerItemClickListener implements ListView.OnItemClickListener{
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


    public void openAllInfo(int positionItem){
        Intent intent = new Intent(SelectedShows.this, ShowActivity.class);
        ShowInfo showInfo = listFirstSection.get(positionItem);

        String title = showInfo.getTitle();
        String time  = showInfo.getTime();
        String dateToParse = showInfo.getDateParse();
        String imgUrl = showInfo.getImgUrl();
        String description = showInfo.getDescription();
        String showDate = myDates.dataFormat(getApplicationContext(), dateToParse);
        String showUrl = showInfo.getShowLink();

        int postn = showInfo.getPosnt();
        int logoForTable = showInfo.getLogoForTablePath();
        String channelLink = showInfo.getChannelLink();
        String channelTitle = showInfo.getChannelTitle();
        int logo = showInfo.getLogoForTablePath();

        intent.putExtra("title", title);
        intent.putExtra("time", time);
        intent.putExtra("description", description);
        intent.putExtra("imgUrl", imgUrl);
        intent.putExtra("dateToParse", dateToParse);
        intent.putExtra("showDate", showDate);
        intent.putExtra("logo", logo);
        intent.putExtra("logoForTable", logoForTable);
        intent.putExtra("showUrl", showUrl);
        //checkForSaved
        intent.putExtra("checkForSaved", true);
        intent.putExtra("channelLink", channelLink);
        intent.putExtra("channelTitle", channelTitle);
        intent.putExtra("postn", postn);
        startActivity(intent);
    }

    private Bitmap loadBitmap(Context context, String picName, String channelTitle){
        Bitmap b = null;

        File rootPath = context.getExternalFilesDir("Telewiz");
        File path = new File(rootPath.toString());
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 5;

        Rect rect = new Rect(4,4,4,4);

        FileInputStream fis;
        try {

            File file = new File(path,picName);
            fis = new FileInputStream(file);

            b = BitmapFactory.decodeStream(fis, rect,options);

            fis.close();

        }
        catch (FileNotFoundException e) {
            Log.d("TAG", "file not found");
            b = chooseRightPicture(channelTitle, options);
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d("TAG", "io exception");
            e.printStackTrace();
        }

        return b;
    }

    private Bitmap chooseRightPicture(String channelTitle, BitmapFactory.Options options){
        Bitmap bitmap = null;
        switch (channelTitle){
            case "1+1":
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.oneplusone_picture, options);
                break;
            case "TET":
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tet_picture, options);
                break;
            case "НЛО ТВ":
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.nlo_picture, options);
                break;
            case "Новий канал":
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.novy_picture, options);
                break;
            case "2+2":
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plustwo_picture, options);
                break;
            case "K1":
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.k1_picture, options);
                break;
        }

        return bitmap;
    }


    private void removePicture(Context context, String picName){

        File rootPath = context.getExternalFilesDir("Telewiz");
        File path = new File(rootPath.toString());

        File file = new File(path, picName);
        file.delete();
    }


    private class ShowsAdapter extends BaseSwipeAdapter implements StickyListHeadersAdapter{
        private LayoutInflater inflater;
        private ArrayList<ShowInfo> arrayList;
        private TextView textViewTitle;
        private TextView textViewTime;
        private ImageView imageViewShow;
        private TextView textViewChannel;
        private ShowInfo showInfo;
        private Bitmap bitmap;
        private Context context;


        public ShowsAdapter(Context context, ArrayList<ShowInfo> arrayList){

            inflater = LayoutInflater.from(context);
            this.context = context;
            this.arrayList = arrayList;

        }


        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipe;
        }

        @Override
        public View generateView(final int position, ViewGroup parent) {

                View v =LayoutInflater.from(context).inflate(R.layout.selected_item_layout, null);

            return v;
        }

        @Override
        public void fillValues(final int position, View convertView) {

            RelativeLayout relativeLayout = (RelativeLayout)convertView.findViewById(R.id.relativeItem);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openAllInfo(position);

                }
            });

            final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe);
                textViewTitle = (TextView) convertView.findViewById(R.id.title_of_show);
                textViewTime = (TextView) convertView.findViewById(R.id.time_of_show);
                imageViewShow = (ImageView) convertView.findViewById(R.id.image_of_show);
                textViewChannel = (TextView) convertView.findViewById(R.id.name_of_channel);
                View v = (View) convertView.findViewById(R.id.delete);

             showInfo = arrayList.get(position);
            String title = showInfo.getTitle();
            String time = showInfo.getTime();
            String channelTitle = showInfo.getChannelTitle();
            int t = showInfo.getUniqueInt();
            bitmap = loadBitmap(getApplicationContext(),String.valueOf(t), channelTitle);
            textViewTitle.setText(title);
            textViewTime.setText(time);
            textViewChannel.setText(channelTitle);
            imageViewShow.setImageBitmap(bitmap);

            swipeLayout.addSwipeListener(new SimpleSwipeListener() {

                @Override
                public void onOpen(SwipeLayout layout) {

                    layout.setClickable(false);
                    YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));

                }

            });

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    swipeLayout.close();
                    removeItem(position);
                }
            });

        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            HeaderViewHolder holder;
            if (convertView == null) {
                holder = new HeaderViewHolder();

                convertView = inflater.inflate(R.layout.header_layout, parent, false);
                holder.textView = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            } else {
                holder = (HeaderViewHolder) convertView.getTag();
            }
            ShowInfo showInfo = arrayList.get(position);
            String dateString = showInfo.getDateParse();
            dateString = dateString.replace("-", "");


            String dateShow = showInfo.getDateShow();
            holder.textView.setText(dateShow);


            if (dateString.equals(todayDay)){

                holder.textView.setSelected(true);
                String dayToday = getApplicationContext().getResources().getString(R.string.today);
                holder.textView.setText(dayToday);

            }else{
                holder.textView.setSelected(false);
            }

            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            ShowInfo showInfo = arrayList.get(position);
            String dateString = showInfo.getDateParse();
            dateString = dateString.replace("-", "");

            long l = Long.parseLong(dateString);
            Log.e("DateShow", String.valueOf(l));

            return l;

        }

        class HeaderViewHolder{
            TextView textView;
        }

    }
}

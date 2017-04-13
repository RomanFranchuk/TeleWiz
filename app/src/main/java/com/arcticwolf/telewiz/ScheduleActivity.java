package com.arcticwolf.telewiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScheduleActivity extends AppCompatActivity {

    private static final int LAYOUT = R.layout.activity_schedule;

    private MyDates dates = new MyDates();
    private Toolbar toolbar;
    private CustomTabLayout tabLayout;
    private ViewPager viewPager;
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;
    private MenuItem menuItem;
    private int logo;
    private TextView userNameView;
    private boolean nextWeek;
    private ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreference();

        setContentView(LAYOUT);

        logo = this.getIntent().getExtras().getInt("logo");
        nextWeek = this.getIntent().getExtras().getBoolean("nextWeek");

        monday = getString(R.string.monday);
        tuesday = getString(R.string.tuesday);
        wednesday = getString(R.string.wednesday);
        thursday = getString(R.string.thursday);
        friday = getString(R.string.friday);
        saturday = getString(R.string.saturday);
        sunday = getString(R.string.sunday);

        initUI();
        showNextWeek();
        initNavigationView();


    }

    private void initUI(){
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (CustomTabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void showNextWeek(){

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                if (position == 6 & nextWeek){
                    adapter.addFrag(new FragmentSchedules(), monday, 8);
                    adapter.addFrag(new FragmentSchedules(), tuesday, 9);
                    adapter.addFrag(new FragmentSchedules(), wednesday, 10);
                    adapter.addFrag(new FragmentSchedules(), thursday, 11);
                    adapter.addFrag(new FragmentSchedules(), friday, 12);
                    adapter.addFrag(new FragmentSchedules(), saturday, 13);
                    adapter.addFrag(new FragmentSchedules(), sunday, 14);

                    adapter.notifyDataSetChanged();

                    for(int i = 0;i<adapter.getCount();i++){
                        try{
                            tabLayout.getTabAt(i).setText(adapter.getPageTitle(i));
                        }
                        catch(IndexOutOfBoundsException e){
                            tabLayout.addTab(tabLayout.newTab().setText(adapter.getPageTitle(i)));
                        }
                    }

                    nextWeek = false;

                }
                switch (position) {
                    case 0:
                        menuItem.setTitle(dates.getDataChange(getApplicationContext(),1));
                        break;
                    case 1:
                        menuItem.setTitle(dates.getDataChange(getApplicationContext(),2));
                        break;
                    case 2:
                        menuItem.setTitle(dates.getDataChange(getApplicationContext(),3));
                        break;
                    case 3:
                        menuItem.setTitle(dates.getDataChange(getApplicationContext(), 4));
                        break;
                    case 4:
                        menuItem.setTitle(dates.getDataChange(getApplicationContext(), 5));
                        break;
                    case 5:
                        menuItem.setTitle(dates.getDataChange(getApplicationContext(), 6));
                        break;
                    case 6:
                        menuItem.setTitle(dates.getDataChange(getApplicationContext(),7));
                        break;
                    case 7:
                        menuItem.setTitle(dates.getDataChange(getApplicationContext(), 8));
                        break;
                    case 8:
                        menuItem.setTitle(dates.getDataChange(getApplicationContext(), 9));
                        break;
                    case 9:
                        menuItem.setTitle(dates.getDataChange(getApplicationContext(), 10));
                        break;
                    case 10:
                        menuItem.setTitle(dates.getDataChange(getApplicationContext(), 11));
                        break;
                    case 11:
                        menuItem.setTitle(dates.getDataChange(getApplicationContext(), 12));
                        break;
                    case 12:
                        menuItem.setTitle(dates.getDataChange(getApplicationContext(), 13));
                        break;
                    case 13:
                        menuItem.setTitle(dates.getDataChange(getApplicationContext(), 14));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_bar, menu);
        menuItem = menu.findItem(R.id.data);
        MenuItem logoImage = menu.findItem(R.id.logo);
        logoImage.setIcon(logo);
        menuItem.setTitle(dates.getDataChange(getApplicationContext(), 1));

        return true;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
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
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user!= null){
            userEmailView.setText(firebaseAuth.getCurrentUser().getEmail());
            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ScheduleActivity.this, ProfileActivity.class));
                }
            });
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

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


    private void setupViewPager(ViewPager viewPager) {

        adapter.addFrag(new FragmentSchedules(), monday, 1);
        adapter.addFrag(new FragmentSchedules(), tuesday, 2);
        adapter.addFrag(new FragmentSchedules(), wednesday, 3);
        adapter.addFrag(new FragmentSchedules(), thursday, 4);
        adapter.addFrag(new FragmentSchedules(), friday, 5);
        adapter.addFrag(new FragmentSchedules(), saturday, 6);
        adapter.addFrag(new FragmentSchedules(), sunday, 7);

        viewPager.setAdapter(adapter);
        Calendar calendar = Calendar.getInstance();
        int dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int positionDay  = dayOfTheWeek - 2;

        if(positionDay == -1){
            positionDay = 6;
        }

        viewPager.setCurrentItem(positionDay);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }


        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        public void addFrag(FragmentSchedules fragment, String title, int position) {

        Bundle bundle = new Bundle();
        bundle.putInt("day", position);
        fragment.setArguments(bundle);
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

}

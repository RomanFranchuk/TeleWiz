package com.arcticwolf.telewiz;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arcticwolf.telewiz.parse.AsyncKOneSchedule;
import com.arcticwolf.telewiz.parse.AsyncNloSchedule;
import com.arcticwolf.telewiz.parse.AsyncNovySchedule;
import com.arcticwolf.telewiz.parse.AsyncPlusOneSchedule;
import com.arcticwolf.telewiz.parse.AsyncPlusTwoSchedule;
import com.arcticwolf.telewiz.parse.AsyncTetSchedule;

import java.io.InterruptedIOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class FragmentSchedules extends android.support.v4.app.Fragment {

    private MyDates myDates = new MyDates();
    private ArrayList<ShowInfo> arrayShow = new ArrayList<>();
    private String link;
    private int day;
    private int logo;
    private ListView listView = null;
    private ProgressBar progressBar;
    public int showingNow;
    private AsyncTask<Void,Void,ArrayList<ShowInfo>> asyncTask;
    private TextView scheduleAvailable;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_schedule, container, false);

        listView = (ListView) root.findViewById(R.id.scheduleList);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
        scheduleAvailable = (TextView) root.findViewById(R.id.id_schedule_available);
        progressBar.setVisibility(View.VISIBLE);

        String nameOfChannel = getActivity().getIntent().getExtras().getString("nameOfChannel");
        logo = getActivity().getIntent().getExtras().getInt("logo");

        day = getArguments().getInt("day");
        getAnotherDay(nameOfChannel, day);
        getParseAsync(nameOfChannel);
        openShow();

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        asyncTask.cancel(true);

    }

    private void getParseAsync(String channelName){
        switch (channelName){
            case "1+1":
                asyncTask = new AsyncPlusOneSchedule(this, link);
                asyncTask.execute();
                break;
            case "k1":
                asyncTask = new AsyncKOneSchedule(this,link);
                asyncTask.execute();
                break;
            case "novyTv":
                asyncTask = new AsyncNovySchedule(this,link);
                asyncTask.execute();
                break;
            case "2+2":
                asyncTask = new AsyncPlusTwoSchedule(this,link);
                asyncTask.execute();
                break;
            case "nloTv":
                asyncTask = new AsyncNloSchedule(this,link);
                asyncTask.execute();
                break;
            case "tet":
                asyncTask = new AsyncTetSchedule(this,link);
                asyncTask.execute();
                break;
            default:
                break;
        }
    }


    private void getAnotherDay(String channelName, int numberOfDay){
        arrayShow.clear();

        switch (channelName){
            case "1+1":
                 link = myDates.getLinkOnePlusOne(numberOfDay);
                break;
            case  "k1":
                 link = myDates.getKOneLink(numberOfDay);
                break;
            case "novyTv":
                 link = myDates.getLinkNovyTv(numberOfDay);
                break;
            case "2+2":
                 link = myDates.getLinkTwoPlusTwo(numberOfDay);
                break;
            case "nloTv":
                 link = myDates.getLinkNloTv(numberOfDay);
                break;
            case "tet":
                 link = myDates.getLinkTet(numberOfDay);
                break;
            default:
                break;
        }
    }

    public void getParsed(ArrayList<ShowInfo> arrayList){

        if (!arrayList.isEmpty()) {
            arrayShow = myDates.rightDate(arrayList, day);
        }else{
            scheduleAvailable.setVisibility(View.VISIBLE);
        }
        MyAdapter adapter = new MyAdapter(getActivity(), R.layout.schedule_text_layout, arrayShow);
        listView.setAdapter(adapter);

        if (day == selectedTodayShow()){
            showingNow = watchingNow(arrayShow);
            listView.setSelection(showingNow);
        }
        progressBar.setVisibility(View.GONE);
    }


    private void openShow(){
            listView.setOnItemClickListener(new DrawerItemClickListener());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), ShowActivity.class);
            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);

            ShowInfo showInfo =  arrayShow.get(position);

            String title = showInfo.getTitle();
            String time  = showInfo.getTime();
            String dateToParse = showInfo.getDateParse();
            String imgUrl = showInfo.getImgUrl();
            String description = showInfo.getDescription();
            String showDate = myDates.dataFormat(getContext(), dateToParse);
            String showUrl = showInfo.getShowLink();
            int postn = getActivity().getIntent().getExtras().getInt("postn");
            int logoForTable = getActivity().getIntent().getExtras().getInt("logoForTable");
            String channelLink = getActivity().getIntent().getExtras().getString("channelLink");
            String channelTitle = getActivity().getIntent().getExtras().getString("channelTitle");

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
            intent.putExtra("checkForSaved", false);
            intent.putExtra("channelLink", channelLink);
            intent.putExtra("channelTitle", channelTitle);
            intent.putExtra("postn", postn);
            startActivity(intent);
        }
    }

    private int selectedTodayShow(){

        Calendar calendar = Calendar.getInstance();
        int dd = calendar.get(Calendar.DAY_OF_WEEK);
        --dd;
        if (dd == 0){
            dd = 7;
        }

        return dd;

    }

    private int watchingNow(ArrayList<ShowInfo> arrayList){
        int index = -1;
        ShowInfo showInfo;
        ShowInfo previousShowInfo;
        String firstTime;
        String previousTime;


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String timeNow = dateFormat.format(calendar.getTime());

        for (int i=0; i<= arrayList.size() -1; i++){
            showInfo = arrayList.get(i);
            firstTime = showInfo.getTime();
            if (i != 0){
                previousShowInfo = arrayList.get(i - 1);
                previousTime = previousShowInfo.getTime();
            }else{
                previousShowInfo = arrayList.get(i);
                previousTime = previousShowInfo.getTime();
            }
            if (firstTime.equals("ЗАРАЗ")){
                index = i;
                break;
            }

            try {
                Date firstShow = new SimpleDateFormat("HH:mm").parse(firstTime);
                Date previousShow = new SimpleDateFormat("HH:mm").parse(timeNow);
                Date thirdShow = new SimpleDateFormat("HH:mm").parse(previousTime);
                if(firstShow.getTime() <= previousShow.getTime() && firstShow.getTime() >= thirdShow.getTime()){

                }else {
                    break;
                }
                index = i;

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        return index;
    }



    private class MyAdapter extends ArrayAdapter {

        LayoutInflater layoutInflater;

        public MyAdapter(Context context, int resource, ArrayList<ShowInfo> arrayList){
            super(context,resource,arrayList);
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            ViewHolder viewHolder;

            if (convertView == null){
                viewHolder = new ViewHolder();

                convertView = layoutInflater.inflate(R.layout.schedule_text_layout, parent, false);

                viewHolder.timeView = (TextView) convertView.findViewById(R.id.time_show);
                viewHolder.titleView = (TextView) convertView.findViewById(R.id.title_show);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            ShowInfo showInfo = arrayShow.get(position);
            String time = showInfo.getTime();
            String title = showInfo.getTitle();

            viewHolder.timeView.setText(time);
            viewHolder.titleView.setText(title);


            if (day == selectedTodayShow() && showingNow == position){
                viewHolder.titleView.setSelected(true);
            }else {
                viewHolder.titleView.setSelected(false);
            }
            return convertView;
        }

        class ViewHolder{
            TextView timeView;
            TextView titleView;
        }

    }

}

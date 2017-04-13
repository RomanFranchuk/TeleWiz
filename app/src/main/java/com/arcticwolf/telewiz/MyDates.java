package com.arcticwolf.telewiz;

import android.content.Context;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class MyDates {

    public String getLinkOnePlusOne(int dayOfWeek){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        int dayNow = calendar.get(Calendar.DAY_OF_WEEK);
        --dayNow;

        if (dayNow == 0){
            int nextDay = dayOfWeek - dayNow;
            calendar.add(Calendar.DAY_OF_WEEK, -7);
            calendar.add(Calendar.DAY_OF_WEEK, nextDay);
        }else {
            if (dayNow < dayOfWeek) {
                int nextDay = dayOfWeek - dayNow;
                calendar.add(Calendar.DAY_OF_WEEK, nextDay);
            } else if (dayNow > dayOfWeek) {
                int prevDay = dayOfWeek - dayNow;
                calendar.add(Calendar.DAY_OF_WEEK, prevDay);
            } else if (dayNow == dayOfWeek) {
                int addZero= 0;
                calendar.add(Calendar.DAY_OF_WEEK, addZero);
            }
        }
        String data = dateFormat.format(calendar.getTime());
        return "http://www.1plus1.ua/teleprograma/"+data;
    }

    public String getLinkTet(int dayOfWeek){
        return "http://tet.tv/uk/schedule/day/" + dayOfWeek;
    }

    public String getLinkNloTv(int dayOfWeek){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();

        int dayNow = calendar.get(Calendar.DAY_OF_WEEK);
        --dayNow;
        if (dayNow == 0){
            int nextDay = dayOfWeek - dayNow;
            calendar.add(Calendar.DAY_OF_WEEK, -7);
            calendar.add(Calendar.DAY_OF_WEEK, nextDay);
        }else {
            if (dayNow < dayOfWeek) {
                int nextDay = dayOfWeek - dayNow;
                calendar.add(Calendar.DAY_OF_WEEK, nextDay);
            } else if (dayNow > dayOfWeek) {
                int prevDay = dayOfWeek - dayNow;
                calendar.add(Calendar.DAY_OF_WEEK, prevDay);
            } else if (dayNow == dayOfWeek) {
                int addZero= 0;
                calendar.add(Calendar.DAY_OF_WEEK, addZero);
            }
        }
        String data = dateFormat.format(calendar.getTime());
        return "#tab" + data;
    }


    public String getLinkNovyTv(int dayOfWeek){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
        Calendar calendar = Calendar.getInstance();

        int dayNow = calendar.get(Calendar.DAY_OF_WEEK);
        --dayNow;
        if (dayNow == 0){
            int nextDay = dayOfWeek - dayNow;
            calendar.add(Calendar.DAY_OF_WEEK, -7);
            calendar.add(Calendar.DAY_OF_WEEK, nextDay);
        }else {
            if (dayNow < dayOfWeek) {
                int nextDay = dayOfWeek - dayNow;
                calendar.add(Calendar.DAY_OF_WEEK, nextDay);
            } else if (dayNow > dayOfWeek) {
                int prevDay = dayOfWeek - dayNow;
                calendar.add(Calendar.DAY_OF_WEEK, prevDay);
            } else if (dayNow == dayOfWeek) {
                int addZero= 0;
                calendar.add(Calendar.DAY_OF_WEEK, addZero);
            }
        }
        String data = dateFormat.format(calendar.getTime());
        String dataLowerCase = data.toLowerCase();
        return "http://novy.tv/ua/tv/" + dataLowerCase;
    }


    public String getKOneLink(int dayOfWeek){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Calendar calendar = Calendar.getInstance();

        int dayNow = calendar.get(Calendar.DAY_OF_WEEK);
        dayNow--;
        if (dayNow == 0){
            int nextDay = dayOfWeek - dayNow;
            calendar.add(Calendar.DAY_OF_WEEK, -7);
            calendar.add(Calendar.DAY_OF_WEEK, nextDay);
        }else {
            if (dayNow < dayOfWeek) {
                int nextDay = dayOfWeek - dayNow;
                calendar.add(Calendar.DAY_OF_WEEK, nextDay);
            } else if (dayNow > dayOfWeek) {
                int prevDay = dayOfWeek - dayNow;
                calendar.add(Calendar.DAY_OF_WEEK, prevDay);
            } else if (dayNow == dayOfWeek) {
                int addZero= 0;
                calendar.add(Calendar.DAY_OF_WEEK, addZero);
            }
        }
        String data = dateFormat.format(calendar.getTime());
        return "http://k1.ua/uk/tv/" + data;
    }


    public String getLinkTwoPlusTwo(int dayOfWeek){
        --dayOfWeek;
        SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MM");
        SimpleDateFormat dateFormatDay = new SimpleDateFormat("dd");
        SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();

        int numberYear = calendar.get(Calendar.YEAR);
        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
        int numberMonth = calendar.get(Calendar.MONTH);
        calendar.clear();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.MONTH, numberMonth);
        calendar.set(Calendar.WEEK_OF_MONTH, weekOfMonth);
        calendar.set(Calendar.YEAR,numberYear);
        calendar.add(Calendar.DAY_OF_MONTH, dayOfWeek);
        String month = dateFormatMonth.format(calendar.getTime());
        String day = dateFormatDay.format(calendar.getTime());
        String year = dateFormatYear.format(calendar.getTime());


        return "http://2plus2.ua/tv-program/?section=0&day="+day+"&month="+month+"&year="+ year;
    }

    public String dataFormat(Context context, String dateToParse){

        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateToParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Locale ukrainian = new Locale("uk");

        String [] months = context.getResources().getStringArray(R.array.months);
        DateFormatSymbols dateFormatSymbols = DateFormatSymbols.getInstance(ukrainian);
        dateFormatSymbols.setMonths(months);
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, ukrainian);

        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) df;

        simpleDateFormat.setDateFormatSymbols(dateFormatSymbols);

        simpleDateFormat.applyPattern("dd MMMM");

        String data = simpleDateFormat.format(date);

        return data;
    }


    public String getDataChange(Context context, int dayOfWeek){

        Locale ukrainian = new Locale("uk");

        DateFormatSymbols dateFormatSymbols = DateFormatSymbols.getInstance(ukrainian);

        String [] months = context.getResources().getStringArray(R.array.months);

        dateFormatSymbols.setMonths(months);


        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, ukrainian);

        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) df;

        simpleDateFormat.setDateFormatSymbols(dateFormatSymbols);
        simpleDateFormat.applyPattern("dd MMMM");

        Calendar calendar = Calendar.getInstance();

        int dayNow = calendar.get(Calendar.DAY_OF_WEEK);
        --dayNow;


        if (dayNow == 0){
            int nextDay = dayOfWeek - dayNow;
            calendar.add(Calendar.DAY_OF_WEEK, -7);
            calendar.add(Calendar.DAY_OF_WEEK, nextDay);
        }else{
        if (dayNow < dayOfWeek){
            int nextDay = dayOfWeek - dayNow;
            calendar.add(Calendar.DAY_OF_WEEK, nextDay);

        }else if (dayNow > dayOfWeek){
            int prevDay = dayOfWeek - dayNow;
            calendar.add(Calendar.DAY_OF_WEEK, prevDay);

        }else if (dayNow == dayOfWeek){
            int addZero= 0;
            calendar.add(Calendar.DAY_OF_WEEK, addZero);
        }
        }
        String data = simpleDateFormat.format(calendar.getTime());
        return data;
    }



    public ArrayList<ShowInfo> rightDate(ArrayList<ShowInfo> arrayList, int dayOfWeek){

        Date firstShow;
        Date secondShow;
        String firstTime;
        String secondTime;
        ShowInfo showInfo;
        String newTime;
        int timeBackPosition = -1;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormatTime = new SimpleDateFormat("HH:mm");
        String parseDate;
        Calendar calendar = Calendar.getInstance();

        for(int i = 0; i <= arrayList.size() -1; i++){

            showInfo = arrayList.get(i);
            if (showInfo.getTime().equals("ЗАРАЗ")){
                timeBackPosition = i;
                newTime = dateFormatTime.format(calendar.getTime());
                showInfo.setTime(newTime);
            }
        }

        int dayNow = calendar.get(Calendar.DAY_OF_WEEK);
        --dayNow;


        if (dayNow == 0){
            int nextDay = dayOfWeek - dayNow;
            calendar.add(Calendar.DAY_OF_WEEK, -7);
            calendar.add(Calendar.DAY_OF_WEEK, nextDay);
        }else{
            if (dayNow < dayOfWeek){
                int nextDay = dayOfWeek - dayNow;
                calendar.add(Calendar.DAY_OF_WEEK, nextDay);
            }else if (dayNow > dayOfWeek){
                int prevDay = dayOfWeek - dayNow;
                calendar.add(Calendar.DAY_OF_WEEK, prevDay);
            }else if (dayNow == dayOfWeek){
                int addZero= 0;
                calendar.add(Calendar.DAY_OF_WEEK, addZero);

            }
        }

        Iterator<ShowInfo> iterator= arrayList.listIterator();
        showInfo = arrayList.get(0);

        while (iterator.hasNext()){

            ShowInfo showInfo1 = iterator.next();
            firstTime = showInfo.getTime();
            secondTime = showInfo1.getTime();

            try {
                firstShow = new SimpleDateFormat("HH:mm").parse(firstTime);
                secondShow = new SimpleDateFormat("HH:mm").parse(secondTime);
                if (firstShow.getTime() <=secondShow.getTime() ){
                    parseDate = dateFormat.format(calendar.getTime());
                    showInfo1.setDateParse(parseDate);
                }else{

                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    parseDate = dateFormat.format(calendar.getTime());
                    showInfo1.setDateParse(parseDate);
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                }

            }catch (ParseException e){
                e.printStackTrace();
            }

        }
        if (timeBackPosition != -1){
            showInfo = arrayList.get(timeBackPosition);
            showInfo.setTime("ЗАРАЗ");
        }
        return arrayList;
    }

}

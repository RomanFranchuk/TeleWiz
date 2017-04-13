package com.arcticwolf.telewiz.parse;

import android.os.AsyncTask;

import com.arcticwolf.telewiz.FragmentSchedules;
import com.arcticwolf.telewiz.ShowInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public class AsyncPlusTwoSchedule extends AsyncTask<Void,Void,ArrayList<ShowInfo>> {

    private FragmentSchedules context;
    private String link;
    private ArrayList<ShowInfo> arrayShow = new ArrayList<>();

    public AsyncPlusTwoSchedule(FragmentSchedules context, String link){
        this.context = context;
        this.link = link;
    }

    @Override
    protected ArrayList<ShowInfo> doInBackground(Void... params) {
        Document doc;

        String selectorShow = "#central_tv_program > form > ul > li.day.first > ul > li";
        String desc = "";
        String imglink = "";
        String time;
        String title;
        String showUrl;
        String channelLink = "http://2plus2.ua";
        String channelTitle = "2+2";

        try {

            doc = Jsoup.connect(link).get();
            Elements blockShow = doc.select(selectorShow);

            Iterator<Element> iteratorBlock = blockShow.listIterator();

            while (iteratorBlock.hasNext()) {

                Element elements = iteratorBlock.next();
                time = elements.child(1).text();
                title = elements.child(3).text();
                showUrl = elements.child(3).attr("href");

                ShowInfo showInfo = new ShowInfo(title,time,desc,imglink,"","",showUrl, channelLink, channelTitle, 0);

                arrayShow.add(showInfo);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayShow;
    }

    @Override
    protected void onPostExecute(ArrayList<ShowInfo> arrayList) {
        super.onPostExecute(arrayList);
        context.getParsed(arrayList);
    }
}

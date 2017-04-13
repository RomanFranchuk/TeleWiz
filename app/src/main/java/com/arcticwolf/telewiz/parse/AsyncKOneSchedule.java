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


public class AsyncKOneSchedule extends AsyncTask<Void,Void,ArrayList<ShowInfo>> {

    private FragmentSchedules context;
    private String link;
    private ArrayList<ShowInfo> arrayShow = new ArrayList<>();

    public AsyncKOneSchedule(FragmentSchedules context, String link){
        this.context = context;
        this.link = link;
    }
    @Override
    protected ArrayList<ShowInfo> doInBackground(Void... params) {
        Document doc;

        String desc = "";
        String imglink = "";
        String channelLink = "http://k1.ua/uk/tv/";
        String channelTitle = "K1";
        String selectorTime = "div.b-tv-programs-body > div.h-tv-programs > div.b-tv-programs-list > table > tbody > tr > td.b-tv-programs-time > span";
        String selectorName = "div.b-tv-programs-body > div.h-tv-programs > div.b-tv-programs-list > table > tbody > tr > td.b-tv-programs-name > a";
        String selectorUrlShow = "div.b-tv-programs-body > div.h-tv-programs > div.b-tv-programs-list > table > tbody > tr > td.b-tv-programs-name > a";


        try {
            doc = Jsoup.connect(link).get();
            Elements timeShow = doc.select(selectorTime);
            Elements titleShow = doc.select(selectorName);
            Elements urlShow = doc.select(selectorUrlShow);

            Iterator<Element> iteratorTitle = titleShow.listIterator();
            Iterator<Element> iteratorTime = timeShow.listIterator();
            Iterator<Element> iteratorUrl = urlShow.listIterator();
            while (iteratorTime.hasNext() && iteratorTitle.hasNext()) {

                Element elementTime = iteratorTime.next();
                Element elementTitle = iteratorTitle.next();
                String showUrl = elementTitle.attr("href");

                if (!urlShow.isEmpty()){
                    Element elementUrl = iteratorUrl.next();
                    showUrl = elementUrl.attr("href");

                }

                String time = elementTime.text();
                String title = elementTitle.text();

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

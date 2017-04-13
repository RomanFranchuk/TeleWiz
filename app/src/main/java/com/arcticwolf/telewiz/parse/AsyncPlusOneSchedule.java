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


public class AsyncPlusOneSchedule extends AsyncTask<Void,Void,ArrayList<ShowInfo>> {

    private FragmentSchedules context;
    private String link;
    private ArrayList<ShowInfo> arrayShow = new ArrayList<>();

    public AsyncPlusOneSchedule(FragmentSchedules context, String link){
        this.context = context;
        this.link = link;
    }

    @Override
    protected ArrayList<ShowInfo> doInBackground(Void... params) {

        Document doc;

        String desc = "";
        String imglink = "";
        String channelLink = "http://1plus1.ua";
        String channelTitle = "1+1";
        String selectorTime = "div.program-info-short > div > div.prog-time > div > div > div.time";
        String selectorName = "div.tv-title > a.show-program-info";
        String description = "div.program-info-short > div > div.prog-description > div.text > div > p";
        String imgUrl = "div.program-info-short > div > div.prog-img > div > a > img";

        try {
            doc = Jsoup.connect(link).get();
            Elements timeShow = doc.select(selectorTime);
            Elements titleShow = doc.select(selectorName);
            Elements descShow = doc.select(description);
            Elements imgUrlShow = doc.select(imgUrl);

            Iterator<Element> iteratorTitle = titleShow.listIterator();
            Iterator<Element> iteratorTime = timeShow.listIterator();
            Iterator<Element> iteratorDesc = descShow.listIterator();
            Iterator<Element> iteratorImgUrl = imgUrlShow.listIterator();

            while (iteratorTime.hasNext() && iteratorTitle.hasNext()) {

                Element elementTime = iteratorTime.next();
                Element elementTitle = iteratorTitle.next();
                String showUrl = elementTitle.attr("href");

                if (!descShow.isEmpty()){
                    Element elementDesc = iteratorDesc.next();
                    desc = elementDesc.text();

                }

                if (!imgUrlShow.isEmpty()){
                    Element elementImgUrl = iteratorImgUrl.next();
                    imglink = elementImgUrl.absUrl("src");

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

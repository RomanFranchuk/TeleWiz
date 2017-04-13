package com.arcticwolf.telewiz.parse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import com.arcticwolf.telewiz.R;
import com.arcticwolf.telewiz.ShowActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class AsyncTetPicture extends AsyncTask<Void,Void,Void> {

    private ShowActivity activity;
    private String link;
    Document document;
    String imageUrl = "";
    String description ="";
    Bitmap image;

    public AsyncTetPicture(ShowActivity activity, String link) {
        this.activity = activity;
        this.link = link;
    }


    @Override
    protected Void doInBackground(Void... params) {

        try {

            document = Jsoup.connect("http://tet.tv" + link)
                    .get();
            String newUrl = document.location();
            Document doc = Jsoup.connect(newUrl + "about").get();

            Elements elementsDesc = doc.select("#page-container > div > div.page-content.clearfix > div > div.section.section-projects-top > div > div > div.text > p");

            if (!elementsDesc.isEmpty()) {
                Elements elementsImg = doc.select("#page-container > div > div.page-content.clearfix > div > div.section.section-projects-top > div > div > div > p:nth-child(1) > img");
                description = elementsDesc.text();
                imageUrl = elementsImg.attr("src");
                image = getImageBitmap(imageUrl);
            }

            if(image == null){
                Elements elementsImg = doc.select("#page-container > div > div.page-content.clearfix > div > div.section.section-projects-top > div > div > div.text > p.lead-image > img");
                imageUrl = elementsImg.attr("src");
                image = getImageBitmap(imageUrl);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        if (image == null){
            image = BitmapFactory.decodeResource(activity.getResources(), R.drawable.tet_picture);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        activity.parsePicture(image, description);
    }


    private Bitmap getImageBitmap(String url){
        Bitmap bitmap = null;
        try {
            URL aURL = new URL(url);
            URLConnection connection = aURL.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            bitmap = BitmapFactory.decodeStream(bufferedInputStream);

            bufferedInputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}

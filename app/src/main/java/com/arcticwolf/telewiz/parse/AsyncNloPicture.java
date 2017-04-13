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

public class AsyncNloPicture extends AsyncTask<Void,Void,Void> {

    private ShowActivity activity;
    private String link;
    private Document document;
    private String imageUrl;
    private String description;
    private Bitmap image;

    public AsyncNloPicture(ShowActivity activity, String link) {
        this.activity = activity;
        this.link = link;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {

        document = Jsoup.connect("http://nlotv.com" + link).get();
        Elements elementsDesc = document.select("body > div.site_wr > div.container > section > section > article.project-info > p");

        if (!elementsDesc.isEmpty()){

            Elements elementsImg = document.select("body > div.site_wr > div.container > section > section > div > img");
            description = elementsDesc.text();
            imageUrl = elementsImg.attr("src");
            image = getImageBitmap(imageUrl);
        }else {
            description = "";
        }

    } catch (IOException e) {
        e.printStackTrace();
    }

        if(image == null){
            image = BitmapFactory.decodeResource(activity.getResources(), R.drawable.nlo_picture);
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

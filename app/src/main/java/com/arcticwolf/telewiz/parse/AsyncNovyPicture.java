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


public class AsyncNovyPicture extends AsyncTask<Void,Void,Void> {

    private ShowActivity activity;
    private String link;
    private Document document;
    private String imageUrl = "";
    private String description = "";
    private Bitmap image;

    public AsyncNovyPicture(ShowActivity activity, String link) {
        this.activity = activity;
        this.link = link;
    }
    @Override
    protected Void doInBackground(Void... params) {

        try {
            document = Jsoup.connect(link).timeout(600000).get();
            Elements elementsDesc = document.select("#content > div.page-wrapper > div.light-text.page-content > div.post-content > p");

            Elements elementsImg = document.select("#content > div.page-wrapper > div.light-text.page-content > div.post-content > div > img");

            Elements elementsDescSecond = document.select("div.constructor-column-2.constructor-current-1 > div > div.section > div.page-wrapper");
            Elements elementsProject = document.select("a > div.kv-main__slider-img > img");
            if (!elementsDesc.isEmpty()){
                description = elementsDesc.text();
            }else if (!elementsDescSecond.isEmpty()){
                description = elementsDescSecond.text();
            }

            if(!elementsImg.isEmpty()){
                imageUrl = elementsImg.attr("src");
                image = getImageBitmap(imageUrl);
            }else if(!elementsProject.isEmpty()){
                 imageUrl = elementsProject.attr("src");
                image = getImageBitmap(imageUrl);
            }else{
                image = BitmapFactory.decodeResource(activity.getResources(), R.drawable.novy_picture);
            }


        } catch (IOException e) {
            e.printStackTrace();
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

package com.arcticwolf.telewiz;

import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;


public class LanguageHelper {

    public static void changeLocal(Resources resources,String locale){
        Configuration configuration;
        configuration = new Configuration(resources.getConfiguration());

        switch (locale){
            case "uk":
                configuration.locale = new Locale("uk");
                break;
            default:
                configuration.locale = Locale.ENGLISH;
                break;
        }

        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }
}

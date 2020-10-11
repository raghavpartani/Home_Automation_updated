package com.example.home_automation;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element adsElement = new Element();
        adsElement.setTitle("Advertise with us");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("This app will make your life more convenient and You can manage all the Appliances of your house using your smart phone.")
                .addItem(new Element().setTitle("Version 1.0"))
                .addItem(new Element().setTitle("CONNECT WITH US"))
                .addEmail("narendok@gmail.com")
                .addWebsite("https://www.tantrabyte.org/")
                .addYoutube("https://www.youtube.com/channel/UCujW4UZk5lMJCfrJkoTwABQ")
                .addPlayStore("")
                .addInstagram("roboslog")
                .addItem(getDeveloper())
                .addItem(getCopyRightsElement())
                .create();
        setContentView(aboutPage);
    }


    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format("Developed by Slogfy Co. 2020", Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setAutoApplyIconTint(true);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(About.this, copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }

    Element getDeveloper() {
        Element developer = new Element();
        final String copyrights = String.format("Developer Team:\n\nSr Android Dev  -> Akshun KC\nJr Android Dev -> Raghav Partani", Calendar.getInstance().get(Calendar.YEAR));
        developer.setTitle(copyrights);
        developer.setAutoApplyIconTint(true);
        developer.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        developer.setIconNightTint(android.R.color.white);
        return developer;
    }
}
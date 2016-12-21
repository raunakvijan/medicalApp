package com.example.raunak.medicalapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    ImageButton im;
    int cam_start = 1;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        im = (ImageButton) findViewById(R.id.imageButton);
        mDrawerList = (ListView) findViewById(R.id.navList);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }

        if (id == R.id.cart) {
            Intent in=new Intent(this,Cart.class);
            startActivity(in);
        }

        if (id == R.id.logout) {
            SharedPreferences pref=getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor edi=pref.edit();
            edi.putBoolean("Login",false);

            edi.commit();
            Intent in = new Intent(this, LoginActivity.class);
            startActivity(in);
        }
        if (id == R.id.exit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                this.finishAffinity();
            }
        }
        if(id==R.id.about)
        {
            Intent in=new Intent(this,About.class);
            startActivity(in);
        }
        return true;
    }

    @Override
    public void onBackPressed() {

    }

    public void pic(View v) {
        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(in, cam_start);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == cam_start) {
            try {
                Bitmap i = (Bitmap) data.getExtras().get("data");


                Intent intent = new Intent(this, Confi1.class);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                i.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytes = stream.toByteArray();
                intent.putExtra("img", bytes);
                startActivity(intent);


            } catch (NullPointerException ne) {

            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.raunak.medicalapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.raunak.medicalapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    public void overthecounter(View v)
    {
        Intent in =new Intent(this,OverTheCounter.class);
        startActivity(in);
    }
}

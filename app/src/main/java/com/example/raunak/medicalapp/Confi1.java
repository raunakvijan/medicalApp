package com.example.raunak.medicalapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class Confi1 extends AppCompatActivity {
    ImageView im;
    EditText medlist,add,prec;
    Bitmap b;
    long pr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confi1);
        byte[] byarr=getIntent().getByteArrayExtra("img");
        b = BitmapFactory.decodeByteArray(byarr,0,byarr.length);
        im= (ImageView) findViewById(R.id.preimg);
        im.setImageBitmap(b);

        prec= (EditText) findViewById(R.id.precno);


    }




    public void next(View v)
    {
        try {


            pr = Long.parseLong(prec.getText().toString());
        }
        catch (Exception e)
        {

        }

        if(prec.getText().toString().isEmpty())
        {
            prec.setHint("enter prescription number");
            prec.setHintTextColor(Color.RED);
        }
        else
        {
        Intent in=new Intent(this,Confi3.class);
        in.putExtra("medlist","");

        in.putExtra("prec",Long.parseLong(prec.getText().toString()));
        try {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] bytes = stream.toByteArray();
            in.putExtra("img", bytes);
            startActivity(in);


        } catch (NullPointerException ne) {

        }
        startActivity(in);
    }}
    int cam_start=2;

    public void hello(View v)
    {
        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(in, cam_start);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == cam_start) {
            try {
                Bitmap i = (Bitmap) data.getExtras().get("data");
                im.setImageBitmap(i);


            } catch (NullPointerException ne) {

            }

        }
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

        }

        if (id == R.id.cart) {

        }

        if (id == R.id.logout) {
            Intent in = new Intent(this, LoginActivity.class);
        }
        if (id == R.id.exit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                this.finishAffinity();
            }
        }
        return true;
    }

}

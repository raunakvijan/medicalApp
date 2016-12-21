package com.example.raunak.medicalapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class Confi2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confi2);
    }
    public void next2(View v)
    {
        Intent in=new Intent(this,Confi3.class);
        String medlist=getIntent().getStringExtra("medlist");

        EditText e= (EditText) findViewById(R.id.text111);
        in.putExtra("medlist",medlist);
        in.putExtra("add",e.getText().toString());

        startActivity(in);
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
    }}


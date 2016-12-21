package com.example.raunak.medicalapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Confi3 extends AppCompatActivity {
    EditText ed;
    String medlist;
    String add;
    long nopre;
    String reg;

    Bitmap b;


    TextView t;
    EditText pro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confi3);
        pro= (EditText) findViewById(R.id.text111);
        ed= (EditText) findViewById(R.id.text111);
        medlist = getIntent().getStringExtra("medlist");
        add = getIntent().getStringExtra("add");
        TextView t= (TextView) findViewById(R.id.confir);
        nopre = getIntent().getLongExtra("prec",-1);
        t= (TextView) findViewById(R.id.conf);
        t.setText("your prescription will be verfied and our constumer care consultant will contact you soon");
        byte[] by=getIntent().getByteArrayExtra("img");
        b = BitmapFactory.decodeByteArray(by,0,by.length);


    }
    public void confirm(View v)
    {

        new MyClass().execute(medlist,add,nopre+"",pro.getText().toString());

    }

     class MyClass extends AsyncTask<String,Void,String>
     {

         @Override
         protected void onPostExecute(String s) {
             Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
             Log.i("error",s);
             if(s.contains("success")) {
                 Intent in = new Intent(getApplicationContext(), MainActivity.class);
                 startActivity(in);
             }
         }

         @Override
         protected String doInBackground(String... strings) {
             String list=strings[0];
             String add=strings[1];
             long no= Long.parseLong(strings[2]);
             String promocode=strings[3];
             ByteArrayOutputStream stream = new ByteArrayOutputStream();
             b.compress(Bitmap.CompressFormat.JPEG, 90, stream); //compress to which format you want.
             byte[] byte_arr = stream.toByteArray();
             String imageStr = Base64.encodeToString(byte_arr,Base64.DEFAULT);

            try
            {
                String data=URLEncoder.encode("list", "UTF-8") + "=" + URLEncoder.encode(list, "UTF-8");

                data+="&"+URLEncoder.encode("no","UTF-8")+"="+URLEncoder.encode(no+"","UTF-8");
                data+="&"+URLEncoder.encode("promo","UTF-8")+"="+URLEncoder.encode(promocode+"","UTF-8");
                data+="&"+URLEncoder.encode("img","UTF-8")+"="+URLEncoder.encode(imageStr+"","UTF-8");

                Log.i("dataa,",data);

                reg = "http://medicalapp.pe.hu/order.php";
                URL url=new URL(reg);
                URLConnection conn=url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter o=new OutputStreamWriter(conn.getOutputStream());
                o.write(data);
                o.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;
                while((line = reader.readLine()) != null)
                {
                    sb.append(line);
                    break;
                }
                return sb.toString();
            }
            catch (Exception e)
            {
                return null;
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

}

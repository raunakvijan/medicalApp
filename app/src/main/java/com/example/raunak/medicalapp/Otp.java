package com.example.raunak.medicalapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Otp extends AppCompatActivity {
    TextView mes;
    EditText otp;
    long mob;
    int code;
    String name,address,email,pas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
         long mobile=getIntent().getExtras().getLong("no");
        code =  getIntent().getExtras().getInt("code");
        mes= (TextView) findViewById(R.id.tv_otp);
        mes.setText("a confirmation code has been sent to your no "+mobile +" enter code "+code);

       ;
        Intent in=getIntent();
        name=in.getStringExtra("name");
       mob= in.getLongExtra("no",0);
        email=in.getStringExtra("email");
       address= in.getStringExtra("address");
        pas=in.getStringExtra("pass");



    }
    public void proceed(View v)
    {
        otp= (EditText) findViewById(R.id.et_verify);

        if(Integer.parseInt(otp.getText().toString())==code)
        {
            new MySync().execute();
        }
        else
        {
            otp.setText("");
            otp.setHint("try again");
            otp.setHintTextColor(Color.RED);

        }

    }
    class MySync extends AsyncTask<Void,Void,String>
    {

        @Override
        protected void onPostExecute(String s) {
            if(s=="yo")
            {
                Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_LONG).show();
                SharedPreferences pref=getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor edi=pref.edit();
                edi.putBoolean("Login",true);
                edi.putString("email",email);
                edi.putString("pass",pas);
                edi.commit();
                Intent in=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);

            }
        }

        @Override
        protected String doInBackground(Void... strings) {
            try {
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                data+="&"+URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");
                data+="&"+URLEncoder.encode("mobile","UTF-8")+"="+URLEncoder.encode(mob+"","UTF-8");
                data+="&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(pas,"UTF-8");
                data+="&"+URLEncoder.encode("address","UTF-8")+"="+URLEncoder.encode(address,"UTF-8");
                Log.i("data",data);
                URL url = new URL("http://medicalapp.pe.hu/createuser.php");
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( data );
                wr.flush();

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
            catch(Exception e){
                return null;
            }
        }
    }
}

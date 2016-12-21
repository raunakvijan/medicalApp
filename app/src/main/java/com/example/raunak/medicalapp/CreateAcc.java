package com.example.raunak.medicalapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

public class CreateAcc extends AppCompatActivity {
    Spinner bloodgroup;
    String[] list;
    int dialid=0;
    EditText dob;
    Intent i;




    int day=1,month=0,year=1990;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);
        bloodgroup= (Spinner) findViewById(R.id.spinner);
        list=new String[]{"A+","A-","AB+","AB-","B+","B+","O-","O+"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,list);
        showDialogDate();
        dob = (EditText) findViewById(R.id.dob);
        bloodgroup.setAdapter(adapter);

    }

         DatePickerDialog.OnDateSetListener dplistener =new DatePickerDialog.OnDateSetListener() {
             @Override
             public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                 year=i;
                 month=i1+1;
                 day=i2;
                 dob.setText(day+"/"+month+"/"+year);

             }
         };
    public void showDialogDate()
    {
        EditText dob= (EditText) findViewById(R.id.dob);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showDialog(dialid);
            }
        });
        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
               if (view.hasFocus())
                   showDialog(dialid);


            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id==dialid)
            return new DatePickerDialog(this,dplistener,year,month,day);
        return null;
    }


    public void submit(View view)
    {
        EditText name= (EditText) findViewById(R.id.et_name);
        EditText email= (EditText) findViewById(R.id.et_emailid);
        EditText dob= (EditText) findViewById(R.id.dob);
        EditText mob= (EditText) findViewById(R.id.mob);
        EditText add=(EditText)findViewById(R.id.address);

        EditText pas=(EditText)findViewById(R.id.passs);
        String nam,eid;
        long mobile;
        String addr;
       eid= email.getText().toString();
        addr= add.getText().toString();

        nam=name.getText().toString();
        String pasword=pas.getText().toString();
        try {
            mobile= Long.parseLong(mob.getText().toString());

        }
        catch (Exception e)
        {
            mobile=0;
        }
        if(eid.contains("@")&&(eid.contains(".com"))&&(eid!=null)&&(mobile!=0)&&(pasword.length()>=5)&& !addr.isEmpty())
        {
            Random r=new Random();
            int code= r.nextInt(9000)+1000;
             new CheckLogin().execute(eid,pasword,mobile+"",nam,addr);
            i = new Intent(this, Otp.class);

            i.putExtra("no",mobile);
            i.putExtra("code",code);
            i.putExtra("name",nam) ;
            i.putExtra("email",eid);
            i.putExtra("address",addr);
            i.putExtra("pass",pasword);
           }
         if((!eid.contains("@"))||(!eid.contains(".com")))
        {
            email.requestFocus();
            email.setText(null);
            Toast.makeText(this,"enter valid email address",Toast.LENGTH_SHORT).show();
            email.setHint("enter valid email address");
            email.setHintTextColor(Color.RED);
        }
         if (eid==null)
        {
            email.requestFocus();
            Toast.makeText(this,"enter valid email address",Toast.LENGTH_SHORT).show();
            email.setHint("enter valid email address");
            email.setHintTextColor(Color.RED);



        }

        if(mobile==0)
        {
            Toast.makeText(this,"enter mobile no",Toast.LENGTH_LONG).show();
            mob.setHint("phone no cannot be empty");
            mob.setHintTextColor(Color.RED);
        }
        if(addr.isEmpty())
        {
            Toast.makeText(this,"enter address",Toast.LENGTH_LONG).show();
            add.setHint("enter address");
            add.setHintTextColor(Color.RED);
        }
        if(pasword.length()<5)
        {

            Toast.makeText(this,"enter password",Toast.LENGTH_LONG).show();
            pas.setHint("password too short");
            pas.setText(null);
            pas.setHintTextColor(Color.RED);
        }





    }
    public static final String REGISTER_URL = "http://medicalapp.pe.hu/checkemailaddstatus.php";

    class CheckLogin extends AsyncTask<String,Void,String>
    {


        @Override
        protected void onPostExecute(String s) {

            Log.i("he",s);
            if(s.contains("yes"))
                Toast.makeText(getApplicationContext(),"email already exits",Toast.LENGTH_LONG).show();


            else             startActivity(i);

        }

        @Override
        protected String doInBackground(String... strings) {
            String id=strings[0];
            String pass=strings[1];

            long mob= Long.parseLong(strings[2]);
            String s=strings[2];
            String name=strings[3];
            String address=strings[4];

            try {
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                data+="&"+URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
                data+="&"+URLEncoder.encode("mobile","UTF-8")+"="+URLEncoder.encode(s,"UTF-8");
                data+="&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8");
                data+="&"+URLEncoder.encode("address","UTF-8")+"="+URLEncoder.encode(address,"UTF-8");
                Log.i("data",data);
                URL url = new URL(REGISTER_URL);
                URLConnection conn = url.openConnection();
                String da=URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(id,"UTF-8");
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( da );
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





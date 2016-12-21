package com.example.raunak.medicalapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OverTheCounter extends AppCompatActivity {
    EditText ed;
    ListView lv;
    String myJSON;
    JSONObject jsonObj;
    JSONArray medicines;
    JSONObject c;
    ArrayList<HashMap<String, String>> medList;


    private static final String TAG_RESULTS="result";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    int noofmedord;
    private static final String TAG_ADD ="address";
    HashMap<String, String> med;
     Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_the_counter);
        ed= (EditText) findViewById(R.id.eee);
        lv= (ListView) findViewById(R.id.listView2);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                HashMap<String, String> ll = medList.get(i);
                String name = ll.get(TAG_ID);
                final String price = ll.get(TAG_NAME);
                final String id = ll.get("ID");
                final int unit = Integer.parseInt(ll.get("noof"));
                Log.i("unn", name);
                dialog = new Dialog(
                        OverTheCounter.this);
                // Include dialog.xml file
                dialog.setContentView(R.layout.dialog);
                // Set dialog title
                dialog.setTitle("ADD TO CART");
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;

                if (unit == 0) {

                    Toast.makeText(OverTheCounter.this, "out  of stock", Toast.LENGTH_LONG).show();
                } else {
                    dialog.show();

                    dialog.getWindow().setAttributes(lp);
                    TextView t = (TextView) dialog.findViewById(R.id.dailtext);
                    TextView pri = (TextView) dialog.findViewById(R.id.price);
                    pri.setText(price + " /item");

                    t.setText(name);
                    TextView t2 = (TextView) dialog.findViewById(R.id.avail);
                    t.setHint("dd");
                    t2.setHint("Select no of Items");
                    final Spinner ss = (Spinner) dialog.findViewById(R.id.diaspin);
                    List<String> nos = new ArrayList<String>();
                    nos.add(1 + "");
                    nos.add(2 + "");
                    nos.add(3 + "");
                    nos.add(4 + "");
                    nos.add(5 + "");
                    nos.add(6 + "");
                    nos.add(7 + "");
                    // Creating adapter for spinner
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(OverTheCounter.this, android.R.layout.simple_spinner_dropdown_item, nos);


                    // attaching data adapter to spinner
                    ss.setAdapter(dataAdapter);


                    ss.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i + 1 > unit) {
                                TextView t2 = (TextView) dialog.findViewById(R.id.avail);


                                TextView TO = (TextView) dialog.findViewById(R.id.total_price);
                                TO.setText("");
                                t2.setTextColor(Color.RED);
                                t2.setText("Only " + unit + " items available");
                            } else {
                                TextView t2 = (TextView) dialog.findViewById(R.id.avail);
                                TextView TO = (TextView) dialog.findViewById(R.id.total_price);
                                t2.setTextColor(Color.GREEN);
                                t2.setText("available");
                                TO.setText("Total= Rs " + Integer.parseInt(price.substring(3)) * (i + 1) + "");
                                noofmedord = i + 1;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    Button cann = (Button) dialog.findViewById(R.id.cann);
                    cann.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    Button add = (Button) dialog.findViewById(R.id.addtocart);
                    add.setOnClickListener(new View.OnClickListener() {
                        String s1;
                        @Override

                        public void onClick(View view) {
                                Log.i("sss","ssd");
                            String v=ss.getSelectedItem().toString();
                            new PostCart().execute(id,v +"");

                        }

                    });

                    dialog.show();


                }
            }
        });
        new GetList().execute();
        ed.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("hecyy","ss");
            showList();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }



    class GetList extends AsyncTask<Void,Void,String>
    {

        @Override
        protected String doInBackground(Void... voids) {
            String url="http://medicalapp.pe.hu/nonprec.php";
            try
            {
            URL ur=new URL(url);
            HttpURLConnection conn=(HttpURLConnection) ur.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            String res;
            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK)
            {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                res = sb.toString();
                return res;
            }

        }
            catch (Exception e)
            {
                return "error";
            }
            return "error";
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!="error") {
                myJSON = s;

                showList();
            }
        }
    }
    class PostCart extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            try {
                SharedPreferences s = getSharedPreferences("MyPref", MODE_PRIVATE);
                String s1 = s.getString("email", "");
                String id=strings[0];
                String unit=strings[1];
                String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(s1, "UTF-8");
                data += "&" + URLEncoder.encode("units", "UTF-8") + "=" + URLEncoder.encode(unit + "", "UTF-8");
                data += "&" + URLEncoder.encode("pno", "UTF-8") + "=" + URLEncoder.encode(id + "", "UTF-8");

                String reg = "http://medicalapp.pe.hu/cart.php";
                URL url = new URL(reg);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter o = new OutputStreamWriter(conn.getOutputStream());
                o.write(data);
                o.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                return line;


            }
            catch (Exception e)
            {
                Log.i("exx",e.toString());
                return "fail";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("raunak",s);
            if (s.equals( "yes")) {
                dialog.dismiss();

                Toast.makeText(OverTheCounter.this, "Added to cart", Toast.LENGTH_LONG).show();

            } else
            {
                Toast.makeText(OverTheCounter.this,"failed to add to cart",Toast.LENGTH_LONG);
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

    void showList()
     {
         medicines = null;

         medList=new ArrayList<HashMap<String, String>>();

         EditText e= (EditText) findViewById(R.id.eee);
         String ss=e.getText().toString().toLowerCase();
         try {
             jsonObj = new JSONObject(myJSON);
             medicines = jsonObj.getJSONArray("result");

             for(int i=0;i<medicines.length();i++){
                 c = medicines.getJSONObject(i);
                 String id = c.getString("id");
                 String name = c.getString("name");
                JSONArray status=new JSONArray();
                 String price = "Rs "+c.getString("price");
                 String unit=c.getString("units");
                 int no= Integer.parseInt(unit);
                    Log.i("heyy",price+"");

                 if(Integer.parseInt(unit)>0)
                 {
                     unit="available";
                 }
                 else
                 {
                     unit="out of stock";
                 }

                 if(name.toLowerCase().contains(ss)) {


                         med = new HashMap<String, String>();
                     med.put("noof",no+"");
                     med.put(TAG_ID, name);
                     med.put(TAG_NAME, price);
                     med.put("ID",id);
                     med.put(TAG_ADD, unit);


                     medList.add(med);
                 }
             }
             ListAdapter adapter;
             adapter = new SimpleAdapter(
                     OverTheCounter.this, medList, R.layout.medlist,
                     new String[]{TAG_ID,TAG_NAME,TAG_ADD},
                     new int[]{R.id.medname, R.id.medprice, R.id.medunit}
             );

             lv.setAdapter(adapter);

         } catch (JSONException e1) {
             e1.printStackTrace();
         }
     }
}

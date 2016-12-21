package com.example.raunak.medicalapp;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class Cart extends AppCompatActivity {
    ListView list;
    private String myJSON;
    private JSONObject jsonObj;
    private JSONArray medicines;
    private ArrayList<HashMap<String, String>> medList;
    private JSONObject c;
    private HashMap<String, String> med;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        list= (ListView) findViewById(R.id.listt);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String textView=((TextView)view).getText().toString();


                Toast.makeText(Cart.this,textView,Toast.LENGTH_SHORT).show();

            }
        });
        new GetCart().execute();

    }
    class GetCart extends AsyncTask<Void,Void,String>
    {

        @Override
        protected String doInBackground(Void... voids) {
            String url="http://medicalapp.pe.hu/getCart.php";
            try
            {
                SharedPreferences sp=getSharedPreferences("MyPref",MODE_PRIVATE);
                String email=sp.getString("email","");

                String data= URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8");
                URL ur=new URL(url);
                HttpURLConnection conn=(HttpURLConnection) ur.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStreamWriter o = new OutputStreamWriter(conn.getOutputStream());
                o.write(data);
                o.flush();

                String res;
                Log.i("exxx",conn.getResponseCode()+"");
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
                    Log.i("resultt",res);
                    return res;
                }

            }
            catch (Exception e)
            {
                Log.i("except",e.toString());
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
    int  total=0;

    void showList() {
        String id = null;
        String price = null;
        String unit;

        String name = null;


        medicines = null;
        medList = new ArrayList<HashMap<String, String>>();
        JSONArray arr;
        JSONObject ca = null;
        try {
            jsonObj = new JSONObject(myJSON);
            medicines = jsonObj.getJSONArray("result");
            arr = jsonObj.getJSONArray("unitorder");
            for (int i = 0; i < medicines.length(); i++) {
                c = medicines.getJSONObject(i);
                id = c.getString("id");
                name = c.getString("name");
                price = "Rs " + c.getString("price");
               // if(c.has("units"))
                unit = c.getString("units");
               int no = Integer.parseInt(unit);
                //Log.i("heyy", price + "");

                if (Integer.parseInt(unit) > 0) {
                    unit = "available";
                } else {
                    unit = "out of stock";
                }



               ca=arr.getJSONObject(i);
                String no_ordered=ca.getString("unitsordered");
                total+=Integer.parseInt(price.substring(3))*Integer.parseInt(no_ordered);
        med = new HashMap<String, String>();
              med.put("units",no+"");
        med.put("name", name);
        med.put("price", price);
        med.put("ID", id);
                med.put("status",unit);
        //    med.put("status", unit);
        med.put("units_ordered",no_ordered+" item(s) currently selected");


        medList.add(med);
    }
        } catch (JSONException e1) {
            Log.i("excep", e1.toString());
        }

            TextView ttt= (TextView) findViewById(R.id.heyy);
        ttt.setText("TOTAL AMOUNT=Rs"+total+"");
            ListAdapter adapter;

            adapter = new SimpleAdapter(
                    Cart.this, medList, R.layout.cartlist,
                    new String[]{"name","price","status","units_ordered"},
                    new int[]{R.id.cartname, R.id.cartprice,R.id.avail,R.id.cartunit}
            );

            list.setAdapter(adapter);

    }
    public void proceed(View v)
    {

    }
    }



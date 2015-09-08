package com.example.win7cmurder.boxer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class showmanifest extends ActionBarActivity {

    JSONParser jParser = new JSONParser();
    JSONArray products = null;
    String bigString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showmanifest);
        new task().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_showmanifest, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class task extends AsyncTask<String, String, Void>
    {
        private ProgressDialog progressDialog = new ProgressDialog(showmanifest.this);
        String result = "";
        String responseBody = "";
        protected void onPreExecute() {
            progressDialog.setMessage("Authenticating data...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    task.this.cancel(true);
                }
            });
        }
        @Override
        protected Void doInBackground(String... params) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.241.212.197/boxer/showManifest.php");
            // Add your data
            List< NameValuePair > nameValuePairs = new ArrayList<NameValuePair>(5);
            nameValuePairs.add(new BasicNameValuePair("u",""));
            nameValuePairs.add(new BasicNameValuePair("p", ""));
            JSONObject json = jParser.makeHttpRequest("http://192.241.212.197/boxer/showManifest.php", "GET", nameValuePairs);
            try {
                products = json.getJSONArray("products");
                bigString="";
                for (int i = 0; i < products.length(); i++) {
                    JSONObject c = products.getJSONObject(i);

                   /* for(int a = 0; a<c.names().length(); a++){
                        Log.d("array", c.names().getString(a) + " value = " + c.get(c.names().getString(a)));
                        bigString += c.names().getString(a).toString() + " = " + c.get(c.names().getString(a).toString()+"\r\n");
                    }*/

                    //Storing each json item in variable
                    bigString += "id "+c.getString("id")+"\r\n";
                    bigString += "number of people "+ c.getString("number_of_people")+"\r\n";
                    bigString += "area id "+c.getString("area_id")+"\r\n";
                    bigString += "timezone "+c.getString("timezone")+"\r\n";
                    bigString += "restaurant notes "+c.getString("restaurant_notes")+"\r\n";
                    bigString += "client notes "+c.getString("client_notes")+"\r\n";
                    bigString += "courier notes "+c.getString("courier_notes")+"\r\n";
                    bigString += "accounting notes "+c.getString("accounting_notes")+"\r\n";
                    bigString += "internal notes "+c.getString("internal_notes")+"\r\n";
                    bigString += "client reference number "+c.getString("client_reference_number")+"\r\n";

                    bigString += "driver id "+c.getString("driver_id")+"\r\n";
                    bigString += "driver email "+c.getString("driver_email")+"\r\n";
                    bigString += "driver first name "+c.getString("driver_first_name")+"\r\n";
                    bigString += "driver_last_name "+c.getString("driver_last_name")+"\r\n";
                    bigString += "driver_phone_number "+c.getString("driver_phone_number")+"\r\n";

                    bigString += "pickup_location_id "+c.getString("pickup_location_id")+"\r\n";
                    bigString += "pickup_location_arrival_time "+c.getString("pickup_location_arrival_time")+"\r\n";
                    bigString += "pickup_location_name "+c.getString("pickup_location_name")+"\r\n";
                    bigString += "pickup_location_address_line_1 "+c.getString("pickup_location_address_line_1")+"\r\n";
                    bigString += "pickup_location_address_line_2 "+c.getString("pickup_location_address_line_2")+"\r\n";
                    bigString += "pickup_location_postal_code "+c.getString("pickup_location_postal_code")+"\r\n";
                    bigString += "pickup_location_city "+c.getString("pickup_location_city")+"\r\n";
                    bigString += "pickup_location_province "+c.getString("pickup_location_province")+"\r\n";
                    bigString += "pickup_location_country "+c.getString("pickup_location_country")+"\r\n";
                    bigString += "pickup_location_country "+c.getString("pickup_location_longitude")+"\r\n";
                    bigString += "pickup_location_latitude "+c.getString("pickup_location_latitude")+"\r\n";

                    bigString += "drop_off_location_id "+c.getString("drop_off_location_id")+"\r\n";
                    bigString += "drop_off_location_arrival_time "+c.getString("drop_off_location_arrival_time")+"\r\n";
                    bigString += "drop_off_location_name "+c.getString("drop_off_location_name")+"\r\n";
                    bigString += "drop_off_location_address_line_1 "+c.getString("drop_off_location_address_line_1")+"\r\n";
                    bigString += "drop_off_location_address_line_2 "+c.getString("drop_off_location_address_line_2")+"\r\n";
                    bigString += "drop_off_location_postal_code "+c.getString("drop_off_location_postal_code")+"\r\n";
                    bigString += "drop_off_location_city "+c.getString("drop_off_location_city")+"\r\n";
                    bigString += "drop_off_location_province "+c.getString("drop_off_location_province")+"\r\n";
                    bigString += "drop_off_location_country "+c.getString("drop_off_location_country")+"\r\n";
                    bigString += "drop_off_location_longitude "+c.getString("drop_off_location_longitude")+"\r\n";
                    bigString += "drop_off_location_latitude "+c.getString("drop_off_location_latitude")+"\r\n";
                    bigString += "\r\n";
                 }//for

                List< NameValuePair > nameValuePairs1 = new ArrayList<NameValuePair>(5);
                nameValuePairs1.add(new BasicNameValuePair("u",""));
                nameValuePairs1.add(new BasicNameValuePair("p", ""));
                JSONObject json1 = jParser.makeHttpRequest("http://192.241.212.197/boxer/showOrders.php", "GET", nameValuePairs1);
                JSONArray products1 = json1.getJSONArray("products");
                for (int i = 0; i < products1.length(); i++) {
                    JSONObject c = products1.getJSONObject(i);
                    bigString += c.getString("order_id")+"\r\n";
                    bigString += c.getString("order_name")+"\r\n";
                    bigString += c.getString("order_description")+"\r\n";
                    bigString += c.getString("order_quantity")+"\r\n\r\n";
                }



                }
                catch (Exception ex){
                    ex.printStackTrace();
                }




                return null;

        }
        protected void onPostExecute(Void v) {

            TextView t = (TextView)findViewById(R.id.textView4);
            t.setMovementMethod(new ScrollingMovementMethod());
            t.setText(bigString);
            this.progressDialog.dismiss();


        }
    }//classasync

}

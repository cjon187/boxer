package com.example.win7cmurder.boxer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class login extends ActionBarActivity {
TextView txtPassword,txtUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sp = getSharedPreferences("key", 0);
        String tValue = sp.getString("login", "");
        if (!tValue.equals("")){
            Intent myIntent = new Intent(login.this, drops.class);
            login.this.startActivity(myIntent);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    public void login(View view)
    {
        txtPassword = (TextView) findViewById(R.id.txtPassword);
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        //validate
        if (username.equals("") || password.equals("")){
            txtUsername.setText("");
            txtPassword.setText("");
            Toast.makeText(getApplicationContext(),"LOGIN FAIL",Toast.LENGTH_SHORT).show();
        }
        else{
            new task().execute(username,password);
        }


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
    class task extends AsyncTask<String, String,String[]>
    {
        private ProgressDialog progressDialog = new ProgressDialog(login.this);
        String result = "";
        String responseBody = "";
        String jon[]=new String[2];
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
        protected String[] doInBackground(String... arg0) {
            String username=arg0[0];
            String password=arg0[1];

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.241.212.197/boxer/login.php");
            // Add your data
            List< NameValuePair > nameValuePairs = new ArrayList<NameValuePair>(5);
            nameValuePairs.add(new BasicNameValuePair("u", username));
            nameValuePairs.add(new BasicNameValuePair("p", password));


            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseBody = EntityUtils.toString(response.getEntity());

                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            jon[0]=responseBody;
            jon[1]=username;
            return jon;

        }
        protected void onPostExecute(String[] jon) {
            String response = jon[0];
            String username =jon[1];
                if (response.equals("bad")){
                    Toast.makeText(getApplication(),"Login Error",Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences sp = getSharedPreferences("key", 0);
                    SharedPreferences.Editor sedt = sp.edit();
                    sedt.putString("login",username);
                    sedt.commit();
                    Intent myIntent = new Intent(login.this, drops.class);
                    login.this.startActivity(myIntent);
                    finish();
                }

                this.progressDialog.dismiss();


        }
    }//classasync
}//mainclass


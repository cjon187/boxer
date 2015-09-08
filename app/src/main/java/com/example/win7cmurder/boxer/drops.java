package com.example.win7cmurder.boxer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class drops extends Activity implements MqttCallback,AdapterView.OnItemClickListener {
    MqttClient client;
    MqttClient client2;
    String mqttid = generateid(15);
    String username;
    double lat, lon;
    BroadcastReceiver mConnReceiver;
    ArrayList<Car> arrayCars;
    ListView listViewCars;
    ListCarsAdapter adapter;

    JSONParser jParser = new JSONParser();
    JSONArray products = null;

    EditText editText;
    Button send;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drops);


        send = (Button) findViewById(R.id.buttonSend);
        editText = (EditText) findViewById(R.id.editSend);
        /*registerReceiver(
                new networkChecker(),
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));*/


        //////



        //network listener
        mConnReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

                if (noConnectivity == true) {
                    Log.d("info", "No internet connection");
                    //checkservice();


                } else {
                    Log.d("info", "Interet connection is UP");
                    new connect().execute();

                }
            }
        };
        registerReceiver(mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        SharedPreferences sp = getSharedPreferences("key", 0);
        String tValue = sp.getString("textvalue", "");
        username = sp.getString("login", "");

        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500,
                0, mLocationListener);
        if (client == null) {
            new connect().execute("");
        } else {
            Log.d("dont need to starts", "");
        }

        arrayCars = new ArrayList<Car>();
        new load().execute();

    }

    //notification
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void Notify(String notificationTitle, String notificationMessage) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.notification_template_icon_bg);
        Intent intent = new Intent(this, drops.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.abc_ab_share_pack_mtrl_alpha));
        builder.setContentTitle(notificationTitle);
        builder.setSound(soundUri);
        builder.setContentText(notificationMessage);
        //builder.setSubText("Tap to view documentation about notifications.");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
            //Log.d("loc chages",location.getLatitude()+" "+location.getLongitude());
            lat = location.getLatitude();
            lon = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void connectionLost(Throwable throwable) {
        Log.d("coon", "conn lost retrying to load");
        //Notify("Network ChangeDetected","Network has changed reopen app");

    }

    @Override
    public void messageArrived(String s, MqttMessage message) throws Exception {
        final String msg = message.toString();
        String[] separated = msg.split("~");
        final String realmsg = separated[0]; // this will contain "Fruit"
        final String time = separated[1]; // this will contain " they taste good"
        if (realmsg.equals("gps")) {
            gps();
        } else {
            System.out.println(message);
            Handler h = new Handler(this.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Notify("Foodee", msg);
                    //add new thing to listview

                    String side="";

                    Car jon = new Car(R.mipmap.download, username, realmsg, time,"right");
                    arrayCars.add(jon);
                    listViewCars.setAdapter(adapter);
                    listViewCars.setSelection(adapter.getCount() - 1);
                }
            });



        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        Log.d("messaged delievers", "");
    }

    protected String append(String existing_contact, String new_contact) {
        String latestfavContacts = existing_contact + new_contact;
        return latestfavContacts;
    }

    //handle activity states
    protected void onResume() {
        super.onResume();
        {
            new load().execute();
            new connect().execute("");
            try {
                registerReceiver(mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            } catch (Exception e) {
                Log.d("cannot register", "");
            }
        }
    }

    protected void onRestart() {
        super.onRestart();
        {
            try {
                registerReceiver(mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            } catch (Exception e) {
                Log.d("cannot register", "");
            }
            new load().execute();
            new connect().execute("");

        }
    }

    protected void onStop() {
        super.onStop();

            unregisterReceiver(mConnReceiver);



    }


    protected void onDestroy() {
        super.onDestroy();
        {

        }
    }

    public void gps() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        try {
            Calendar cal = Calendar.getInstance();
            String now = String.valueOf(cal.get(Calendar.HOUR)) + ":" + String.valueOf(cal.get(Calendar.MINUTE));
            String append = "~" + String.valueOf(cal.get(Calendar.HOUR)) + ":" + String.valueOf(cal.get(Calendar.MINUTE));
            Log.d("GPS", lat + " " + lon);

            String latlon = Double.toString(lat) + "," + Double.toString(lon);

            MemoryPersistence persistence1 = new MemoryPersistence();
            client2 = new MqttClient("tcp://192.241.212.197:1883", generateid(15), persistence1);
            client2.connect();
            //client2.setCallback(this);
            String link = "<div class=\"ui-bar ui-bar-a ui-corner-all\"><a href=\"https://www.google.com/maps/embed/v1/place?q=" + Double.toString(lat) + "%2C" + Double.toString(lon) + "&key=AIzaSyDSS7De8hhOvvhx3djmHlpye2ht8_39y5s\" target=\"i\">" + username + " map</a>" + append;
            MqttMessage message2 = new MqttMessage();
            message2.setPayload(link
                    .getBytes());
            client2.publish("admin/"+username, message2);

            //upload to db
            // Create a new HttpClient and Post Header

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.241.212.197/webmq/chatinsert.php");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("message", link + append));
                nameValuePairs.add(new BasicNameValuePair("sen", username));
                nameValuePairs.add(new BasicNameValuePair("rec", "Dispatch"));
                nameValuePairs.add(new BasicNameValuePair("sentTime", now));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("sending msg error", "");
        }


    }//gps

    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";

    public String generateid(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }


    ////////connect to mqtt broker
    private class connect extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                MemoryPersistence persistence = new MemoryPersistence();
                client = new MqttClient("tcp://192.241.212.197:1883", username, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(false);
                connOpts.setKeepAliveInterval(60);
                client.connect(connOpts);
                client.setCallback(drops.this);
                client.subscribe(username, 1);
                Log.d("connnetc worked", "");
            } catch (MqttException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public void send (View view){
        String msg=editText.getText().toString();
        if(client==null){
            new connect().execute();
        }
        new send().execute(msg);
    }
    ////////////////send message
    private class send extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog = new ProgressDialog(drops.this);
        Calendar cal = Calendar.getInstance();
        String now = String.valueOf(cal.get(Calendar.HOUR))+":"+String.valueOf(cal.get(Calendar.MINUTE));
        String append = "~"+String.valueOf(cal.get(Calendar.HOUR))+":"+String.valueOf(cal.get(Calendar.MINUTE));
        @Override
        protected String doInBackground(String... arg0) {


            String msg=arg0[0];
            try {
                new connect().execute();
                MqttMessage message = new MqttMessage();
                String temp="<r>"+username+"</r>-><b>Dispatch: </b>"+"<div class=\"ui-bar ui-bar-a ui-corner-all\">"+msg+"</div>"+append;
                message.setPayload(temp
                        .getBytes());
                client.publish("admin/"+username, message);

                //client.close();
            } catch (MqttException e) {
                e.printStackTrace();
            }

            //insert into db
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://192.241.212.197/webmq/chatinsert.php");

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("message", msg + append));
                nameValuePairs.add(new BasicNameValuePair("sen", username));
                nameValuePairs.add(new BasicNameValuePair("rec", "Dispatch"));
                nameValuePairs.add(new BasicNameValuePair("sentTime", now));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("sent", result);
            this.progressDialog.dismiss();
            send.setAlpha(1);
            send.setClickable(true);
            editText.setEnabled(true);
            editText.setText("");
            String side="";
            //add new thing to listview
            if(username.equals("Dispatch")){
                side="right";
            }
            else{
                side="left";
            }

            Car jon = new Car(R.mipmap.download, username,result ,now, side);
            arrayCars.add(jon);
            adapter=null;
            adapter = new ListCarsAdapter(drops.this, arrayCars);
            listViewCars.setAdapter(adapter);
            listViewCars.setSelection(adapter.getCount() - 1);

        }

        @Override
        protected void onPreExecute() {
            send.setAlpha(.5f);
            send.setClickable(false);
            editText.setEnabled(false);
            progressDialog.setMessage("Authenticating data...");
            progressDialog.show();
            /*progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface arg0) {
                    send.this.cancel(true);
                }
            });*/
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
    ///////end of send message
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Car selectedCar = arrayCars.get(position);
        Toast.makeText(this, "You've selected :\n Car Model : " + selectedCar.getSender() + "\n Car Color : "+ selectedCar.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void add(View view){
        arrayCars = new ArrayList<Car>();
        new load().execute();
     }

    public void goManifest(View view){
        Intent myIntent = new Intent(drops.this, notice.class);
        drops.this.startActivity(myIntent);
    }
    //////load the listlive from db
    private class load extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... arg0) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("u", username));
            JSONObject json = jParser.makeHttpRequest("http://192.241.212.197/boxer/showMessages.php", "GET", params);

            try {
                products = json.getJSONArray("products");

                for (int i = 0; i < products.length(); i++) {
                    JSONObject c = products.getJSONObject(i);

                    // Storing each json item in variable
                    String id = c.getString("id");
                    String sen = c.getString("sen");
                    String rec = c.getString("rec");
                    String msg = c.getString("message");
                    String sendTime=c.getString("sendtime");

                    String side="";
                    if(sen.equals("Dispatch")){
                        side="right";
                    }
                    else{
                        side="left";
                    }
                    Car jon = new Car(R.mipmap.download,sen , msg, sendTime,side);
                    arrayCars.add(jon);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            listViewCars = (ListView) findViewById(R.id.list_cars);
            adapter=null;
            adapter = new ListCarsAdapter(drops.this, arrayCars);
            listViewCars.setAdapter(adapter);
            // Set the onItemClickListener on the ListView to listen for items clicks
            listViewCars.setOnItemClickListener(drops.this);
            //scroll to bottom
            listViewCars.setSelection(adapter.getCount() - 1);

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}


package com.example.phenom23.pokemon;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import static com.example.phenom23.pokemon.Pokedex.EXTRA_MESSAGE;
import static com.example.phenom23.pokemon.R.id.imageView;
import static com.example.phenom23.pokemon.R.id.text2;
import static com.example.phenom23.pokemon.R.id.textView2;


public class PokeDetail extends AppCompatActivity {
    private  String TAG = PokeDetail.class.getSimpleName();



    private ProgressDialog pDialog;
    private static String url = "http://pokeapi.co/api/v2/pokemon/";
    public String Pokid;
    ArrayList<HashMap<String, String>> pokemonList;
    private int weight;
    
    private class Getpokemons extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(PokeDetail.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();



        }

        @Override  
   protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    String test= jsonObj.getString("weight");
                    Log.e(TAG, "Response from test: " +test.toString());

                    weight=Integer.parseInt(test);


                    JSONArray moves= jsonObj.getJSONArray("moves");
                    Pokid=moves.length()+"";

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    PokeDetail.this, pokemonList,
                    R.layout.list_item, new String[]{"id ", "name" }, new int[]{R.id.id, R.id.name});
			TextView textView2= (TextView) findViewById(R.id.textView2);
            textView2.setText("This pokemon has a weight of "+weight+"\nAnd it knows "+Pokid+" moves");
        }

    }









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_detail);
        Intent intent = getIntent();
        String message = intent.getStringExtra("key");
        String id = intent.getStringExtra("id");

        TextView textView = (TextView) findViewById(R.id.textView);
        // message+=EXTRA_MESSAGE;
        // Capture the layout's TextView and set the string as its text
        textView.setText(message);
        ImageView imageview = (ImageView) findViewById(R.id.imageView);

        Picasso.with(this).load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + id + ".png").into(imageview);

        url= "http://pokeapi.co/api/v2/pokemon/"+id;

        new Getpokemons().execute();


    }





}

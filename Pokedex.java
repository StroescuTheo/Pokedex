package com.example.phenom23.pokemon;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
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


public class Pokedex extends AppCompatActivity {
    public static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";



//http://pokeapi.co/api/v2/pokemon/1/ API


    private String TAG = Pokedex.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private static String url = "http://pokeapi.co/api/v2/pokemon/";
    public String Pokid;
    ArrayList<HashMap<String, String>> pokemonList;
    public ArrayList<String> cL=new ArrayList<String>();
    private class Getpokemons extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Pokedex.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();


            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,int position, long id)
                {
					String selectedFromList = (lv.getItemAtPosition(position).toString());
					String substr=selectedFromList.substring(0,selectedFromList.indexOf(","));
					String finName=substr.substring(6,substr.length());
					String  substr2=selectedFromList.substring(selectedFromList.indexOf("pokemon/"));
					Pokid= substr2.substring(8,substr2.length()-2);
					Intent myIntent = new Intent(Pokedex.this, PokeDetail.class);
					myIntent.putExtra("id",Pokid);
					myIntent.putExtra("key",finName);
					Pokedex.this.startActivity(myIntent);

                }});
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
                    JSONArray pokemon= jsonObj.getJSONArray("results");
                    for (int i = 0; i < pokemon.length(); i++) {
                        JSONObject c = pokemon.getJSONObject(i);
                        String name = c.getString("name");
                        String id= c.getString("url");
                        HashMap<String, String> pokemon = new HashMap<>();
                        pokemon.put("name", name);
                        cL.add(name);
                        pokemon.put("id",id);
                        pokemonList.add(pokemon);
                    }
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
                    Pokedex.this, pokemonList,
                    R.layout.list_item, new String[]{"name"}, new int[]{R.id.name});

            lv.setAdapter(adapter);

            Log.e(TAG, "Response from cl: " + cL);

            final Button button = (Button) findViewById(R.id.button);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                    Intent myIntent = new Intent(Pokedex.this, Compare.class);


                    myIntent.putStringArrayListExtra("id",  cL);
                    Pokedex.this.startActivity(myIntent);
                }
            });


        }

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex);
        pokemonList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new Getpokemons().execute();




    }




}

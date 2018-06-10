package com.example.phenom23.pokemon;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class Compare extends AppCompatActivity {


    private String TAG = Pokedex.class.getSimpleName();

    private Spinner spinner1, spinner2;
    private Button btnSubmit;
    ArrayList<String> listP=new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        addItemsOnSpinner2();
        addListenerOnButton();
        addListenerOnSpinnerItemSelection();




    }


    // add items into spinner dynamically
    public void addItemsOnSpinner2() {

        Intent intent = getIntent();
        listP = intent.getStringArrayListExtra("id");
        Log.e(TAG, "Response from COMP: "+listP.size());
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner1 = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        Log.e(TAG, "enter add"+listP.size());
        for(int i=0 ;i<listP.size();i++)
        {
            Log.e(TAG, "Response from COMP: "+i+" " + listP.get(i));

            list.add((listP.get(i)).toString());

        }

        Collections.sort(list, String.CASE_INSENSITIVE_ORDER);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
        spinner1.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner1 = (Spinner) findViewById(R.id.spinner);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {

        spinner1 = (Spinner) findViewById(R.id.spinner  );
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        btnSubmit = (Button) findViewById(R.id.button2);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(Compare.this,
                        "OnClickListener : " +
                                "\nSelection 1 : "+ String.valueOf(spinner1.getSelectedItem()) +
                                "\nSelection 2 : "+ String.valueOf(spinner2.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();



               final Random r = new Random();
                int hp1= r.nextInt(100);
                int pow_max1=r.nextInt(50);
                int hp2= r.nextInt(100);
                int pow_max2=r.nextInt(50);

                while( pow_max1>hp2)
                {
                    pow_max1=r.nextInt(50);
                    hp2=r.nextInt(100);
                }
                while( pow_max2>hp1)
                {
                    pow_max2=r.nextInt(50);
                    hp1=r.nextInt(100);
                }

                final int pow_maxf1=pow_max1;
                final int pow_maxf2=pow_max2;


                final TextView tv = (TextView) findViewById(R.id.textView4);
                tv.setMovementMethod(new ScrollingMovementMethod());
               final String pok1=String.valueOf((spinner1.getSelectedItem()));
                final String pok2=String.valueOf((spinner2.getSelectedItem()));
                if (pok1==pok2)
                {
                    Toast.makeText(Compare.this,
                            "The same pokemon can't fight with each other",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    TextView stat = (TextView) findViewById(R.id.textView7);
                    stat.setText("Current stats:\n" + "Pokemon\t\t" + "HP\n"
                            + pok1 + "\t\t" + hp1 + "\n" + pok2 + "\t\t" + hp2);
                    while (hp1 > 0 && hp2 > 0) {
                        int attack1 = r.nextInt(pow_maxf1);
                        int attack2 = r.nextInt(pow_maxf2);

                        hp1 = hp1 - attack2;
                        hp2 = hp2 - attack1;
                        tv.setText(tv.getText() + "\n" + pok1
                                + " has attacked " + pok2 + "with an attack of " + attack1 + " leaving him with " + hp2 + "hp\n"
                                + pok2 + " has attacked " + pok1 + "with an attack of " + attack2 + " leaving him with " + hp1 + "hp\n");
                        stat.setText("Current stats:\n" + "Pokemon\t" + "HP\n"
                                + pok1 + "\t" + hp1 + "\n" + pok2 + "\t" + hp2);


                    }

                    TextView winner = (TextView) findViewById(R.id.textView5);
                    if (hp1 < 0 && hp2 < 0) {
                        winner.setText("Draw");
                    }
                    if (hp1 < 0) {
                        winner.setText("Winner is " + pok2);
                    } else if (hp2 < 0) {
                        winner.setText("Winner is " + pok1);
                    }
                }






            }

        });
    }

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            Toast.makeText(parent.getContext(),
                    "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}

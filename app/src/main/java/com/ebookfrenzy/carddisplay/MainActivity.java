package com.ebookfrenzy.carddisplay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private ListView myList;
    final ArrayList<String> cardNamesList = new ArrayList<String>();
    final ArrayList<String> cardEffectsList = new ArrayList<String>();

    final HashMap<String, String> cardsAndEffects = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myList = (ListView) findViewById(R.id.card_list_view);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3){
                String value = (String) adapter.getItemAtPosition(position);
                try {
                    Card card = parseCardInfo(cardsAndEffects.get(value));
                    Toast.makeText(MainActivity.this, card.print(),
                            Toast.LENGTH_LONG).show();
                } catch (IOException e){}
            }
        });

        //loads the card names and effects into a hashmap
       try {
           scanFileToList();
       } catch (IOException e){
            Log.v("error", "Can't find file");
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, cardNamesList);
       myList.setAdapter(adapter);

    }

    public  void scanFileToList() throws FileNotFoundException, IOException {
        int count = 0;
            InputStream inputStream = getResources().openRawResource(R.raw.effects);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    Log.v("effect", line);
                    Card c = parseCardInfo(line);
                    if (c != null) {
                        cardNamesList.add(c.getName());
                        cardsAndEffects.put(c.getName(), line);
                    }
                }
            }
    }

    public static Card parseCardInfo(String cardData) throws MalformedURLException, IOException {
                        Card thisCard = new Card();
                        boolean ST = false;
        System.out.println(cardData);
        thisCard.setRawInfo(cardData);

        Log.v("hello", "right here for " + cardData);
        String status = cardData.substring(cardData.indexOf("\"status\"")+9, cardData.indexOf("," +
                ""));
        if (status.equals("\"fail\"")){
//            thisCard.setName("ERROR, CARD NOT FOUND");
//            thisCard.setText("");
//            thisCard.setCard_type("");
//            thisCard.setType("");
//            thisCard.setFamily("");
//            thisCard.setAtk(0);
//            thisCard.setDef(0);
//            thisCard.setLevel(0);
            return null;
        } else {

            String name = cardData.substring(cardData.indexOf("\"name\"") + 7, cardData.indexOf(",\"text\""));
            thisCard.setName(name.replace("\"", ""));

            String text = cardData.substring(cardData.indexOf("\"text\"") + 7, cardData.indexOf(",\"card_type\""));
            thisCard.setText(text.replace("\"", ""));

            String card_type = cardData.substring(cardData.indexOf("\"card_type\"") + 12, cardData.indexOf(",\"type\""));
            thisCard.setCard_type(card_type.replace("\"", ""));
            if (thisCard.getCard_type().equals("trap") || thisCard.getCard_type().equals("spell"))
                ST = true;

            String type = cardData.substring(cardData.indexOf("\"type\"") + 7, cardData.indexOf(",\"family\""));
            if (ST) thisCard.setType(null);
            else thisCard.setType(type.replace("\"", ""));

            String family = cardData.substring(cardData.indexOf("\"family\"") + 9, cardData.indexOf(",\"atk\""));
            if (ST) thisCard.setFamily(null);
            else thisCard.setFamily(family.replace("\"", ""));

            if (ST) thisCard.setAtk(0);
            else {
                int atk = Integer.parseInt(cardData.substring(cardData.indexOf("\"atk\"") + 6, cardData.indexOf(",\"def\"")));
                thisCard.setAtk(atk);
            }

            if (ST) thisCard.setAtk(0);
            else {
                int def = Integer.parseInt(cardData.substring(cardData.indexOf("\"def\"") + 6, cardData.indexOf(",\"level\"")));
                thisCard.setDef(def);
            }

            if (ST) thisCard.setLevel(0);
            else {
                int level = Integer.parseInt(cardData.substring(cardData.indexOf("\"level\"") + 8, cardData.indexOf(",\"property\"")));
                thisCard.setLevel(level);
            }
        }
        return thisCard;
    }


}

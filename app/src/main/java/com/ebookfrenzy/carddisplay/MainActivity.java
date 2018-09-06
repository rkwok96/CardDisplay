package com.ebookfrenzy.carddisplay;

import android.support.v4.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private static final int REQUEST_WRITE_PERMISSION = 786;

    private ListView myList;
    final ArrayList<String> cardNamesList = new ArrayList<String>(); // list of card names

    final HashMap<String, String> cardsAndEffects = new HashMap<String, String>(); // hashmap of card name / unparsed card data pairs
    static final ArrayList<Card> cardslist = new ArrayList<Card>(); // list of all the card objects for use in the adapter
    public static final String CARD_KEY = "card_key";
    public static EditText search;
    public static CardAdapter adapter;
    public static CheckBox cBox;
    public boolean isChecked = false;
    private static final int PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);

        final DownloadFileAsync downloadTask = new DownloadFileAsync();

        search = (EditText) findViewById(R.id.searchText);
        cBox = (CheckBox) findViewById(R.id.checkBox);

        myList = (ListView) findViewById(R.id.card_list_view);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3){
                Helper.hideKeyboard(MainActivity.this, v);

                Card card = (Card) adapter.getItemAtPosition(position);
                String value = card.getRawInfo();

                Bundle bundle = new Bundle();
                bundle.putParcelable(CARD_KEY, card);
                Fragment fragment = new CardDispFragment();
                fragment.setArguments(bundle);

//                cBox.setVisibility(View.INVISIBLE);

                //not sure
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.your_placeholder, fragment);
                ft.addToBackStack(null);
                ft.commit();


            }
        });

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View v,
                                           int position, long arg3) {
                Helper.hideKeyboard(MainActivity.this, v);
                Card card = (Card) adapter.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putParcelable(CARD_KEY, card);
                ImageDialog fragment = new ImageDialog();
                fragment.setArguments(bundle);

                FragmentManager fm = getSupportFragmentManager();
                fragment.show(fm, "tag");

                return true;
            }

        });

        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Helper.hideKeyboard(MainActivity.this, v);
                }
            }
        });

        //loads the card names and effects into a hashmap
       try {
           scanFileToList();
       } catch (IOException e){
            Log.v("error", "Can't find file");
        }

        adapter = new CardAdapter(this, cardslist);

       Log.v("fucking", "size in main " + cardslist.size());
//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, cardNamesList);
        Log.v("fucking", "" +adapter.getCount());
       myList.setAdapter(adapter);

       cBox.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(cBox.isChecked()){
                   isChecked = true;
                   if(!search.getText().toString().equals(""))
                       adapter.getFilter(isChecked).filter(search.getText().toString());
               } else {
                   isChecked = false;
                   if(!search.getText().toString().equals(""))
                   adapter.getFilter(isChecked).filter(search.getText().toString());
               }
           }
       });


       search.addTextChangedListener((new TextWatcher(){
           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count){
 //              System.out.println("Text ["+s+"]");
               adapter.getFilter(isChecked).filter(s.toString());
           }

           @Override
           public void beforeTextChanged(CharSequence s, int start, int count,
                                         int after) {
//               cBox.setVisibility(View.VISIBLE);
           }

           @Override
           public void afterTextChanged(Editable s) {
               myList.setSelection(0);
           }
       }));

       requestPermission();
       downloadTask.execute("https://cs.sankakucomplex.com/data/76/f8/76f8d8a633fa25719a729b6d5ce907a8.jpg?e=1533143855&m=lanJ9-s-4hhyADOKh_12wA");

    }



    public  void scanFileToList() throws FileNotFoundException, IOException {
        int count = 0;
            emptyCardLists();
            InputStream inputStream = getResources().openRawResource(R.raw.effects);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    Log.v("effect", line);
                    Card c = parseCardInfo(line);
                    if (c != null) {
                        cardNamesList.add(c.getName());
                        cardsAndEffects.put(c.getName(), line);
                        cardslist.add(c);
                    }
                }
            }
    }

    public void emptyCardLists(){
        if (!cardslist.isEmpty()) cardslist.clear();
        if (!cardNamesList.isEmpty()) cardNamesList.clear();
        if (!cardsAndEffects.isEmpty()) cardsAndEffects.clear();
    }

    public static Card parseCardInfo(String cardData) throws MalformedURLException, IOException {
                        Card thisCard = new Card();
                        boolean ST = false;
//        System.out.println(cardData);
        thisCard.setRawInfo(cardData);

//        Log.v("hello", "right here for " + cardData);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //stuff here
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            //stuff here
        }
    }


}

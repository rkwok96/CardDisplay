package com.ebookfrenzy.carddisplay;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by Randy on 7/11/2018.
 */

public class CardDispFragment extends Fragment {

    private Card card;
    private TextView nameView;
    private TextView descView;
    private TextView atkView;
    private TextView defView;
    private TextView cardTypeView;
    private ImageView attribImageView;
    private ImageView levelView;

    ImageButton imageButton;
    ImageView imageView;
    boolean toggledImage = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.card_disp_fragment, container, false);
        if(getArguments() != null){
            card = getArguments().getParcelable(MainActivity.CARD_KEY);
        }

        imageView = (ImageView) view.findViewById(R.id.cardImageView);
        imageButton = (ImageButton) view.findViewById(R.id.openImageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Helper.isNetworkAvailable(getActivity())){
                    if (!toggledImage) {
                        //for orbital 7 (card), replace spaces with underscores

                        Picasso.get().load(("http://yugiohprices.com/api/card_image/" + card.getName().replace(" ", "%20").toLowerCase())).fit().into(imageView);
                        hideStats(view);
                        imageButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        toggledImage = true;
                        } else {
                        imageButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                        imageView.setImageResource(android.R.color.transparent);
                        displayStats(view);
                        toggledImage = false;
                        }
                } else {
                    Toast.makeText(getActivity(), "No internet connection!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        if (card != null){
            initializeViews(view);
        }

        displayStats(view);
        return view;
    }

    private void initializeViews(View v){
        nameView = (TextView) v.findViewById(R.id.nameView);
        descView = (TextView) v.findViewById(R.id.DescView);
        atkView = (TextView) v.findViewById(R.id.AtkView);
        defView = (TextView) v.findViewById(R.id.DefView);
        cardTypeView = (TextView) v.findViewById(R.id.cardTypeView);
        attribImageView = (ImageView) v.findViewById(R.id.attribImage);
        levelView = (ImageView) v.findViewById(R.id.levelView);
    }

    private void hideStats(View v){
        nameView.setText("");
        descView.setText("");
        atkView.setText("");
        defView.setText("");
        cardTypeView.setText("");
        attribImageView.setImageResource(android.R.color.transparent);
        levelView.setImageResource(android.R.color.transparent);
    }

    private void displayStats(View v){
        descView.setSingleLine(false);

        SpannableString name = new SpannableString(card.getName());
        name.setSpan(new UnderlineSpan(), 0, name.length(), 0);
        nameView.setText(name);

        descView.setMovementMethod(new ScrollingMovementMethod());
        descView.setText(card.getText().replace("\\n", System.getProperty("line.separator")).replace("\\", "\"").replace("<br/>", System.getProperty("line.separator")));

        setLevel(v);
        setAttribute(v);
        setCtype(v);
        setAtkDef(v);
        setBG(v);
    }

    private void setLevel(View v){
        if (card.getCard_type().equals("monster")){
            switch(card.getLevel()){
                case 1 :
                    levelView.setImageResource(R.drawable.onestar);
                    break;
                case 2 :
                    levelView.setImageResource(R.drawable.twostar);
                    break;
                case 3 :
                    levelView.setImageResource(R.drawable.threestar);
                    break;
                case 4 :
                    levelView.setImageResource(R.drawable.fourstar);
                    break;
                case 5 :
                    levelView.setImageResource(R.drawable.fivestar);
                    break;
                case 6 :
                    levelView.setImageResource(R.drawable.sixstar);
                    break;
                case 7 :
                    levelView.setImageResource(R.drawable.sevenstar);
                    break;
                case 8 :
                    levelView.setImageResource(R.drawable.eightstar);
                    break;
                case 9 :
                    levelView.setImageResource(R.drawable.ninestar);
                    break;
                case 10 :
                    levelView.setImageResource(R.drawable.tenstar);
                    break;
                case 11 :
                    levelView.setImageResource(R.drawable.elevenstar);
                    break;
                case 12 :
                    levelView.setImageResource(R.drawable.twelvestar);
                    break;
                default:
                    levelView.setImageResource(R.drawable.nostar);
            }
        } else levelView.setImageResource(R.drawable.nostar);
    }

    private void setAttribute(View v){
        if (card.getCard_type().equals("monster")){
            switch(card.getFamily()){
                case "dark" :
                    attribImageView.setImageResource(R.drawable.dark);
                    break;
                case "divine" :
                    attribImageView.setImageResource(R.drawable.divine);
                    break;
                case "earth" :
                    attribImageView.setImageResource(R.drawable.earth);
                    break;
                case "fire" :
                    attribImageView.setImageResource(R.drawable.fire);
                    break;
                case "light" :
                    attribImageView.setImageResource(R.drawable.light);
                    break;
                case "water" :
                    attribImageView.setImageResource(R.drawable.water);
                    break;
                case "wind" :
                    attribImageView.setImageResource(R.drawable.wind);
                    break;
                default:
            }

        } else if (card.getCard_type().equals("spell")) attribImageView.setImageResource(R.drawable.spell);
        else attribImageView.setImageResource(R.drawable.trap);
    }

    private void setCtype(View v){
       if(card.getCard_type().equals("monster")){
           cardTypeView.setText(card.getType().toUpperCase());

       } else if (card.getCard_type().equals("trap")) cardTypeView.setText("TRAP");
        else if (card.getCard_type().equals("spell")) cardTypeView.setText("SPELL");
    }

    private void setAtkDef(View v){
        if (card.getCard_type().equals("monster")) {
            if (!card.getType().contains("Link")) {
                atkView.setText("ATK: " + Integer.toString(card.getAtk()));
                defView.setText("DEF: " + Integer.toString(card.getDef()));
            } else{
                defView.setText("LINK");
                atkView.setText("ATK: " + Integer.toString(card.getAtk()));
            }

        } else {
            atkView.setText("");
            defView.setText("");
        }


    }

    private void setBG(View v){
        if (card.getCard_type().equals("monster") && card.getType().contains("Pendulum")) nameView.setTextColor(Color.parseColor("#23825f"));

        if (card.getCard_type().equals("trap")) v.setBackgroundColor(Color.parseColor("#ea6bd1"));
        else if (card.getCard_type().equals("spell")) v.setBackgroundColor(Color.parseColor("#97e5d9"));
        else if (card.getCard_type().equals("monster") && (card.getType().contains("Effect") || card.getType().contains("Pendulum"))) v.setBackgroundColor(Color.parseColor("#f2b69d"));
        else if (card.getCard_type().equals("monster") && card.getType().contains("Xyz")) v.setBackgroundColor(Color.parseColor("#6b6a6a"));
        else if (card.getCard_type().equals("monster") && card.getType().contains("Synchro")) v.setBackgroundColor(Color.parseColor("#a3a1a1"));
        else if (card.getCard_type().equals("monster") && card.getType().contains("Link")) v.setBackgroundColor(Color.parseColor("#92b8cc"));
        else if (card.getCard_type().equals("monster") && card.getType().contains("Fusion")) v.setBackgroundColor(Color.parseColor("#c36bea"));
        else if (card.getCard_type().equals("monster") && card.getType().contains("Ritual")) v.setBackgroundColor(Color.parseColor("#83e8fc"));

        else v.setBackgroundColor(Color.parseColor("#efe7b8"));
    }


}

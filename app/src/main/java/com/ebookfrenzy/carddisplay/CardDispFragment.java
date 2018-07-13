package com.ebookfrenzy.carddisplay;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Randy on 7/11/2018.
 */

public class CardDispFragment extends Fragment{

    private Card card;
    private TextView nameView;
    private TextView descView;
    private TextView atkView;
    private TextView defView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.card_disp_fragment, container, false);
        if(getArguments() != null){
            card = getArguments().getParcelable(MainActivity.CARD_KEY);
        }

        if (card != null){
            initializeViews(view);
        }

        nameView.setText(card.getName());
        return view;
    }

    private void initializeViews(View view){
        nameView = (TextView) view.findViewById(R.id.nameView);
        descView = (TextView) view.findViewById(R.id.DescView);
        atkView = (TextView) view.findViewById(R.id.AtkView);
        defView = (TextView) view.findViewById(R.id.DefView);
    }

}

package com.ebookfrenzy.carddisplay;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Randy on 7/9/2018.
 */

public class CardAdapter extends BaseAdapter {

    private Context myContext;
    private LayoutInflater myInflater;
    private ArrayList<Card> myData;

    public CardAdapter(Context context, ArrayList<Card> items){
        myContext = context;
        myData = items;
        myInflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount(){
        return myData.size();
    }

    @Override
    public Object getItem(int position){
        return myData.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View rowView = myInflater.inflate(R.layout.list_item_card, parent, false);

        TextView cardTitleView = (TextView) rowView.findViewById(R.id.card_list_title);
        TextView cardDetailView = (TextView) rowView.findViewById(R.id.card_detail);

        Card card = (Card) getItem(position);

        cardTitleView.setText(card.getName());
        cardDetailView.setText("");

        return rowView;
    }

}

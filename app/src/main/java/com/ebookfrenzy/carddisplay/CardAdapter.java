package com.ebookfrenzy.carddisplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Randy on 7/9/2018.
 */

public class CardAdapter extends BaseAdapter implements Filterable {

    private Context myContext;
    private LayoutInflater myInflater;
    private ArrayList<Card> myData = null;
    private ArrayList<Card> filteredData = null;
    private ItemFilter myFilter = new ItemFilter();
    private boolean checkDesc = false;

    public CardAdapter(Context context, ArrayList<Card> items){
        myContext = context;
        myData = items;
        myInflater = LayoutInflater.from(context);

        filteredData = items;

    }

    @Override
    public int getCount(){
        return filteredData == null ? 0 : filteredData.size();
    }

    @Override
    public Object getItem(int position){
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;


        if (convertView == null){
            convertView = myInflater.inflate(R.layout.list_item_card, null);
            holder = new ViewHolder();
            holder.cardTitleView = (TextView) convertView.findViewById(R.id.card_list_title);
            holder.cardDetail = (TextView) convertView.findViewById(R.id.card_detail);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Card card = (Card) getItem(position);
        holder.cardTitleView.setText(card.getName());

        switch(card.getCard_type()){
            case "monster":
                holder.cardDetail.setText("[MNST]");
                break;
            case "spell":
                holder.cardDetail.setText("[SPELL]");
                break;
            case "trap":
                holder.cardDetail.setText("[TRAP]");
                break;
            default:
                holder.cardDetail.setText("");
        }

        return convertView;
    }

    static class ViewHolder{
        TextView cardTitleView;
        TextView cardDetail;
    }

    public Filter getFilter(boolean checked){
        if (checked) checkDesc = true;
        else checkDesc = false;
        return myFilter;
    }

    public Filter getFilter(){
        return myFilter;
    } //the old filter

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            if (filterString.length() > 0 && filterString.charAt(filterString.length()-1) == ' ')filterString = filterString.substring(0, filterString.length()-1);
            FilterResults results = new FilterResults();

            final List<Card> list = myData;
            int count = list.size();
            final ArrayList<Card> nlist = new ArrayList<Card>(count);
            final ArrayList<Card> othercards = new ArrayList<Card>();

            Card filterableCard;

            for (int i = 0; i < count; i++) {
                filterableCard = list.get(i);
                if (filterableCard.getName().toLowerCase().contains(filterString)) {
                    nlist.add(filterableCard);
                } else othercards.add(filterableCard);
            }

            if (checkDesc){
                for (int i = 0; i < othercards.size(); i++){
                    filterableCard = othercards.get(i);
                    if (filterableCard.getText().toLowerCase().contains(filterString)){
                        nlist.add(filterableCard);
                    }
                }
            }

            results.values = nlist;
            results.count = nlist.size();
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results){

            filteredData = (ArrayList<Card>) results.values;
//            Log.v("fucking", "size of filtereddata " + filteredData.size());

            notifyDataSetChanged();
         }
    }

}



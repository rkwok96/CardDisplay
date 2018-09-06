package com.ebookfrenzy.carddisplay;

import java.util.Comparator;

/**
 * Created by Randy on 7/22/2018. Don't think I'll ever use this but whatever.
 */

public class CardComparator implements Comparator<Card> {
    @Override
    public int compare(Card o1, Card o2){
        return o1.getName().compareTo(o2.getName());
    }
}

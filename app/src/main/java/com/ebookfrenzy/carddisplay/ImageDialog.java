package com.ebookfrenzy.carddisplay;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageDialog extends DialogFragment{

    ImageView imageView;
    Card c;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_diag, container, false);

        if(getArguments() != null){
            c = getArguments().getParcelable(MainActivity.CARD_KEY);
        }

        imageView = (ImageView) view.findViewById(R.id.imageViewDiag);
        String thisUrl = "http://yugiohprices.com/api/card_image/" + c.getName().replace(" ", "%20").toLowerCase();

        Picasso.get().load((thisUrl)).into(imageView);


        Log.v("fucking hell", "passed this card into dialog " + thisUrl);
        return view;
    }

    public static void loadImage(Card c){
    }

}

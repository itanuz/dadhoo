/**
 * 
 */
package com.dadhoo.fragments;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dadhoo.R;

/**
 * @author gaecarme
 *
 */
public class DrawerArrayAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] presidents;
    private final Integer[] imageIds;

    public DrawerArrayAdapter(Activity context, String[] linkText, Integer[] iconIds) {
        super(context, R.layout.event_row, linkText);
        this.context = context;
        this.presidents = linkText;
        this.imageIds = iconIds;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        //---print the index of the row to examine---
        Log.d("DrawerArrayAdapter",String.valueOf(position));

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.event_row, null, true);

        //---get a reference to all the views on the xml layout---
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txtPresidentName);
        TextView txtDescription = (TextView) rowView.findViewById(R.id.txtDescription);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        //---customize the content of each row based on position---
        txtTitle.setText(presidents[position]);
        txtDescription.setText(presidents[position] + "...Somedescriptions here...");
        imageView.setImageResource(imageIds[position]);
        return rowView;
    }
}


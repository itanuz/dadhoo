/**
 * 
 */
package com.dadhoo.adapters;

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
    private final String[] sectionTitle;
    private final Integer[] iconIds;

    public DrawerArrayAdapter(Activity context, String[] linkText, Integer[] iconIds) {
        super(context, R.layout.event_item, linkText);
        this.context = context;
        this.sectionTitle = linkText;
        this.iconIds = iconIds;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        //---print the index of the row to examine---
        Log.d("DrawerArrayAdapter",String.valueOf(position));

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.drawer_list_item, null, true);

        //---get a reference to all the views on the xml layout---
        TextView txtTitle = (TextView) rowView.findViewById(R.id.link_drawer_text);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon_drawer);

        //---customize the content of each row based on position---
        txtTitle.setText(sectionTitle[position]);
        imageView.setImageResource(iconIds[position]);
        return rowView;
    }
}


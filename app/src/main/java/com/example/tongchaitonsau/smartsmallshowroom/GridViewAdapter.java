package com.example.tongchaitonsau.smartsmallshowroom;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tongchaitonsau on 9/21/2017 AD.
 */

public class GridViewAdapter extends ArrayAdapter<Product> {
    public GridViewAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if(null == v) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.grid_item, null);
        }
        Product product = getItem(position);
        ImageView img = (ImageView) v.findViewById(R.id.image_a);
        TextView txtTitle = (TextView) v.findViewById(R.id.name_a);
        TextView txtDescription = (TextView) v.findViewById(R.id.price_a);

        img.setImageResource(product.getId());
        txtTitle.setText(product.getName());
        txtDescription.setText(product.getPrice());

        return v;
    }

}

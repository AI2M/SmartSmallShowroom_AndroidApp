package com.example.tongchaitonsau.smartsmallshowroom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by tongchaitonsau on 9/21/2017 AD.
 */

public class GridViewAdapter extends ArrayAdapter<Product> {
    ImageView img;

    public GridViewAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);


    }


    @NonNull
    @Override
    public View getView( int position , View convertView, ViewGroup parent) {
        View v = convertView;
        if(position>8)
        {
            position =0;
        }

        String url ="http://139.59.251.210/musicbox/musicbox"+Integer.toString(position+1)+".jpg";



        if(null == v) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.grid_item, null);


        }



        Product product = getItem(position);
        img = (ImageView) v.findViewById(R.id.image_a);
        TextView txtTitle = (TextView) v.findViewById(R.id.name_a);
        TextView txtDescription = (TextView) v.findViewById(R.id.price_a);
        Picasso.with(getContext()).load(url).resize(100,100).into(img);
        img.setTag(url);

        new LoadimageTask().execute(url);





       // img.setImageResource(product.getId());
        txtTitle.setText(product.getName());
        txtDescription.setText(product.getPrice());




        return v;
    }

    class LoadimageTask extends AsyncTask<String,Void,Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            URL url = null;
            try {
                url = new URL(urls[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                return BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        protected void onPostExecute(Bitmap result){
            img.setImageBitmap(result);
        }


    }

}

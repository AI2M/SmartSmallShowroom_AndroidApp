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
    MainActivity mainactivity;

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
        Product product = getItem(position);

        String url ="http://202.28.24.69/~oasys10/SSS_web/images/music_boxes/music_pic"+product.getMusic_id()+".jpg";



        if(null == v) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.grid_item, null);


        }




        img = (ImageView) v.findViewById(R.id.image_a);
        TextView txtTitle = (TextView) v.findViewById(R.id.name_a);
        TextView txtDescription = (TextView) v.findViewById(R.id.music_id);
        Picasso.with(getContext()).load(url).resize(100,100).into(img);
        img.setTag(url);

        new LoadimageTask().execute(url);





       // img.setImageResource(product.getId());
        txtTitle.setText(product.getName());
        txtDescription.setText("Price : "+product.getPrice());




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

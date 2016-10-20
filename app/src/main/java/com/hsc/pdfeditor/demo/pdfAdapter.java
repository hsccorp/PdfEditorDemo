package com.hsc.pdfeditor.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by hscuser on 14/10/16.
 */
public class pdfAdapter extends ArrayAdapter<String> {

    String TAG = "pdfAdapter";
    Bitmap bmp;
    public pdfAdapter(Context context,List<String> list ) {
        super(context, R.layout.image_list_item,list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        //convertView = new LinearLayout(getContext());
        String inflater = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
        if(convertView == null) {
            convertView = vi.inflate(R.layout.image_list_item, parent, false);
            holder = new ViewHolder();
            holder.Img = (ImageView) convertView.findViewById(R.id.renderedImageView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        Log.d(TAG, "Decoding Image ------>");
        bmp = BitmapFactory.decodeFile(getItem(position));
        Log.d(TAG, "Image Decoded <-------------");
        holder.Img.getLayoutParams().height = height;
        holder.Img.setImageBitmap(bmp);
         return convertView;
    }
    static class ViewHolder {
        private ImageView Img;
    }
}

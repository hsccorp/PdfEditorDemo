package com.hsc.pdfeditor.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by hscuser on 14/10/16.
 */
public class pdfAdapter extends ArrayAdapter<String> {

    String TAG = "pdfAdapter";
    Bitmap bmp;

    Paint paint;
    Bitmap editBitMap;
    float last_x=0f, last_y=0f;

    Matrix matrix = new Matrix();
    Float scale = 1f;
    ScaleGestureDetector SGD;
    //boolean pointerDown=false;
 //   ScaleListener scaleListener;
    private boolean editEnabled =false;

    public pdfAdapter(Context context,List<String> list ) {
        super(context, R.layout.image_list_item,list);
        //paint object to define paint properties
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setTextSize(50);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(6);
//        scaleListener = new ScaleListener();
//        SGD = new ScaleGestureDetector(context, scaleListener);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
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

        //adding touch listener
        editBitMap = bmp.copy(Bitmap.Config.ARGB_8888,true);
        holder.Img.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent motionEvent) {
                // TODO Auto-generated method stub
//gettin x,y cordinates on screen touch
                float scr_x = motionEvent.getRawX();
                float scr_y = motionEvent.getRawY();
                Log.d("pdfAdaptor:onTouch",scr_x+":"+scr_y+":::"+motionEvent.getAction());
//funtion called to perform drawing
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        last_x = scr_x;
                        last_y = scr_y;
                        //pointerDown = true;
                        return true;
                    case MotionEvent.ACTION_UP:
                        //pointerDown = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
//                        if (pointerDown) {
//                            scaleListener.setImageView(holder.Img);
//                            SGD.onTouchEvent(motionEvent);
//                        }
//                        else
                            createImage(scr_x, scr_y,holder.Img);
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
  //                      pointerDown = true;
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
   //                     pointerDown = false;
                        break;
                    case MotionEvent.ACTION_CANCEL:
                           Log.d("pdfAdaptor",": ontouch cancel");
                }
                return true;
            }

        });
         return convertView;
    }
    static class ViewHolder {
        private ImageView Img;
    }

    public void createImage(float scr_x,float scr_y,ImageView currentView){
        //canvas object with bitmap image as constructor
        Canvas canvas = new Canvas(editBitMap);
//fuction to draw text on image. you can try more drawing funtions like oval,point,rect,etc...
        //canvas.drawText(""+user_text, scr_x, scr_y, paint);
        canvas.drawLine(last_x,last_y,scr_x,scr_y,paint);
        currentView.setImageBitmap(editBitMap);
        last_x=scr_x;
        last_y=scr_y;
    }
}

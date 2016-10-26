package com.hsc.pdfeditor.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PdfToImage extends Activity {

    String TAG = "PdfToImage";
    ArrayList<String> pageImageList = new ArrayList<String>();
    pdfAdapter adapter ;
    ZoomListView list;
    ProgressDialog progress;
    int pageLimit = 3;
    FloatingActionButton fab;

    Boolean editModeOn =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_to_image);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editModeOn) {
                    editModeOn = true;
                    fab.setImageResource(R.drawable.save);
                }else{
                    editModeOn =false;
                    fab.setImageResource(R.drawable.file_edit);
                }
                adapter.setEditableMode(editModeOn);
            }
        });
    }
    private void showPdfToImage() {

        new AsyncTask<Void, Void, ArrayList<String>>()
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress = new ProgressDialog(PdfToImage.this);
                progress.setTitle(" PDF Images ");
                progress.setMessage("Loading Images");
                progress.setIndeterminate(false);
                progress.show();
            }

            @Override
            protected ArrayList<String> doInBackground(Void... params) {
                PdfRenderer renderer = null;
                String file_path = getIntent().getStringExtra("filePath");
                try {
                    renderer = new PdfRenderer(ParcelFileDescriptor.open(new File(file_path), ParcelFileDescriptor.MODE_READ_ONLY));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                for(int i=0;i<renderer.getPageCount();i++) {
                    PdfRenderer.Page page = renderer.openPage(i);
                    Log.d(TAG,"starting bitmap ------->");
                    Bitmap pageImage = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                    Log.d(TAG,"Bitmap Created <--------");
                    page.render(pageImage, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                    String img_path = Environment.getExternalStorageDirectory().getAbsoluteFile() + "/render_" + i + ".jpg";
                    File renderFile = new File(img_path);
                    try {
                        renderFile.createNewFile();
                        FileOutputStream fileOut = null;
                        fileOut = new FileOutputStream(renderFile);
                        pageImage.compress(Bitmap.CompressFormat.JPEG, 100, fileOut);
                        pageImageList.add(img_path);
                        fileOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    page.close();
                }
                renderer.close();
                return pageImageList;
            }

            @Override
            protected void onPostExecute(ArrayList<String> pageImageList) {
                super.onPostExecute(pageImageList);
                adapter = new pdfAdapter(PdfToImage.this,pageImageList);
                list = (ZoomListView)findViewById(R.id.img_listview);
                list.setAdapter(adapter);
                progress.dismiss();
            }
        }.execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        showPdfToImage();
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}


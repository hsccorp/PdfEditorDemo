package com.hsc.pdfeditor.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.os.Environment;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends Activity {

    Button imgToPdf, pdfToImg;
    AssetManager assetManager;
    TextView tv;
    private static final int REQUEST_PDF_FILE = 1;
    private static final int REQUEST_IMG_FILE = 2;
    private File selectedFile;
    FilePickerDialog dialog;
    DialogProperties properties;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgToPdf = (Button)findViewById(R.id.img_to_pdf);
        pdfToImg = (Button)findViewById(R.id.pdf_to_img);
        tv = (TextView)findViewById(R.id.pdfStatus);
        imgToPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                properties = new DialogProperties();
                properties.selection_mode= DialogConfigs.SINGLE_MODE;
                properties.selection_type=DialogConfigs.FILE_SELECT;
                properties.root=new File(DialogConfigs.DEFAULT_DIR);
                properties.error_dir=new File(DialogConfigs.DEFAULT_DIR);
                properties.extensions=new String[]{"jpg","png","jpeg","bmp"};
                dialog = new FilePickerDialog(MainActivity.this,properties);
                dialog.setTitle("Select a File");
                dialog.setDialogSelectionListener(new DialogSelectionListener() {
                    @Override
                    public void onSelectedFilePaths(String[] files) {
                        //files is the array of the paths of files selected by the Application User.
                        createPdf(files[0]);

                    }
                });
                dialog.show();
            }
        });

        pdfToImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renderPdf();
            }
        });
    }

    public void renderPdf() {
        try {
            properties = new DialogProperties();
            properties.selection_mode= DialogConfigs.SINGLE_MODE;
            properties.selection_type=DialogConfigs.FILE_SELECT;
            properties.root=new File(DialogConfigs.DEFAULT_DIR);
            properties.error_dir=new File(DialogConfigs.DEFAULT_DIR);
            properties.extensions= new String[]{"pdf"};
            dialog = new FilePickerDialog(MainActivity.this,properties);
            dialog.setTitle("Select a File");
            dialog.setDialogSelectionListener(new DialogSelectionListener() {
                @Override
                public void onSelectedFilePaths(String[] files) {
                    //files is the array of the paths of files selected by the Application User.
                    Intent intent = new Intent(MainActivity.this,PdfToImage.class);
                    intent.putExtra("filePath",files[0]);
                    startActivity(intent);

                }
            });
            dialog.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //Add this method to show Dialog when the required permission has been granted to the app.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(dialog!=null)
                    {   //Show dialog if the read permission has been granted.
                        dialog.show();
                    }
                }
                else {
                    //Permission has not been granted. Notify the user.
                    Toast.makeText(MainActivity.this,"Permission is Required for getting list of files",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public void createPdf(String imageFileName) {
        try {
            InputStream file;

            file = new FileInputStream(imageFileName);
            // create a new document
            PdfDocument document = new PdfDocument();
            Bitmap bmp = BitmapFactory.decodeFile(imageFileName);
            // crate a page description
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bmp.getWidth(), bmp.getHeight(), 1).create();

            // start a page
            PdfDocument.Page page = document.startPage(pageInfo);

            // draw something on the page
            Bitmap content = BitmapFactory.decodeStream(file);
            page.getCanvas().drawBitmap(content, 0, 0, null);

            // finish the page
            document.finishPage(page);

            // add more pages

            // write the document content
            File out = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/created.pdf");
            document.writeTo(new FileOutputStream(out));
            tv.setText("Successfully wrote PDF to " + out);

            // close the document
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

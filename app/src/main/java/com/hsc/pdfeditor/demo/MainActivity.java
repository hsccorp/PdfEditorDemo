package com.hsc.pdfeditor.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends Activity {//AppCompatActivity {

    Button imgToPdf, pdfToImg;
    AssetManager assetManager;
    TextView tv;
    private static final int REQUEST_PDF_FILE = 1;
    private static final int REQUEST_IMG_FILE = 2;
    private File selectedFile;

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
                Intent filepicker_intent = new Intent(MainActivity.this, FilePicker.class);
                ArrayList<String> extentions = new ArrayList<String>();
                extentions.add("jpg");
                extentions.add("png");
                extentions.add("jpeg");
                extentions.add("bmp");
                filepicker_intent.putStringArrayListExtra(FilePicker.EXTRA_ACCEPTED_FILE_EXTENSIONS,extentions);
                startActivityForResult(filepicker_intent, REQUEST_IMG_FILE);
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
            Intent filepicker_intent = new Intent(this, FilePicker.class);
            ArrayList<String> extentions = new ArrayList<String>();
            extentions.add("pdf");
            filepicker_intent.putStringArrayListExtra(FilePicker.EXTRA_ACCEPTED_FILE_EXTENSIONS, extentions);
            startActivityForResult(filepicker_intent, REQUEST_PDF_FILE);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {

            switch(requestCode) {

                case REQUEST_PDF_FILE:

                    if(data.hasExtra(FilePicker.EXTRA_FILE_PATH)) {

//                        selectedFile = new File
//                                (data.getStringExtra(FilePicker.EXTRA_FILE_PATH));
                        Intent intent = new Intent(this,PdfToImage.class);
                        intent.putExtra("filePath",data.getStringExtra(FilePicker.EXTRA_FILE_PATH));
                        startActivity(intent);
                        //filePath.setText(selectedFile.getPath());
                    }
                    break;
                case REQUEST_IMG_FILE:
                    if(data.hasExtra(FilePicker.EXTRA_FILE_PATH)) {

//                        selectedFile = new File
//                                (data.getStringExtra(FilePicker.EXTRA_FILE_PATH));
                        createPdf(data.getStringExtra(FilePicker.EXTRA_FILE_PATH));
                        //filePath.setText(selectedFile.getPath());
                    }
                    break;
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
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bmp.getWidth(),bmp.getHeight(),1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // draw something on the page
        Bitmap content = BitmapFactory.decodeStream(file);
        page.getCanvas().drawBitmap(content,0,0,null);

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


//////////////////////////////////////



       /* PdfDocument document = new PdfDocument();
        PdfDocument.Page page = new PdfDocument.Page();
        document.addPage(page);

        // Create a new font object selecting one of the PDF base fonts
        PDFont font = PDType1Font.HELVETICA;
        // Or a custom font
//		try {
//			PDType0Font font = PDType0Font.load(document, assetManager.open("MyFontFile.TTF"));
//		} catch(IOException e) {
//			e.printStackTrace();
//		}

        PDPageContentStream contentStream;

        try {
            // Define a content stream for adding to the PDF
            contentStream = new PDPageContentStream(document, page);

            // Write Hello World in blue text
            contentStream.beginText();
            contentStream.setNonStrokingColor(15, 38, 192);
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Hello World");
            contentStream.endText();

            // Load in the images
            InputStream in = assetManager.open("falcon.jpg");
            //InputStream alpha = assetManager.open("trans.png");

            // Draw a green rectangle
            contentStream.addRect(5, 500, 100, 100);
            contentStream.setNonStrokingColor(0, 255, 125);
            contentStream.fill();

            // Draw the falcon base image
            PDImageXObject ximage = JPEGFactory.createFromStream(document, in);
            contentStream.drawImage(ximage, 20, 20);

            // Draw the red overlay image
            //Bitmap alphaImage = BitmapFactory.decodeStream(alpha);
            //PDImageXObject alphaXimage = LosslessFactory.createFromImage(document, alphaImage);
            //contentStream.drawImage(alphaXimage, 20, 20 );

            // Make sure that the content stream is closed:
            contentStream.close();

            // Save the final pdf document to a file
            String path = root.getAbsolutePath() + "/Download/Created.pdf";
            document.save(path);
            document.close();
            tv.setText("Successfully wrote PDF to " + path);

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

}

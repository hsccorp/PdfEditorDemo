				PdfEditorDemo

The project 'PdfEditorDemo' aims to display pdf from file chooser and allows user to put marks on the pdf. All the edited pages should be added and entire pdf should be regenerated from all edited pages of pdf.While saving final edited version of PDF user should be able to provide the directory location where they want to save the edited pdf and should be able to provide the name.

Tasks Done
1.User is able to browse and select a pdf file from internal memory and sdcard.
2.While browsing, file type(pdf,jpeg) is specified and only specified type of file appears on the list.
3.Selected pdf is displayed single page full screen at time starting from first page of pdf file. 
4.On scrolling up/down the next/ previous page is displayed. Here conversion from pdf to image is taking place using PdfRenderer.
5.On Image to pdf button click, single image is converted to pdf and stored at the specified location. This is done using pdfDocument.
6.Added .gitignore file so that unwanted files could not be tracked by git.
7.Set minifyEnabled to true so that proguard rules can be applied.
8.Added Product flavors such that application id is made free.
9.Added proguard rule to Diable logs from release build,enable obfuscation and further optimisations.
10.Release build made by signing apk.
11.Implemented File Picker Library into existing code. Taken code from github.(https://github.com/Angads25/android-filepicker)

Tasks Remaining
1.Caching of bitmaps
2.Placing all images to pdf.
3.UI changes in added library (File Picker)
4.UI changes in application (Material Design)

Challenges faced while combining all images to single pdf : Only the last image is saved in the pdf i.e the pdf so obtained is of single page. It should contain all the pages.

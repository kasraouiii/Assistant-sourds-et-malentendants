package com.example.first.myfirstapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.first.myfirstapplication.SoundRecorder.JavaSoundRecorder;
import com.example.first.myfirstapplication.marf.SpeakerIdentApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;


public class newSpeaker extends AppCompatActivity{

    ImageView imageView;
    EditText firstname,lastname;
    TextView textView,title;
    Button confirm,btn_choose;
    ImageButton micro;
    Intent mIntent;
    String previousActivity;
    String log1,log2,log3,log4,log5,log6,log7,log10;
    final int REQUEST_CODE_GALLERY = 1;
    final int REQUEST_CODE_CAMERA = 2;
    public static DatabaseAmis myDB;
    static int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newspeak);
        final String working_path = getExternalFilesDir("training").getPath();
        final String path = getExternalFilesDir("").getPath() + File.separator + "speakers.txt";
        Dexter.withActivity(this).withPermissions(permissions)
                .withListener(new MultiplePermissionsListener() {
                                  @Override
                                  public void onPermissionsChecked(MultiplePermissionsReport report) {
                                      if (report.areAllPermissionsGranted()) {

                                      } else {
                                          Toast.makeText(getApplication(), "Permissions denied", Toast.LENGTH_LONG).show();
                                          finish();
                                          //onDestroy();
                                      }
                                  }

                                  @Override
                                  public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                                  }
                              }

                ).onSameThread().check();

        SpeakerIdentApp.path = getExternalFilesDir("").getPath();

        //Prepare the files used by MARF
        File file = new File(getExternalFilesDir("").getPath() + File.separator + "speakers.txt");
        if (!file.exists()) {
            copyToFile(file, R.raw.speakers);
        }
        file = new File(getExternalFilesDir("").getPath() + File.separator + "marf.Storage.TrainingSet.107.308.532.gzbin");
        if (!file.exists()) {
            copyToFile(file, R.raw.ds);
        }
        imageView=findViewById(R.id.imageView);
        firstname=findViewById(R.id.firstName);
        lastname=findViewById(R.id.lastName);
        textView=findViewById(R.id.amis_Name);
        confirm= findViewById(R.id.confirm);
        btn_choose= findViewById(R.id.btn_choose);
        micro= findViewById(R.id.microButton);
        TextView merci=findViewById(R.id.merci);
        TextView phrase=findViewById(R.id.phrase);
        title=findViewById(R.id.textView5);
        Button take=(Button) findViewById(R.id.takePic);
        myDB = new DatabaseAmis(this, "AmisDB.sqlite", null, 1);
        myDB.queryData("CREATE TABLE IF NOT EXISTS Last(Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR,image BLOB)");

        Cursor test= myDB.getData();
        count= test.getCount();
        confirm.setEnabled(false);
        log1="Data not sent, please fill in all fields";
        log2="Data sent correctly";
        log3="Recording";
        log4="Please talk while recording \n 00:10";
        log5="Please talk while recording \n 00:0";
        log6="Training ..";
        log7="Are you sure ?";
        log10="Cancel";
        mIntent = getIntent();
        previousActivity = mIntent.getStringExtra("FROM_ACTIVITY");
        if (previousActivity.equals("Menu_FR")) {
            title.setText("Ajouter un nouveau locuteur");
            firstname.setHint("Prenom");
            lastname.setHint("Nom");
            btn_choose.setText("Choisir une photo");
            take.setText("Prendre une photo");
            merci.setText("Cliquez sur le microphone et lisez la phrase suivante:");
            phrase.setText("L'excellence est un art gagné par l'entraînement et l'accoutumance. Nous n'agissons  pas correctement parce que nous avons la vertu ou l'excellence, mais nous préférons les avoir par ce que nous avons agi correctement.");
            confirm.setText("Confirmer");
            log1="Données non envoyées, veuillez remplir tous les champs ";
            log2="Données envoyées corréctement";
            log3="Enregistrement";
            log4="S'il vous plaît parler pendant l'enregistrement \n 00:10";
            log5="S'il vous plaît parler pendant l'enregistrement \n 00:0";
            log6="Entrainement ..";
            log7="Vous-êtes sur ?";
            log10 = "Annuler";



        }




        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(newSpeaker.this);

                builder.setMessage(log7).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(firstname.getText().toString().equals("")||lastname.getText().toString().equals("")){
                            Toast.makeText(newSpeaker.this,log1,Toast.LENGTH_LONG).show();
                        }
                        else{
                            try{
                                String name=firstname.getText().toString()+" "+lastname.getText().toString();
                                myDB.insertData(name,imageViewToByte(imageView));
                                firstname.setText("");
                                lastname.setText("");
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        Toast.makeText(newSpeaker.this,log2,Toast.LENGTH_LONG).show(); }                   }
                }).setNegativeButton(log10,null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        newSpeaker.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });
        micro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String file_name = new SimpleDateFormat("yyyyMMddHHmmss'.wav'").format(new Date());
                final String[] MARF_ARGS = {"--single-train", "-aggr" ,"-raw", "-eucl", working_path + File.separator + file_name};
                final JavaSoundRecorder recorder = new JavaSoundRecorder();

                recorder.setFilepath(working_path);
                recorder.setRecordNameFile(file_name);
                //Set the length of the recognizing sample in milliseconds
                recorder.setRECORDDING_TIME(9000);
                recorder.startRecord();
                Log.i("Button 1", "start");
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(newSpeaker.this);
                alertDialogBuilder.setTitle(log3);
                alertDialogBuilder.setMessage(log4);
                alertDialogBuilder.setPositiveButton(log10,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                recorder.stopRecord();
                                arg0.dismiss();
                            }
                        });
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                new CountDownTimer(10000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (alertDialog.isShowing())
                            if (millisUntilFinished / 1000 > 1)
                                alertDialog.setMessage(log5 + (millisUntilFinished / 1000));
                            else
                                alertDialog.setMessage(log6);
                        else
                            cancel();
                    }

                    @Override
                    public void onFinish() {
                        AddText addText = new AddText(path, 19, 10+count+1, file_name);
                        SpeakerIdentApp.main(MARF_ARGS);
                        alertDialog.dismiss();
                    }
                }.start();
                confirm.setEnabled(true);
            }
        });
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(e,REQUEST_CODE_CAMERA);
            }
        });
    }
    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void copyToFile(File file, int id) {
        try {
            InputStream inputStream = getResources().openRawResource(id);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                fileOutputStream.write(buf, 0, len);
            }

            fileOutputStream.close();
            inputStream.close();
        } catch (IOException ignored) {
        }

    }
    private final String[] permissions = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        else if(requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null){
            Bitmap bitmap=(Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

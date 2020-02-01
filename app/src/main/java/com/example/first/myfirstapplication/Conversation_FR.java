package com.example.first.myfirstapplication;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.first.myfirstapplication.Adapter.CustomAdapter;
import com.example.first.myfirstapplication.Models.AmisModel;
import com.example.first.myfirstapplication.Models.ChatModel;
import com.example.first.myfirstapplication.SoundRecorder.JavaSoundRecorder;
import com.example.first.myfirstapplication.marf.SpeakerIdentApp;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Conversation_FR extends Menu_FR {

    ListView listView;
    EditText editText;
    List<ChatModel> list_chat = new ArrayList<>();
    FloatingActionButton btn_send_message;
    ImageButton micro;
    String txt;
    EditText et;
    TextToSpeech tts;
    TextView info;
    DatabaseHelper myDB;
    DatabaseAmis amisDB;
    ImageView imageView;
    CustomAdapter adapter;
    byte [] image;
    String nom;
    String log1,log2,log3,log4,log5,log6,log7,log8,log9,log10,log12,log13;
    Locale loc;
    static int id,count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        count=0;
        final String working_path = getExternalFilesDir("recognizing").getPath();
        final String[] MARF_ARGS = {"--ident","-aggr" ,"-raw", "-eucl", working_path + File.separator + "recWav.wav"};
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
        listView = (ListView)findViewById(R.id.list_of_message);
        editText = (EditText)findViewById(R.id.user_message);
        btn_send_message = (FloatingActionButton)findViewById(R.id.fab);
        micro =(ImageButton) findViewById(R.id.micro);
        btn_send_message.setEnabled(false);
       // info=(TextView)findViewById(R.id.infos);
       // Button settings=(Button) findViewById(R.id.action_settings);
       // settings.setText("Menu principal");
        image=new byte[20];
        nom="Moi";
        myDB = new DatabaseHelper(this);
        Cursor data = myDB.getListContents();
        loc=Locale.FRENCH;
        log1="La conversation est encore vide! \n Commencer votre conversation maintenant :)";
        log2="Dites-ce que vous voulez !";
        log3="Reconnaissance du locuteur";
        log4="Veuillez parler quelques secondes pour s'identifier \n 00:10";
        log5="Veuillez parler quelques secondes pour s'identifier\n 00:0";
        log6="Resultat final ..";
        log7="Attention!";
        log8="Etes-vous sûr de vouloir supprimer ceci?";
        log9="Supprimer avec succès !!";
        log10="Annuler";
        log12="Supprimer";
        log13="Choisir une action";
        Intent mIntent = getIntent();
        String previousActivity = mIntent.getStringExtra("FROM_ACTIVITY");
        if(previousActivity.equals("Menu_EN")) {
            loc = Locale.US;
            log1 = "The conversation is still empty! Start your conversation now :)";
            log2 = "Say something !";
            log3 = "Speaker recognition";
            log4 = "Please talk a few seconds to identify \n 00:10";
            log5 = "Please talk a few seconds to identify \n 00:0";
            log6 = "Final result ..";
            log7 = "Warning !";
            log8 = "Are you sure you want to this delete?";
            log9 = "Deleted successfully !!";
            log10 = "Cancel";
            log12 ="Delete";
            log13 ="Choose action";
            editText.setHint("Enter your message");
        }




        if(data.getCount() == 0){
            Toast.makeText(this,log1,Toast.LENGTH_LONG).show();
        }else{
            while(data.moveToNext()){
                String message=data.getString(1);
                String cote=data.getString(2);
                String nom=data.getString(3);
                image = data.getBlob(4);
                boolean bol=false;
                if(cote.equals("send"))
                    bol=true;
                else
                    bol=false;
                ChatModel model=new ChatModel(message,bol,nom,image);
                list_chat.add(model);
                adapter = new CustomAdapter(list_chat,getApplicationContext());
                listView.setAdapter(adapter);
            }
        }

        et=(EditText)findViewById(R.id.user_message);
        tts=new TextToSpeech(Conversation_FR.this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                // TODO Auto-generated method stub
                if(status == TextToSpeech.SUCCESS){
                    int result=tts.setLanguage(loc);
                    if(result==TextToSpeech.LANG_MISSING_DATA ||
                            result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Log.e("error", "This Language is not supported");
                    }
                    else{
                        ConvertTextToSpeech();
                    }
                }
                else
                    Log.e("error", "Initilization Failed!");
            }
        });


        btn_send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                amisDB = new DatabaseAmis(Conversation_FR.this, "AmisDB.sqlite", null, 1);
                Cursor cursor = amisDB.getInfo(1);
                while (cursor.moveToNext()) {
                    nom = cursor.getString(1);
                    image = cursor.getBlob(2);
                }
                AddData(text,"send",nom,image);
                ChatModel model = new ChatModel(text,true,nom,image); // user send message
                list_chat.add(model);
                adapter = new CustomAdapter(list_chat,getApplicationContext());
                listView.setAdapter(adapter);
                ConvertTextToSpeech();

                //remove user message
                editText.setText("");
                btn_send_message.setEnabled(false);
            }
        });
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    btn_send_message.setEnabled(false);
                } else {
                    btn_send_message.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                btn_send_message.setEnabled(true);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                btn_send_message.setEnabled(false);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence[] items = {log12};
                android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(Conversation_FR.this);

                dialog.setTitle(log13);
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        // delete
                        Cursor c = myDB.getListContents();
                        ArrayList<Integer> arrID = new ArrayList<Integer>();
                        while (c.moveToNext()){
                            arrID.add(c.getInt(0));
                        }
                        showDialogDelete(arrID.get(position));


                    }
                });
                dialog.show();
                return true;
            }
        });
        micro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    final JavaSoundRecorder recorder = new JavaSoundRecorder();
                    recorder.setFilepath(working_path);
                    recorder.setRecordNameFile("recWav.wav");
                    //Set the length of the recognizing sample in milliseconds
                    recorder.setRECORDDING_TIME(9000);
                    recorder.startRecord();
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Conversation_FR.this);
                    alertDialogBuilder.setTitle(log3);
                    alertDialogBuilder.setMessage(log4);
                    alertDialogBuilder.setPositiveButton(log10,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    // recorder.stopRecord();
                                    arg0.dismiss();
                                }
                            });
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    new CountDownTimer(10000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (alertDialog.isShowing())
                                if (millisUntilFinished / 1000 != 1)
                                    alertDialog.setMessage(log5 + (millisUntilFinished / 1000));
                                else
                                    alertDialog.setMessage(log6);
                            else
                                cancel();
                        }

                        @Override
                        public void onFinish() {
                            SpeakerIdentApp.main(MARF_ARGS);
                            alertDialog.dismiss();
                            String pers = Variables.name.substring(0, Variables.name.length() - 1);
                            id = 1;
                            if (pers.equals("Person 1")) id = 1;
                            else if (pers.equals("Person 2")) id = 2;
                            else if (pers.equals("Person 3")) id = 3;
                            else if (pers.equals("Person 4")) id = 4;
                            else if (pers.equals("Person 5")) id = 5;
                            else if (pers.equals("Person 6")) id = 6;
                            else if (pers.equals("Person 7")) id = 7;
                            else if (pers.equals("Person 8")) id = 8;


                            //result.setText(Variables.name.substring(0, Variables.name.length() -1));
                        }
                    }.start();
                    count++;
                }

                if (count == 1) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            promptSpeechInput();
                        }
                    }, 10000);
                    count++;
                }
                else{
                    promptSpeechInput();
            }
            }
        });
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
    private void showDialogDelete(final int idAmis){
        final android.support.v7.app.AlertDialog.Builder dialogDelete = new android.support.v7.app.AlertDialog.Builder(Conversation_FR.this);

        dialogDelete.setTitle(log7);
        dialogDelete.setMessage(log8);
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    myDB.deleteData(idAmis);
                    Toast.makeText(getApplicationContext(), log9,Toast.LENGTH_SHORT).show();
                    //   adapter.notifyDataSetChanged();
                } catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateAmisList();
            }
        });

        dialogDelete.setNegativeButton(log10, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }
    private void updateAmisList(){
        // get all data from sqlite
        Cursor cursor = myDB.getListContents();
        list_chat.clear();
        while (cursor.moveToNext()) {
            String message=cursor.getString(1);
            String cote=cursor.getString(2);
            String nom=cursor.getString(3);
            image = cursor.getBlob(4);
            boolean bol=false;
            if(cote.equals("send"))
                bol=true;
            else
                bol=false;
            ChatModel model=new ChatModel(message,bol,nom,image);
            list_chat.add(model);
        }
        adapter.notifyDataSetChanged();
    }

    public void promptSpeechInput(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE , loc);
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,log2);

        try {
            startActivityForResult(i, 100);
        }
        catch (ActivityNotFoundException a){
            Toast.makeText(Conversation_FR.this,"Désolé votre tele ne supporte pas le speech",Toast.LENGTH_LONG).show();
        }


    }

    private void ConvertTextToSpeech() {
        // TODO Auto-generated method stub
        txt = et.getText().toString();
            tts.speak(txt, TextToSpeech.QUEUE_FLUSH, null);
    }
    public void onActivityResult(int request_code, int result_code, Intent i){

        switch (request_code){
            case 100: if(result_code== RESULT_OK && i!=null){


                ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                amisDB = new DatabaseAmis(Conversation_FR.this, "AmisDB.sqlite", null, 1);      Cursor curs=amisDB.getData();
                int tot=curs.getCount();
                Cursor cursor;
                if(id>0 && id<=tot) {
                    cursor = amisDB.getInfo(id);
                    while (cursor.moveToNext()) {
                        nom = cursor.getString(1);
                        image = cursor.getBlob(2);
                        ChatModel model = new ChatModel(result.get(0), false, nom, image);
                        list_chat.add(model);
                        CustomAdapter adapter = new CustomAdapter(list_chat, getApplicationContext());
                        listView.setAdapter(adapter);
                        AddData(result.get(0), "recv", nom, image);
                    }
                }
                else{
                    Intent x = new Intent(Conversation_FR.this, newSpeaker.class);
                    x.putExtra("FROM_ACTIVITY", "Conversation_FR");
                    startActivity(x);
                }

            }
                break;
        }
    }
    private final String[] permissions = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public void AddData(String firstEntry,String secondEntry,String third,byte[] fourth) {

         myDB.addData(firstEntry,secondEntry,third,fourth);

    }
   /* public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(Conversation_FR.this, MainActivity.class);
            i.putExtra("FROM_ACTIVITY", "Conversation_FR");
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

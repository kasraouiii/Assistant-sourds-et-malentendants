package com.example.first.myfirstapplication;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.first.myfirstapplication.Adapter.AmisAdapter;
import com.example.first.myfirstapplication.Models.AmisModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class Amis extends MainActivity {

    ListView gridView;
    ArrayList<AmisModel> list;
    AmisAdapter adapter = null;
    final int REQUEST_CODE_GALLERY = 999;
    ImageView ImageView;
    DatabaseAmis myDB;
    String log1,log2,log3,log10,log4,log5;
    public static DatabaseAmis sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amis);
        TextView txt = (TextView) findViewById(R.id.mes_amis);
        Intent mIntent = getIntent();
        String previousActivity = mIntent.getStringExtra("FROM_ACTIVITY");
        log1="Attention !!";
        log2="Supprimer";
        log3="Choisir une action";
        log4="Supprimer avec succés !!!";
        log5="Etes-vous sûr de vouloir supprimer ceci?";
        log10="Annuler";
        if (previousActivity.equals("Menu_EN")) {
            txt.setText("My friends");
            log1="Warning!!";
            log2="Delete";
            log3="Choose action";
            log4="Delete successfully!!!";
            log5="Are you sure you want to this delete?";
            log10="Cancel";
        }
        gridView = (ListView) findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new AmisAdapter(this, R.layout.amis_item, list);
        gridView.setAdapter(adapter);
        myDB = new DatabaseAmis(this, "AmisDB.sqlite", null, 1);



        // get all data from sqlite
        Cursor cursor = myDB.getData();
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            byte[] image = cursor.getBlob(2);
            list.add(new AmisModel(id, name,image));
        }
        adapter.notifyDataSetChanged();


            gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                    CharSequence[] items = {log2};
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Amis.this);

                    dialog.setTitle(log3);
                    dialog.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {

                                // delete
                                Cursor c = myDB.getData();
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
        }



        private void showDialogDelete(final int idAmis){
            final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(Amis.this);

            dialogDelete.setTitle(log1);
            dialogDelete.setMessage(log5);
            dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        myDB.deleteData(idAmis);
                        Toast.makeText(getApplicationContext(), log4,Toast.LENGTH_SHORT).show();
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
        Cursor cursor = myDB.getData();
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            byte[] image = cursor.getBlob(2);

            list.add(new AmisModel(id, name,image));
        }
        adapter.notifyDataSetChanged();
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

        if(requestCode == 888){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 888 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ImageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}

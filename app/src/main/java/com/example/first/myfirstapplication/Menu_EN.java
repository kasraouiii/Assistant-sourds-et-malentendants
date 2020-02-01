package com.example.first.myfirstapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Menu_EN extends MainActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_fr);
        Button b1=(Button) findViewById(R.id.nv_loc);
        Button b2=(Button) findViewById(R.id.amis);
        Button b3=(Button) findViewById(R.id.conv);

        b1.setText("Add new speaker");
        b2.setText("My friends");
        b3.setText("My conversation");


    }
    public void onButtonClick(View v) {
        if(v.getId()==R.id.conv) {
            Intent i = new Intent(Menu_EN.this, Conversation_FR.class);
            i.putExtra("FROM_ACTIVITY", "Menu_EN");
            startActivity(i);
        }
        else if(v.getId()==R.id.amis){
            Intent mIntent = new Intent(Menu_EN.this, Amis.class); //'this' is Activity A
            mIntent.putExtra("FROM_ACTIVITY", "Menu_EN");
            startActivity(mIntent);
        }
        else if(v.getId()==R.id.nv_loc){
            Intent mIntent = new Intent(Menu_EN.this, newSpeaker.class); //'this' is Activity A
            mIntent.putExtra("FROM_ACTIVITY", "Menu_EN");
            startActivity(mIntent);
        }
    }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.example.first.myfirstapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Menu_FR extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_fr);
        TextView txt=findViewById(R.id.textView);

        txt.setText("Bienvenue dans l'application des sourds et malentendants");
    }
    public void onButtonClick(View v) {
        if(v.getId()==R.id.conv) {
            Intent i = new Intent(Menu_FR.this, Conversation_FR.class);
            i.putExtra("FROM_ACTIVITY", "Menu_FR");
            startActivity(i);
        }
        else if(v.getId()==R.id.amis){
            Intent mIntent = new Intent(Menu_FR.this, Amis.class); //'this' is Activity A
            mIntent.putExtra("FROM_ACTIVITY", "Menu_FR");
            startActivity(mIntent);
        }
        else if(v.getId()==R.id.nv_loc){
            Intent mIntent = new Intent(Menu_FR.this, newSpeaker.class); //'this' is Activity A
            mIntent.putExtra("FROM_ACTIVITY", "Menu_FR");
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

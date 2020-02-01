package com.example.first.myfirstapplication.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.first.myfirstapplication.Models.ChatModel;
import com.example.first.myfirstapplication.R;
import com.github.library.bubbleview.BubbleTextView;

import java.util.List;


/**
 * Created by reale on 2/28/2017.
 */
public class CustomAdapter extends BaseAdapter {

    private List<ChatModel> list_chat_models;
    private Context context;
    private LayoutInflater layoutInflater;


    public CustomAdapter(List<ChatModel> list_chat_models, Context context) {

        this.list_chat_models = list_chat_models;
        this.context = context;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list_chat_models.size();
    }

    @Override
    public Object getItem(int position) {
        return list_chat_models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            if (list_chat_models.get(position).isSend) {
                view = layoutInflater.inflate(R.layout.item_msg_send, null);
            }
            else if(!list_chat_models.get(position).isSend) {
                view = layoutInflater.inflate(R.layout.item_msg_recv, null);
            }
        }
            BubbleTextView text_message = (BubbleTextView)view.findViewById(R.id.text_message);
            text_message.setText(list_chat_models.get(position).message);
            TextView moi=(TextView)view.findViewById(R.id.moi);
            moi.setText(list_chat_models.get(position).nom);
            ImageView my_img=(ImageView) view.findViewById(R.id.mon_image);
            byte[] amiImage = list_chat_models.get(position).getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(amiImage, 0, amiImage.length);
            //my_img.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 40, 40, false));
        return view;
    }
}
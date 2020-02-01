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

import com.example.first.myfirstapplication.Models.AmisModel;
import com.example.first.myfirstapplication.R;

import java.util.ArrayList;

public class AmisAdapter extends BaseAdapter {
    private Context context;
    private  int layout;
    private ArrayList<AmisModel> AmisList;

    public AmisAdapter(Context context,int layout,ArrayList<AmisModel> AmisList){
        this.AmisList=AmisList;
        this.layout=layout;
        this.context=context;
    }

    @Override
    public int getCount() {
        return AmisList.size();
    }

    @Override
    public Object getItem(int position) {
        return AmisList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private class ViewHolder{
        ImageView imageView;
        TextView txtName;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtName = (TextView) row.findViewById(R.id.amis_Name);
            holder.imageView = (ImageView) row.findViewById(R.id.imgAmi);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        AmisModel ami = AmisList.get(position);
        holder.txtName.setText(ami.getName());
        byte[] amiImage = ami.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(amiImage, 0, amiImage.length);
        holder.imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 120, 120, false));

        return row;

    }
}

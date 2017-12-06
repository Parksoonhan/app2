package com.example.hywoman.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by TonyChoi on 2016. 3. 29..
 */
public class CustomAdapter  extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<InfoClass> InfoArr;
    private ViewHolder holder;

    public CustomAdapter(Context c, ArrayList<InfoClass> array) {
        mInflater = LayoutInflater.from(c);
        InfoArr = array;
    }
    @Override
    public int getCount() {
        return InfoArr.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            holder = new ViewHolder();
            v = mInflater.inflate(R.layout.listview_item, null);
            holder.name = (TextView)v.findViewById(R.id.tvName);
            holder.contact = (TextView)v.findViewById(R.id.tvContact);
            holder.email = (TextView)v.findViewById(R.id.tvEmail);

            v.setTag(holder);
        } else {
            holder = (ViewHolder)v.getTag();
        }

        //InfoClass를 생성하여 각 뷰의 포지션에 맞는 데이터를 가져옴
        InfoClass info = InfoArr.get(position);

        //리스트뷰의 아이템에 맞는 String값을 입력
        holder.name.setText(info.name);
        holder.contact.setText(info.contact);
        holder.email.setText(info.email);

        return v;
    }

    //ArrayList Getter And Setter
    public void setArrayList(ArrayList<InfoClass> arrays) {
        this.InfoArr = arrays;
    }

    public ArrayList<InfoClass> getArrayList(){
        return InfoArr;
    }


    /**
     * ViewHolder Class 생성
     */
    private class ViewHolder {
        TextView name;
        TextView contact;
        TextView email;
    }
}



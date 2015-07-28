package com.example.win7cmurder.boxer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by win7Cmurder on 7/26/2015.
 */
public class ListCarsAdapter extends BaseAdapter {

    Context context;

    protected List<Car> listCars;
    LayoutInflater inflater;

    public ListCarsAdapter(Context context, List<Car> listCars) {
        this.listCars = listCars;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public int getCount() {
        return listCars.size();
    }

    public Car getItem(int position) {
        return listCars.get(position);
    }

    public long getItemId(int position) {
        return listCars.get(position).getDrawableId();
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Car car = listCars.get(position);
        if (convertView == null) {
            holder = new ViewHolder();

            if(car.getSide().equals("left")){
                convertView = this.inflater.inflate(R.layout.layout_list_item2,
                        parent, false);
            }
            if (car.getSide().equals("right")){
                convertView = this.inflater.inflate(R.layout.layout_list_item,
                        parent, false);
            }

            holder.sender = (TextView) convertView
                    .findViewById(R.id.sender);
            holder.message = (TextView) convertView
                    .findViewById(R.id.message);
            holder.time = (TextView) convertView
                    .findViewById(R.id.time);
            holder.imgCar = (ImageView) convertView.findViewById(R.id.img_car);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.sender.setText(car.getSender());
        holder.message.setText(car.getMessage());
        holder.time.setText(car.getTime());
        holder.imgCar.setImageResource(car.getDrawableId());



        return convertView;
    }

    private class ViewHolder {
        TextView sender;
        TextView message;
        TextView time;
        ImageView imgCar;
    }

}

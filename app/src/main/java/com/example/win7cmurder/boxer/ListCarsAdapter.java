package com.example.win7cmurder.boxer;

import android.content.Context;
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
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.layout_list_item,
                    parent, false);

            holder.txtModel = (TextView) convertView
                    .findViewById(R.id.txt_car_model);
            holder.txtColor = (TextView) convertView
                    .findViewById(R.id.txt_car_color);
            holder.txtPrice = (TextView) convertView
                    .findViewById(R.id.txt_car_price);
            holder.imgCar = (ImageView) convertView.findViewById(R.id.img_car);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Car car = listCars.get(position);
        holder.txtModel.setText(car.getModel());
        holder.txtColor.setText(car.getColor());
        holder.txtPrice.setText(car.getPrice() + " €");
        holder.imgCar.setImageResource(car.getDrawableId());

        return convertView;
    }

    private class ViewHolder {
        TextView txtModel;
        TextView txtColor;
        TextView txtPrice;
        ImageView imgCar;
    }

}

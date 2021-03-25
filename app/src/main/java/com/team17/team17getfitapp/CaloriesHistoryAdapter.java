package com.team17.team17getfitapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CalorieHistoryAdapter extends BaseAdapter {
    public static List<CaloriesModel> historyCaloriesModelList;
    long value = 0;
    LayoutInflater inflater;
    long values = 0;
    int count = 0;
    private long calorieResult;

    public CalorieHistoryAdapter(@NonNull Context context, List<CaloriesModel> modelList) {
        this.inflater = LayoutInflater.from(context);
        historyCaloriesModelList = modelList;
    }


    @Override
    public int getCount() {
        return historyCaloriesModelList.size();
    }

    @Override
    public CaloriesModel getItem(int i) {
        return historyCaloriesModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.layout_item_history, viewGroup, false);
            holder.textViewDate = (TextView) convertView.findViewById(R.id.txt_view_his_item);
            holder.textViewCalorieValue = (TextView) convertView.findViewById(R.id.txt_view_his_calorie);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CaloriesModel model = historyCaloriesModelList.get(position);
        holder.textViewDate.setText(model.getItemName());
        holder.textViewCalorieValue.setText(String.valueOf(model.getCalorieValue()));

        return convertView;
    }

    private static class ViewHolder {
        TextView textViewDate;
        TextView textViewCalorieValue;
    }

}
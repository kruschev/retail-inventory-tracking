package com.example.retail_stock_management;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerButtonViewAdapter extends RecyclerView.Adapter<RecyclerButtonViewAdapter.ButtonViewHolder> {
    private ArrayList<String> mData;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ButtonViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public Button buttonView;
        public ButtonViewHolder(View v) {
            super(v);
            buttonView = v.findViewById(R.id.recycler_view_buttonView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerButtonViewAdapter(ArrayList<String> stringList) {
        this.mData = stringList;
        Log.d("e", stringList.toString());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerButtonViewAdapter.ButtonViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_button_view, parent, false);
        ButtonViewHolder vh = new ButtonViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ButtonViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.buttonView.setText(mData.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mData.size();
    }
}

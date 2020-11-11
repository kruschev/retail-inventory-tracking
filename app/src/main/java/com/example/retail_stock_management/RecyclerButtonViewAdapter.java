package com.example.retail_stock_management;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerButtonViewAdapter extends RecyclerView.Adapter<RecyclerButtonViewAdapter.ButtonViewHolder> {
    private ArrayList<String> mData;
    private OnItemClickListener listener;

    //Interface to connect between Activity and RecyclerView
    public interface OnItemClickListener {
        void onItemClick(String button_text);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerButtonViewAdapter(ArrayList<String> stringList, OnItemClickListener listener) {
        this.mData = stringList;
        this.listener = listener;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ButtonViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public Button buttonView;

        public ButtonViewHolder(View recyclerView) {
            super(recyclerView);
            buttonView = recyclerView.findViewById(R.id.recycler_button);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerButtonViewAdapter.ButtonViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View recycler_view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_button_view, parent, false);
        ButtonViewHolder Vh = new ButtonViewHolder(recycler_view);
        return Vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ButtonViewHolder holder, int position) {
        // - get element from your dataset at this position
        final String button_text = mData.get(position);
        holder.buttonView.setText(button_text);

        // Callback function when button is clicked in Activity, communication through listener interface
        holder.buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(button_text);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mData.size();
    }

}

package com.example.retail_stock_management;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerItemViewAdapter extends RecyclerView.Adapter<RecyclerItemViewAdapter.ItemViewHolder> {
    private ArrayList<Product> mData;
    private OnItemClickListener listener;

    //Interface to connect between Activity and RecyclerView
    public interface OnItemClickListener {
        void onItemClick(String product_id);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerItemViewAdapter(ArrayList<Product> productList, RecyclerItemViewAdapter.OnItemClickListener listener) {
        this.mData = productList;
        this.listener = listener;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView prodNameView;
        public TextView prodPriceView;
        public TextView prodQuantView;

        public ItemViewHolder(View recyclerView) {
            super(recyclerView);
            imageView = recyclerView.findViewById(R.id.recycler_img);
            prodNameView = recyclerView.findViewById(R.id.recycler_prod_name);
            prodPriceView = recyclerView.findViewById(R.id.recycler_prod_price);
            prodQuantView = recyclerView.findViewById(R.id.recycler_prod_quant);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerItemViewAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                                         int viewType) {
        // create a new view
        View recycler_view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item_view, parent, false);
        RecyclerItemViewAdapter.ItemViewHolder Vh = new RecyclerItemViewAdapter.ItemViewHolder(recycler_view);
        return Vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerItemViewAdapter.ItemViewHolder holder, int position) {
        // - get element from your dataset at this position
        final Product product = mData.get(position);

        Picasso.get().load(product.imageUrl).into(holder.imageView);
        holder.prodNameView.setText(product.name);
        holder.prodPriceView.setText(holder.itemView.getContext().getString(R.string.prefix_price, product.priceSell));
        holder.prodQuantView.setText(holder.itemView.getContext().getString(R.string.prefix_quant, product.quant));

        // Callback function when button is clicked in Activity, communication through listener interface
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(product.id);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mData.size();
    }
}

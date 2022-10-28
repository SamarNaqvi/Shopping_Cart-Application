package com.example.shoppingcart.Adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingcart.Model.Product_item;
import com.example.shoppingcart.R;

import java.util.ArrayList;
import java.util.Collection;

public class ShoppingAdaptor extends RecyclerView.Adapter<ShoppingAdaptor.ShoppingViewHolder> implements Filterable {

    private ArrayList<Product_item> dataSet;
    private ArrayList<Product_item>filteredItems;
    private Filter filter;
    private ItemClickListener listener;


    public ShoppingAdaptor(ArrayList<Product_item> ds, ItemClickListener listener)
    {
        dataSet = ds;
        filteredItems=new ArrayList<Product_item>(ds);
        this.listener= listener;
    }


    @Override
    public ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.product_item,parent,false);
        return new ShoppingViewHolder(v, this.listener);
    }

    @Override
    public void onBindViewHolder(@NonNull  ShoppingViewHolder holder, int position) {
        String name = filteredItems.get(position).getName();
        holder.name.setText(name);
        holder.description.setText(filteredItems.get(position).getDescription());
        float price = filteredItems.get(position).getPrice();
        holder.price.setText("$" + Float.toString(price));
        String imgRsc = filteredItems.get(position).getImgName();
        int imgResource = holder.img.getContext().getResources().getIdentifier(imgRsc, "drawable", holder.img.getContext().getPackageName());
        holder.img.setImageResource(imgResource);

//        holder.img.setImageDrawable(holder.itemView.getContext().getDrawable(imgResource));
    }



    @Override
    public int getItemCount() {
        return filteredItems.size();
    }

    @Override
    public Filter getFilter() {
       if(filter==null)
       {
           filter = new FilteredItems();
       }
       return filter;
    }

    public class ShoppingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img;
        TextView name, description, price;
        ItemClickListener listener;
        public ShoppingViewHolder(@NonNull View itemView, ItemClickListener listener) {
            super(itemView);
            img = itemView.findViewById(R.id.card_img);
            name = itemView.findViewById(R.id.item_name);
            description = itemView.findViewById(R.id.item_desc);
            price = itemView.findViewById(R.id.item_price);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            listener.onItemClick(getAbsoluteAdapterPosition());
        }
    }

    private class FilteredItems extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Product_item> filteredList = new ArrayList<Product_item>();

                String  filterConstraint = constraint.toString().toLowerCase().trim();

                for (int i = 0; i < dataSet.size(); i++) {
                    if (dataSet.get(i).getName().toLowerCase().contains(filterConstraint)) {
                        filteredList.add(dataSet.get(i));
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;

            } else {
                results.count = dataSet.size();
                results.values = dataSet;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItems.clear();
            filteredItems.addAll((Collection<? extends Product_item>) results.values);
            notifyDataSetChanged();
        }

    }

    public interface ItemClickListener{
      public void onItemClick(int pos);
    };
    }

package com.example.shoppingcart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdaptor extends RecyclerView.Adapter<CartAdaptor.CartViewHolder> {

    private ArrayList<CartModel> cartItems= new ArrayList<CartModel>();

    public CartAdaptor(ArrayList<CartModel> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.cart_items, parent, false);
        return new CartAdaptor.CartViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product_item item = this.cartItems.get(position).getProduct();

        holder.name.setText(item.getName());
        int imgResource = item.getImg();
        float price = item.getPrice();
        holder.price.setText("$" + Float.toString(price));
        holder.productIcon.setImageDrawable(holder.itemView.getContext().getDrawable(imgResource));
        holder.quantity.setText(Integer.toString(this.cartItems.get(position).getQuantity()));
        holder.incQuantity.setTag(position);
        holder.decQuantity.setTag(position);
        holder.removeIcon.setTag(position);

    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView productIcon, removeIcon;
        TextView name, price, quantity;
        Button incQuantity, decQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productIcon = itemView.findViewById(R.id.prod_img);
            removeIcon = itemView.findViewById(R.id.removeItem);
            quantity = itemView.findViewById(R.id.quantity);
            name = itemView.findViewById(R.id.prod_name);
            price = itemView.findViewById(R.id.prod_price);
            incQuantity = itemView.findViewById(R.id.inc_quantity);
            decQuantity = itemView.findViewById(R.id.dec_quantity);

            removeIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();
                    CartModel obj = cartItems.get(pos);
                    cartItems.remove(obj);
                    notifyDataSetChanged();
                }
            });

            decQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();
                    CartModel obj = cartItems.get(pos);
                    int quantity = obj.getQuantity();

                    if(quantity==1)
                    {
                        cartItems.remove(obj);
                    }
                    else
                    {
                        obj.setQuantity(quantity-1);
                    }
                    notifyDataSetChanged();
                }
            });

            incQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (int) view.getTag();
                    CartModel obj = cartItems.get(pos);
                    int quantity = obj.getQuantity();

                    obj.setQuantity(quantity+1);

                    notifyDataSetChanged();
                }
            });

        }
    }
}

package com.example.shoppingcart.Model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.shoppingcart.DB.dataLayer;

import java.util.ArrayList;
import java.util.Hashtable;

public class CartModel implements Parcelable {

    private Product_item product;
    private int quantity;

    public CartModel(Product_item product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static void saveCart(ArrayList<CartModel>cartItems, Context context)
    {
        ArrayList<Hashtable<String,String>>cartList= new ArrayList<Hashtable<String,String>>();

        for( CartModel item : cartItems)
        {
            Hashtable<String,String>obj=new Hashtable<String,String>();
            obj.put("prodid",Integer.toString(item.getProduct().getID()));
            obj.put("quantity", Integer.toString(item.getQuantity()));
            cartList.add(obj);
        }

        dataLayer dbHelper = new dataLayer(context);
        dbHelper.save(cartList, "CART");
    }

    public static ArrayList<CartModel> loadItems(Context context)
    {
        dataLayer dbHelper = new dataLayer(context);
        ArrayList<Hashtable<String,String>> Items = dbHelper.loadALL("CART");
        if(Items==null)
        {
            return new ArrayList<CartModel>();
        }
        ArrayList<CartModel>cartItems = new ArrayList<CartModel>();

        for ( Hashtable<String,String> item: Items)
        {
            Hashtable<String,String> product= dbHelper.loadById(item.get("prodid"),"Product");
            cartItems.add(
                    new CartModel(
                            Product_item.generateProduct(product),
                            Integer.parseInt(item.get("quantity"))
                    )
            );
        }

        return cartItems.size()>0? cartItems : new ArrayList<CartModel>();
    }

    protected CartModel(Parcel in) {
        product = in.readParcelable(Product_item.class.getClassLoader());
        quantity = in.readInt();
    }

    public static final Creator<CartModel> CREATOR = new Creator<CartModel>() {
        @Override
        public CartModel createFromParcel(Parcel in) {
            return new CartModel(in);
        }

        @Override
        public CartModel[] newArray(int size) {
            return new CartModel[size];
        }
    };

    public static void removeItem(String cart, int id, Context context) {
        dataLayer dbHelper = new dataLayer(context);
       dbHelper.removeItem(cart, id);
    }

    public static void updateItem(String cart, CartModel obj, Context context) {
        dataLayer dbHelper = new dataLayer(context);
        dbHelper.updateItem(cart, obj);
    }

    public Product_item getProduct() {
        return product;
    }

    public void setProduct(Product_item product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(product, i);
        parcel.writeInt(quantity);
    }
}

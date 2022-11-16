package com.example.shoppingcart.Model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.shoppingcart.DB.dataLayer;
import com.example.shoppingcart.DB.firebaseDb;

import java.util.ArrayList;
import java.util.Hashtable;

public class CartModel implements Parcelable {

    private Product_item product;
    private int quantity;
    public static firebaseDb db;
    public CartModel(Product_item product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static void saveCart(ArrayList<CartModel>cartItems, Context context, boolean dbType)
    {
        ArrayList<Hashtable<String,String>>cartList= new ArrayList<Hashtable<String,String>>();

        for( CartModel item : cartItems)
        {
            Hashtable<String,String>obj=new Hashtable<String,String>();
            obj.put("ProdId",Integer.toString(item.getProduct().getID()));
            obj.put("Quantity", Integer.toString(item.getQuantity()));
            cartList.add(obj);
        }

        if(dbType) //Sql lite if true
        {
            dataLayer dbHelper = new dataLayer(context);
            dbHelper.save(cartList, "CART");
        }
        else
        {
            db.saveCartItems(cartList, cartItems);
        }
    }



    public static ArrayList<CartModel> loadItems(Context context)
    {
        dataLayer dbHelper = new dataLayer(context);
        ArrayList<Hashtable<String,String>> Items = dbHelper.loadALL("CART");
        if(Items==null)
        {
            return new ArrayList<CartModel>();
        }

        return Cart_generator(Items,null, dbHelper);
    }



    public static  ArrayList<CartModel> Cart_generator(ArrayList<Hashtable<String,String>> Items, ArrayList<Product_item>products, dataLayer db)
    {
        ArrayList<CartModel>cartItems = new ArrayList<CartModel>();

        for ( Hashtable<String,String> item: Items)
        {
            Product_item product = null;
            if(products==null)
            {
                product = Product_item.generateProduct(
                        db.loadById(item.get("prodid"), "Product"));
            }
            else {
                product = getProduct(products, Integer.parseInt(item.get("ProdId")));
            }

            cartItems.add(

                    new CartModel(
                            product,
                            Integer.parseInt(item.get("Quantity"))
                    )
            );
        }

        return cartItems.size()>0? cartItems : new ArrayList<CartModel>();
    }

    public static Product_item getProduct(ArrayList<Product_item> products, int id)
    {
        for(Product_item item: products)
        {
            if(item.getID()==id)
            {
                return item;
            }
        }
        return null;
    }

    public static ArrayList<CartModel> getCartItems()
    {
        if(db!=null)
            return db.getCartList();
        else
            return null;
    }

    public static void loadItemsFromFirebase(firebaseDb database)
    {
        db = database;
        db.loadProducts("CartItems");
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

    public static void removeItem(String cart, int id, Context context, boolean dbType) {
        if(dbType) { //if dbType=>true then use sql lite otherwise use firebase
            dataLayer dbHelper = new dataLayer(context);
            dbHelper.removeItem(cart, id);
        }
        else
        {
            db.removeItem("CartItems", id);
        }
    }

    public static void updateItem(String cart, CartModel obj, Context context, boolean dbType) {
        if(dbType) { //if dbType=>true then use sql lite otherwise use firebase
            dataLayer dbHelper = new dataLayer(context);
            dbHelper.updateItem(cart, obj);
        }
        else
        {
            db.updateItem("CartItems",obj);
        }
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

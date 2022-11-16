package com.example.shoppingcart.Model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.shoppingcart.Adaptor.ShoppingAdaptor;
import com.example.shoppingcart.DB.dataLayer;
import com.example.shoppingcart.DB.firebaseDb;

import java.util.ArrayList;
import java.util.Hashtable;


public class Product_item implements Parcelable {

    private int id;
    private String name;
    private String description;
    private String imgName;
    private float Price;
    public static firebaseDb db;

    public Product_item(int id, String name, String description, String imgName, float price) {
        this.id =  id;
        this.name = name;
        this.description = description;

        this.imgName = imgName;
        Price = price;
    }

    public Product_item(){};


    protected Product_item(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        imgName = in.readString();
        Price = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(imgName);
        dest.writeFloat(Price);
    }

    public static Product_item generateProduct(Hashtable<String,String>product)
    {
        return new Product_item(
                Integer.parseInt(product.get("id")),
                product.get("name"),
                product.get("description"),
                product.get("imgName"),
                Float.parseFloat(product.get("price")
                ));
    }

    public static  void loadItemsFromFirebase(ShoppingAdaptor adp, Context ctx)
    {
        if(db==null) {
            db = new firebaseDb(adp, ctx);
            db.loadProducts("Products");
        }
    }



    public static ArrayList<Product_item> loadItems(Context context)
    {
        ArrayList<Hashtable<String,String>>productList = new ArrayList<Hashtable<String,String>>();
        dataLayer dbAccess = new dataLayer(context);
        productList.addAll(dbAccess.loadALL("Product"));
        return Product_genertation(productList);
    }

    public static ArrayList<Product_item>Product_genertation(ArrayList<Hashtable<String,String>>productList)
    {
        ArrayList<Product_item>data = new ArrayList<Product_item>();

        for(Hashtable<String,String> product : productList){
            data.add(generateProduct(product));
        }
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product_item> CREATOR = new Creator<Product_item>() {
        @Override
        public Product_item createFromParcel(Parcel in) {
            return new Product_item(in);
        }

        @Override
        public Product_item[] newArray(int size) {
            return new Product_item[size];
        }
    };

    public static ArrayList<Product_item> getProdItems()
    {
        if(db!=null)
        return db.getDataList();
        else return  null;
    }

    public int getID(){ return this.id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getImgName() {
        return imgName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }


}

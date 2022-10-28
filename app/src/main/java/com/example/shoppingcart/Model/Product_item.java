package com.example.shoppingcart.Model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.shoppingcart.DB.dataLayer;

import java.util.ArrayList;
import java.util.Hashtable;


public class Product_item implements Parcelable {

    private int id;
    private String name;
    private String description;
    private String imgName;
    private float Price;

    public Product_item(int id, String name, String description, String imgName, float price) {
        this.id =  id;
        this.name = name;
        this.description = description;

        this.imgName = imgName;
        Price = price;
    }


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
                product.get("icon"),
                Float.parseFloat(product.get("price")
                ));
    }
    public static ArrayList<Product_item> loadItems(Context context)
    {
        ArrayList<Hashtable<String,String>>productList = new ArrayList<Hashtable<String,String>>();
        dataLayer dbAccess = new dataLayer(context);
        productList.addAll(dbAccess.loadALL("Product"));

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

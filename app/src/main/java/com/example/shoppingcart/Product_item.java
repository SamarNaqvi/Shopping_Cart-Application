package com.example.shoppingcart;

import android.os.Parcel;
import android.os.Parcelable;



public class Product_item implements Parcelable {

    private String name;
    private String description;
    private int img;
    private String imgName;
    private float Price;

    public Product_item(String name, String description, int img, String imgName, float price) {
        this.name = name;
        this.description = description;
        this.img = img;
        this.imgName = imgName;
        Price = price;
    }

    protected Product_item(Parcel in) {
        name = in.readString();
        description = in.readString();
        img = in.readInt();
        imgName = in.readString();
        Price = in.readFloat();
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

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeInt(img);
        parcel.writeString(imgName);
        parcel.writeFloat(Price);
    }
}

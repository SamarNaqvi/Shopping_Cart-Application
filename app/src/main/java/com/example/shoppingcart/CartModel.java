package com.example.shoppingcart;

import android.os.Parcel;
import android.os.Parcelable;

public class CartModel implements Parcelable {

    private Product_item product;
    private int quantity;

    public CartModel(Product_item product, int quantity) {
        this.product = product;
        this.quantity = quantity;
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

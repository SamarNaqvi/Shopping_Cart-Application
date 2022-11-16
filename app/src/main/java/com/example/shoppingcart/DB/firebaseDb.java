package com.example.shoppingcart.DB;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.shoppingcart.Adaptor.ShoppingAdaptor;
import com.example.shoppingcart.Model.CartModel;
import com.example.shoppingcart.Model.Product_item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class firebaseDb implements dataInterface{

    FirebaseDatabase database;
    DatabaseReference myRef;
    Context ctx;
    ShoppingAdaptor ProductAdapter;
    private ArrayList<Product_item>dataList;
    private ArrayList<CartModel>cartList;

    public ArrayList<Product_item> getDataList() {
        return dataList;
    }

    public ArrayList<CartModel> getCartList() {
        return cartList;
    }



    public firebaseDb(ShoppingAdaptor adp , Context context){
        this.ProductAdapter = adp;
        this.ctx = context;
        initDb();
    }


    public void initDb()
    {
        if(myRef==null)
        {
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        myRef = database.getReference();
        }
    }

    public void  initProducts()
    {
        ArrayList<Product_item>products = new ArrayList<Product_item>();
        products.add(new Product_item(1,"Chair","It is a chair","chair", 200F));
        products.add(new Product_item(2,"Pencil color","It is a pencil color box","colors", 100F));
        products.add(new Product_item(3,"Television","It is a TV", "tv",600F));
        products.add(new Product_item(4,"Laptop","It is a Laptop",  "laptop",1000F));
        products.add(new Product_item(5,"Perfume","It is a perfume","perfume", 300F));
        products.add(new Product_item(6,"School Bag","It is a School Bag", "bag", 100F));
        products.add(new Product_item(7,"NoteBook","It is Notebook", "notebook",50F));
        products.add(new Product_item(8,"Shoes","A pair of Jogger shoes","shoes", 200F));
        products.add(new Product_item(9,"Bicycle","It is bicycle", "bicycle", 500F));
        products.add(new Product_item(10,"Phone","It is phone", "phone", 5000F));

        for(Product_item item : products)
        {
            myRef.child(Integer.toString(item.getID())).setValue(item);
        }

    }




    @Override
    public void save(Hashtable<String, String> attributes, String tableName) {

    }

    public void saveCartItems(ArrayList<Hashtable<String,String>>cartList, ArrayList<CartModel> updatedList) {

        this.cartList=null;
        this.cartList = updatedList;
        for(Hashtable<String,String> item : cartList)
            myRef.child("CartItems").child(item.get("ProdId")).setValue(item);
    }

    @Override
    public void save(ArrayList<Hashtable<String, String>> objects, String tableName) {

    }

    @Override
    public ArrayList<Hashtable<String, String>> loadALL(String tableName) {
        return null;
    }

    public ArrayList<Hashtable<String,String>>  dataCreation(DataSnapshot dataSnapshot)
    {
        ArrayList<Hashtable<String,String>> data = new ArrayList<Hashtable<String,String>>();
        for (DataSnapshot d : dataSnapshot.getChildren()) {
            GenericTypeIndicator<HashMap<String, Object>> type = new GenericTypeIndicator<HashMap<String, Object>>() {
            };
            HashMap<String, Object> map = d.getValue((GenericTypeIndicator<HashMap<String, Object>>) type);

            Hashtable<String, String> obj = new Hashtable<String, String>();
            for (String key : map.keySet()) {
                obj.put(key, map.get(key).toString());
            }
            data.add(obj);
        }

        return data;
    }

    public void postWork(String tableName, ArrayList<Hashtable<String,String>>data)
    {
        if (tableName == "Products") {
            dataList = null;
            dataList = Product_item.Product_genertation(data);
            ProductAdapter.setDataSet(dataList, (ShoppingAdaptor.ItemClickListener) ctx);
            ProductAdapter.notifyDataSetChanged();
        } else if (cartList == null) {

            cartList = CartModel.Cart_generator(data, dataList, null);
        }
    }

    public void loadProducts(String tableName) {



        Handler handler = new Handler();


        myRef.child(tableName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {

                    if (cartList != null || dataList != null) {
                        Thread thread = new Thread(new Runnable() {

                            @Override
                            public void run() {

                                ArrayList<Hashtable<String,String>> data;
                                data = dataCreation(dataSnapshot);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        postWork(tableName, data);
                                    }
                                });
                            }
                        });
                        thread.start();
                    } else {
                        ArrayList<Hashtable<String, String>> data;
                        data = dataCreation(dataSnapshot);
                        postWork(tableName, data);
                    }


                } catch (Exception ex) {
                    Log.e("firebasedb", ex.getMessage());
                }
            }


            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("firebasedb", "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    public Hashtable<String, String> loadById(String id, String tableName) {
        return null;
    }

    @Override
    public void updateItem(String tableName, CartModel obj) {
       myRef.child(tableName).child(Integer.toString(obj.getProduct().getID())).child("Quantity").setValue(obj.getQuantity());
    }

    @Override
    public void removeItem(String tableName, int prodId) {
        myRef.child(tableName).child(Integer.toString(prodId)).removeValue();
    }
}

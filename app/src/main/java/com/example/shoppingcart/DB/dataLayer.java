package com.example.shoppingcart.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.shoppingcart.Model.CartModel;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class dataLayer implements dataInterface{

    private Context context;

    public dataLayer(Context ctx) {
        context = ctx;
    }

    @Override
    public void save(Hashtable<String, String> attributes, String tableName) {
        ProductDbHelper dbHelper = new ProductDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        ContentValues content = new ContentValues();
        Enumeration<String> keys = attributes.keys();
        while (keys.hasMoreElements()){
            String key = keys.nextElement();
            content.put(key,attributes.get(key));
        }

        String id = tableName=="Product"? "id":"prodid";
        String [] arguments = new String[1];
        arguments[0] = attributes.get(id);
        Hashtable obj = loadById(arguments[0].toUpperCase(),tableName);

        if (obj.get(id) != null && obj.get(id).equals(arguments[0])){
            db.update(tableName,content,"PRODID = ?",arguments);
        }
        else{
            db.insert(tableName,null,content);
        }

    }

    @Override
    public void save(ArrayList<Hashtable<String, String>> objects, String tableName) {
        for(Hashtable<String,String> obj : objects){
            save(obj, tableName);
        }
    }


    @Override
    public ArrayList<Hashtable<String, String>> loadALL(String tableName) {
        ProductDbHelper dbHelper = new ProductDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM "+tableName+ "";
        Cursor cursor = db.rawQuery(query,null);



        ArrayList<Hashtable<String,String>> objects = new ArrayList<Hashtable<String, String>>();
        while(cursor.getCount()>0 && cursor!= null && cursor.moveToNext()){
            Hashtable<String,String> obj = new Hashtable<String, String>();
            String [] columns = cursor.getColumnNames();

            for(String col : columns){
              int columnIndex = cursor.getColumnIndex(col);
              String val = cursor.getString(columnIndex);
              if(val==null)
              {
                  cursor.close();
                  db.close();
                  return null;
              }
              obj.put(col.toLowerCase(),val);
            }
            objects.add(obj);
        }

        return objects.size()==0?null:objects;
    }

    @Override
    public Hashtable<String, String> loadById(String id, String tableName) {
        ProductDbHelper dbHelper = new ProductDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String idName = tableName=="Product"? "Id": "PRODID";
        String query = "SELECT * FROM "+ tableName+" WHERE "+idName+" = ?";
        String[] arguments = new String[1];
        arguments[0] = id;
        Cursor cursor = db.rawQuery(query,arguments);


        Hashtable<String,String> obj = new Hashtable<String, String>();
        while(cursor.moveToNext()){
            String [] columns = cursor.getColumnNames();
            for(String col : columns){
                int columnIndex = cursor.getColumnIndex(col);
                obj.put(col.toLowerCase(),cursor.getString(columnIndex));
            }
        }

        return obj;

    }

    @Override
    public void updateItem(String tableName, CartModel obj) {
        ProductDbHelper dbHelper = new ProductDbHelper(context);
        SQLiteDatabase db  = dbHelper.getWritableDatabase();

        String[] arg = new String[1];
        arg[0] = Integer.toString(obj.getProduct().getID());

        ContentValues content = new ContentValues();
        content.put("Quantity",obj.getQuantity());
        content.put("PRODID",obj.getProduct().getID());
        db.update(tableName, content, "PRODID = ?", arg);

    }

    @Override
    public void removeItem(String tableName, int prodId) {

        ProductDbHelper dbHelper = new ProductDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] arg = new String[1];
        arg[0] = Integer.toString(prodId);

        db.delete(tableName,"PRODID = ?",arg);

    }
}

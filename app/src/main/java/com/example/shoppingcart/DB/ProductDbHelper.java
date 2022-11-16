package com.example.shoppingcart.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ProductDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Shopping_Cart.db";
    private static boolean tableExists = false;
    public ProductDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String table1 = "CREATE TABLE Product (Id TEXT PRIMARY KEY, " +
                "name TEXT," +
                "price REAL," + "description TEXT," + "icon TEXT) ";
        String table2 = "CREATE TABLE CART (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " Quantity Text,"+ "PRODID TEXT, FOREIGN KEY (PRODID) REFERENCES PRODUCT(Id)); ";
        sqLiteDatabase.execSQL(table1);
        sqLiteDatabase.execSQL(table2);

        if(!tableExists)
        {
            initProducts(sqLiteDatabase);
            tableExists=true;
        }

    }

    public void initProducts(SQLiteDatabase db)
    {

        initProducts("1","Chair","It is a chair","chair", 200F,db);
        initProducts("2","Pencil color","It is a pencil color box","colors", 100F,db);
        initProducts("3","Television","It is a TV", "tv",600F,db);
        initProducts("4","Laptop","It is a Laptop",  "laptop",1000F,db);
        initProducts("5","Perfume","It is a perfume","perfume", 300F,db);
        initProducts("6","School Bag","It is a School Bag", "bag", 100F,db);
        initProducts("7","NoteBook","It is Notebook", "notebook",50F,db);
        initProducts("8","Shoes","A pair of Jogger shoes","shoes", 200F,db);
        initProducts("9","Bicycle","It is bicycle", "bicycle", 500F,db);
        initProducts("10","Phone","It is phone", "phone", 5000F,db);
       // db.close();
    }

    public void initProducts(String id, String name, String desc, String img, Float price, SQLiteDatabase db)
    {
        ContentValues obj = new ContentValues();
        obj.put("Id",id);
        obj.put("name",name);
        obj.put("price",price.toString());
        obj.put("description",desc);
        obj.put("icon",img);

        if(!db.isOpen())
        {
            db =  this.getWritableDatabase();
        }
        db.insert("Product",null,obj);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Product");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS CART");
        onCreate(sqLiteDatabase);
        tableExists=false;
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion,newVersion);
    }
}

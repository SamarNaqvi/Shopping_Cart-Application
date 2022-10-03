package com.example.shoppingcart;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;


public class Product_Details extends AppCompatActivity  {

    private TextView name, price, description;
    private ImageView img;
    private Button addCart;
    private ArrayList<CartModel> cartItems = new ArrayList<CartModel>();
    private ActivityResultLauncher<Intent> shopLauncher;
    private int rscId;
    private boolean isAdded;
    private String imgRsc, price2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Intent intent = getIntent();

        name = findViewById(R.id.item_name);
        price = findViewById(R.id.item_price);
        description = findViewById(R.id.item_desc);
        img = findViewById(R.id.card_icon);
        addCart = findViewById(R.id.add_cart);


        name.setText(intent.getStringExtra("name"));
        price2 = intent.getStringExtra("price");
        price.setText("$"+price2);

        description.setText(intent.getStringExtra("description"));
        imgRsc = intent.getStringExtra("img");
        rscId = getResources().getIdentifier(imgRsc, "drawable", getPackageName());
        img.setImageResource(rscId);
        cartItems = intent.getExtras().getParcelableArrayList("products");


        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAdded=true;
                Product_item item = new Product_item(
                        name.getText().toString(),description.getText().toString(),
                        rscId, imgRsc, Float.parseFloat(price2)
                );

                int index = -1;
                for (CartModel cartItem : cartItems)
                {
                    if(item.getName().equals( cartItem.getProduct().getName()))
                    {
                        index = cartItems.indexOf(cartItem);
                    }
                }


                if(index==-1)
                {
                    cartItems.add(new CartModel(item,1));
                }
                else
                {
                    cartItems.get(index).setQuantity(cartItems.get(index).getQuantity()+1);
                }

                Intent intent2 = new Intent(view.getContext(), CartActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("products",cartItems);
                intent2.putExtras(bundle);
                intent2.putExtra("src","buy_item");
                startActivity(intent2);
            }
        });

        //register launcher
        shopLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 1) {
                            Intent intent = result.getData();
                            cartItems.clear();
                            cartItems = intent.getExtras().getParcelableArrayList("products");
                        }

                    }
                });


    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();
        if(isAdded)
        {

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("products",cartItems);
            intent.putExtras(bundle);
            setResult(1,intent);

        }
        else {
            setResult(2, intent);
        }
        Product_Details.super.onBackPressed();
    }
}
package com.example.shoppingcart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ArrayList<CartModel> cartItems = new ArrayList<CartModel>();
    private CartAdaptor adaptor;
    private RecyclerView recyclerView;
    private Button genPrice, BackToItems;
    private TextView totalPrice;
    private String src;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        populateData();

        recyclerView = findViewById(R.id.cart_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adaptor = new CartAdaptor(cartItems);
        recyclerView.setAdapter(adaptor);

        genPrice = findViewById(R.id.genPrice);
        totalPrice = findViewById(R.id.show_Price);
        BackToItems = findViewById(R.id.home);

        genPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float comPrice = 0;
                for (CartModel item : cartItems) {
                    comPrice += (item.getQuantity() * item.getProduct().getPrice());
                }
                totalPrice.setText(Float.toString(comPrice));
            }
        });

        BackToItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent;
                if(!src.equals("main"))
                {
                    intent = new Intent(view.getContext(),MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("products", cartItems);
                    intent.putExtras(bundle);
                    intent.putExtra("src","cart");
                    startActivity(intent);
                }
                else {
                    intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("products", cartItems);
                    intent.putExtras(bundle);
                    setResult(1, intent);
                    CartActivity.super.onBackPressed();
                }

            }
        });
    }

    public void populateData() {
        Intent intent = getIntent();
        src = intent.getStringExtra("src");
        cartItems.clear();
        cartItems = intent.getExtras().getParcelableArrayList("products");
    }
}
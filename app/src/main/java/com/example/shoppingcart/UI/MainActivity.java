package com.example.shoppingcart.UI;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.Toast;

import com.example.shoppingcart.DB.firebaseDb;
import com.example.shoppingcart.Model.CartModel;
import com.example.shoppingcart.ViewModel.CartViewModel;
import com.example.shoppingcart.ViewModel.ItemsViewModel;
import com.example.shoppingcart.Model.Product_item;
import com.example.shoppingcart.R;
import com.example.shoppingcart.Adaptor.ShoppingAdaptor;
import com.example.shoppingcart.DB.dataLayer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ShoppingAdaptor.ItemClickListener {

    private RecyclerView recyclerView;
    private ShoppingAdaptor adaptor;
    private ArrayList<CartModel> cartItems = new ArrayList<CartModel>();
    private ArrayList<Product_item> data;
    private Filterable filterable;
    private EditText search;
    private Button viewCart;
    private Button CustomerSupport;
    private ActivityResultLauncher<Intent> shopLauncher;
    private String src;
    private dataLayer dbAccess;
    ItemsViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        vm = new ViewModelProvider(this).get(ItemsViewModel.class);
        data = vm.getNotes(savedInstanceState,"data");

        RecyclerView recyclerView = findViewById(R.id.itemsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptor = new ShoppingAdaptor(data, this);

        recyclerView.setAdapter(adaptor);

        createData();

        search = findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adaptor.getFilter().filter(search.getText());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        viewCart = findViewById(R.id.viewCart);
        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(view.getContext(), CartActivity.class);

                if(cartItems==null)
                {
                    cartItems = CartModel.getCartItems();
                }

                if(cartItems!=null && cartItems.size()>0)
                {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("products",cartItems);
                    intent.putExtras(bundle);
                    intent.putExtra("src","main");
                    shopLauncher.launch(intent);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "No Items to Show", Toast.LENGTH_SHORT).show();
                }



            }
        });

        CustomerSupport =findViewById(R.id.customerSupport);
        CustomerSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0123456789"));
                startActivity(intent);
            }
        });

        //register launcher
        shopLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 1) {

                            cartItems = CartModel.getCartItems();
                        }

                    }
                });



    }


    public void createData()
    {
        data = Product_item.getProdItems();
        if(data==null || data.size()==0) {
                Product_item.loadItemsFromFirebase(adaptor, MainActivity.this);
        }

        cartItems = CartModel.getCartItems();
        if(cartItems==null || cartItems.size()==0)
        {
            CartModel.loadItemsFromFirebase(Product_item.db);
        }

        if(data!=null && data.size()>0 && adaptor.getItemCount()==0)
        {
            adaptor.setDataSet(data,this);
            adaptor.notifyDataSetChanged();
        }

    }

    @Override
    public void onItemClick(int pos) {
        Intent intent =  new Intent(this, Product_Details.class);
        if(this.data==null || this.data.size()==0)
        {
            this.data = Product_item.getProdItems();
        }

        if(cartItems==null || cartItems.size()==0)
        {
            cartItems = CartModel.getCartItems();
        }

        Product_item item  = this.data.get(pos);
        intent.putExtra("id",Integer.toString(item.getID()));
        intent.putExtra("name",item.getName());
        intent.putExtra("description",item.getDescription());
        intent.putExtra("price",Float.toString(item.getPrice()));
        intent.putExtra("img",item.getImgName());

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("products",cartItems);
        intent.putExtras(bundle);

        shopLauncher.launch(intent);
    }
}
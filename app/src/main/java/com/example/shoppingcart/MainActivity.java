package com.example.shoppingcart;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ShoppingAdaptor.ItemClickListener {

    private RecyclerView recyclerView;
    private ShoppingAdaptor adaptor;
    private ArrayList<CartModel> cartItems = new ArrayList<CartModel>();
    private ArrayList<Product_item> data;
    private Filterable filterable;
    private EditText search;
    private Button viewCart;
    private ActivityResultLauncher<Intent> shopLauncher;
    private String src;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ItemsViewModel vm = new ViewModelProvider(this).get(ItemsViewModel.class);
        data = vm.getNotes(savedInstanceState,"data");

        createData();

        RecyclerView recyclerView = findViewById(R.id.itemsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adaptor = new ShoppingAdaptor(data, this);

        recyclerView.setAdapter(adaptor);

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


                if(cartItems.size()>0)
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


    public void createData()
    {
        data.add(new Product_item("Chair","It is a chair", R.drawable.chair,"chair", 200));
        data.add(new Product_item("Pencil color","It is a pencil color box", R.drawable.colors,"colors", 100));
        data.add(new Product_item("Television","It is a TV", R.drawable.tv, "tv",600));
        data.add(new Product_item("Laptop","It is a Laptop", R.drawable.laptop, "laptop",1000));
        data.add(new Product_item("Perfume","It is a perfume", R.drawable.perfume,"perfume", 300));
        data.add(new Product_item("School Bag","It is a School Bag", R.drawable.bag,"bag", 100));
        data.add(new Product_item("NoteBook","It is Notebook", R.drawable.notebook, "notebook",50));
        data.add(new Product_item("Shoes","A pair of Jogger shoes", R.drawable.shoes,"shoes", 200));
        data.add(new Product_item("Bicycle","It is bicycle", R.drawable.bicycle,"bicycle", 500));

        Intent intent = getIntent();
        src = intent.getStringExtra("src");
        if(src!=null)
        {
            cartItems.clear();
            cartItems = intent.getExtras().getParcelableArrayList("products");
        }
    }

    @Override
    public void onItemClick(int pos) {
        Intent intent =  new Intent(this, Product_Details.class);
        Product_item item  = this.data.get(pos);
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
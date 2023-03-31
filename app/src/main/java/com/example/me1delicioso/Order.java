package com.example.me1delicioso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Order extends AppCompatActivity {
    ImageButton railwaycl, spicyaloo, greekf, spinachncorn, paneerdelight, ultpaneer;
    String burgname, railwaycutlet, spicyaloocrunch,greekfalafel,spinachcorn,paneerd,ultimatepaneer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        railwaycl = findViewById(R.id.railwaycutlet);
        spicyaloo = findViewById(R.id.spicyaloo);
        greekf = findViewById(R.id.greekfalafel);
        spinachncorn = findViewById(R.id.spinachncorn);
        paneerdelight = findViewById(R.id.paneerdelight);
        ultpaneer = findViewById(R.id.ultimatepaneer);

        railwaycutlet = getString(R.string.railwaycutlet);
        spicyaloocrunch = getString(R.string.spicyaloo);
        greekfalafel = getString(R.string.greekf);
        spinachcorn = getString(R.string.spinachncorn);
        paneerd = getString(R.string.paneerdelight);
        ultimatepaneer = getString(R.string.ultpaneer);

        railwaycl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                burgname = railwaycutlet;
                Intent intent = new Intent(Order.this, Checkout.class);
                intent.putExtra("burger", burgname);
                intent.putExtra("value", 1);
                startActivity(intent);
            }
        });
        spicyaloo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                burgname = spicyaloocrunch;
                Intent intent = new Intent(Order.this, Checkout.class);
                intent.putExtra("burger", burgname);
                intent.putExtra("value", 2);
                startActivity(intent);
            }
        });
        greekf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                burgname = greekfalafel;
                Intent intent = new Intent(Order.this, Checkout.class);
                intent.putExtra("burger", burgname);
                intent.putExtra("value", 3);
                startActivity(intent);
            }
        });
        spinachncorn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                burgname = spinachcorn;
                Intent intent = new Intent(Order.this, Checkout.class);
                intent.putExtra("burger", burgname);
                intent.putExtra("value", 4);
                startActivity(intent);
            }
        });
        paneerdelight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                burgname = paneerd;
                Intent intent = new Intent(Order.this, Checkout.class);
                intent.putExtra("burger", burgname);
                intent.putExtra("value", 5);
                startActivity(intent);
            }
        });
        ultpaneer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                burgname = ultimatepaneer;
                Intent intent = new Intent(Order.this, Checkout.class);
                intent.putExtra("burger", burgname);
                intent.putExtra("value", 6);
                startActivity(intent);
            }
        });
    }
}
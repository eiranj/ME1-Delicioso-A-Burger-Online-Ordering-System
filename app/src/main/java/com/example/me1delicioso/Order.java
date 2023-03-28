package com.example.me1delicioso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Order extends AppCompatActivity {

    private ListView mListView;
    int [] images = {R.drawable.railwaycutlet , R.drawable.spicyaloocrunch ,
            R.drawable.greekfalafel , R.drawable.spinach,
            R.drawable.paneerdelight, R.drawable.crispybean
            , R.drawable.ultimatepaneer};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        mListView = findViewById(R.id.listview);



        MyAdapter adapter = new MyAdapter();
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){

                    startActivity(new Intent(Order.this,RailwayCutlet.class));

                }else if(position==1){

                    startActivity(new Intent(Order.this,SpicyAlooCrunch.class));

                }else if(position==2){

                    startActivity(new Intent(Order.this,GreekFalafel.class));

                }else if(position==3){

                    startActivity(new Intent(Order.this,SpinachNCorn.class));
                }else if(position==4){

                    startActivity(new Intent(Order.this,PaneerDelight.class));
                }else if(position==5){

                    startActivity(new Intent(Order.this,CrispyBean.class));

                }else{
                    startActivity(new Intent(Order.this,UltimatePaneer.class));
                }
            }
        });

    }
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.items ,parent , false);
            TextView textView = convertView.findViewById(R.id.textview);
            ImageView imageView = convertView.findViewById(R.id.imageview);

            imageView.setImageResource(images[position]);
            return  convertView;
        }
    }
}
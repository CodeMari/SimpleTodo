package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
//import android.os.FileUtils;
import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //List<String> items = new ArrayList<>(FileUtils.readLines(getDataFileI(), Charset.defaultCharset());

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE=20;

    List<String> items;


    Button buttonAdd;
    EditText editTextItem;
    RecyclerView  rvItems;
    ItemsAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = findViewById(R.id.buttonAdd);
        editTextItem = findViewById(R.id.editTextItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();
        //editTextItem.setText("i'm doing this from java!");
        //new itemsAdapter()
        //items = new ArrayList<>();
        /*items.add("Buy Milk");
        items.add("Go to Gym");
        items.add("Have fun!");
*/
        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //delete the items from the model
                items.remove(position);

                //notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemsClicked(int position) {
                Log.e("MainActivity", "Single Click at position" + position);

                //CREATE THE NEW ACTIVITY
                Intent i = new Intent(MainActivity.this, EditActivity.class);

                //pass data to data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                //display the acitivity
                startActivityForResult(i, EDIT_TEXT_CODE);


            }
        };
        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        //ItemsAdapter itemsAdapter = new ItemsAdapter(items, onLongClickListener);

        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = editTextItem.getText().toString();
                items.add(todoItem);
                itemsAdapter.notifyItemInserted(items.size() - 1);

                editTextItem.setText("");

                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();

                saveItems();
            }
        });

    }
    //handle the result of edit activity
        @SuppressLint("MissingSuperCall")
        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
            //super.onActivityResult(requestCode, resultCode, data);
            if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
                //RETRIEVE UPDATED TEXT VALue
                String itemText = data.getStringExtra(KEY_ITEM_TEXT);
                int position = data.getExtras().getInt(KEY_ITEM_POSITION);
                items.set(position, itemText);

                itemsAdapter.notifyItemChanged(position);
                saveItems();
                Toast.makeText(getApplicationContext(), "item updated successfully", Toast.LENGTH_SHORT).show();


            }else{
                Log.w("MainActivity", "Unknown call...");
            }
    }



        private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
        }

        //load items
        private void loadItems(){
            try {
                items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
            }catch (IOException e){
                Log.e( "MainActivity", "Error reading items", e);
                items = new ArrayList<>();
        }
        }
        private void saveItems(){
        try{
        FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
        }
    }
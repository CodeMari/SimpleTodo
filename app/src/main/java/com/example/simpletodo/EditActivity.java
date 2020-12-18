package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText editItem;
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editItem = findViewById(R.id.editItem);
        buttonSave = findViewById(R.id.buttonSave);

        getSupportActionBar().setTitle("Edit item");

        editItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create Intent that will contain new results
                Intent intent = new Intent();

                //pass the new results
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, editItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                //set the result of the intent
                setResult(RESULT_OK, intent);

                //finish the activity, close current screen and go bakc
                finish();
            }
        });


    }
}
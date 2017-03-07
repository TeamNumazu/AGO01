package com.example.silve.ago01.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.example.silve.ago01.R;
import com.example.silve.ago01.fragments.DatePickerFragment;
import com.example.silve.ago01.models.DataBaseHelper;
import com.example.silve.ago01.models.entity.Item;
import com.example.silve.ago01.models.repository.ItemRepository;

import java.util.List;

public class ItemRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    /**
     * datePickerを表示する
     *
     * @param v
     */
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * 登録ボタンをクリックした時
     *
     * @param v
     */
    public void onClickRegisterButton(View v) {
        EditText itemName = (EditText) findViewById(R.id.item_name);
        EditText expiredAt = (EditText) findViewById(R.id.expired_at);
        EditText number = (EditText) findViewById(R.id.number);

        Item item = new Item();
        item.setItemName(itemName.getText().toString());
        item.setExpiredAt(expiredAt.getText().toString());
        item.setNumber(Long.parseLong(number.getText().toString()));

        DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
        ItemRepository iRepository = new ItemRepository(dbHelper);
        iRepository.add(item);
    }

}

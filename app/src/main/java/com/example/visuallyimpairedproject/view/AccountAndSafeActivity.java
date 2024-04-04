package com.example.visuallyimpairedproject.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.visuallyimpairedproject.R;
import com.example.visuallyimpairedproject.utils.Utils;

public class AccountAndSafeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setStatusBar(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_and_safe);
    }
}
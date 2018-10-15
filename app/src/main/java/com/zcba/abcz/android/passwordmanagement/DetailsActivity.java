package com.zcba.abcz.android.passwordmanagement;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private TextView textViewName;
    private TextView textViewId;
    private TextView textViewPw;

    private String titleName;
    private String passId;
    private String passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setTitle(R.string.da_title);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        findViews();

        Intent intent = getIntent();

        titleName = intent.getStringExtra("titleName");
        passId = intent.getStringExtra("passId");
        passWord = intent.getStringExtra("passWord");

        textViewName.setText(titleName);
        textViewId.setText(passId);
        textViewPw.setText(passWord);
    }

    /**
     * 各部品の結びつけ処理
     * findViews()
     */
    private void findViews() {
        textViewName = findViewById(R.id.tv_moreDetailTitle);
        textViewId = findViewById(R.id.tv_moreDetailId);
        textViewPw = findViewById(R.id.tv_moreDetailPw);
    }

    /**
     * アクションバーの戻るを押したときの処理
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

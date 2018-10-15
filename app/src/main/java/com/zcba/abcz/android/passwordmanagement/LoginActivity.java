package com.zcba.abcz.android.passwordmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    // 保存するファイル名
    private static final String PREF_FILE_NAME ="MasterPasswordFile";
    // SharedPreferencesのキー
    private static final String KEY = "Master";
    private static String value;
    private String decValue;
    private String masterPassWord;
    private String encmasterPassWord;
    private TextView textView;
    private EditText editText;
    private static CryptUtil cryptUtil;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 各部品の結びつけ
        findViews();


        sharedPreferences = getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        value = sharedPreferences.getString(KEY, null);

        // sharedPreferencesがあるかどうかで表示テキストを変更
        if (value == null) {
            textView.setText(R.string.la_tv_mastertextviewmsg1);
        } else {
            textView.setText(R.string.la_tv_mastertextviewmsg2);
            }

        cryptUtil = new CryptUtil();
    }

    /**
     * LOGINボタンが押されたときの処理
     * onLoginClick()
     */
    public void onLoginClick(View view) {
        masterPassWord = editText.getText().toString();

        // 文字が入力されているか確認
        if (masterPassWord.equals("")) {
            Toast.makeText(LoginActivity.this, R.string.la_to_passwordinput, Toast.LENGTH_LONG).show();
        } else {
            // sharedPreferencesがない場合
            if (value == null) {
                try {
                    encmasterPassWord = cryptUtil.encrypt(masterPassWord);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY, encmasterPassWord).commit();

                    intent = new Intent(LoginActivity.this, PassWordListActivity.class);
                    startActivity(intent);

                } catch (Exception e) {
                }
            } else {
                // sharedPreferencesがある場合
                try {
                    decValue = cryptUtil.decrypt(value);

                    if (decValue.equals(masterPassWord)) {

                        intent = new Intent(LoginActivity.this, PassWordListActivity.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(LoginActivity.this, R.string.la_to_passwordmiss, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * 各部品を結びつけるクラス
     * findViews()
     */
    private void findViews() {
        textView = findViewById(R.id.tvMasterTextView);
        editText = findViewById(R.id.edMasterPassInput);
    }
}

package com.zcba.abcz.android.passwordmanagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.List;

public class PassNewAddActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextId;
    private EditText editTextPw;

    private SharedPreferences sharedPreferences;
    private static CryptUtil cryptUtil;

    // SharedPreferencesのキー
    private static final String KEY = "Master";
    private static final String PREF_FILE_NAME ="MasterPasswordFile";

    private String dbPassWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_new_add);
        setTitle(R.string.ps_title);

        findViews();

        init();

        cryptUtil = new CryptUtil();

    }

    /**
     * 追加ボタンが押されたときの処理
     */
    public void OnAddButtonClick(View view) {
        // マスターパスワードを取得
        sharedPreferences = getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        String _value = sharedPreferences.getString(KEY, null);

        try {
            dbPassWord = cryptUtil.decrypt(_value);
        } catch (Exception e) {
        }

        // DBへ登録
        SaveDbList(dbPassWord);
    }

    /**
     * 戻るボタンが押されたときの処理
     */
    public void OnReturnButtonClick(View view) {
        finish();
    }

    /**
     * EditTextに入力したテキストをDBに登録
     * SaveDbList()
     */
    private void SaveDbList(String dbPassWord) {

        // 各EditTextで入力されたテキストを取得
        String strName = editTextName.getText().toString();
        String strPassid = editTextId.getText().toString();
        String strPassword = editTextPw.getText().toString();

        // DBへの登録処理
        if (strName.equals("")) {
            Toast.makeText(PassNewAddActivity.this, R.string.ps_to_notname, Toast.LENGTH_LONG).show();
        } else {
            SQLiteDatabase.loadLibs(PassNewAddActivity.this);
            DBAdapter dbAdapter = new DBAdapter(PassNewAddActivity.this);
            dbAdapter.openDB(dbPassWord);
            dbAdapter.saveDB(strName, strPassid, strPassword);
            dbAdapter.closeDB();

            init();
        }
    }

    /**
     * 各部品の結びつけ処理
     * findViews()
     */
    private void findViews() {
        editTextName = findViewById(R.id.PassTitle);
        editTextId = findViewById(R.id.IdInput);
        editTextPw = findViewById(R.id.PassInput);
    }

    /**
     * 初期値設定
     * init()
     */
    private void init() {
        editTextName.setText("");
        editTextId.setText("");
        editTextPw.setText("");
    }
}

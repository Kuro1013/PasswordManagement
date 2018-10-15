package com.zcba.abcz.android.passwordmanagement;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class PassWordListActivity extends AppCompatActivity {

    private DBAdapter dbadapter;
    private MyBaseAdapter myBaseAdapter;
    private List<ListViewItem> items;
    private ListView listView;
    protected ListViewItem listViewItem;
    private SharedPreferences sharedPreferences;
    private Intent intent;

    private String[] colums = null;

    private static String dbPassWord;

    private static CryptUtil cryptUtil;

    // SharedPreferencesのキー
    private static final String KEY = "Master";
    private static final String PREF_FILE_NAME ="MasterPasswordFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_word_list);
        setTitle(R.string.pl_title);

        cryptUtil = new CryptUtil();

        dbadapter = new DBAdapter(PassWordListActivity.this);

        items = new ArrayList<>();

        myBaseAdapter = new MyBaseAdapter(PassWordListActivity.this, items);

        listView = findViewById(R.id.lv_listview);

        DbLoadList();

        // リストビューの各ボタンが押されたときの処理
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                switch (view.getId()) {
                    case R.id.tv_Name:

                        // DBからそれぞれデータを取得
                        listViewItem = items.get(position);

                        String titleName = listViewItem.getName();
                        String passId = listViewItem.getPassId();
                        String passWord = listViewItem.getPassWord();

                        intent = new Intent(PassWordListActivity.this, DetailsActivity.class);
                        intent.putExtra("titleName", titleName);
                        intent.putExtra("passId", passId);
                        intent.putExtra("passWord", passWord);

                        startActivity(intent);
                        break;
                    case R.id.bt_Id:

                        // passIDを取得
                        listViewItem = items.get(position);
                        String _passId = listViewItem.getPassId();

                        // クリップボードへコピー
                        copyToClipboard(PassWordListActivity.this,"", _passId);

                        Toast.makeText(PassWordListActivity.this, R.string.pl_to_id, Toast.LENGTH_LONG).show();

                        break;
                    case R.id.bt_Pw:

                        // パスワードを取得
                        listViewItem = items.get(position);
                        String _passWord = listViewItem.getPassWord();

                        // クリップボードへコピー
                        copyToClipboard(PassWordListActivity.this,"", _passWord);

                        Toast.makeText(PassWordListActivity.this, R.string.pl_to_pw, Toast.LENGTH_LONG).show();

                        break;
                    case R.id.bt_Delete:
                        // アラートダイアログ表示
                        AlertDialog.Builder builder = new AlertDialog.Builder(PassWordListActivity.this);
                        builder.setTitle("削除");
                        builder.setMessage("削除しますか？");
                        // OKの時の処理
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // IDを取得
                                listViewItem = items.get(position);
                                int listId = listViewItem.getId();

                                SQLiteDatabase.loadLibs(PassWordListActivity.this);
                                dbadapter.openDB(dbPassWord);
                                dbadapter.selectDelete(String.valueOf(listId));
                                dbadapter.closeDB();
                                DbLoadList();
                            }
                        });

                        builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        // ダイアログ表示
                        AlertDialog dialog = builder.create();
                        dialog.show();

                        break;
                }
            }
        });
    }

    /**
     * DBを読み込む＆更新する処理
     * DbLoadList()
     */
    private void DbLoadList() {

        items.clear();

        SQLiteDatabase.loadLibs(PassWordListActivity.this);

        //DBのパスワードを取得する
        sharedPreferences = getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        String _value = sharedPreferences.getString(KEY, null);

        try {
            dbPassWord = cryptUtil.decrypt(_value);
        } catch (Exception e) {
        }


        dbadapter.openDB(dbPassWord);

        Cursor cursor = dbadapter.getDB(colums);

        if (cursor.moveToFirst()) {
            do {
                listViewItem = new ListViewItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));

                items.add(listViewItem);

            }while (cursor.moveToNext());
        }
        cursor.close();
        dbadapter.closeDB();
        listView.setAdapter(myBaseAdapter);
        myBaseAdapter.notifyDataSetChanged();
    }

    /**
     * BaseAdapterを継承したクラス
     * MyBaseAdapter
     */
    public class MyBaseAdapter extends BaseAdapter {

        private Context context;
        private List<ListViewItem> items;

        private class ViewHolder {
            TextView textView;
            Button btId;
            Button btPw;
            Button btDe;
        }

        // コンストラクタの生成
        public MyBaseAdapter(Context context, List<ListViewItem> items) {
            this.context = context;
            this.items = items;
        }

        // Listの要素数を返す
        @Override
        public int getCount() {
            return items.size();
        }

        // indexやオブジェクトを返す
        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        // IDを他のindexに返す
        @Override
        public long getItemId(int position) {
            return position;
        }

        // 新しいデータが表示されるタイミングで呼び出される
        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            View view = convertView;
            ViewHolder holder;

            // データを取得
            listViewItem = items.get(position);

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.row, parent, false);

                TextView textView = view.findViewById(R.id.tv_Name);
                Button btId = view.findViewById(R.id.bt_Id);
                Button btPw = view.findViewById(R.id.bt_Pw);
                Button btDe = view.findViewById(R.id.bt_Delete);

                // holderにviewを持たせておく
                holder = new ViewHolder();
                holder.textView = textView;
                holder.btId = btId;
                holder.btPw = btPw;
                holder.btDe = btDe;
                view.setTag(holder);

            } else {
                // 初めて表示されるときにつけておいたtagを元にviewを取得する
                holder = (ViewHolder) view.getTag();
            }

            // 取得した各データを各TextViewにセット
            holder.textView.setText(listViewItem.getName());

            // 詳細画面用のリスナーを登録
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ListView) parent).performItemClick(view, position, R.id.tv_Name);
                }
            });

            // 各ボタンにリスナーを登録
            holder.btId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ListView) parent).performItemClick(view, position, R.id.bt_Id);
                }
            });
            holder.btPw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ListView) parent).performItemClick(view, position, R.id.bt_Pw);
                }
            });
            holder.btDe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ListView) parent).performItemClick(view, position, R.id.bt_Delete);
                }
            });

            return view;
        }
    }

    /**
     * 新規追加ボタンが押されたときの処理
     */
    public void onNewAddButtonClick(View view) {
        intent = new Intent(PassWordListActivity.this, PassNewAddActivity.class);
        startActivity(intent);
    }

    /**
     * クリップボードを使用するためのクラス
     * copyToClipboard
     */
    public static void copyToClipboard(Context context, String label, String text) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text));
    }

    @Override
    public void onRestart() {
        super .onRestart();

        // リストビュー更新
        DbLoadList();
    }
}

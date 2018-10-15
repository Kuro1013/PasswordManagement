package com.zcba.abcz.android.passwordmanagement;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

/**
 * Databaseに関連するクラス
 * DBAdapter
 */
public class DBAdapter {
    private final static String DB_NAME = "password.db";       // DB名
    private final static String DB_TABLE = "mypassword";       // DBのテーブル名
    private final static int DB_VERSION = 1;                   // DBのバージョン

    /**
     * DBのカラム名
     */
    public final static String COL_ID = "_id";                 // id
    public final static String COL_NAME = "name";              // 名前
    public final static String COL_PASSID = "passid";          // ユーザID
    public final static String COL_PASSWROD = "password";      // パスワード

    private SQLiteDatabase db = null;
    private DBHelper dbHelper = null;
    protected Context context;

    // コンストラクタ
    public DBAdapter(Context context) {
        this.context = context;
        dbHelper = new DBHelper(this.context);
    }

    /**
     * DBを開く
     * openDB()
     *
     * @return this 自身のオブジェクト
     */
    public DBAdapter openDB(String Dbpassword) {
        db = dbHelper.getWritableDatabase(Dbpassword);
        return this;
    }

    /**
     * DBを閉じる
     * closeDB()
     */
    public void closeDB() {
        db.close();
        db = null;
    }

    /**
     * DBへ登録
     * saveDB()
     *
     * @param name 名前
     * @param passid  ユーザID
     * @param password  パスワード
     */
    public void saveDB(String name, String passid, String password) {

        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(COL_NAME, name);
            values.put(COL_PASSID, passid);
            values.put(COL_PASSWROD, password);

            db.insert(DB_TABLE, null, values);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * DBのデータを取得
     * getDB()
     *
     * @param columns String[] 取得するカラム名 nullの場合は全カラムを取得
     * @return DBのデータ
     */
    public Cursor getDB(String[] columns) {

        // queryメソッド DBのデータを取得
        // 第1引数：DBのテーブル名
        // 第2引数：取得するカラム名
        // 第3引数：選択条件(WHERE句)
        // 第4引数：第3引数のWHERE句において?を使用した場合に使用
        // 第5引数：集計条件(GROUP BY句)
        // 第6引数：選択条件(HAVING句)
        // 第7引数：ソート条件(ODERBY句)
        return db.query(DB_TABLE, columns, null, null, null, null, null);
    }

    /**
     * DBのレコードの単一削除
     * selectDelete()
     *
     * @param position String
     */
    public void selectDelete(String position) {

        db.beginTransaction();
        try {
            db.delete(DB_TABLE, COL_ID + "=?", new String[]{position});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * SQLiteOpenHelperを継承したクラス
     * DBHelper
     */
    private static class DBHelper extends SQLiteOpenHelper {

        // コンストラクタ
        public DBHelper(Context context) {
            //第1引数：コンテキスト
            //第2引数：DB名
            //第3引数：factory nullでよい
            //第4引数：DBのバージョン
            super(context, DB_NAME, null, DB_VERSION);
        }

        /**
         * DB生成時に呼ばれる
         * onCreate()
         *
         * @param db SQLiteDatabase
         */
        @Override
        public void onCreate(SQLiteDatabase db) {

            //テーブルを作成するSQL文の定義
            String createTbl = "CREATE TABLE " + DB_TABLE + " ("
                    + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_NAME + " TEXT NOT NULL,"
                    + COL_PASSID + " TEXT NOT NULL,"
                    + COL_PASSWROD + " TEXT NOT NULL"
                    + ");";

            db.execSQL(createTbl);
        }

        /**
         * DBアップグレード(バージョンアップ)時に呼ばれる
         * 今回は何もしない
         *
         * @param db         SQLiteDatabase
         * @param oldVersion int 古いバージョン
         * @param newVersion int 新しいバージョン
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}

package com.zcba.abcz.android.passwordmanagement;

/**
 * ListViewに必要なデータを取得するクラス
 * ListViewItem
 */
public class ListViewItem {
    protected int id;            // id
    protected String name;       // 名前
    protected String passid;     // ユーザID
    protected String password;   // パスワード

    /**
     * ListViewItem()
     *
     * @param id        int ID
     * @param name      String 名前
     * @param passid    String ユーザID
     * @param password  String パスワード
     */
    public ListViewItem(int id, String name, String passid, String password) {
        this.id = id;
        this.name = name;
        this.passid = passid;
        this.password = password;
    }

    /**
     * IDを取得
     * getId()
     *
     * @return id int ID
     */
    public int getId() {
        return id;
    }

    /**
     * 名前を取得
     * getName()
     *
     * @return name String 名前
     */
    public String getName() {
        return name;
    }

    /**
     * ユーザIDを取得
     * getPassId()
     *
     * @return passid String ユーザID
     */
    public String getPassId() {
        return passid;
    }

    /**
     * パスワードを取得
     * getPassWord()
     *
     * @return password String パスワード
     */
    public String getPassWord() {
        return password;
    }
}

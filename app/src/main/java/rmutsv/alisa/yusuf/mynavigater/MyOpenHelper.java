package rmutsv.alisa.yusuf.mynavigater;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by masterung on 7/23/2017 AD.
 */

public class MyOpenHelper extends SQLiteOpenHelper{

    private Context context;
    public static final String databaseName = "Navigate.db";
    private static final int databaseVersion = 1;
    private static final String createTable = "Create Table navigateTABLE (" +
            "id Integer Primary Key, " +
            "NameMap Text, " +
            "MyDate Text, " +
            "Distance Text, " +
            "Lat Text, " +
            "Lng Text);";

    public MyOpenHelper(Context context) {
        super(context, databaseName, null, databaseVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}   // Main Class

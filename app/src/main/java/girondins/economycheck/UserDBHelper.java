package girondins.economycheck;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Girondins on 14/09/15.
 * Klass som pratar med databas
 */
public class UserDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME ="userinfo";
    public static final String COLUMN_NAME = "username";
    public static final String COLUMN_INOUT = "incomeoutcome";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_ID = "inputid";
    public static final String COLUMN_PROD = "prodname";

    private static final String DATABASE_NAME = "economycheckdatabas.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "(" +
                                                    COLUMN_ID + " integer not null primary key autoincrement, " +
                                                    COLUMN_DATE + " text not null, " +
                                                    COLUMN_CATEGORY + " text not null, " +
                                                    COLUMN_NAME + " text not null, " +
                                                    COLUMN_PROD + " text not null, " +
                                                    COLUMN_INOUT + " text not null);";




    public UserDBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);

    }

    /**
     * Metod som kallas ifall databasen redan existerar
     * @param db databas
     * @param oldVersion äldreversion
     * @param newVersion nya versionen
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(UserDBHelper.class.getName(),"Updating database from version" + oldVersion + "to"
                                            + newVersion + ", which will replace all old data");
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);

    }

    /**
     * Metod som lägger till information i databasen
     * @param input informationen som läggs till
     */
    public void addInOutcome(Input input){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserDBHelper.COLUMN_DATE,input.getDate());
        values.put(UserDBHelper.COLUMN_NAME,input.getUserName());
        values.put(UserDBHelper.COLUMN_INOUT,input.getInput());
        values.put(UserDBHelper.COLUMN_CATEGORY,input.getCategory());
        values.put(UserDBHelper.COLUMN_PROD,input.getProduct());
        db.insert(UserDBHelper.TABLE_NAME,"",values);

    }

    /**
     * Metod som retunerar/hämtar all information om användaren från databas
     * @param user användaren
     * @return information om användaren
     */
    public Input[] getUserInputs(String user){
        int date,category,inout,prod;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + UserDBHelper.TABLE_NAME + " WHERE " + UserDBHelper.COLUMN_NAME + " = " + "'" + user + "'", null);
        Input [] inputs = new Input[cursor.getCount()];

        date = cursor.getColumnIndex(UserDBHelper.COLUMN_DATE);
        category = cursor.getColumnIndex(UserDBHelper.COLUMN_CATEGORY);
        inout = cursor.getColumnIndex(UserDBHelper.COLUMN_INOUT);
        prod = cursor.getColumnIndex(UserDBHelper.COLUMN_PROD);

        for(int i=0; i<inputs.length; i++){
            cursor.moveToPosition(i);
            inputs[i] = new Input(user,cursor.getString(inout),
                                    cursor.getString(date),cursor.getString(category),cursor.getString(prod));

        }
        return inputs;

    }

}

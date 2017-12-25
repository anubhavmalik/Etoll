package com.anubhavmalikdeveloper.e_toll.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.anubhavmalikdeveloper.e_toll.models.RouteModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by PC on 25-12-2017.
 */

public class TripDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="TripDB";
    private static final String TABLE_NAME="tableTrip";
    private static final String TRIP_ID="trip_id";
    private static final String LAT_COLUMN="lat";
    private static final String LNG_COLUMN="lng";
    private static final String DISTANCE_COLUMN="distance";
    private static final String TIME_COLUMN="time";
    private static final String DATE_TIME_COLUMN="date_time";
    private static final String SOURCE_COlUMN="source";
    private static final String DESTINATION_COLUMN="destination";
    private static TripDBHelper mInstance=null;

    public TripDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public static TripDBHelper getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new TripDBHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + LAT_COLUMN + " TEXT,"
                + LNG_COLUMN + " TEXT, " + DISTANCE_COLUMN + " TEXT, "+ TIME_COLUMN + " TEXT, "
                + DATE_TIME_COLUMN + " TEXT, "+ SOURCE_COlUMN + " TEXT, "+ DESTINATION_COLUMN + " TEXT "+ ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addTrip(RouteModel routeModel,String source, String destination) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LAT_COLUMN, routeModel.getLatArrayList().toString().substring(1,routeModel.getLatArrayList().toString().length()-1));
        values.put(LNG_COLUMN, routeModel.getLngArrayList().toString().substring(1,routeModel.getLngArrayList().toString().length()-1));
        values.put(DISTANCE_COLUMN,routeModel.getDistance());
        values.put(TIME_COLUMN,routeModel.getTime());
        values.put(DATE_TIME_COLUMN,formattedDate);
        values.put(SOURCE_COlUMN,source);
        values.put(DESTINATION_COLUMN,destination);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public RouteModel getTrip(int id){
        RouteModel routeModel=null;
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { LAT_COLUMN,
                        LNG_COLUMN, DISTANCE_COLUMN, TIME_COLUMN, SOURCE_COlUMN, DESTINATION_COLUMN }, TRIP_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            Log.i("tag", new ArrayList<String>(Arrays.asList(cursor.getString(cursor.getColumnIndex(LNG_COLUMN)).split(","))).toString());
            routeModel = new RouteModel(new ArrayList<String>(Arrays.asList(cursor.getString(cursor.getColumnIndex(LAT_COLUMN)).split(","))),
                    new ArrayList<String>(Arrays.asList(cursor.getString(cursor.getColumnIndex(LNG_COLUMN)).split(","))),
                    cursor.getString(cursor.getColumnIndex(DISTANCE_COLUMN)), cursor.getString(cursor.getColumnIndex(TIME_COLUMN)),
                    cursor.getString(cursor.getColumnIndex(SOURCE_COlUMN)), cursor.getString(cursor.getColumnIndex(DESTINATION_COLUMN)));
        }
        cursor.close();
        return routeModel;
    }

    public HashMap<Integer,String> getBookingTime(){
        HashMap<Integer, String> hashMap=new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT "+TRIP_ID+" , "+DATE_TIME_COLUMN+" FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                hashMap.put(cursor.getInt(cursor.getColumnIndex(TRIP_ID)),cursor.getString(cursor.getColumnIndex(DATE_TIME_COLUMN)));
            } while (cursor.moveToNext());
        }
        return hashMap;
    }
}

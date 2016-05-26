package com.voxalto.automotovoxalto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.sql.RowId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Sabah on 5/6/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "auto_moto_voxalto";
    // Cars table name
    private static final String TABLE_NAME = "cars";
    // Cars Table Columns names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_VIN = "vin";
    private static final String COLUMN_MAKE = "make";
    private static final String COLUMN_MODEL = "model";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_ENGINE_OIL_TYPE = "engine_oil_type";
    private static final String COLUMN_ENGINE_COOLANT_TYPE = "engine_coolant_type";
    private static final String COLUMN_BRAKE_TYPE = "brake_type";
    private static final String COLUMN_STEERING_TYPE = "power_steering_type";
    private static String dbPath = "";

    SQLiteDatabase db;
    private static final String TABLE_CREATE = "create table if not exists cars  (id integer primary key, vin text not null, make text not null,"+
            " model text not null, year integer not null, engine_oil_type text not null, engine_coolant_type text not null,  brake_type text not null,"+
            " power_steering_type text not null, unique (id, vin));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        dbPath = String.valueOf(context.getDatabasePath(DATABASE_NAME).getAbsolutePath());
        Log.d("DB Path : ", dbPath);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        this.db = db;
    }

    public void insertCar(Car c) {
        db = getWritableDatabase();
        if (db == null) {
            return;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_MAKE, c.getMake());
            values.put(COLUMN_MODEL, c.getModel());
            values.put(COLUMN_YEAR, c.getYear());
            values.put(COLUMN_VIN, c.getVin());
            values.put(COLUMN_ENGINE_OIL_TYPE, c.getEngineOilType());
            values.put(COLUMN_ENGINE_COOLANT_TYPE, c.getEngineCoolantType());
            values.put(COLUMN_BRAKE_TYPE, c.getBrakeType());
            values.put(COLUMN_STEERING_TYPE, c.getSteeringType());
            db.insert(TABLE_NAME, null, values);
        }
    }

    public void clearTable()   {
        db = getWritableDatabase();
        db.delete(TABLE_NAME, null,null);
    }

    public List<Car> getAllCar() {
        List<Car> cars = new ArrayList<>();
        String CARS_SELECT_QUERY = String.format("SELECT * FROM "+ TABLE_NAME);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(CARS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Car car = new Car();
                    String vin = cursor.getString(cursor.getColumnIndex(COLUMN_VIN));
                    String make = cursor.getString(cursor.getColumnIndex(COLUMN_MAKE));
                    String model = cursor.getString(cursor.getColumnIndex(COLUMN_MODEL));
                    String year = cursor.getString(cursor.getColumnIndex(COLUMN_YEAR));
                    int yearInteger = Integer.parseInt(year);
                    String engineOilType = cursor.getString(cursor.getColumnIndex(COLUMN_ENGINE_OIL_TYPE));
                    String engineCoolantType = cursor.getString(cursor.getColumnIndex(COLUMN_ENGINE_COOLANT_TYPE));
                    String brakeType = cursor.getString(cursor.getColumnIndex(COLUMN_BRAKE_TYPE));
                    String steeringType = cursor.getString(cursor.getColumnIndex(COLUMN_STEERING_TYPE));

                    car.setVin(vin);
                    car.setMake(make);
                    car.setModel(model);
                    car.setYear(yearInteger);
                    car.setEngineOilType(engineOilType);
                    car.setEngineCoolantType(engineCoolantType);
                    car.setBrakeType(brakeType);
                    car.setSteeringType(steeringType);

                    cars.add(car);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("CARS", "Error while trying to get cars from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return cars;
    }

    public List<String> getAllVins() {
        List<String> vins = new ArrayList<String>();
        String CARS_SELECT_QUERY = String.format("SELECT * FROM " + TABLE_NAME);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery(CARS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String vin = cursor.getString(cursor.getColumnIndex(COLUMN_VIN));
                    vins.add(vin);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("VINS", e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return vins;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS "+ TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }
}

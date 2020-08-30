package com.harnet.locationtest.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.harnet.locationtest.models.Place;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaceDaoSQL {
    private SQLiteDatabase db;

    public PlaceDaoSQL(SQLiteDatabase db) {
        this.db = db;
    }

    // create Places table
    public void createPlacesTable(){
        String sql = "CREATE TABLE IF NOT EXISTS places (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, lat REAL, lng REAL, image int)";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.execute();
        Log.i("SQLITEEE", "createPlacesTable: ");
    }

    // add book to a database
    public void addPlaceToDb(Place place) throws SQLException {
        //TODO think about using transaction instead binding
        String sql = "INSERT INTO places (name, description, lat, lng, image) VALUES (?, ?, ?, ?, ?)";
        SQLiteStatement stmt = db.compileStatement(sql);

        //parse a place
        String name = place.getName();
        String description = place.getDescription();
        Double lat = place.getLat();
        Double lng = place.getLng();
        //TODO when a place photo will be implemented - change to place.getImage();
        int image = 0;

        // binding values to statement
        stmt.bindString(1, name);
        stmt.bindString(2, description);
        stmt.bindDouble(3, lat);
        stmt.bindDouble(4, lng);
        //TODO think how to bind Integer after a place image implemented
        stmt.bindDouble(5, 0);

        long rowId = stmt.executeInsert();
        Log.i("SQLITEEE", "addPlaceToDb: Place was added to row: " + rowId);
    }

    public List<Place> getAllPlaces(){
        List<Place> places = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from places",null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                //Parse table row for data
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                Double lat = Double.valueOf(cursor.getString(cursor.getColumnIndex("lat")));
                Double lng = Double.valueOf(cursor.getString(cursor.getColumnIndex("lng")));
//                int image = Integer.parseInt(cursor.getString(cursor.getColumnIndex("image")));

                places.add(new Place(name, description, lat, lng, 0));
                cursor.moveToNext();
            }
        }
        cursor.close();

        return places;
    }
}

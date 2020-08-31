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
        String sql = "CREATE TABLE IF NOT EXISTS places (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, lat REAL, lng REAL, image int)";
        SQLiteStatement stmt = db.compileStatement(sql);
        stmt.execute();
    }

    // add Place to a places table
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
    }

    public void addPlacesToDB(List<Place> places){
        for(Place place : places){
            try{
                addPlaceToDb(place);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // get all places from SQLite database
    public List<Place> getAllPlaces(){
        List<Place> places = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from places",null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                //Parse table row for data
                //TODO swiched off id because of app crash
//                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id")));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                Double lat = Double.valueOf(cursor.getString(cursor.getColumnIndex("lat")));
                Double lng = Double.valueOf(cursor.getString(cursor.getColumnIndex("lng")));
//                int image = Integer.parseInt(cursor.getString(cursor.getColumnIndex("image")));

                Place newPlace = new Place(name, description, lat, lng, 0);
                //TODO swiched off id because of app crash
//                newPlace.setId(id);
                places.add(newPlace);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return places;
    }

    public void deletePlaceFromDB(){
//        try {
//            db.beginTransaction();
//            String sql = "DELETE FROM " + places +
//                    " WHERE " + column_1 + " = ?";
//            SQLiteStatement statement = db.compileStatement(sql);
//
//            for (Long id : words) {
//                statement.clearBindings();
//                statement.bindLong(1, id);
//                statement.executeUpdateDelete();
//            }
//
//            db.setTransactionSuccessful();
//
//        } catch (SQLException e) {
//            Log.w("Exception:", e);
//        } finally {
//            db.endTransaction();
//        }
    }

    public void clearPlacesTable(){
        db.delete("places", null, null);
    }
}

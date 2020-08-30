package com.harnet.locationtest.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.harnet.locationtest.models.Place;

import java.sql.SQLException;
import java.util.List;


public class PlaceDaoDatabase implements Dao<Place> {
    private Context appContext;
    private PlaceDaoSQL placeDaoSQL; // work with sql queries

    public PlaceDaoDatabase(Context context) {
        this.appContext = context;
        //TODO can produce stackOverflow error
        initDB();
    }

    // initiate SQLite database
    private void initDB(){
        // create database
        SQLiteDatabase db = appContext.openOrCreateDatabase("LocationManager", Context.MODE_PRIVATE, null);
        // instantiate class for working with SQL queries
        placeDaoSQL = new PlaceDaoSQL(db);
        //create Places table
        placeDaoSQL.createPlacesTable();
    }

    @Override
    public void add(Place item) throws SQLException {
        placeDaoSQL.addPlaceToDb(item);
    }

    @Override
    public void update(Place item, int id) throws SQLException {

    }

    @Override
    public void delete(Place item) throws SQLException {

    }

    @Override
    public Place get(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Place> getAll() throws SQLException {
        return null;
    }
}

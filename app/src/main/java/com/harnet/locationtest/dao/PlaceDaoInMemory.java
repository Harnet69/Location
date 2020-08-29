package com.harnet.locationtest.dao;

import com.harnet.locationtest.models.Place;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaceDaoInMemory implements Dao<Place> {
    Map<Integer, Place> places = new HashMap<>();
    int idCounter = 0;

    @Override
    public void add(Place place){
        place.setId(idCounter);
        idCounter++;
        places.put(place.getId(), place);
    }
    @Override
    public void update(Place place, int id){
        places.put(place.getId(), place);
    }

    @Override
    public Place get(int id){
        return places.get(id);
    }

    @Override
    public List<Place> getAll(){
        return new ArrayList<>(places.values());
    }
}
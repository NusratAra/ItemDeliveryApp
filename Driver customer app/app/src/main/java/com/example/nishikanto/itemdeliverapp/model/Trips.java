package com.example.nishikanto.itemdeliverapp.model;

import java.util.ArrayList;

public class Trips {
    ArrayList<Trip> trips = new ArrayList<>();
    float total;

//    Trip trip;
//
//    public Trip getTrip() {
//        return trip;
//    }
//
//    public void setTrip(Trip trip) {
//        this.trip = trip;
//    }


    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public ArrayList<Trip> getTrips() {
        return trips;
    }

    public void setTrips(ArrayList<Trip> trips) {
        this.trips = trips;
    }
}

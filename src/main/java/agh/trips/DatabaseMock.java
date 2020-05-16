package agh.trips;

import java.util.*;

public class DatabaseMock {
    private Map<String, List<Trip>> trips = new HashMap<>();

    public DatabaseMock(){
        trips.put("ala", new LinkedList<>(Arrays.asList(
                new Trip("Paris 2020", new Date(), new Date(), ""),
                new Trip("RPA 2021", new Date(), new Date(), ""))));
    }

    public List<Trip> getTrips(String user){
        return trips.get(user);
    }

    public List<Trip> getTrip(String user, Trip trip){
        return trips.get(user);
    }

    public void addTrip(String user, Trip trip){
        List<Trip> trips = this.trips.get(user);
        if(trips == null){
            trips = new LinkedList<>();
        }
        trips.add(trip);
        this.trips.put(user, trips);
    }

}

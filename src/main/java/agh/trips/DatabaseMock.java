package agh.trips;

import java.util.*;

public class DatabaseMock {
    private Map<String, List<Trip>> trips = new HashMap<>();

    /* This is just a mock and do not work properly in all cases. It rather should be List<Trip>
    and more sophisticated method for getTrips. */

    public DatabaseMock(){
        trips.put("ala", new LinkedList<>(Arrays.asList(
                new Trip("Paris 2020", new Date(), new Date(), ""),
                new Trip("RPA 2021", new Date(), new Date(), ""))));
    }

    public List<Trip> getTrips(String user){
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

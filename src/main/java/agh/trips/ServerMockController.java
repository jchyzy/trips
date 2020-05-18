package agh.trips;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/server")
public class ServerMockController {
    private DatabaseMock database = new DatabaseMock();

    @GetMapping("/getTrips")
    public List<Trip> getTrips(@RequestParam(value = "user") String user){
        return database.getTrips(user);
    }

    @RequestMapping(value = "/addTrip", method = RequestMethod.POST)
    public void addTrip(@RequestParam String user, @RequestBody Trip trip){
        database.addTrip(user, trip);
    }

    @RequestMapping(value = "/editTrip", method = RequestMethod.POST)
    public void editTrip(@RequestParam String user, @RequestParam String tripName, @RequestBody Trip trip){
        database.editTrip(user, tripName, trip);
    }

}

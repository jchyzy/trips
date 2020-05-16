package agh.trips;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/server")
public class ServerMockController {
    private DatabaseMock database = new DatabaseMock();

    @GetMapping("/getTrips")
    public List<Trip> getTrips(@RequestParam(value = "user", required = false, defaultValue = "bob") String user){
        return database.getTrips(user);
    }

    @GetMapping("/getTrip")
    public List<Trip> getTrip(@RequestParam(value = "user") String user,
                              @RequestBody Trip trip){
        return database.getTrip(user, trip);
    }

    @RequestMapping(value = "/addTrip", method = RequestMethod.POST)
    public void addTrip(@RequestParam String user, @RequestBody Trip trip){
        database.addTrip(user, trip);
    }

}

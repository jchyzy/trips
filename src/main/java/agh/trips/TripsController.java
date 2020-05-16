package agh.trips;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Controller
public class TripsController {

    private String user;
    private List<Trip> trips;

    private final String SERVER_ADDRESS = "http://localhost:8080/server";
    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/trips")
    public String getTrips(Model model){
        model.addAttribute("trip", new Trip());

        Trip[] tripsArray = restTemplate.getForObject(SERVER_ADDRESS + "/getTrips?user=" + user, Trip[].class);
        if(tripsArray != null) {
            trips = Arrays.asList(tripsArray);
        }

        model.addAttribute("trips", trips);

        return "trips";
    }

    @PostMapping("/trips")
    public String addTrip(@ModelAttribute Trip trip){
        List<String> participants = trip.getParticipantsList();
        if(participants == null){
            participants = new LinkedList<>();
        }
        if(!participants.contains(user)) {
            participants.add(user);
        }
        trip.setParticipantsList(participants);
        restTemplate.postForObject(SERVER_ADDRESS + "/addTrip?user=" + user, trip, Trip.class);

        return "redirect:trips";
    }

    @GetMapping("/")
    public String getStartPage(){
        return "index";
    }

    @PostMapping("/")
    public String selectUser(@RequestParam(value = "user") String user){
        this.user = user;
        return "redirect:/trips";
    }

    private Trip getTripForName(String tripName){
        for(Trip trip : trips){
            if(trip.getName().equals(tripName)){
                return trip;
            }
        }
        return null;
    }

    @GetMapping("/trips/{tripName}")
    public String getTrip(@PathVariable String tripName, Model model){
        Trip trip = getTripForName(tripName);
        model.addAttribute("trip", trip);
        return "trip";
    }

}

package agh.trips;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Controller
public class TripsController {

    private String user;
    private List<Trip> trips;

    @Value("${server}")
    private String SERVER_ADDRESS;
    //private final String SERVER_ADDRESS = "http://localhost:8080/server";

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/trips")
    public String getTrips(Model model){
        model.addAttribute("trip", new Trip());

        //Trip[] tripsArray = restTemplate.getForObject(SERVER_ADDRESS + "/getTrips?user=" + user, Trip[].class)
        Trip[] tripsArray = restTemplate.getForObject(SERVER_ADDRESS + "/api/user/" + user + "/trips", Trip[].class);

        if(tripsArray != null) {
            trips = Arrays.asList(tripsArray);
        }
        else{
            trips = new LinkedList<>();
        }

        model.addAttribute("trips", trips);
        model.addAttribute("user", user);

        return "trips";
    }

    @PostMapping("/trips")
    public String addTrip(@ModelAttribute Trip trip){
        List<String> participants = trip.getParticipantsList();
        trip.setOwner(user);

        //restTemplate.postForObject(SERVER_ADDRESS + "/addTrip?user=" + user, trip, Trip.class);
        restTemplate.postForObject(SERVER_ADDRESS + "/api/user/" + user + "/create-trip", trip, Trip.class);

        return "redirect:trips";
    }

    @GetMapping("/login")
    public String getStartPage(){
        return "index";
    }

    @PostMapping("/login")
    public String selectUser(@RequestParam(value = "user") String user, @RequestParam(value = "password") String password){
        JSONObject request = new JSONObject();
        request.put("username", user);
        request.put("password", password);
        try {
            restTemplate.postForObject(SERVER_ADDRESS + "/api/user/login", request, String.class);
            this.user = user;
        } catch (HttpClientErrorException e){
            return "redirect:/login?error";
        }

        return "redirect:/trips";
    }

    @PostMapping("/create")
    public String createUser(@RequestParam(value = "user") String user, @RequestParam(value = "password") String password){
        JSONObject request = new JSONObject();
        request.put("username", user);
        request.put("password", password);
        String response = restTemplate.postForObject(SERVER_ADDRESS +  "/api/user/create", request, String.class);
        //restTemplate.postForObject("/api/user/create", request, String.class);
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

    private Trip getTripForId(int tripId){
        for(Trip trip : trips){
            if(trip.getId() == tripId){
                return trip;
            }
        }
        return null;
    }

    @GetMapping("/trips/{tripId}")
    public String getTrip(@PathVariable int tripId, Model model){
        Trip trip = getTripForId(tripId);
        model.addAttribute("trip", trip);
        model.addAttribute("user", user);
        return "trip";
    }

    @GetMapping("/trips/{tripId}/edit")
    public String getTripEditing(@PathVariable int tripId, Model model){
        Trip trip = getTripForId(tripId);
        model.addAttribute("trip", trip);
        model.addAttribute("user", user);
        return "edit-trip";
    }

    @PostMapping("/trips/{tripId}/edit")
    public String editTrip(@PathVariable int tripId, @ModelAttribute Trip trip){
        restTemplate.put(SERVER_ADDRESS +  "/api/user/" + user + "/trip/" + tripId + "/update", trip);
        //restTemplate.postForObject(SERVER_ADDRESS + "/editTrip?user=" + user + "&tripName=" + tripName, trip, Trip.class);
        return "redirect:/trips";
    }

    @PostMapping("/trips/{tripId}/delete")
    public String deleteTrip(@PathVariable int tripId, @ModelAttribute Trip trip){
        restTemplate.delete(SERVER_ADDRESS +  "/api/user/" + user + "/trip/" + tripId + "/delete", trip);
        //restTemplate.delete(SERVER_ADDRESS + "/api/user/" + user + "/trip/" + tripName + "/delete");
        return "redirect:/trips";
    }

}

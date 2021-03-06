package agh.trips.controller;

import agh.trips.model.Trip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.json.simple.JSONObject;
import org.thymeleaf.util.StringUtils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Controller
public class TripsController {

    private String user;
    private List<Trip> trips;
    private String message;

    @Value("${remote_server}")
    private String SERVER_ADDRESS;

    private RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/trips")
    public String getTrips(Model model){
        model.addAttribute("trip", new Trip());
        Trip[] tripsArray = restTemplate.getForObject(SERVER_ADDRESS + "/api/user/" + user + "/all-trips", Trip[].class);

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
        trip.setOwner(user);
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
        Trip oldTrip = getTripForId(tripId);
        if(oldTrip == null) {
            return "error";
        }
        Trip trip = new Trip(oldTrip);

        model.addAttribute("trip", trip);
        model.addAttribute("user", user);
        return "edit-trip";
    }

    public JSONObject generateJsonForDifferences(Trip oldTrip, Trip trip){
        JSONObject request = new JSONObject();
        Format format = new SimpleDateFormat("yyyy-MM-dd");

        if(!oldTrip.getName().equals(trip.getName())){
            request.put("trip_name", trip.getName());
        }
        if(!oldTrip.getFrom().equals(trip.getFrom())){
            request.put("date_from", format.format(trip.getFrom()));
        }
        if(!oldTrip.getTo().equals(trip.getTo())){
            request.put("date_to", format.format(trip.getTo()));
        }
        if(!oldTrip.getParticipantsList().equals(trip.getParticipantsList())){
            request.put("participants", trip.getParticipantsList());
        }

        if(!request.isEmpty()){
            request.put("trip_id", trip.getId());
            return request;
        }
        else{
            return null;
        }
    }

    @PostMapping("/trips/{tripId}/edit")
    public String editTrip(@PathVariable int tripId, @ModelAttribute Trip trip){
        Trip oldTrip = getTripForId(tripId);
        JSONObject json = generateJsonForDifferences(oldTrip, trip);
        if(json != null) {
            restTemplate.put(SERVER_ADDRESS + "/api/user/" + user + "/trip/" + tripId + "/update", json);
        }
        return "redirect:/trips";
    }

    @PostMapping("/trips/{tripId}/delete")
    public String deleteTrip(@PathVariable int tripId, @ModelAttribute Trip trip){
        restTemplate.delete(SERVER_ADDRESS +  "/api/user/" + user + "/trip/" + tripId + "/delete", trip);
        return "redirect:/trips";
    }

    @ExceptionHandler
    public String handleException(Model model, Exception exception){
        model.addAttribute("exception", exception);

        if(exception.getClass().equals(ResourceAccessException.class)){
            String message = "There is a problem with a connection to the server";
            model.addAttribute("message", message);
        }
        exception.printStackTrace();
        return "error";
    }

    @Autowired
    private Environment environment;

    @EventListener({ApplicationReadyEvent.class})
    public void handleApplicationReadyEvent(){
        String port = environment.getProperty("local.server.port");
        if(port != null){
            String horizontalLine = StringUtils.repeat('-', 70);
            System.out.println(horizontalLine);
            System.out.println("You can open this application in the browser: http://localhost:" + port);
            System.out.println(horizontalLine);
        }
    }
}

package agh.trips;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;
import org.thymeleaf.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Trip implements Serializable {

    @JsonProperty("trip_id")
    private int id;

    @JsonProperty("trip_name")
    private String name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("date_from")
    private Date from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("date_to")
    private Date to;

    @JsonProperty("owner")
    private String owner;

    private String participants;

    private List<String> participantsList;

    public Trip(){}

    public Trip(String name, Date from, Date to, String participants) {
        this.name = name;
        this.from = from;
        this.to = to;
        setParticipants(participants);
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
        if(participants.equals("")){
            this.participantsList = new ArrayList<>();
        }
        else {
            this.participantsList = new ArrayList<>(Arrays.asList(participants.split("\\s*,\\s*")));
        }
    }

    public List<String> getParticipantsList() {
        return participantsList;
    }

    public void setParticipantsList(List<String> participantsList) {
        this.participantsList = participantsList;
        if(participantsList != null) {
            this.participants = StringUtils.join(participantsList, ", ");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}

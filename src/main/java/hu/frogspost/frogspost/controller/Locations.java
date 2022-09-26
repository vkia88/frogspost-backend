package hu.frogspost.frogspost.controller;

import hu.frogspost.frogspost.enums.LocationTypes;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class Locations {

    @GetMapping("/locations")
    public LocationTypes[] index() {
        return LocationTypes.values();
    }

}
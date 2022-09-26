package hu.frogspost.frogspost.controller.Locations;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class Location {

    @GetMapping("/locations")
    public LocationTypes[] index() {
        return LocationTypes.values();
    }

}
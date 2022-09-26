package hu.frogspost.frogspost.controller.Locations;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Location {

    @GetMapping("/locations")
    public LocationTypes[] index() {
        return LocationTypes.values();
    }

}
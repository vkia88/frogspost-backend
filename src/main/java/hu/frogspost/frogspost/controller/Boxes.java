package hu.frogspost.frogspost.controller;

import hu.frogspost.frogspost.repository.BoxRepository;
import hu.frogspost.frogspost.repository.PublicBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/boxes")
@CrossOrigin(origins = "http://localhost:3000")
public class Boxes {
    @Autowired
    private BoxRepository boxRepository;

    @GetMapping("/location/{locationId}")
    public ResponseEntity<Map<String, Object>> getBoxesAtLocation(@PathVariable Integer locationId) {
        Map<String, Object> response = new HashMap<>();

        List<PublicBox> boxes = boxRepository.getByLocationId(locationId);

        response.put("boxes", boxes);

        return ResponseEntity.ok(response);
    }
}

package hu.frogspost.frogspost.controller;

import hu.frogspost.frogspost.enums.BoxSizes;
import hu.frogspost.frogspost.model.Box;
import hu.frogspost.frogspost.model.Parcel;
import hu.frogspost.frogspost.model.ParcelResponse;
import hu.frogspost.frogspost.repository.BoxRepository;
import hu.frogspost.frogspost.repository.ParcelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/parcels")
@CrossOrigin(origins = "http://localhost:3000")
public class Parcels {
    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private BoxRepository boxRepository;

    @PostMapping("/location/{locationId}")
    public ResponseEntity<Object> deliverParcel(@PathVariable Integer locationId, @RequestBody Parcel parcel) {
        if (parcel.getSize() == null) {
            return ResponseEntity.badRequest().body("Please select size");
        }

        Box box = getBoxForParcel(locationId, parcel);

        if (box == null) {
            return ResponseEntity.badRequest().body("No empty space");
        }

        if (parcel.getName() == null || parcel.getName().isBlank()) {
            UUID uuid = UUID.randomUUID();
            parcel.setName(uuid.toString());
        }

        String password;

        if (parcel.getPassword() == null || parcel.getPassword().isBlank()) {
            // oneliner origin: https://stackoverflow.com/a/53349505/5924640
            password = new Random()
                .ints(4, 97, 122)
                .mapToObj(i -> String.valueOf((char)i))
                .collect(Collectors.joining());
        } else {
            password = parcel.getPassword();
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10, new SecureRandom());
        String encodedPassword = encoder.encode(password);

        // Saving the parcel with encoded password.
        parcel.setPassword(encodedPassword);

        Parcel savedParcel = parcelRepository.save(parcel);

        try {
            box.setParcel(savedParcel);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Parcel already exist");
        }
        boxRepository.save(box);

        // Returning the original password once for the user to save.
        ParcelResponse parcelResponse = new ParcelResponse(savedParcel.getName(), password);

        return ResponseEntity.ok(parcelResponse);
    }

    private Box getFreeBoxByType(Integer locationId, List<Box> boxes, BoxSizes boxSize) {
        return boxes
                .stream()
                .filter(box -> box.getSize() == boxSize && box.getParcel() == null)
                .findFirst()
                .orElse(null);
    }

    private Box getBoxForParcel(Integer locationId, Parcel parcel) {
        List<Box> boxes = boxRepository.getByLocationId(locationId);
        Box foundBox = null;

        switch (parcel.getSize()) {
            case A:
                foundBox = getFreeBoxByType(locationId, boxes, BoxSizes.A);
                // no-break
            case B:
                if (foundBox == null) {
                    foundBox = getFreeBoxByType(locationId, boxes, BoxSizes.B);
                }
                // no-break
            case C:
                if (foundBox == null) {
                    foundBox = getFreeBoxByType(locationId, boxes, BoxSizes.C);
                }
                break;
        }

        return foundBox;
    }
}

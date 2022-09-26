package hu.frogspost.frogspost.controller;

import hu.frogspost.frogspost.enums.BoxSizes;
import hu.frogspost.frogspost.model.Box;
import hu.frogspost.frogspost.model.Parcel;
import hu.frogspost.frogspost.model.ParcelRequest;
import hu.frogspost.frogspost.model.ParcelResponse;
import hu.frogspost.frogspost.repository.BoxRepository;
import hu.frogspost.frogspost.repository.ParcelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/parcels")
@CrossOrigin(origins = "http://localhost:3000")
public class Parcels {
    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private BoxRepository boxRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10, new SecureRandom());

    @PostMapping("location/{locationId}/get/")
    public ResponseEntity<Object> pickupParcel(@PathVariable Integer locationId, @RequestBody ParcelRequest parcel) {
        Optional<Parcel> optionalParcel = parcelRepository.getByNameIgnoreCase(parcel.getName());

        if (optionalParcel.isEmpty()) {
            // same error message when only password is false, so username is not leaked.
            return ResponseEntity.badRequest().body("Invalid name or password");
        }

        Parcel foundParcel = optionalParcel.get();

        if (!encoder.matches(parcel.getPassword(), foundParcel.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid name or password");
        }

        if (!Objects.equals(foundParcel.getBox().getLocationId(), locationId)) {
            return ResponseEntity.badRequest().body("Your parcel is at another location");
        }

        if (LocalDate.now().isAfter(foundParcel.getUntil())) {
            return ResponseEntity.badRequest().body("You missed the deadline");
        }

        String removedName = foundParcel.getName();

        parcelRepository.delete(foundParcel);

        return ResponseEntity.ok("Successfully picked up parcel: " + removedName);
    }

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

        String encodedPassword = encoder.encode(password);

        // Saving the parcel with encoded password.
        parcel.setPassword(encodedPassword);

        Parcel savedParcel;

        try {
            savedParcel = parcelRepository.save(parcel);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Parcel name already exist");
        }

        box.setParcel(savedParcel);
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

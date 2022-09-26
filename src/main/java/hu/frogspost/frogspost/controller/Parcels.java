package hu.frogspost.frogspost.controller;

import hu.frogspost.frogspost.enums.BoxSizes;
import hu.frogspost.frogspost.model.Box;
import hu.frogspost.frogspost.model.Parcel;
import hu.frogspost.frogspost.repository.BoxRepository;
import hu.frogspost.frogspost.repository.ParcelRepository;
import hu.frogspost.frogspost.repository.PublicBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
        Box box = getBoxForParcel(locationId, parcel);

        if (box == null) {
            return ResponseEntity.badRequest().build();
        }

        if (parcel.getName().isEmpty()) {
            UUID uuid = UUID.randomUUID();
            parcel.setName(uuid.toString());

        }
        Parcel savedParcel = parcelRepository.save(parcel);

        box.setParcel(savedParcel);
        boxRepository.save(box);

        return ResponseEntity.ok(savedParcel);
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

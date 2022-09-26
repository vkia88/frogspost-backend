package hu.frogspost.frogspost.defaultData;

import hu.frogspost.frogspost.enums.BoxSizes;
import hu.frogspost.frogspost.enums.LocationTypes;
import hu.frogspost.frogspost.model.Box;
import hu.frogspost.frogspost.repository.BoxRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class defaultData {
    @Autowired
    private final BoxRepository boxRepository;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        LocationTypes location = LocationTypes.Budapest;

        boxRepository.save(createBox(location, BoxSizes.A));
        boxRepository.save(createBox(location, BoxSizes.B));
        boxRepository.save(createBox(location, BoxSizes.C));

        location = LocationTypes.Szeged;

        for (int i = 0; i < 2; ++i) {
            boxRepository.save(createBox(location, BoxSizes.A));
            boxRepository.save(createBox(location, BoxSizes.B));
            boxRepository.save(createBox(location, BoxSizes.C));
        }

        location = LocationTypes.Debrecen;

        for (int i = 0; i < 3; ++i) {
            boxRepository.save(createBox(location, BoxSizes.A));
            boxRepository.save(createBox(location, BoxSizes.B));
            boxRepository.save(createBox(location, BoxSizes.C));
        }
    }

    private Box createBox(LocationTypes location, BoxSizes boxSize) {
        Box box = new Box();
        box.setLocationId(location.getId());
        box.setSize(boxSize);

        return box;
    }
}

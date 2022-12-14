package hu.frogspost.frogspost.repository;

import hu.frogspost.frogspost.model.Box;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoxRepository extends CrudRepository<Box, Integer> {
    List<PublicBox> getPublicByLocationId(Integer locationId);
    List<Box> getByLocationId(Integer locationId);
}

package hu.frogspost.frogspost.repository;

import hu.frogspost.frogspost.model.Box;
import hu.frogspost.frogspost.model.Parcel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParcelRepository extends CrudRepository<Parcel, Integer> {

}

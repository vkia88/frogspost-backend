package hu.frogspost.frogspost.controller.Locations;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@JsonFormat(shape=JsonFormat.Shape.OBJECT)
@AllArgsConstructor
@Getter
public enum LocationTypes implements Serializable {
    Budapest("Budapest", 1),
    Szeged("Szeged", 2),
    Debrecen("Debrecen", 3);

    private final String name;
    private final int id;
}

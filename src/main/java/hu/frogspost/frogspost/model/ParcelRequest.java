package hu.frogspost.frogspost.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class ParcelRequest {
    String name;
    String password;
}

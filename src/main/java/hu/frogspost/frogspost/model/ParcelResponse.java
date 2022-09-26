package hu.frogspost.frogspost.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
@Getter
public class ParcelResponse {
    String name;
    String password;
}

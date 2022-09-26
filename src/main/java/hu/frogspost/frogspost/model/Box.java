package hu.frogspost.frogspost.model;

import com.sun.istack.NotNull;
import hu.frogspost.frogspost.enums.BoxSizes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Box {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotNull
    private Integer locationId;

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull
    private BoxSizes size;

    @Column
    private Integer parcelId;
}

package hu.frogspost.frogspost.model;

import com.sun.istack.NotNull;
import hu.frogspost.frogspost.enums.BoxSizes;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotNull
    private String name;

    @Column
    @NotNull
    private String password;

    @Column
    @Timestamp
    private LocalDate until;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "box_id", referencedColumnName = "id")
    private Box box;
}

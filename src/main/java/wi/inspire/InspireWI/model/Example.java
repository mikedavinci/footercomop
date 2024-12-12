package wi.inspire.InspireWI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

@Data // Generates getters, setters, equals, hashCode, and toString
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor // Generates an all-args constructor
@Entity
public class Example {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String email;

}

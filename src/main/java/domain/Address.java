package domain;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.SoftDelete;

@Setter
@Getter
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
@ToString
@SoftDelete
public class Address {
    private String street;
    private String city;
    private String state;
    private String postalCode;

    @Builder(builderMethodName = "addressBuilder")
    public Address(String street, String city, String state, String postalCode) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }
}


package domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SoftDelete;

import java.util.Date;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@SoftDelete
public class Employee extends Person{

    @NotNull
    @Column(name = "payslip")
    Double salary = null;

    @Builder(builderMethodName = "EmplouyeeBuilder")
    public Employee(Integer integer, String firstName, String lastName, Date birthDate, String username, String password, Gender gender, Address address, Double salary) {
        super ( integer, firstName, lastName, birthDate, username, password, gender, address );
        this.salary = salary;
    }
}

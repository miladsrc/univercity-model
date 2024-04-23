package domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SoftDelete;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@ToString
@Entity
@SoftDelete
public class Professor extends Person {

    @OneToMany(mappedBy = "professor",cascade = CascadeType.ALL)
    List<Course> courses = new ArrayList<> ();

    @NotNull(message = "professor type must be filled")
    @Enumerated
    ProfessorType professorType;

    @Column
    Integer currentTerm;



    @Builder(builderMethodName = "professorBuilder")
    public Professor(Integer integer, String firstName, String lastName, Date birthDate, String username, String password, Gender gender, Address address, List<Course> courses, ProfessorType professorType, Integer currentTerm) {
        super ( integer, firstName, lastName, birthDate, username, password, gender, address );
        this.courses = courses;
        this.professorType = professorType;
        this.currentTerm = currentTerm;
    }

    public Professor(){}
}

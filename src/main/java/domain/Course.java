package domain;

import base.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.validator.constraints.Range;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Entity
@SoftDelete

public class Course extends BaseEntity<Integer> {

    @NotNull(message = "name can't be null'")
    @Column
    String name;

    @Range(min = 0, max = 30, message = "Capacity must be between 0 and 30.")
    @Column
    int capacity = 1;

    @NotNull(message = "Course type cannot be null.")
    @Enumerated(EnumType.ORDINAL)
    CourseType courseType;

    @NotNull(message = "Professor cannot be null.")
    @ManyToOne(cascade = CascadeType.ALL)
    Professor professor;

    @Positive(message = "Term must be a positive integer.")
    @Column
    int term;

    @NotNull
    @Range(min = 1, max =4)
    @Column(name = "courese_unit")
    int unit;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL)
    List<TermInformation> termInformationList = new ArrayList<> ();

    @Builder(builderMethodName = "courseBuilder")
    public Course(Integer integer, String name, int capacity, CourseType courseType, Professor professor, int term, List<TermInformation> termInformationList) {
        super ( integer );
        this.name = name;
        this.capacity = capacity;
        this.courseType = courseType;
        this.professor = professor;
        this.term = term;
        this.termInformationList = termInformationList;
    }



    public Course(String name, int capacity, CourseType courseType, Professor professor, int term, List<TermInformation> termInformationList) {
        this.name = name;
        this.capacity = capacity;
        this.courseType = courseType;
        this.professor = professor;
        this.term = term;
        this.termInformationList = termInformationList;
    }

    public Course(int term) {
        this.term = term;
    }
}

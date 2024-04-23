package domain;

import base.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SoftDelete;


@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@SoftDelete
public class TermInformation extends BaseEntity<Integer> {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id")
    Student student;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "course_id")
    Course course;

    @Column
    @Size(min = 0, max = 20,message = "please enter grade correctly ( 0 to 100 ) !")
    int grade;

    @Builder(builderMethodName = "TermInfoBuilder")
    public TermInformation(Integer integer, Student student, Course course, int grade) {
        super ( integer );
        this.student = student;
        this.course = course;
        this.grade = grade;
    }

    public TermInformation(Student student, Course course, int grade) {
        this.student = student;
        this.course = course;
        this.grade = grade;
    }
}

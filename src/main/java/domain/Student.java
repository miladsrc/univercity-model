package domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Remove;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@SoftDelete
public class Student extends Person implements Serializable {


    @Column
    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    List<TermInformation> termInformationList = new ArrayList<> ();


    @Builder(builderMethodName = "studentBuilder")
    public Student(Integer integer, String firstName, String lastName, Date birthDate, String username, String password, Gender gender, Address address, List<TermInformation> termInformationList) {
        super ( integer, firstName, lastName, birthDate, username, password, gender, address);
        this.termInformationList = termInformationList;
    }


    @Column(name = "current_term")
    Integer currentTerm;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer ().getPersistentClass () : o.getClass ();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer ().getPersistentClass () : this.getClass ();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Student student = (Student) o;
        return getId () != null && Objects.equals ( getId (), student.getId () );
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer ().getPersistentClass ().hashCode () : getClass ().hashCode ();
    }
}

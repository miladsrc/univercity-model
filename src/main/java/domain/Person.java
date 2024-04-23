package domain;

import base.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.proxy.HibernateProxy;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@MappedSuperclass
@SoftDelete
public class Person extends BaseEntity<Integer> {

    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String BIRTH_DATE = "birth_date";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String GENDER = "gender";
    public static final String ADDRESS = "address";
    public static final String IS_ACTIVE = "is_active";

    @Override
    public Integer getId() {
        return super.getId ();
    }

    @NotNull(message = "First name cannot be null")
    @Column(name = Person.FIRST_NAME)
    String firstName;

    @NotNull(message = "Last name cannot be null")
    @Column(name = Person.LAST_NAME)
    String lastName;

    @NotNull(message = "Birth date cannot be null")
    @Temporal(TemporalType.DATE)
    @Column(name = Person.BIRTH_DATE)
    Date birthDate;

    @NotNull(message = "Username cannot be null")
    @Column(name = Person.USERNAME, unique = true)
    String username;

    @NotNull(message = "Password cannot be null")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$",message = "Password should contain c/s letters and numbers and not be less than 8!")
    @Column(name = Person.PASSWORD)
    String password;

    @NotNull(message = "Gender cannot be null")
    @Column(name = Person.GENDER)
    @Enumerated(EnumType.ORDINAL)
    Gender gender;

    @Column(name = Person.ADDRESS)
    @Embedded
    Address address;



    @Builder(builderMethodName = "perosnBuilder")
    public Person(Integer integer, String firstName, String lastName, Date birthDate, String username, String password, Gender gender, Address address) {
        super ( integer );
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.address = address;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer ().getPersistentClass () : o.getClass ();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer ().getPersistentClass () : this.getClass ();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Person person = (Person) o;
        return getId () != null && Objects.equals ( getId (), person.getId () );
    }

    @Override
    public int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer ().getPersistentClass ().hashCode () : getClass ().hashCode ();
    }
}

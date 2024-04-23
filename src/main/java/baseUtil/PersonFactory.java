package baseUtil;

import domain.*;

public class PersonFactory {
    public Person createPerson(PersonType personType){
        switch(personType){
            case EMPLOYEE -> {
                return new Employee();
            }
            case PROFESSOR -> {
                return new Professor ();
            }
            case STUDENT -> {
                return new Student ();
            }
        }
        return new Person();
    }

}

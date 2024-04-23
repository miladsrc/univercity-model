import baseUtil.ApplicationContext;
import domain.*;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    private final Scanner scanner = new Scanner(System.in);
    static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    static Validator validator = validatorFactory.getValidator();


    public static void main(String[] args) {
//        Course course = new Course();
//        TermInformation t = new TermInformation();
//        Student studet = new Professor ("reza", "razavian",new Date (),
//                "ed84h7s30daern", "123", Gender.MALE,
//                new Address ("12","12","jason","java"),true  );



//        Student student = Student.StudentBuilder ()
//                .firstName ( "javad" )
//                .build ();

//        ApplicationContext.getStudentService ().saveOrUpdate ( student );



//        StudentService studentService = ApplicationContext.getStudentService ();
//        boolean s = studentService.existsByUsernameAndPassword ( "1234","1234" );
//        if (s){
//            System.out.println ("user exists");
//        }

//        Menu menu = new Menu();
//        menu.menu ();


//        List<Student> byId = ApplicationContext.getStudentService ().findAll (  );
//
//        for (Student student : studetnList) {
//            System.out.println (student.getId () + ": " + student);
//        }
         Professor professor = Professor.professorBuilder ()
                .firstName ( "miald" )
                .lastName ( "barani" )
                .username ( "123" )
                .password ( "Sf" )
                 .gender ( Gender.MALE )
                .birthDate ( new Date () )
                .address ( new Address() )
                .isActive ( true )
                .professorType ( ProfessorType.FACULTY_MEMBER ).build ();

        ApplicationContext.getProfessorService ().saveOrUpdate ( professor );

//
//        System.out.println (professor.toString ());

//        System.out.println ();
//        Student person = studentService.findById ( 1 );
//         studentService.deleteById (302);
//        System.out.println (student1.toString ());


//        SessionFactory sessionFactory = SessionFactorySingleton.getInstance ();
//        Session session = sessionFactory.openSession ();
//        Transaction transaction = session.beginTransaction ();
//
//        session.persist ( student );
//
//        transaction.commit ();
//        session.close ();
    }

//    public static  <T> void validateAndSave(T entity) {
//        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
//        Validator validator = validatorFactory.getValidator();
//
//        Set<ConstraintViolation<T>> violations = validator.validate(entity);
//
//        if (violations.isEmpty()) {
//            ApplicationContext.getStudentService().saveOrUpdate((Student) entity);
//            System.out.println("Valid user data provided.");
//        } else {
//            System.out.println("Invalid user data found:");
//            for (ConstraintViolation<T> violation : violations) {
//                System.out.println(violation.getMessage());
//            }
//        }
//    }

}

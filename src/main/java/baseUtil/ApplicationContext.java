package baseUtil;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import repository.*;
import repository.Impl.*;
import service.*;
import service.Impl.*;

public class ApplicationContext {


    private ApplicationContext(){
    }

    //SESSION FACTORY
    private static final SessionFactory SESSION_FACTORY;
    private static final Session SESSION;

    //REPOSITORY
    private static final ProfessorRepository PROFESSOR_REPOSITORY;
    private static final EmployeeRepository EMPLOYEE_REPOSITORY;
    private static final StudentRepository STUDENT_REPOSITORY;
    private static final TermInfoRepository TERM_INFO_REPOSITORY;
    private static final CourseRepository COURSE_REPOSITORY;
    private static final PersonRepository PERSON_REPOSITORY;


    //SERVICE
    private static final ProfessorService PROFESSOR_SERVICE;
    private static final StudentService STUDENT_SERVICE;
    private static final EmployeeService EMPLOYEE_SERVICE;
    private static final TermInfoService TERM_INFO_SERVICE;
    private static final CourseService COURSE_SERVICE;
    private static final PersonService PERSON_SERVICE;

    static {
        SESSION_FACTORY = SessionFactorySingleton.getInstance ();
        SESSION = SESSION_FACTORY.openSession ();
        //REPOSITORY
        PROFESSOR_REPOSITORY = new ProfessorRepositroyImpl ( SESSION );
        STUDENT_REPOSITORY = new StudentRepositoryImpl ( SESSION );
        EMPLOYEE_REPOSITORY = new EmployeeRepositoryImpl ( SESSION );
        TERM_INFO_REPOSITORY = new TerminfoRepositoryImpl ( SESSION );
        COURSE_REPOSITORY = new CourseReprositoryImpl ( SESSION );
        PERSON_REPOSITORY = new PersonRepositoryImpl ( SESSION );
        //SERVICE
        PROFESSOR_SERVICE = new ProfessorServiceImpl ( PROFESSOR_REPOSITORY );
        STUDENT_SERVICE = new StudentServiceImpl ( STUDENT_REPOSITORY );
        EMPLOYEE_SERVICE = new EmployeeServiceImpl ( EMPLOYEE_REPOSITORY );
        TERM_INFO_SERVICE = new TermInfoServiceImpl ( TERM_INFO_REPOSITORY );
        COURSE_SERVICE = new CourceServiceImpl ( COURSE_REPOSITORY );
        PERSON_SERVICE = new PersonServiceImpl ( PERSON_REPOSITORY );
    }

    //GETTER
    public static ProfessorService getProfessorService() {
        return PROFESSOR_SERVICE;
    }
    public static StudentService getStudentService() {
        return STUDENT_SERVICE;
    }
    public static EmployeeService getEmployeeService() {
        return EMPLOYEE_SERVICE;
    }
    public static TermInfoService getTermInfoService() { return TERM_INFO_SERVICE;}
    public static CourseService getCourseService() { return COURSE_SERVICE;}
    public static PersonService getPersonService() { return PERSON_SERVICE;}
}

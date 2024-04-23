package menu;

import baseUtil.ApplicationContext;
import domain.*;
import service.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class Menu {

    static final Scanner scanner = new Scanner ( System.in );
    static ZoneId defaultZoneId = ZoneId.systemDefault ();


    Optional<Employee> employee = null;
    Optional<Student> student = null;
    Optional<Professor> professor = null;


    StudentService studentService = ApplicationContext.getStudentService ();
    ProfessorService professorService = ApplicationContext.getProfessorService ();
    EmployeeService employeeService = ApplicationContext.getEmployeeService ();
    TermInfoService termInfoService = ApplicationContext.getTermInfoService ();
    CourseService courseService = ApplicationContext.getCourseService ();
    PersonService personService = ApplicationContext.getPersonService ();


    static String username = null;
    static String password = null;
    static int counter = 0;


    //MENU START ----------------------------------------------------------------------------------------------

    public void menu() {

        if (counter == 0) {
            try {
                System.out.println ( "Please enter your username:" );
                username = scanner.next ();
                System.out.println ( "Please enter your password:" );
                password = scanner.next ();
                counter++;
            } catch (Exception e) {
                System.out.println ( "An error occurred: " + e.getMessage () );
            }
        } else {
            while (true) {

                employee = ApplicationContext.getEmployeeService ().existsByUsernameAndPassword ( username, password );
                professor = ApplicationContext.getProfessorService ().existsByUsernameAndPassword ( username, password );
                student = ApplicationContext.getStudentService ().existsByUsernameAndPassword ( username, password );
                if (employee.isPresent ()) {
                    employeeMenu ();
                    break;
                } else if (professor.isPresent ()) {
                    professorMenu ();
                    break;
                } else if (student.isPresent ()) {
                    studentMenu ();
                    break;
                } else {
                    System.out.println ( "Choice not found. Please try again!" );
                }
            }
        }
    }


    //MAIN REFERENCE MENU



    //PROFESSION MENU----------------------------------------------------------------
    public void professorMenu() {
        Professor professor1 = professorService.findById(professor.get().getId());
        System.out.println("Welcome " + professor1.getFirstName() + "! Here are your options:");
        while (true) {
            System.out.println("1. View your profile");
            System.out.println("2. Record student grades");
            System.out.println("3. View salary slip");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    viewProfessorProfile(professor1);
                    break;
                case 2:
                    recordStudentGrades(professor1);
                    break;
                case 3:
                    viewSalarySlip(professor1);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    //1----------------------------------------------------------------
    private void viewProfessorProfile(Professor professor) {
        System.out.println("Professor ID: " + professor.getId());
        System.out.println("First Name: " + professor.getFirstName());
        System.out.println("Last Name: " + professor.getLastName());
        System.out.println("Birth Date: " + professor.getBirthDate());
        System.out.println("Username: " + professor.getUsername());
        System.out.println("Password: " + professor.getPassword());
        System.out.println("Gender: " + professor.getGender());
        System.out.println("Address: " + professor.getAddress());
    }

    //2----------------------------------------------------------------
    private void recordStudentGrades(Professor professor) {
        List<Student> students = studentService.findAll();
        for (Student student : students) {
            System.out.println (student.getId ()+" : "+student.getFirstName()+" "+student.getLastName());
        }

        System.out.println("Enter the student ID to record the grade (enter 0 to finish):");
        int studentId;
        while ((studentId = scanner.nextInt()) != 0) {
            Student student = findStudentById(students, studentId);
            if (student == null) {
                System.out.println("Student not found. Please enter a valid student ID.");
                continue;
            }

            System.out.println("Enter the grade for student " + student.getFirstName() + " " + student.getLastName() + ":");
            int grade = scanner.nextInt ();

            Student student1 = studentService.findById ( studentId );
            TermInformation studentGrade = new TermInformation ();
            studentGrade.setStudent(student);
            studentGrade.setGrade(grade);

            for (Course course: professor.getCourses ()
                 ) {
                System.out.println (course.getId ()+" " + course.getName ());
            }
            System.out.println ("Please choose between courses :");
            int index = scanner.nextInt ();
            studentGrade.setCourse ( professor.getCourses ().get ( index ) );

            // Save the student grade
            ApplicationContext.getTermInfoService ().saveOrUpdate ( studentGrade );
            System.out.println("Grade recorded successfully.");
        }
    }

    private Student findStudentById(List<Student> students, int studentId) {
        for (Student student : students) {
            if (student.getId() == studentId) {
                return student;
            }
        }
        return null;
    }

    //3----------------------------------------------------------------
    private void viewSalarySlip(Professor professor) {
        final long fixedSalary = 5_000_000L;
        final long salaryPerUnit = 1_000_000L;

        int unitsTaught = getUnitsTaughtByProfessor(professor);

        long totalSalary;
        if (professor.getProfessorType() == ProfessorType.FACULTY_MEMBER) {
            totalSalary = fixedSalary + (unitsTaught * salaryPerUnit);
        } else {
            totalSalary = unitsTaught * salaryPerUnit;
        }

        System.out.println("Professor Name: " + professor.getFirstName() + " " + professor.getLastName());
        System.out.println("Total Units Taught: " + unitsTaught);
        System.out.println("Total Salary: " + totalSalary);
    }

    private int getUnitsTaughtByProfessor(Professor professor) {


        int totalUnits = 0;
        if(professor.getCourses ()==null||professor.getCourses().isEmpty ()){
            return 0;
        }

//        int totalUnits = professor.getCourses().stream()
//                .filter(course -> this.term = course.getTerm())
//                .mapToInt(Course::getUnit)
//                .sum();


        for (Course course : professor.getCourses()) {
            if (course.getTerm() == this.term) {
                totalUnits += course.getUnit();
            }
        }

        return totalUnits;

    }




    //STUDENT MENU----------------------------------------------------------------
    public void studentMenu() {
        String firstName = student.get ().getFirstName ();
        System.out.println ( "Welcome " + firstName + "! Here are your options:" );
        while (true) {
            System.out.println ( "1. View your profile" );
            System.out.println ( "2. View list of courses available" );
            System.out.println ( "3. Choosing courses for midterm" );
            System.out.println ( "4. View courses with points received" );
            System.out.println ( "5. Menu" );
            System.out.println ( "6. Exit" );

            int choice = scanner.nextInt ();
            switch (choice) {
                case 1:
                    showStudentProfile ();
                    break;
                case 2:
                    showAvailableCourses ();
                    break;
                case 3:
                    courseSelection ();
                    break;
                case 4:
                    viewCoursesWithPoints ();
                    break;
                case 5:
                    menu ();
                    break;
                case 6:
                    System.out.println ( "Exiting..." );
                    return;
                default:
                    System.out.println ( "Invalid option. Please try again." );
            }
        }
    }

    //1----------------------------------------------------------------
    private void showStudentProfile() {
        System.out.println ( "Student ID: " + student.get ().getId () );
        System.out.println ( "First Name: " + student.get ().getFirstName () );
        System.out.println ( "Last Name: " + student.get ().getLastName () );
        System.out.println ( "Birth Date: " + student.get ().getBirthDate () );
        System.out.println ( "Username: " + username );
        System.out.println ( "Password: " + password );
        System.out.println ( "Gender: " + student.get ().getGender () );
        System.out.println ( "Address: " + student.get ().getAddress ().toString () );
        System.out.println ( "Term Information (sorted by Course ID):" );
        student.get ().getTermInformationList ().sort ( Comparator.comparingInt ( termInfo -> termInfo.getCourse ().getId () ) );
        for (TermInformation termInfo : student.get ().getTermInformationList ()) {
            System.out.println ( "  Course ID: " + termInfo.getCourse ().getId () + ", Grade: " + termInfo.getGrade () );
        }
    }

    //2---------------------------------------------------------------
    public void showAvailableCourses() {
        List<Course> courses = courseService.findAll ();
        if (courses.isEmpty ()) {
            System.out.println ( "No courses available!" );
        } else {
            System.out.println ( "Courses available :" );
            for (Course course : courses) {
                System.out.println ( "Course : " + course.getName () + ", Capcity: " + course.getCapacity () + ", Type: " + course.getCourseType () );
            }
        }
    }


    //3----------------------------------------------------------------

    public void courseSelection() {
        Student student1 = studentService.findById ( student.get ().getId () );
        double lastTermAverage = calculateLastTermAverage ( student );
        int maxUnits = lastTermAverage >= 18 ? 24 : 20;

        List<Course> selectedCourses = new ArrayList<> ();
        List<Course> passedCourses = getPassedCourses ( student1 );
        List<Course> currentTermCourses = getCurrentTermCourses ( student1 );

        System.out.println ( "You can select up to " + maxUnits + " units." );
        System.out.println ( "Please enter the course IDs you wish to select (enter 0 to finish):" );

        while (selectedCourses.size () < maxUnits) {
            int courseId = scanner.nextInt ();
            if (courseId == 0) {
                break;
            }

            Course course = courseService.findById ( courseId );
            if (course == null) {
                System.out.println ( "Course not found." );
            } else if (passedCourses.contains ( course )) {
                System.out.println ( "You have already passed this course." );
            } else if (currentTermCourses.contains ( course )) {
                System.out.println ( "You cannot select the same course twice in one term." );
            } else {
                selectedCourses.add ( course );
                System.out.println ( "Course " + course.getName () + " selected." );
            }
        }

        if (selectedCourses.size () > maxUnits) {
            System.out.println ( "You have selected more than the allowed units. Please deselect some courses." );
        } else {
            registerCoursesForStudent ( student1, selectedCourses );
            System.out.println ( "Course selection completed successfully." );
        }
    }

    private void registerCoursesForStudent(Student student1, List<Course> selectedCourses) {
        TermInfoService termInfoService = ApplicationContext.getTermInfoService();

        List<TermInformation> termInfoList = new ArrayList<> ();

        for (Course course : selectedCourses) {

            TermInformation termInfo = new TermInformation ();

            termInfo.setStudent(student1);
            termInfo.setCourse(course);
            termInfo.setGrade(0);

            termInfoList.add (termInfo);
        }
        student1.setTermInformationList ( termInfoList );
        studentService.saveOrUpdate ( student1 );
    }

    private List<Course> getCurrentTermCourses(Student student1) {
        List<TermInformation> termInformationList = termInfoService.findAll ();
        List<Course> currentTermCourses = new ArrayList<> ();

        for (TermInformation termInfo : termInformationList) {
            if (isCurrentTerm ( termInfo )) {
                currentTermCourses.add ( termInfo.getCourse () );
            }
        }
        return currentTermCourses;
    }

    private boolean isCurrentTerm(TermInformation termInfo) {
        int courseTerm = termInfo.getCourse ().getTerm ();
        int studentTerm = termInfo.getStudent ().getCurrentTerm ();
        return courseTerm == studentTerm ;
    }

    private List<Course> getPassedCourses(Student student1) {
        List<TermInformation> termInformationList = student1.getTermInformationList ();
        List<Course> passedCourses = new ArrayList<> ();

        for (TermInformation termInfo : termInformationList) {
            if (termInfo.getGrade () >= 10) {
                passedCourses.add ( termInfo.getCourse () );
            }
        }

        return passedCourses;
    }

    private double calculateLastTermAverage(Optional<Student> student1) {
        List<TermInformation> termInformationList = student1.get ().getTermInformationList ();
        double totalGrade = 0;
        int totalUnits = 0;

        for (TermInformation termInfo : termInformationList) {
            totalGrade += termInfo.getGrade ();
            totalUnits++;
        }

        if (totalUnits == 0) {
            return 0; // To avoid division by zero
        } else {
            return totalGrade / totalUnits;
        }
    }

    //4----------------------------------------------------------------
    private void viewCoursesWithPoints() {
        List<TermInformation> termInformationList = student.get().getTermInformationList();

        if (termInformationList.isEmpty()) {
            System.out.println("You have not taken any courses yet.");
        } else {
            System.out.println("Courses taken:");
            for (TermInformation termInfo : termInformationList) {
                Course course = termInfo.getCourse();
                System.out.println("Course ID: " + course.getId() + ", Name: " + course.getName() + ", Grade: " + termInfo.getGrade());
            }
        }
    }



    //EMPLOYEE MENU ----------------------------------------------------------------


    private void employeeMenu() {
        String employeeName = employee.get ().getFirstName ();
        System.out.println ( "Welcome " + employeeName + "! Here are your options:" );
        while (true) {
            System.out.println ( "1. Register, delete and edit student details" );
            System.out.println ( "2. Register, delete and edit professor details" );
            System.out.println ( "3. Register, delete and edit employee details" );
            System.out.println ( "4. Register, edit and delete course" );
            System.out.println ( "5. View payslip" );
            System.out.println ( "6. Exit" );

            int choice = scanner.nextInt ();
            switch (choice) {
                case 1:
                    CRUDStudent ();
                    break;
                case 2:
                    CRUDProfessor ();
                    break;
                case 3:
                    CRUDEmployee ();
                    break;
                case 4:
                    CRUDCourse ();
                    break;
                case 5:
                    showPaySlip ();
                    break;
                case 6:
                    System.out.println ( "Exiting the system" );
                    menu ();
                    break;
                default:
                    System.out.println ( "Invalid option. Please try again." );
            }
        }
    }


    //EMPLOYEE IMPLEMENTATION
    private void showPaySlip() {

        Employee employee1 = employeeService.findById ( employee.get ().getId () );

        if (employee1 != null) {
            System.out.println ( "Payslip for Employee ID: " + employee1.getId () );
            System.out.println ( "Name: " + employee1.getFirstName () + " " + employee1.getLastName () );
            System.out.println ( "Username: " + employee1.getUsername () );
            System.out.println ( "Birth Date: " + employee1.getBirthDate () );
            System.out.println ( "Gender: " + employee1.getGender () );
            System.out.println ( "Address: " + employee1.getAddress () );
            System.out.println ( "Salary: " + employee1.getSalary () );
        } else {
            System.out.println ( "No employee found with ID: " + employee1.getId () );
        }
    }

    private void CRUDCourse() {

        while (true) {
            System.out.println ( "1. Create Course" );
            System.out.println ( "2. Read Course" );
            System.out.println ( "3. Update Course" );
            System.out.println ( "4. Delete Course" );
            System.out.println ( "5. Exit" );

            System.out.print ( "Enter your choice: " );
            int choice = scanner.nextInt ();

            switch (choice) {
                case 1:
                    addCourse ();
                    System.out.println ( "Course added successfully!" );
                    break;
                case 2:
                    List<Course> courseList = courseService.findAll ();
                    for (Course course : courseList) {
                        System.out.println ( course.getId () + ": " + course );
                    }
                    break;
                case 3:
                    List<Course> courseList1 = courseService.findAll ();
                    for (Course course : courseList1) {
                        System.out.println ( course.getId () + ": " + course );
                    }
                    System.out.print ( "Enter course ID to update: " );
                    Integer courseId = scanner.nextInt ();
                    Course updateCourse = courseService.findById ( courseId );
                    updateCourse.setId ( courseId );
                    courseService.saveOrUpdate ( updateCourse );
                    System.out.println ( "Course information updated successfully!" );
                    break;
                case 4:
                    List<Course> courseList2 = courseService.findAll ();
                    for (Course course : courseList2) {
                        System.out.println ( course.getId () + ": " + course );
                    }
                    System.out.print ( "Enter course ID to delete: " );
                    Integer deleteCourseId = scanner.nextInt ();
                    courseService.deleteById ( deleteCourseId );
                    System.out.println ( "Course deleted successfully!" );
                    break;
                case 5:
                    menu ();
                    break;
                default:
                    System.out.println ( "Invalid choice. Please try again." );
            }
        }
    }



    private void CRUDEmployee() {

        while (true) {
            System.out.println ( "1. Create Employee" );
            System.out.println ( "2. Read Employee" );
            System.out.println ( "3. Update Employee" );
            System.out.println ( "4. Delete Employee" );
            System.out.println ( "5. Exit" );

            System.out.print ( "Enter your choice: " );
            int choice = scanner.nextInt ();

            switch (choice) {
                case 1:
                    addEmployee ();
                    System.out.println ( "Employee added successfully!" );
                    break;
                case 2:
                    List<Employee> employeeList = employeeService.findAll ();
                    for (Employee employee : employeeList) {
                        System.out.println ( employee.getId () + ": " + employee );
                    }
                    System.out.print ( "Enter employee ID to update: " );
                    Integer employeeId = scanner.nextInt ();
                    Employee updateEmployee = employeeService.findById ( employeeId );
                    updateEmployee.setId ( employeeId );
                    employeeService.saveOrUpdate ( updateEmployee );
                    System.out.println ( "Employee information updated successfully!" );
                    break;
                case 3:
                    List<Employee> employeeList1 = employeeService.findAll ();
                    for (Employee employee : employeeList1) {
                        System.out.println ( employee.getId () + ": " + employee );
                    }
                    System.out.print ( "Enter employee ID to delete: " );
                    Integer deleteEmployeeId = scanner.nextInt ();
                    employeeService.deleteById ( deleteEmployeeId );
                    System.out.println ( "Employee deleted successfully!" );
                    break;
                case 4:
                    List<Employee> employeeList2 = employeeService.findAll ();
                    for (Employee employee : employeeList2)
                        System.out.println ( employee.getId () + ": " + employee );
                    break;
                case 5:
                    menu ();
                    break;
                default:
                    System.out.println ( "Invalid choice. Please try again." );
            }
        }
    }

    private void CRUDProfessor() {

        while (true) {
            System.out.println ( "1. Create Professor" );
            System.out.println ( "2. Read Professor" );
            System.out.println ( "3. Update Professor" );
            System.out.println ( "4. Delete Professor" );
            System.out.println ( "5. Exit" );

            System.out.print ( "Enter your choice: " );
            int choice = scanner.nextInt ();

            switch (choice) {
                case 1:
                    addProfessor ();
                    System.out.println ( "Professor added successfully!" );
                    break;
                case 2:
                    List<Professor> professorList = professorService.findAll ();
                    for (Professor professor : professorList) {
                        System.out.println ( professor.getId () + ": " + professor );
                    }
                    System.out.print ( "Enter professor ID to update: " );
                    Integer professorId = scanner.nextInt ();
                    Professor updateProfessor = professorService.findById ( professorId );
                    updateProfessor.setId ( professorId );
                    professorService.saveOrUpdate ( updateProfessor );
                    System.out.println ( "Professor information updated successfully!" );
                    break;
                case 3:
                    List<Professor> professorList1 = professorService.findAll ();
                    for (Professor professor : professorList1) {
                        System.out.println ( professor.getId () + ": " + professor );
                    }
                    System.out.print ( "Enter professor ID to delete: " );
                    Integer deleteProfessorId = scanner.nextInt ();
                    professorService.deleteById ( deleteProfessorId );
                    System.out.println ( "Professor deleted successfully!" );
                    break;
                case 4:
                    List<Professor> professorList2 = professorService.findAll ();
                    for (Professor professor : professorList2)
                        System.out.println ( professor.getId () + ": " + professor );
                case 5:
                    menu ();
                    break;
                default:
                    System.out.println ( "Invalid choice. Please try again." );
            }
        }
    }

    private void CRUDStudent() {

        while (true) {
            System.out.println ( "1. Add new student" );
            System.out.println ( "2. Update student information" );
            System.out.println ( "3. Delete student" );
            System.out.println ( "4. View all students" );
            System.out.println ( "5. Menu" );
            System.out.print ( "Enter your choice: " );

            int choice = scanner.nextInt ();
            scanner.nextLine (); //employee choice

            switch (choice) {
                case 1:
                    addStudent ();
                    System.out.println ( "Student added successfully!" );
                    break;
                case 2:
                    List<Student> studetnList = studentService.findAll ();
                    for (Student student : studetnList) {
                        System.out.println ( student.getId () + ": " + student );
                    }
                    System.out.print ( "Enter student ID to update: " );
                    Integer studentId = scanner.nextInt ();
                    Student updatedStudent = studentService.findById ( studentId );
                    updatedStudent.setId ( studentId );
                    studentService.saveOrUpdate ( updatedStudent );
                    System.out.println ( "Student information updated successfully!" );
                    break;
                case 3:
                    List<Student> studetnList1 = studentService.findAll ();
                    for (Student student : studetnList1) {
                        System.out.println ( student.getId () + ": " + student );
                    }
                    System.out.print ( "Enter student ID to delete: " );
                    Integer deleteStudentId = scanner.nextInt ();
                    studentService.deleteById ( deleteStudentId );
                    System.out.println ( "Student deleted successfully!" );
                    break;
                case 4:
                    List<Student> studetnList2 = studentService.findAll ();
                    for (Student student : studetnList2)
                        System.out.println ( student.getId () + ": " + student );

                case 5:
                    menu ();
                    break;
                default:
                    System.out.println ( "Invalid choice. Please try again." );
            }
        }
    }

    public Professor addProfessor() {

        System.out.print ( "Enter first name: " );
        String firstName = scanner.nextLine ();

        System.out.print ( "Enter last name: " );
        String lastName = scanner.nextLine ();

        System.out.print ( "Enter date of birth (yyyy-MM-dd): " );
        String dobString = scanner.nextLine ();
        LocalDate dob = LocalDate.parse ( dobString );
        Date date = Date.from ( dob.atStartOfDay ( defaultZoneId ).toInstant () );

        System.out.println ( "Select gender (MALE/FEMALE/OTHER): " );
        Gender gender = Gender.valueOf ( scanner.nextLine ().toUpperCase () );

        System.out.print ( "Enter street: " );
        String street = scanner.nextLine ();

        System.out.print ( "Enter city: " );
        String city = scanner.nextLine ();

        System.out.print ( "Enter state: " );
        String state = scanner.nextLine ();

        System.out.print ( "Enter postal code: " );
        String postalCode = scanner.nextLine ();


        System.out.println ( "enter professorType(LECTURER,FACULTY_MEMBER): " );
        ProfessorType professorType = ProfessorType.valueOf ( scanner.nextLine ().toUpperCase () );

        Address address = Address.addressBuilder ()
                .street ( street )
                .city ( city )
                .state ( state )
                .postalCode ( postalCode )
                .build ();

        Professor professor = Professor.professorBuilder ()
                .firstName ( firstName )
                .lastName ( lastName )
                .birthDate ( date )
                .gender ( gender )
                .address ( address )
                .build ();


        return professor;

    }


    private Student addStudent() {

        System.out.print ( "Enter first name : " );
        String firstName = scanner.nextLine ();

        System.out.print ( "Enter last name: " );
        String lastName = scanner.nextLine ();

        System.out.print ( "Enter date of birth (yyyy-MM-dd): " );
        String dobString = scanner.nextLine ();
        LocalDate dob = LocalDate.parse ( dobString );
        Date date = Date.from ( dob.atStartOfDay ( defaultZoneId ).toInstant () );

        System.out.println ( "Select gender (MALE/FEMALE/OTHER): " );
        Gender gender = Gender.valueOf ( scanner.nextLine ().toUpperCase () );

        System.out.print ( "Enter street: " );
        String street = scanner.nextLine ();

        System.out.print ( "Enter city: " );
        String city = scanner.nextLine ();

        System.out.print ( "Enter state: " );
        String state = scanner.nextLine ();

        System.out.print ( "Enter postal code: " );
        String postalCode = scanner.nextLine ();



        Address address = Address.addressBuilder ()
                .street ( street )
                .city ( city )
                .state ( state )
                .postalCode ( postalCode )
                .build ();

        Student student = Student.studentBuilder ()
                .firstName ( firstName )
                .lastName ( lastName )
                .birthDate ( date )
                .gender ( gender )
                .address ( address )
                .build ();

        return student;
    }

    public Employee addEmployee() {

        System.out.print ( "Enter first name: " );
        String firstName = scanner.nextLine ();

        System.out.print ( "Enter last name: " );
        String lastName = scanner.nextLine ();

        System.out.print ( "Enter date of birth (yyyy-MM-dd): " );
        String dobString = scanner.nextLine ();
        LocalDate dob = LocalDate.parse ( dobString );
        Date date = Date.from ( dob.atStartOfDay ( defaultZoneId ).toInstant () );

        System.out.println ( "Select gender (MALE/FEMALE/OTHER): " );
        Gender gender = Gender.valueOf ( scanner.nextLine ().toUpperCase () );

        System.out.print ( "Enter street: " );
        String street = scanner.nextLine ();

        System.out.print ( "Enter city: " );
        String city = scanner.nextLine ();

        System.out.print ( "Enter state: " );
        String state = scanner.nextLine ();

        System.out.print ( "Enter postal code: " );
        String postalCode = scanner.nextLine ();

        System.out.println ( "Is the Employee active? (true/false): " );
        boolean isActive = Boolean.parseBoolean ( scanner.nextLine () );

        System.out.print ( "Enter salary: " );
        Double salary = scanner.nextDouble ();
        scanner.nextLine ();

        Address address = Address.addressBuilder ()
                .street ( street )
                .city ( city )
                .state ( state )
                .postalCode ( postalCode )
                .build ();

        Employee employee = Employee.EmplouyeeBuilder ()
                .firstName ( firstName )
                .lastName ( lastName )
                .birthDate ( date )
                .gender ( gender )
                .address ( address )
                .salary ( salary )
                .build ();

        return employee;
    }

    public Course addCourse() {

        System.out.print ( "Enter course name: " );
        String name = scanner.nextLine ();

        System.out.print ( "Enter course capacity (0-30): " );
        int capacity = scanner.nextInt ();
        scanner.nextLine ();

        System.out.println ( "Select course type (MATH," +
                "    BIOLOGY," +
                "    CHEMISTRY," +
                "    PHYSICS," +
                "    LITERATURE): " );
        CourseType courseType = CourseType.valueOf ( scanner.nextLine ().toUpperCase () );


        List<Professor> professorList2 = professorService.findAll ();
        for (Professor professor : professorList2)
            System.out.println ( professor.getId () + ": " + professor );
        System.out.println ( "Enter professor ID: " );
        int professorId = scanner.nextInt ();
        scanner.nextLine ();
        Professor professor = professorService.findById ( professorId );

        System.out.print ( "Enter term: " );
        int term = scanner.nextInt ();
        scanner.nextLine ();

        Course course = Course.courseBuilder ()
                .courseType ( courseType )
                .name ( name )
                .capacity ( capacity )
                .term ( term )
                .professor ( professor )
                .build ();

        return course;
    }

}









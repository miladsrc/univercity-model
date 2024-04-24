package menu;

import baseUtil.ApplicationContext;
import domain.*;
import service.*;

import java.sql.Date;
import java.time.ZoneId;
import java.util.*;

public class Menu {

    static final Scanner scanner = new Scanner ( System.in );
    static ZoneId defaultZoneId = ZoneId.systemDefault ();


    Optional<Employee> employee = Optional.empty ();
    Optional<Professor> professor = Optional.empty ();
    Optional<Student> student = Optional.empty ();


    StudentService studentService = ApplicationContext.getStudentService ();
    ProfessorService professorService = ApplicationContext.getProfessorService ();
    EmployeeService employeeService = ApplicationContext.getEmployeeService ();
    TermInfoService termInfoService = ApplicationContext.getTermInfoService ();
    CourseService courseService = ApplicationContext.getCourseService ();
    PersonService personService = ApplicationContext.getPersonService ();


    static String username = null;
    static String password = null;
    static int counter = 0;


    //MAIN REFRENCE MENU ----------------------------------------------------------------------------------------------

    public void menu() {

        if (counter == 0) {
            try {
                System.out.println ( "Please enter your username:" );
                username = scanner.nextLine ();
                System.out.println ( "Please enter your password:" );
                password = scanner.nextLine ();
                counter++;
            } catch (Exception e) {
                System.out.println ( "An error occurred: " + e.getMessage () );
            }
        }

        while (true) {
            try {
                System.out.println ( "what is your role :" +
                        "\n1: employee" +
                        "\n2: professor" +
                        "\n3: student" );
                int choice = scanner.nextInt ();
                switch (choice) {
                    case 1:
                        employee = ApplicationContext.getEmployeeService ().existsByUsernameAndPassword ( username, password );
                        employeeMenu ();

                    case 2:
                        professor = ApplicationContext.getProfessorService ().existsByUsernameAndPassword ( username, password );
                        professorMenu ();

                    case 3:
                        student = ApplicationContext.getStudentService ().existsByUsernameAndPassword ( username, password );
                        studentMenu ();
                }
            } catch (Exception e) {
                System.out.println ( "An error occurred: " + e.getMessage () );
                System.out.println ( "Please enter your username:" );
                username = scanner.nextLine ();
                System.out.println ( "Please enter your password:" );
                password = scanner.nextLine ();
            }
        }
    }


    //PROFESSION MENU----------------------------------------------------------------
    public void professorMenu() {
        Professor professor1 = professorService.findById ( professor.get ().getId () );
        System.out.println ( "Welcome " + professor1.getFirstName () + "! Here are your options:" );
        while (true) {
            System.out.println ( "1. View your profile" );
            System.out.println ( "2. Record student grades" );
            System.out.println ( "3. View salary slip" );
            System.out.println ( "4. Exit" );

            int choice = scanner.nextInt ();
            switch (choice) {
                case 1:
                    viewProfessorProfile ( professor1 );
                    break;
                case 2:
                    recordStudentGrades ( professor1 );
                    break;
                case 3:
                    viewSalarySlip ( professor1 );
                    break;
                case 4:
                    System.out.println ( "Exiting..." );
                    return;
                default:
                    System.out.println ( "Invalid option. Please try again." );
            }
        }
    }

    //1----------------------------------------------------------------
    private void viewProfessorProfile(Professor professor) {
        System.out.println ( "Professor ID: " + professor.getId () );
        System.out.println ( "First Name: " + professor.getFirstName () );
        System.out.println ( "Last Name: " + professor.getLastName () );
        System.out.println ( "Birth Date: " + professor.getBirthDate () );
        System.out.println ( "Username: " + professor.getUsername () );
        System.out.println ( "Password: " + professor.getPassword () );
        System.out.println ( "Gender: " + professor.getGender () );
        System.out.println ( "Address: " + professor.getAddress () );
    }

    //2----------------------------------------------------------------
    private void recordStudentGrades(Professor professor) {
        List<Student> students = studentService.findAll ();
        for (Student student : students) {
            System.out.println ( student.getId () + " : " + student.getFirstName () + " " + student.getLastName () );
        }

        System.out.println ( "Enter the student ID to record the grade (enter 0 to finish):" );
        int studentId;
        while ((studentId = scanner.nextInt ()) != 0) {
            Student student = findStudentById ( students, studentId );
            if (student == null) {
                System.out.println ( "Student not found. Please enter a valid student ID." );
                continue;
            }

            System.out.println ( "Enter the grade for student " + student.getFirstName () + " " + student.getLastName () + ":" );
            int grade = scanner.nextInt ();

            Student student1 = studentService.findById ( studentId );
            TermInformation studentGrade = new TermInformation ();
            studentGrade.setStudent ( student );
            studentGrade.setGrade ( grade );

            for (Course course : professor.getCourses ()
            ) {
                System.out.println ( course.getId () + " " + course.getName () );
            }
            System.out.println ( "Please choose between courses :" );
            int index = scanner.nextInt ();
            studentGrade.setCourse ( professor.getCourses ().get ( index ) );

            // Save the student grade
            ApplicationContext.getTermInfoService ().saveOrUpdate ( studentGrade );
            System.out.println ( "Grade recorded successfully." );
        }
    }

    private Student findStudentById(List<Student> students, int studentId) {
        for (Student student : students) {
            if (student.getId () == studentId) {
                return student;
            }
        }
        return null;
    }

    //3----------------------------------------------------------------
    private void viewSalarySlip(Professor professor) {
        final long fixedSalary = 5_000_000L;
        final long salaryPerUnit = 1_000_000L;

        int unitsTaught = getUnitsTaughtByProfessor ( professor );

        long totalSalary;
        if (professor.getProfessorType () == ProfessorType.FACULTY_MEMBER) {
            totalSalary = fixedSalary + (unitsTaught * salaryPerUnit);
        } else {
            totalSalary = unitsTaught * salaryPerUnit;
        }

        System.out.println ( "Professor Name: " + professor.getFirstName () + " " + professor.getLastName () );
        System.out.println ( "Total Units Taught: " + unitsTaught );
        System.out.println ( "Total Salary: " + totalSalary );
    }

    private int getUnitsTaughtByProfessor(Professor professor) {


        int totalUnits = 0;
        if (professor.getCourses () == null || professor.getCourses ().isEmpty ()) {
            return 0;
        }

//        int totalUnits = professor.getCourses().stream()
//                .filter(course -> this.term = course.getTerm())
//                .mapToInt(Course::getUnit)
//                .sum();


        for (Course course : professor.getCourses ()) {
            if (course.getTerm () == professor.getCurrentTerm ()) {
                totalUnits += course.getUnit ();
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
        TermInfoService termInfoService = ApplicationContext.getTermInfoService ();

        List<TermInformation> termInfoList = new ArrayList<> ();

        for (Course course : selectedCourses) {

            TermInformation termInfo = new TermInformation ();

            termInfo.setStudent ( student1 );
            termInfo.setCourse ( course );
            termInfo.setGrade ( 0 );

            termInfoList.add ( termInfo );
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
        return courseTerm == studentTerm;
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
        List<TermInformation> termInformationList = student.get ().getTermInformationList ();

        if (termInformationList.isEmpty ()) {
            System.out.println ( "You have not taken any courses yet." );
        } else {
            System.out.println ( "Courses taken:" );
            for (TermInformation termInfo : termInformationList) {
                Course course = termInfo.getCourse ();
                System.out.println ( "Course ID: " + course.getId () + ", Name: " + course.getName () + ", Grade: " + termInfo.getGrade () );
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
            System.out.println ( "2. Update Employee" );
            System.out.println ( "3. Delete Employee" );
            System.out.println ( "4. View all Employees" );
            System.out.println ( "5. Exit" );

            System.out.print ( "Enter your choice: " );
            int choice = scanner.nextInt ();

            switch (choice) {
                case 1:
                    addEmployee ();
                    break;
                case 2:
                    List<Employee> employeeList = employeeService.findAll ();
                    for (Employee employee : employeeList) {
                        System.out.println ( employee.getId () + ": " + employee );
                    }
                    System.out.print ( "Enter employee ID to update: " );
                    Integer employeeId = scanner.nextInt ();
                    Employee updateEmployee = employeeService.findById ( employeeId );
                    updateEmployee( updateEmployee );
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
            System.out.println ( "1. Add Professor" );
            System.out.println ( "2. Update Professors" );
            System.out.println ( "3. Delete Professor" );
            System.out.println ( "4. View All Professors" );
            System.out.println ( "5. Exit" );

            System.out.print ( "Enter your choice: " );
            int choice = scanner.nextInt ();

            switch (choice) {
                case 1:
                    addProfessor ();
                    break;
                case 2:
                    List<Professor> professorList = professorService.findAll();
                    for (Professor professor : professorList) {
                        System.out.println(professor.getId() + ": " + professor);
                    }
                    System.out.println("Enter professor id to continue... :");

                    try {
                        int choice1 = scanner.nextInt();
                        Professor professor1 = professorService.findById(choice1);
                        if (professor1 != null) {
                            updateProfessor(Optional.ofNullable(professor1));
                        } else {
                            System.out.println("Professor with ID " + choice1 + " not found.");
                        }
                    } catch (InputMismatchException ime) {
                        System.out.println("Invalid input. Please enter a valid integer for professor ID.");
                    } catch (Exception e) {
                        System.out.println("An error occurred: " + e.getMessage());
                    }
                    break;
                case 3:
                    List<Professor> professorList1 = professorService.findAll ();
                    for (Professor professor : professorList1) {
                        System.out.println ( professor.getId () + ": " + professor );
                    }
                    System.out.print ( "Enter professor ID to delete: " );
                    Integer deleteProfessorId = scanner.nextInt ();
                    professorService.deleteById ( deleteProfessorId );
                    break;
                case 4:
                    List<Professor> professorList2 = professorService.findAll ();
                    for (Professor professor : professorList2)
                        System.out.println ( professor.getId () + ": " + professor );
                    break;
                case 5:
                    menu ();
                    break;
                default:
                    System.out.println ( "Invalid choice. Please try again." );
            }
        }
    }


    //CRUD STUDENT
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
                    break;
                case 2:
                    try {
                        List<Student> studentList = studentService.findAll ();
                        for (Student student : studentList) {
                            System.out.println ( student.getId () + ": " + student );
                        }
                        System.out.print ( "Enter student ID to update: " );
                        Integer studentId = scanner.nextInt ();
                        Student updatedStudent1 = studentService.findById ( studentId );
                        if (updatedStudent1 != null) {
                            updateStudent ( updatedStudent1 );
                        } else {
                            System.out.println ( "Student with ID " + studentId + " not found." );
                        }
                    } catch (Exception e) {
                        System.out.println ( "Error occurred: " + e.getMessage () );
                    }
                    break;
                case 3:
                    List<Student> studentList1 = studentService.findAll ();
                    for (Student student : studentList1) {
                        System.out.println ( student.getId () + ": " + student );
                    }
                    System.out.print ( "Enter student ID to delete: " );
                    Integer deleteStudentId = scanner.nextInt ();
                    studentService.deleteById ( deleteStudentId );
                    break;
                case 4:
                    List<Student> studentList2 = studentService.findAll ();
                    for (Student student : studentList2) {
                        System.out.println ( student.getId () + ": " + student );
                    }
                    break;
                case 5:
                    menu ();
                    break;
                default:
                    System.out.println ( "Invalid choice. Please try again." );
            }

        }
    }

    public Professor addProfessor() {
        ProfessorType professorType = null;
        try {
            System.out.print ( "Enter first name: " );
            String firstName = scanner.next ();
            scanner.nextLine ();

            System.out.print ( "Enter last name: " );
            String lastName = scanner.nextLine ();

            System.out.print ( "Enter date of birth (yyyy-MM-dd): " );
            String dobString = scanner.nextLine ();
            Date date = Date.valueOf ( dobString );

            System.out.println ( "Select gender (MALE/FEMALE/OTHER): " );
            Gender gender = Gender.valueOf ( scanner.nextLine ().toUpperCase () );

            System.out.print ( "Enter street: " );
            String street = scanner.nextLine ();

            System.out.print ( "Enter city: " );
            String city = scanner.nextLine ();

            System.out.print ( "Enter username: " );
            String username = scanner.nextLine ();

            System.out.print ( "Enter password: " );
            String password = scanner.nextLine ();

            System.out.print ( "Enter state: " );
            String state = scanner.nextLine ();

            System.out.print ( "Enter postal code: " );
            String postalCode = scanner.nextLine ();

            System.out.print ( "Enter current term: " );
            Integer currentTerm = scanner.nextInt ();

            while (professorType == null) {
                System.out.println ( "enter professorType(1-LECTURER,\n2-FACULTY_MEMBER): " );
                int choice = scanner.nextInt ();
                scanner.nextLine ();

                switch (choice) {
                    case 1:
                        professorType = ProfessorType.LECTURER;
                        break;
                    case 2:
                        professorType = ProfessorType.FACULTY_MEMBER;
                        break;
                    default:
                        System.out.println ( "Invalid choice. Please enter 1 for LECTURER or 2 for FACULTY_MEMBER." );
                        break;
                }
            }


            Address address = Address.addressBuilder ()
                    .street ( street )
                    .city ( city )
                    .state ( state )
                    .postalCode ( postalCode )
                    .build ();

            Professor professor = Professor.professorBuilder ()
                    .firstName ( firstName )
                    .lastName ( lastName )
                    .username ( username )
                    .password ( password )
                    .birthDate ( date )
                    .gender ( gender )
                    .address ( address )
                    .currentTerm ( currentTerm )
                    .professorType ( professorType )
                    .build ();
            Professor professor1 = professorService.saveOrUpdate ( professor );

            if (professor1 != null) {
                System.out.println ( professor.getFirstName () + " done processing !\n" );
                return professor1;
            } else {
                return professor1;
            }


        } catch (Exception e) {
            e.printStackTrace ();
        }

        return null;
    }

    public Professor updateProfessor(Optional<Professor> professor) {
        ProfessorType pt = null;
        try {
            System.out.print ( "Enter first name: " );
            String firstName = scanner.next ();
            scanner.nextLine ();

            System.out.print ( "Enter last name: " );
            String lastName = scanner.nextLine ();

            System.out.print ( "Enter date of birth (yyyy-MM-dd): " );
            String dobString = scanner.nextLine ();
            Date date = Date.valueOf ( dobString );

            System.out.println ( "Select gender (MALE/FEMALE/OTHER): " );
            Gender gender = Gender.valueOf ( scanner.nextLine ().toUpperCase () );

            System.out.print ( "Enter street: " );
            String street = scanner.nextLine ();

            System.out.print ( "Enter city: " );
            String city = scanner.nextLine ();

            System.out.print ( "Enter username: " );
            String username = scanner.nextLine ();

            System.out.print ( "Enter password: " );
            String password = scanner.nextLine ();

            System.out.print ( "Enter state: " );
            String state = scanner.nextLine ();

            System.out.print ( "Enter postal code: " );
            String postalCode = scanner.nextLine ();

            System.out.print ( "Enter current term: " );
            Integer currentTerm = scanner.nextInt ();


            while (pt == null) {
                System.out.println ( "enter professorType(1-LECTURER,\n2-FACULTY_MEMBER): " );
                int choice = scanner.nextInt ();
                scanner.nextLine ();

                switch (choice) {
                    case 1:
                        pt = ProfessorType.LECTURER;
                        break;
                    case 2:
                        pt = ProfessorType.FACULTY_MEMBER;
                        break;
                    default:
                        System.out.println ( "Invalid choice. Please enter 1 for LECTURER or 2 for FACULTY_MEMBER." );
                        break;
                }
            }
            Address address = Address.addressBuilder ()
                    .street ( street )
                    .city ( city )
                    .state ( state )
                    .postalCode ( postalCode )
                    .build ();

            Professor professor1 = Professor.professorBuilder ()
                    .integer ( professor.get ().getId () )
                    .firstName ( firstName )
                    .lastName ( lastName )
                    .username ( username )
                    .password ( password )
                    .birthDate ( date )
                    .gender ( gender )
                    .address ( address )
                    .currentTerm ( currentTerm )
                    .professorType ( pt )
                    .build ();
            Professor professor2 = professorService.saveOrUpdate ( professor1 );

            if (professor2 != null) {
                System.out.println ( professor.get ().getFirstName () + " done processing !\n" );
                return professor2;
            } else {
                return professor1;
            }

        } catch (Exception e) {
            e.printStackTrace ();
        }
        return null;
    }


    // UPDATE STUDENT

    private Student addStudent() {
        try {
            System.out.print ( "Enter first name: " );
            String firstName = scanner.nextLine ();

            System.out.print ( "Enter last name: " );
            String lastName = scanner.nextLine ();

            System.out.print ( "Enter date of birth (yyyy-MM-dd): " );
            String dobString = scanner.nextLine ();
            Date date = Date.valueOf ( dobString );

            System.out.println ( "Select gender (MALE/FEMALE/OTHER): " );
            Gender gender = Gender.valueOf ( scanner.nextLine ().toUpperCase () );

            System.out.print ( "Enter username: " );
            String username = scanner.nextLine ();

            System.out.print ( "Enter password: " );
            String password = scanner.nextLine ();

            System.out.print ( "Enter street: " );
            String street = scanner.nextLine ();

            System.out.print ( "Enter city: " );
            String city = scanner.nextLine ();

            System.out.print ( "Enter state: " );
            String state = scanner.nextLine ();

            System.out.print ( "Enter postal code: " );
            String postalCode = scanner.nextLine ();

            System.out.print ( "Enter current term: " );
            Integer currentTerm = scanner.nextInt ();

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
                    .username ( username )
                    .password ( password )
                    .gender ( gender )
                    .address ( address )
                    .currentTerm ( currentTerm )
                    .build ();

            Student student1 = studentService.saveOrUpdate ( student );
            if (student1 != null) {
                System.out.println ( student1.getFirstName () + " done processing !\n" );
            }
            return student1;
        } catch (Exception e) {
            System.out.println ( "Error occurred: " + e.getMessage () );
        }
        return null;
    }


    private Student updateStudent(Student student) {
        try {
            System.out.print ( "Enter first name: " );
            String firstName = scanner.next ();
            scanner.nextLine ();

            System.out.print ( "Enter last name: " );
            String lastName = scanner.nextLine ();

            System.out.print ( "Enter date of birth (yyyy-MM-dd): " );
            String dobString = scanner.nextLine ();
            Date date = Date.valueOf ( dobString );

            System.out.println ( "Select gender (MALE/FEMALE/OTHER): " );
            Gender gender = Gender.valueOf ( scanner.nextLine ().toUpperCase () );

            System.out.print ( "Enter username: " );
            String username = scanner.nextLine ();

            System.out.print ( "Enter password: " );
            String password = scanner.nextLine ();

            System.out.print ( "Enter street: " );
            String street = scanner.nextLine ();

            System.out.print ( "Enter city: " );
            String city = scanner.nextLine ();

            System.out.print ( "Enter state: " );
            String state = scanner.nextLine ();

            System.out.print ( "Enter postal code: " );
            String postalCode = scanner.nextLine ();

            System.out.print ( "Enter current term: " );
            Integer currentTerm = scanner.nextInt ();

            Address address = Address.addressBuilder ()
                    .street ( street )
                    .city ( city )
                    .state ( state )
                    .postalCode ( postalCode )
                    .build ();

            Student student2 = Student.studentBuilder ()
                    .integer ( student.getId () )
                    .firstName ( firstName )
                    .lastName ( lastName )
                    .birthDate ( date )
                    .username ( username )
                    .password ( password )
                    .gender ( gender )
                    .address ( address )
                    .currentTerm ( currentTerm )
                    .build ();

            Student student1 = studentService.saveOrUpdate ( student2 );
            if (student1 != null) {
                System.out.println ( student1.getFirstName () + " done processing !\n" );
            }
            return student1;
        } catch (Exception e) {
            System.out.println ( "Error occurred: " + e.getMessage () );
        }
        return null;
    }


    //ADD EMPLOYEE

    public Employee addEmployee() {
        try {
            System.out.print("Enter first name: ");
            String firstName = scanner.next ();
            scanner.nextLine ();

            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();

            System.out.print("Enter date of birth (yyyy-MM-dd): ");
            String dobString = scanner.nextLine();
            Date date = Date.valueOf(dobString);

            System.out.println("Select gender (MALE/FEMALE/OTHER): ");
            Gender gender = Gender.valueOf(scanner.nextLine().toUpperCase());

            System.out.print("Enter street: ");
            String street = scanner.nextLine();

            System.out.print("Enter city: ");
            String city = scanner.nextLine();

            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            System.out.print("Enter state: ");
            String state = scanner.nextLine();

            System.out.print("Enter postal code: ");
            String postalCode = scanner.nextLine();



            System.out.print("Enter salary: ");
            Double salary = scanner.nextDouble();

            Address address = Address.addressBuilder()
                    .street(street)
                    .city(city)
                    .state(state)
                    .postalCode(postalCode)
                    .build();

            Employee employee = Employee.EmplouyeeBuilder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .birthDate(date)
                    .username(username)
                    .password(password)
                    .gender(gender)
                    .address(address)
                    .salary(salary)
                    .build();

            Employee employee1 = employeeService.saveOrUpdate ( employee );
            if (employee1 != null) {
                System.out.println ( employee1.getFirstName () + " done processing !\n" );
            }
            return employee1;
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return null;
        }
    }


    public Employee updateEmployee(Employee employee) {
        try {
            System.out.print("Enter first name: ");
            String firstName = scanner.next ();
            scanner.nextLine ();

            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();

            System.out.print("Enter date of birth (yyyy-MM-dd): ");
            String dobString = scanner.nextLine();
            Date date = Date.valueOf(dobString);

            System.out.println("Select gender (MALE/FEMALE/OTHER): ");
            Gender gender = Gender.valueOf(scanner.nextLine().toUpperCase());

            System.out.print("Enter street: ");
            String street = scanner.nextLine();

            System.out.print("Enter city: ");
            String city = scanner.nextLine();

            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            System.out.print("Enter state: ");
            String state = scanner.nextLine();

            System.out.print("Enter postal code: ");
            String postalCode = scanner.nextLine();


            System.out.print("Enter salary: ");
            Double salary = scanner.nextDouble();


            Address address = Address.addressBuilder()
                    .street(street)
                    .city(city)
                    .state(state)
                    .postalCode(postalCode)
                    .build();

            Employee employee1 = Employee.EmplouyeeBuilder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .birthDate(date)
                    .username(username)
                    .password(password)
                    .gender(gender)
                    .address(address)
                    .integer ( employee.getId () )
                    .salary(salary)
                    .build();

            Employee employee2 = employeeService.saveOrUpdate ( employee1 );
            if (employee1 != null) {
                System.out.println ( employee2.getFirstName () + " done processing !\n" );
            }
            return employee1;
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
            return null;
        }
    }




    //ADD COURSE
    public Course addCourse() {
        try {
            System.out.print("Enter course name: ");
            String name = scanner.nextLine();

            System.out.print("Enter course capacity (0-30): ");
            int capacity = scanner.nextInt();

            System.out.println("Select course type (MATH, BIOLOGY, CHEMISTRY, PHYSICS, LITERATURE): ");
            CourseType courseType = CourseType.valueOf(scanner.nextLine().toUpperCase());

            List<Professor> professorList = professorService.findAll();
            for (Professor professor : professorList) {
                System.out.println(professor.getId() + ": " + professor);
            }
            System.out.println("Enter professor ID: ");
            int professorId = scanner.nextInt();
            Professor professor = professorService.findById(professorId);

            System.out.print("Enter term: ");
            int term = scanner.nextInt();

            System.out.println("Enter Unit (must be between 1 and 4):");
            int unit = scanner.nextInt();

            Course course = Course.courseBuilder()
                    .courseType(courseType)
                    .name(name)
                    .capacity(capacity)
                    .term(term)
                    .professor(professor)
                    .unit(unit)
                    .build();

            courseService.saveOrUpdate(course);
            return course;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            return null;
        }
    }



    public Course updateCourse(Course course) {
        try {
            System.out.print("Enter course name: ");
            String name = scanner.nextLine();

            System.out.print("Enter course capacity (0-30): ");
            int capacity = scanner.nextInt();

            System.out.println("Select course type (MATH, BIOLOGY, CHEMISTRY, PHYSICS, LITERATURE): ");
            CourseType courseType = CourseType.valueOf(scanner.nextLine().toUpperCase());

            List<Professor> professorList = professorService.findAll();
            for (Professor professor : professorList) {
                System.out.println(professor.getId() + ": " + professor);
            }
            System.out.println("Enter professor ID: ");
            int professorId = scanner.nextInt();
            Professor professor = professorService.findById(professorId);

            System.out.print("Enter term: ");
            int term = scanner.nextInt();

            System.out.println("Enter Unit (must be between 1 and 4):");
            int unit = scanner.nextInt();

            Course updatedCourse = Course.courseBuilder()
                    .courseType(courseType)
                    .integer(course.getId())
                    .name(name)
                    .capacity(capacity)
                    .term(term)
                    .professor(professor)
                    .unit(unit)
                    .build();

            return courseService.saveOrUpdate(updatedCourse);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            return null;
        }
    }
}









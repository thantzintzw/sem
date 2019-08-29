package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class App {

    public Department getDepartment(String dept_name) {
        try {
            Department d=new Department();
            //Create an SQL statement
            Statement stmt = con.createStatement();

            System.out.println("in department " + dept_name);
            //Create string for SQL statement
            String strSelect =
                    "SELECT * from department where dept_name ='"+dept_name+"'";
            //Execute SQL statement
            ResultSet rset =stmt.executeQuery(strSelect);
            System.out.println(rset);
            if (rset.next()){
                d.dept_no=rset.getString("dept_no");
                d.dept_name=rset.getString("dept_name");
                return d;
            }
            else
                return null;

        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("No Department Data");
            return null;
        }
    }

    
    /**
     * Prints a list of employees.
     *
     * @param employees The list of employees to print.
     */
    public void printSalaries(ArrayList<Employee> employees) {
        // Check employees is not null
        if (employees == null) {
            System.out.println("No employees");
            return;
        }
        // Print header
        System.out.println(String.format("%-10s %-15s %-20s %-8s", "Emp No", "First Name", "Last Name", "Salary"));
        // Loop over all employees in the list
        for (Employee emp : employees) {
            if (emp == null)
                continue;
            String emp_string =
                    String.format("%-10s %-15s %-20s %-8s",
                            emp.emp_no, emp.first_name, emp.last_name, emp.salary);
            System.out.println(emp_string);
        }
    }


    /**
     * Gets all the current employees and salaries.
     *
     * @return A list of all employees and salaries, or null if there is an error.
     */
    public ArrayList<Employee> getAllSalaries() {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                            + "FROM employees, salaries "
                            + "WHERE employees.emp_no = salaries.emp_no AND salaries.to_date = '9999-01-01' "
                            + "ORDER BY employees.emp_no ASC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Extract employee information
            ArrayList<Employee> employees = new ArrayList<Employee>();
            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("employees.first_name");
                emp.last_name = rset.getString("employees.last_name");
                emp.salary = rset.getInt("salaries.salary");
                employees.add(emp);
            }
            return employees;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get salary details");
            return null;
        }
    }


    public void displayEmployee(Employee emp) {
        if (emp != null) {
            System.out.println(
                    emp.emp_no + " "
                            + emp.first_name + " "
                            + emp.last_name + "\n"
                            + emp.title + "\n"
                            + "Salary:" + emp.salary + "\n"
                            + emp.dept + "\n"
                            + "Manager: " + emp.manager + "\n");
        }
        else {
            System.out.println("this is no employee");
        }
    }

    public Employee getEmployee(int ID) {
        Employee emp=new Employee();
        try {
            String strSelect =
                    "SELECT emp_no, first_name, last_name from employees where emp_no=?";

            // Create an SQL statement
            PreparedStatement stmt = con.prepareStatement(strSelect);
            //System.out.println(" after premare");
            // Create string for SQL statement
           stmt.setInt(1,ID);
            //System.out.println(" after binding");

            // Execute SQL statement
            ResultSet rset = stmt.executeQuery();
            if (rset==null) {
                System.out.println("In  rset Null No employee &&&&&&&&&&Found");

            }
            // Return new employee if valid.
            // Check one is returned
            else{
            while (rset.next()) {

                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                System.out.println(emp.first_name+emp.last_name+emp.salary);


            }
               }
            return emp;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get employee details");
            return null;
        }

    }


    public static void main(String[] args) {
        // Create new Application
        App a = new App();

        // Connect to database
        if (args.length < 1)
        {
            a.connect("localhost:33060");
        }
        else
        {
            a.connect(args[0]);
        }

        // Extract employee salary information
        ArrayList<Employee> employees = a.getAllSalaries();
        a.printSalaries(employees);

        a.displayEmployee(null);

        // Test the size of the returned data - should be 240124
        System.out.println(employees.size());

        Department d1= a.getDepartment( "Sales");
        System.out.println(d1);

        //for integration test
        Employee e1 = a.getEmployee(255530);
        System.out.println(e1);

        // Disconnect from database
        a.disconnect();
    }


    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect(String location)
    {
        try
        {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 30;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");
            try
            {
                // Wait a bit for db to start
                Thread.sleep(30000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + location + "/employees?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect() {
        if (con != null) {
            try {
                // Close connection
                con.close();
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    public Department getDepartment(int dept_no) {
        return null;
    }

}
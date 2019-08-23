package com.napier.sem;

public class Department {
    public String dept_no;

    @Override
    public String toString() {
        return "Department {" +
                "dept_no='" + dept_no +
                ",dept_name='"+ dept_name + '\''+
                ",manager=" + manager + '}';
    }
   // Public String getDept_name() {
      //  return dept_name;}
    public void setDept_name(String dept_name) {this.dept_name = dept_name;}
    public Employee getManager() {return manager;}
    public void setManager(Employee manager) {this.manager = manager;}

    public String dept_name;
    public Employee manager;
}

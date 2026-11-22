package by.bsuir.lab5.model;
import java.io.Serializable;
public class Developer implements Serializable {
    private final int id; private final String name, specialty, department;
    public Developer(int id, String name, String specialty, String department) { this.id=id; this.name=name; this.specialty=specialty; this.department=department; }
    public int getId(){return id;} public String getName(){return name;} public String getSpecialty(){return specialty;} public String getDepartment(){return department;}
}

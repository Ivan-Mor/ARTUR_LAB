package com.jaxb.test;

import com.jaxb.model.Department;
import com.jaxb.model.Employee;
import com.jaxb.model.Organization;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Задание JAXB: запись организации (список отделов с сотрудниками) в XML
 * и чтение организации из XML (маршалинг и демаршалинг).
 */
public class TestExample {
    private static final String XML_FILE = "organization.xml";

    public static void main(String[] args) {
        try {
            // Подготовка данных: организация из двух отделов
            Department dev = new Department("D01", "DEVELOPMENT", "MINSK");
            Department qa = new Department("D02", "QUALITY ASSURANCE", "BREST");
            List<Department> departments = new ArrayList<>();
            dev.setEmployees(List.of(
                    new Employee("E01", "Tom", null),
                    new Employee("E02", "Mary", "E01")
            ));
            qa.setEmployees(List.of(
                    new Employee("E03", "John", null),
                    new Employee("E04", "Kate", "E03")
            ));
            departments.add(dev);
            departments.add(qa);
            Organization org = new Organization("IT-Company", "BELARUS");
            org.setDepartments(departments);

            // Маршалинг: Java -> XML
            JAXBContext context = JAXBContext.newInstance(Organization.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(org, System.out);
            File outFile = new File(XML_FILE);
            m.marshal(org, outFile);
            System.err.println("Write to file: " + outFile.getAbsolutePath());

            // Демаршалинг: XML -> Java
            Unmarshaller um = context.createUnmarshaller();
            Organization orgFromFile = (Organization) um.unmarshal(new FileReader(XML_FILE));
            System.out.println("Organization read from file: " + orgFromFile.getOrganizationName());
            for (Department d : orgFromFile.getDepartments()) {
                System.out.println("Department: " + d.getDepartmentName() + " (" + d.getLocation() + ")");
                List<Employee> emps = d.getEmployees();
                for (Employee emp : emps) {
                    System.out.println("    Employee: " + emp.getEmployeeName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

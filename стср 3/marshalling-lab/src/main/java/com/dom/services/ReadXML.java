package com.dom.services;

import com.dom.model.Employee;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Чтение всех сотрудников из XML-файла (DOM).
 */
public class ReadXML {
    public static void main(String[] args) {
        File file = new File("src/main/resources/employee.xml");
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            String rootNode = document.getDocumentElement().getNodeName();
            System.out.println("Root Element : " + rootNode);

            List<Employee> employees = getEmployeesData(document);
            for (Employee emp : employees) {
                System.out.println(emp);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static List<Employee> getEmployeesData(Document document) {
        NodeList list = document.getElementsByTagName("Employee");
        int length = list.getLength();
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                Employee emp = getEmployee(element);
                employees.add(emp);
            }
        }
        return employees;
    }

    private static Employee getEmployee(Element element) {
        String id = element.getAttribute("id");
        Employee emp = new Employee();
        emp.setId(Integer.parseInt(id));
        emp.setName(getEmployeeDetails(element, "name"));
        emp.setAge(Integer.parseInt(getEmployeeDetails(element, "age")));
        emp.setGender(getEmployeeDetails(element, "gender"));
        emp.setRole(getEmployeeDetails(element, "role"));
        emp.setSalary(Double.parseDouble(getEmployeeDetails(element, "salary")));
        emp.setEmail(getEmployeeDetails(element, "email"));
        emp.setDepartment(getEmployeeDetails(element, "department"));
        return emp;
    }

    private static String getEmployeeDetails(Element element, String property) {
        return element.getElementsByTagName(property).item(0).getTextContent();
    }
}

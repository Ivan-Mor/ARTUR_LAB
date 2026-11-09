package com.dom.services;

import com.dom.model.Employee;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Поиск сотрудника в XML-файле по значению атрибута id (DOM).
 */
public class SearchXML {
    public static void main(String[] args) {
        File file = new File("src/main/resources/employee.xml");
        Scanner input = null;
        try {
            input = new Scanner(System.in);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            System.out.println("Enter employee id : ");
            int id = input.nextInt();
            Employee employee = getEmployee(document, id);
            if (employee == null) {
                System.out.println("Employee not exist with id = " + id);
            } else {
                System.out.println(employee);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    private static Employee getEmployee(Document document, int id) {
        NodeList list = document.getElementsByTagName("Employee");
        int length = list.getLength();
        Employee emp = null;
        for (int i = 0; i < length; i++) {
            Node node = list.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (String.valueOf(id).equals(element.getAttribute("id"))) {
                    emp = new Employee();
                    emp.setId(Integer.parseInt(element.getAttribute("id")));
                    emp.setName(element.getElementsByTagName("name").item(0).getTextContent());
                    emp.setAge(Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent()));
                    emp.setGender(element.getElementsByTagName("gender").item(0).getTextContent());
                    emp.setRole(element.getElementsByTagName("role").item(0).getTextContent());
                    emp.setSalary(Double.parseDouble(element.getElementsByTagName("salary").item(0).getTextContent()));
                    emp.setEmail(element.getElementsByTagName("email").item(0).getTextContent());
                    emp.setDepartment(element.getElementsByTagName("department").item(0).getTextContent());
                }
            }
        }
        return emp;
    }
}

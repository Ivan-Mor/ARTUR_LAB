package com.dom.services;

import com.dom.model.Employee;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Добавление сотрудника в существующий XML-файл (DOM).
 * Значение атрибута id тега Employee вычисляется автоматически.
 */
public class AddEmployee {
    public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException {
        File file = new File("src/main/resources/employee.xml");
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(System.in));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);

            System.out.println("Enter name : ");
            String name = input.readLine();
            System.out.println("Enter gender : (male/female)");
            String gender = input.readLine();
            System.out.println("Enter age : ");
            int age = Integer.parseInt(input.readLine());
            System.out.println("Enter role : ");
            String role = input.readLine();
            System.out.println("Enter salary : ");
            double salary = Double.parseDouble(input.readLine());
            System.out.println("Enter email : ");
            String email = input.readLine();
            System.out.println("Enter department : ");
            String department = input.readLine();

            Employee employee = new Employee();
            employee.setName(name);
            employee.setGender(gender);
            employee.setAge(age);
            employee.setRole(role);
            employee.setSalary(salary);
            employee.setEmail(email);
            employee.setDepartment(department);

            Element element = getEmployeeNode(employee, document);
            document.getDocumentElement().appendChild(element);

            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult sResult = new StreamResult(file);
            transformer.transform(source, sResult);

            System.out.println("Employee has been added successfully.");
        } catch (org.xml.sax.SAXException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    private static Element getEmployeeNode(Employee employee, Document document) {
        Element element = document.createElement("Employee");
        NodeList list = document.getElementsByTagName("Employee");
        int count = list.getLength();
        element.setAttribute("id", String.valueOf(++count));

        element.appendChild(getPropertyNode("name", document, employee.getName()));
        element.appendChild(getPropertyNode("gender", document, employee.getGender()));
        element.appendChild(getPropertyNode("age", document, String.valueOf(employee.getAge())));
        element.appendChild(getPropertyNode("role", document, employee.getRole()));
        element.appendChild(getPropertyNode("salary", document, String.valueOf(employee.getSalary())));
        element.appendChild(getPropertyNode("email", document, employee.getEmail()));
        element.appendChild(getPropertyNode("department", document, employee.getDepartment()));
        return element;
    }

    private static Element getPropertyNode(String property, Document document, String value) {
        Element element = document.createElement(property);
        element.setTextContent(value);
        return element;
    }
}

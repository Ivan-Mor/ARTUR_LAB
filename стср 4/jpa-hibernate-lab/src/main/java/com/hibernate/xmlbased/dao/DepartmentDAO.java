package com.hibernate.xmlbased.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hibernate.xmlbased.config.SessionConfig;
import com.hibernate.xmlbased.model.Department;
import com.hibernate.xmlbased.model.Developer;
import com.hibernate.xmlbased.model.User;

public class DepartmentDAO {
    private final SessionConfig sc;

    public DepartmentDAO() {
        sc = SessionConfig.getInstanceOfSeccionFactory();
    }

    public List<Department> getDepartments() {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        List<Department> deps = session.createQuery("FROM Department", Department.class).getResultList();
        transaction.commit();
        session.close();
        return deps;
    }

    // этот кусок кода решает проблему n+1
    public List<Department> getDepartmentWithWorkers() {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        List<Department> departments = session
                .createQuery("SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.developers ORDER BY d.departmentId",
                        Department.class)
                .getResultList();
        transaction.commit();
        session.close();
        return departments;
    }

    public List<Developer> getDevelopersByDepartment(Department department) {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Department dep = session.get(Department.class, department.getDepartmentId());
        org.hibernate.Hibernate.initialize(dep.getDevelopers());
        List<Developer> devs = dep.getDevelopers();
        transaction.commit();
        session.close();
        return devs;
    }

    // ЗАДАНИЕ: найти департамент по ID
    public Department findDepartmentByID(String departmentID) {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Department department = session.get(Department.class, departmentID);
        transaction.commit();
        session.close();
        return department;
    }

    // ЗАДАНИЕ: добавить департамент
    public Department addDepartament(Department dep) {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(dep);
            transaction.commit();
            return dep;
        } finally {
            session.close();
        }
    }

    // ЗАДАНИЕ: удалить департамент (каскадом удаляются и его сотрудники,
    // а перед этим - их аккаунты, т.к. users.user_id ссылается на developers.id)
    public void deleteDepartment(String ID) {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Department dep = session
                    .createQuery("FROM Department d LEFT JOIN FETCH d.developers WHERE d.departmentId = :id",
                            Department.class)
                    .setParameter("id", ID)
                    .getSingleResult();
            for (Developer dev : dep.getDevelopers()) {
                User user = session.find(User.class, dev.getId());
                if (user != null) {
                    session.remove(user);
                }
            }
            // CascadeType.ALL на List<Developer> удаляет всех сотрудников департамента
            session.remove(dep);
            transaction.commit();
        } finally {
            session.close();
        }
    }

    // Собственный SELECT 1 (HQL): департаменты, ID которых начинается с символа
    public List<Department> findDepartmentByLetterInID(char letter) {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        List<Department> deps = session
                .createQuery("FROM Department d WHERE d.departmentId LIKE :letter", Department.class)
                .setParameter("letter", letter + "%")
                .getResultList();
        transaction.commit();
        session.close();
        return deps;
    }
}

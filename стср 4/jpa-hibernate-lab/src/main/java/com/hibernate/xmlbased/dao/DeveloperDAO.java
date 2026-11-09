package com.hibernate.xmlbased.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hibernate.xmlbased.config.SessionConfig;
import com.hibernate.xmlbased.model.Department;
import com.hibernate.xmlbased.model.Developer;
import com.hibernate.xmlbased.model.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class DeveloperDAO {
    private final SessionConfig sc;

    public DeveloperDAO() {
        sc = SessionConfig.getInstanceOfSeccionFactory();
    }

    public void addDeveloper(Developer developer) {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.persist(developer);
        transaction.commit();
        session.close();
    }

    public Developer getDeveloperById(Integer id) {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Developer developer = session.get(Developer.class, id);
        transaction.commit();
        session.close();
        return developer;
    }

    public List<Developer> getDevelopers() {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        List<Developer> developers = session.createQuery("FROM Developer", Developer.class).list();
        transaction.commit();
        session.close();
        return developers;
    }

    // ЗАДАНИЕ: перевести сотрудника в другой департамент
    public Developer updateDevelopersDepartment(Integer devId, Department department) {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            Developer developer = session.get(Developer.class, devId);
            Department dep = session.get(Department.class, department.getDepartmentId());
            developer.setDepartment(dep);
            transaction.commit();
            return developer;
        } finally {
            session.close();
        }
    }

    public void removeDeveloper(Integer id) {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        // сначала удаляем аккаунт сотрудника (users.user_id ссылается на developers.id)
        User user = session.find(User.class, id);
        if (user != null) {
            session.remove(user);
        }
        Developer developer = session.get(Developer.class, id);
        if (developer != null) {
            session.remove(developer);
        }
        transaction.commit();
        session.close();
    }

    // Собственный SELECT 2 (HQL): сотрудники с заданным опытом работы
    public List<Developer> findByExperienceEqual(Integer experience) {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        List<Developer> devs = session
                .createQuery("FROM Developer d WHERE d.experience = :exp", Developer.class)
                .setParameter("exp", experience)
                .getResultList();
        transaction.commit();
        session.close();
        return devs;
    }

    // Собственный SELECT 3 (Criteria): сотрудники с заданным опытом работы
    public List<Developer> findByExperienceEqualCriteria(Integer experience) {
        Session session = sc.getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();
        try {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Developer> criteria = criteriaBuilder.createQuery(Developer.class);
            Root<Developer> root = criteria.from(Developer.class);
            criteria.select(root).where(criteriaBuilder.equal(root.get("experience"), experience));
            List<Developer> devs = session.createQuery(criteria).getResultList();
            transaction.commit();
            return devs;
        } finally {
            session.close();
        }
    }
}

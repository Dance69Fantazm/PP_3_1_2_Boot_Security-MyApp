package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RoleDaoImp implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Role> receiveRoleById(List<Long> id) {
        return entityManager.createQuery(
                        "SELECT r FROM Role r WHERE r.id IN :id", Role.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<Role> receiveAllRoles() {
        return entityManager.createQuery(
                        "SELECT r FROM Role r", Role.class)
                .getResultList();
    }
}

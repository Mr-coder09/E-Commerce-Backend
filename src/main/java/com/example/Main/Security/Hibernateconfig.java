package com.example.Main.Security;



import org.hibernate.Session;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class Hibernateconfig {

    @PersistenceContext
    private EntityManager entityManager;

    public void enableSoftDeleteFilter() {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("softDeleteFilter")
               .setParameter("isDeleted", false);
    }
}
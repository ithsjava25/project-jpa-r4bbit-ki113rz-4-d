package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    public static final EntityManagerFactory emf =
        Persistence.createEntityManagerFactory("BulletinPU");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}

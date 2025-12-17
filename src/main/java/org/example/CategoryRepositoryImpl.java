package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

public class CategoryRepositoryImpl implements CategoryRepository{
    private final EntityManagerFactory emf;

    public CategoryRepositoryImpl (EntityManagerFactory emf) { this.emf = emf; }
    @Override
    public Category save(Category category) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            category = em.merge(category);
            tx.commit();
            return category;
        } finally {
            em.close();
        }
    }

    @Override
    public Category getById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Category> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "select c from Category c order by c.name", Category.class)
                .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Category> findByName(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Category> res = em.createQuery(
                "select c from Category c where c.name = :name",
                Category.class)
                .setParameter("name", name).getResultList();

            return res.isEmpty() ? Optional.empty() : Optional.of(res.get(0));
        } finally {
            em.close();
        }
    }
}

package org.example;

import jakarta.persistence.EntityManagerFactory;

/**
 * Handles post data from database
 */
public class PostRepositoryImpl implements PostRepository {
    private final EntityManagerFactory emf;
    public PostRepositoryImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    //Implement methods here:
}

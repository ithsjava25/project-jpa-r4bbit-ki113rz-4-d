package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import org.hibernate.sql.Update;

import java.math.BigDecimal;

public class UserRepositoryImpl implements UserRepository {

    private final PersistenceConfiguration cfg;
    public UserRepositoryImpl(PersistenceConfiguration cfg) {
        this.cfg = cfg;
    }
    @Override
    public boolean createUser(String firstName, String lastName, String password) {

        try (EntityManagerFactory emf = cfg.createEntityManagerFactory()) {

//            Insert/Persist of new entity
//            Update of managed entity
            emf.runInTransaction(em -> {
                User user = new User();
                user.setFirst_name(firstName);
                user.setLast_name(lastName);
                user.setPassword(password);
                user.setUsername(createUserName(firstName, lastName));
                em.persist(user);
            });
        }
        return false;
    }

    public String createUserName(String firstName, String lastName) {
        return firstName.substring(0,3) + lastName.substring(0,3);
    }

    @Override
    public boolean deleteUser(Long id) {
        return false;
    }

    @Override
    public boolean validateUser(String username, String password) {
        return false;
    }

    @Override
    public boolean updatePassword(String username, String password) {
        return false;
    }
}

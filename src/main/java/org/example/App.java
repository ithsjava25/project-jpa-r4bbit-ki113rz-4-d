package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceConfiguration;
import org.hibernate.jpa.HibernatePersistenceConfiguration;

public class App {
    public static void main(String[] args) {

        final PersistenceConfiguration cfg = new HibernatePersistenceConfiguration("emf")
            .jdbcUrl("jdbc:mysql://localhost:3306/bulletin")
            .jdbcUsername("root")
            .jdbcPassword("root")
            .property("hibernate.hbm2ddl.auto", "update")
            .property("hibernate.show_sql", "true")
            .property("hibernate.format_sql", "true")
            .property("hibernate.highlight_sql", "true")
            .managedClasses(User.class);



        UserRepositoryImpl userRepo = new UserRepositoryImpl(cfg);
        boolean result = userRepo.createUser("admin", "admin", "admin");
        System.out.println(result);

        UserRepositoryImpl updatePassword = new UserRepositoryImpl(cfg);
        boolean updated = updatePassword.updatePassword("admadm", "newPassword");
        System.out.println("Password updated: " + updated);


    }
}

package org.example.Repositories;

import jakarta.persistence.EntityManagerFactory;
import org.example.Entities.Profile;
import org.example.Entities.User;

import java.util.Optional;

public class ProfileRepositoryImpl implements ProfileRepository {

    EntityManagerFactory emf;

    public ProfileRepositoryImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Profile save(Profile profile) {
            if (profile == null) {
                throw new IllegalArgumentException("User cannot be null");
            }

            return emf.callInTransaction(em -> em.merge(profile));
    }

    @Override
    public Optional<Profile> findById(Long id) {
        return Optional.ofNullable(emf.callInTransaction(em -> em.find(Profile.class, id)));
    }

    @Override
    public Optional<String> bioById(String id) {
        return emf.callInTransaction(em ->
            em.createQuery(
                    "select p.bio from Profile p where p.user.id = :userId",
                    String.class
                )
                .setParameter("userId", id)
                .getResultStream()
                .findFirst()
        );
    }



}

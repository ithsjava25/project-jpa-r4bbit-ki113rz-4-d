package org.example.Repositories;

import org.example.Entities.Profile;
import org.example.Entities.User;

import java.util.Optional;

public interface ProfileRepository {
    Profile save(Profile profile);
    Optional<Profile> findById(Long id);
    Optional<String> bioById(Long id);
}

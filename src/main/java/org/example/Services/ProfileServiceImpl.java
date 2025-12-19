package org.example.Services;

import jakarta.persistence.EntityManagerFactory;
import org.example.Entities.Profile;
import org.example.Entities.User;
import org.example.Repositories.ProfileRepository;

import java.util.Optional;

public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepo;

    public ProfileServiceImpl(ProfileRepository profileRepo) {
        this.profileRepo = profileRepo;
    }
}

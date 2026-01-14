package org.example.Services;

import org.example.Repositories.ProfileRepository;

public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepo;

    public ProfileServiceImpl(ProfileRepository profileRepo) {
        this.profileRepo = profileRepo;
    }
}

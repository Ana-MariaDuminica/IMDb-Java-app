package org.example;

public class ResolveRequestExperienceStrategy implements ExperienceStrategy {
    private static final int RESOLVED_REQUEST_EXPERIENCE = 1;

    @Override
    public int calculateExperience() {
        return RESOLVED_REQUEST_EXPERIENCE;
    }
}

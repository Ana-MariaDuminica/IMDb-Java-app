package org.example;

public class RatingExperienceStrategy implements ExperienceStrategy {
    private static final int RATING_EXPERIENCE_POINTS = 1;

    @Override
    public int calculateExperience() {
        return RATING_EXPERIENCE_POINTS;
    }
}

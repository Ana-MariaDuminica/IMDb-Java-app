package org.example;

public class AddItemExperienceStrategy implements ExperienceStrategy {
    private static final int ADD_ITEM_EXPERIENCE = 1;

    @Override
    public int calculateExperience() {
        return ADD_ITEM_EXPERIENCE;
    }
}

package org.example;

public class UserFactory {
    public static User createUser(AccountType type) {
        if (type == AccountType.Regular) {
            return new Regular();
        } else if (type == AccountType.Admin) {
            return new Admin();
        } else if (type == AccountType.Contributor) {
            return new Contributor();
        }
        return null;
    }
}
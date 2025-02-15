package org.example;

public class NavigationManager {
    private LoginPage loginPage;
    private HomePage homePage;

    public void showLoginPage() {
        if (loginPage == null) {
            loginPage = new LoginPage(this);
        }
        loginPage.setVisible(true);
    }

    public void showHomePage(User authenticatedUser) {
        homePage = new HomePage(authenticatedUser, this);
        homePage.setVisible(true);
        loginPage.setVisible(false);
    }

    public void logout() {
        // sterge utilizatorul autentificat și revine la pagina de login
        homePage.dispose();
        homePage = null;
        showLoginPage();
    }
}


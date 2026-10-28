package pos_app;

import javax.swing.SwingUtilities;
import pos_app.ui.Theme;
import pos_app.util.SQLSeeder;
import pos_app.util.Session;
import pos_app.view.LoginFrame;
import pos_app.view.MainFrame;

/** Application entry point; keeps startup concerns outside the window classes. */
public final class App {
    private App() { }

    public static void main(String[] args) {
        Theme.install();
        SQLSeeder.run("src/pos_app/sql/pos_app.sql");
        SwingUtilities.invokeLater(() -> {
            Session.autoLoginFromFile();
            if (Session.isLoggedIn()) {
                new MainFrame().setVisible(true);
            } else {
                LoginFrame login = new LoginFrame();
                login.setLocationRelativeTo(null);
                login.setVisible(true);
            }
        });
    }
}

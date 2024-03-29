package conf;

import db.DatabaseInitializationExecutor;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent contextEvent) {
        new DatabaseInitializationExecutor().run();
        System.out.println("MyServletContextListener contextInitialized");
    }
}

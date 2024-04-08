package conf;

import db.DatabaseInitializationExecutor;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import scheduler.ReminderScheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MyServletContextListener implements ServletContextListener {
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    @Override
    public void contextInitialized(ServletContextEvent contextEvent) {
        new DatabaseInitializationExecutor().run();

        scheduler.scheduleAtFixedRate(() -> new ReminderScheduler().sendEmailReminders(), 10, 60, SECONDS);

        System.out.println("MyServletContextListener contextInitialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
        scheduler.shutdown();
    }
}

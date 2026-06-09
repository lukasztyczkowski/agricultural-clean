package pl.mruczekprogramista;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.ColorScheme;

import javax.sql.DataSource;

import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.theme.Theme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.ApplicationDataSourceScriptDatabaseInitializer;
import org.springframework.boot.sql.autoconfigure.init.SqlInitializationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import pl.mruczekprogramista.data.SprayRepository;


/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@EnableScheduling

public class Application implements AppShellConfigurator {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
    @Override
    public void configurePage(AppShellSettings settings) {
        settings.addFavIcon("icon", "icons/icon.png", "192x192");
        settings.addLink("stylesheet", "context://themes/spray/styles.css");
    }
    @Scheduled(fixedRate = 7200000)
    public void keepDatabaseAlive() {
        try {
            jdbcTemplate.execute("SELECT 1");
            System.out.println("=== PING: Baza danych Aiven podtrzymana przy życiu! ===");
        } catch (Exception e) {
            System.err.println("=== PING BŁĄD: " + e.getMessage() + " ===");
        }
    }




}

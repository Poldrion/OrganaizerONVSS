package organizer;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import organizer.model.BaseRepositoryImpl;
import organizer.views.controller.LoginController;

import java.io.IOException;

@Configuration
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class Organizer extends Application {

    private static ConfigurableApplicationContext applicationContext;

    @Override
    public void init() throws Exception {
        applicationContext = SpringApplication.run(Organizer.class);
    }

    @Override
    public void stop() throws Exception {
        applicationContext.close();

    }

    @Override
    public void start(Stage stage) throws IOException {
        LoginController.loadView(stage);

    }


    public static void main(String[] args) {
        launch(args);
    }

    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }


}

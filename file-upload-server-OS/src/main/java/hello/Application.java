package hello;

//import it.sauronsoftware.cron4j.Scheduler;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import hello.storage.StorageProperties;
import hello.storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        //Scheduler s = new Scheduler();
        // TODO put these 2 lines in once mosaiccreator is imported somehow. 
        // Rename "main" in Mosaic.java to public void run() {...} and use public class Mosaic implements Runnable {...}
        //Mosaic m = new Mosaic();
        //s.schedule("0 0 * * *", m);

        // Schedule a once-a-day task
        //s.schedule("* * * * *", new Runnable() {
        //    public void run() {
        //        System.out.println("Another minute ticked away...");
        //    }
        //});
        //s.start();
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}

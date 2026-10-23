package sg.edu.ntu.taskflow_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Entry point. @SpringBootApplication turns on component scanning for this package. */
@SpringBootApplication
public class TaskflowApiApplication {

	/** Boots the Spring context and starts the embedded Tomcat server. */
	public static void main(String[] args) {
		SpringApplication.run(TaskflowApiApplication.class, args);
	}

}

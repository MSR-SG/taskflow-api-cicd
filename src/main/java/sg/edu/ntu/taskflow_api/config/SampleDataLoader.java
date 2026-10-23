package sg.edu.ntu.taskflow_api.config;

import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sg.edu.ntu.taskflow_api.model.Task;
import sg.edu.ntu.taskflow_api.service.TaskService;

/** Seeds demo data at startup with sample tasks. 
 * Kept out of the service so production logic stays clean. */
@Component
public class SampleDataLoader implements CommandLineRunner {

    private final TaskService taskService;

    /** Same constructor injection as everywhere else, for consistency. */
    public SampleDataLoader(TaskService taskService) {
        this.taskService = taskService;
    }

    /** Good practice: seeds through the service, so sample rows get real
     *  generated ids and follow the same rules as any other task. */
    @Override
    public void run(String... args) {
        taskService.createTask(
                newTask("Book flight tickets", "Book the tickets for upcoming trip.", LocalDate.now().minusDays(3), true));
        taskService.createTask(
                newTask("Book accommodation", "Book the accommodation for the trip.", LocalDate.now().minusDays(2), true));
        taskService.createTask(
                newTask("Pack luggage", "Pack all necessary items for the trip.", LocalDate.now().plusDays(5), false));
        taskService.createTask(
                newTask("Book large cab to airport", "Book a large cab to the airport for luggage and passengers.", LocalDate.now().plusDays(7), false));
    }

    /** Small private factory that keeps run() readable. Dates are relative,
     *  so the sample data never goes stale. */
    private Task newTask(String title, String description, LocalDate dueDate, boolean completed) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setCompleted(completed);
        return task;
    }
}

package sg.edu.ntu.taskflow_api.controller;

import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sg.edu.ntu.taskflow_api.model.Task;
import sg.edu.ntu.taskflow_api.service.TaskService;

/** This is the AI Controller for AI endpoints. 
 * Task data is read from the service, never accepted from the caller. */
@RestController
public class AiController {

    /** Good practice: one shared prompt constant, so both endpoints cannot drift apart. */
    private static final String SUMMARY_SYSTEM_PROMPT =
            "You are a friendly and professional personal assistant. " +
            "You help users by reading a list of tasks and summarizing it. " +
            "Summarize the task in 1 line to tell how many total tasks they have. " +
            "Give a number of completed tasks, incomplete tasks, and tell which the most urgent task i.e. with nearest due date. " +
            "Keep your answers concise and practical. ";

    private final ChatClient chatClient;
    private final TaskService taskService;

    /** Spring injects the pre-configured builder, so no API key or URL is hardcoded here. */
    public AiController(ChatClient.Builder chatClientBuilder, TaskService taskService) {
        this.chatClient = chatClientBuilder.build();
        this.taskService = taskService;
    }

    /** GET /summary - good practice: entity() maps the reply into a typed record,
     *  so clients get real JSON fields instead of prose they must parse themselves. */
    @GetMapping("/summary")
    public TasksSummary summarizeTasks() {
        Collection<Task> tasks = taskService.findAllTasks().values();
        return chatClient.prompt()
            .system(SUMMARY_SYSTEM_PROMPT)
            .user(u -> u.text("Summarize this list of tasks: {tasks}. " +
                            "Summarize the tasks including total count, completed count, incomplete count, and the most urgent task.")
                        .param("tasks", formatTasks(tasks)))
            .call()
            .entity(TasksSummary.class);
    }

     /** GET /summary-text - plain prose summary. The system prompt sets the role,
     *  the user prompt carries the data, which is the recommended split. */
    @GetMapping("/summary-text")
    public String summary() {
        Collection<Task> tasks = taskService.findAllTasks().values();
        return chatClient.prompt()
            .system(SUMMARY_SYSTEM_PROMPT)
            .user("Here are the tasks:\n" + formatTasks(tasks))
            .call()
            .content();
    }

    /** Good practice: sends the model only the fields it needs, in a compact
     *  line-per-task shape. Keeps prompts small and avoids leaking internals. */
    private String formatTasks(Collection<Task> tasks) {
        if (tasks.isEmpty()) {
            return "(no tasks)";
        }
        return tasks.stream()
            .map(task -> "- " + task.getTitle()
                    + " | due " + task.getDueDate()
                    + " | " + (task.isCompleted() ? "completed" : "incomplete"))
            .collect(Collectors.joining("\n"));
    }
}

package sg.edu.ntu.taskflow_api.service;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;
import sg.edu.ntu.taskflow_api.exception.TaskNotFoundException;
import sg.edu.ntu.taskflow_api.model.Task;
import sg.edu.ntu.taskflow_api.repository.TaskRepository;

/** Holds the business rules. Knows nothing about HTTP, so it stays reusable and testable. */
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final AtomicLong idCounter = new AtomicLong();

    /** Good practice: constructor injection keeps the dependency final
     *  and makes the service easy to instantiate in a unit test. */
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /** Thin delegation. The repository already returns an unmodifiable copy. */
    public Map<Long, Task> findAllTasks() {
        return taskRepository.getTasks();
    }

    /** Good practice: turns a null lookup into a domain exception,
     *  so callers never have to null-check and the 404 is handled in one place. */
    public Task findTaskById(Long id) {
        Task task = taskRepository.getTask(id);
        if (task == null) {
            throw new TaskNotFoundException(id);
        }
        return task;
    }
 
    /** Good practice: the id is generated server-side with AtomicLong,
     *  so ids stay unique under concurrency and a client cannot choose its own. */
    public Task createTask(Task task) {
        Long id = idCounter.incrementAndGet();
        task.setId(id);
        taskRepository.addTask(id, task);
        return task;
    }

    /** Checks existence first so deleting an unknown id reports 404 instead of silently passing. */
    public void deleteTask(Long id) {
        Task existingTask = taskRepository.getTask(id);
        if (existingTask == null) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.removeTask(id);
    }

    /** Full PUT replacement: every field is copied, so an omitted field is cleared.
     *  Returns the stored object, not the request body, so the response is always the truth. */
    public Task updateTask(Long id, Task task) {
        Task existingTask = taskRepository.getTask(id);
        if (existingTask == null) {
            throw new TaskNotFoundException(id);
        }
        // Update the existing task with the new values from the provided task object
        existingTask.setId(id);        
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setCompleted(task.isCompleted());
        taskRepository.updateTask(id, existingTask);
        return existingTask;
    }

    /** A named action for a common state change, clearer than making clients PUT a whole task. */
    public Task markTaskAsComplete(Long id) {
        Task task = taskRepository.getTask(id);
        if (task == null) {
            throw new TaskNotFoundException(id);
        }
        task.setCompleted(true);
        taskRepository.updateTask(id, task);
        return task;
    }
}

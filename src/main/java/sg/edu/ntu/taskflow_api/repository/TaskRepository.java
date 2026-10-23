package sg.edu.ntu.taskflow_api.repository;

import org.springframework.stereotype.Repository;   
import java.util.Map;
import sg.edu.ntu.taskflow_api.model.Task;
import java.util.concurrent.ConcurrentHashMap;
    
/** In-memory store. ConcurrentHashMap keeps it safe under concurrent requests. */
@Repository
public class TaskRepository {

    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();

    /** Good practice: hands back an immutable snapshot, so callers cannot
     *  add or delete behind the service's back. Writes must go through the methods below. */
    public Map<Long, Task> getTasks() {
        return Map.copyOf(tasks);
    }

    /** Stores a new task. The id comes from the service, never from the client. */
    public void addTask(Long id, Task task) {
        tasks.put(id, task);
    }

    /** Returns null when absent. Deciding what "absent" means is the service's job. */
    public Task getTask(Long id) {
        return tasks.get(id);
    }

    /** Removes a task. Idempotent, so a repeated delete is harmless. */
    public void removeTask(Long id) {
        tasks.remove(id);
    }

    /** Overwrites an existing task. Kept separate from addTask to document intent. */
    public void updateTask(Long id, Task task) {
        tasks.put(id, task);
    }
}

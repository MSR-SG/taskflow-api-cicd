package sg.edu.ntu.taskflow_api.exception;

/** Good practice: a domain-specific unchecked exception. The service can signal
 *  "not found" without importing anything web-related. */
public class TaskNotFoundException extends RuntimeException {

    /** Builds the message from the id, so every 404 explains exactly what was missing. */
    public TaskNotFoundException(Long id) {
        super("Could not find task with id " + id);
    }
}

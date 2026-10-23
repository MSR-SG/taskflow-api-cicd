package sg.edu.ntu.taskflow_api.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import sg.edu.ntu.taskflow_api.model.Task;
import sg.edu.ntu.taskflow_api.service.TaskService;

/** Good practice: a thin controller layer. Every method delegates to the service
 *  and only decides the HTTP status, so no business rules leak in here. */
@RestController
@RequestMapping("/tasks")   
public class TaskController {

    private final TaskService taskService;

    /** Constructor injection on a final field. */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /** GET /tasks - returns (a safe copy) of every task. */
    @GetMapping
    public ResponseEntity<Map<Long, Task>> getAllTasks() {
        return new ResponseEntity<>(taskService.findAllTasks(), HttpStatus.OK);
    }

    /** GET /tasks/{id} - gets a task by id.
     * A missing id raises TaskNotFoundException, which becomes a 404. */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return new ResponseEntity<>(taskService.findTaskById(id), HttpStatus.OK);
    }

    /** POST /tasks - creates a new task. 
     * Good practice: returns 201 CREATED, not a blanket 200. */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return new ResponseEntity<>(taskService.createTask(task), HttpStatus.CREATED);
    }

    /** PUT /tasks/{id} - updates the task.
     * Id comes from the path, data from the body. */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return new ResponseEntity<>(taskService.updateTask(id, task), HttpStatus.OK);
    }

    /** DELETE /tasks/{id} - deletes the task by id.
     * ResponseEntity<Void> states plainly that there is no body. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /** PUT /tasks/{id}/complete - marks the task as complete. */
    @PutMapping("/{id}/complete")
    public ResponseEntity<Task> markTaskAsComplete(@PathVariable Long id) {
        return new ResponseEntity<>(taskService.markTaskAsComplete(id), HttpStatus.OK);
    }
}

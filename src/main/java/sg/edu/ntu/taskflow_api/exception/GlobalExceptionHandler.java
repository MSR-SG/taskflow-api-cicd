package sg.edu.ntu.taskflow_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Good practice: error handling lives in one place, so no controller
 *  needs a try/catch and every endpoint reports 404 the same way. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Maps the domain exception to 404, keeping HTTP concerns out of the service. */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFound(TaskNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}

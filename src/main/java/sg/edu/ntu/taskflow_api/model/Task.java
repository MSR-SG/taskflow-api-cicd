package sg.edu.ntu.taskflow_api.model;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

/** This is the pojo for the task.
 *  Good practice: LocalDate rather than String for dueDate, so dates are
 *  validated and comparable. @Data generates the getters Jackson needs. */
@Data
@NoArgsConstructor

public class Task {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private boolean completed;
       
}

package sg.edu.ntu.taskflow_api.controller;

/** Good practice: an immutable record as the AI response contract. 
 * Spring AI fills these fields from the model's reply.
 * This approach gives callers typed JSON instead of prose. */
public record TasksSummary(
    String summary,
    Integer countCompleted,
    Integer countIncomplete,
    String mostUrgentTask
) {}

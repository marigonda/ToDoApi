package todo.example.ToDoApi.controllers;

import java.util.List;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Metrics;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import todo.example.ToDoApi.model.ToDo;
import todo.example.ToDoApi.exceptions.ToDoNotFoundException;
import todo.example.ToDoApi.model.ToDoRepository;
import todo.example.ToDoApi.utils.ToDoCustomLoggingFilter;

@RestController
public class ToDoApiController {

    // In-memory "persistence" repo
    private final ToDoRepository repository;

    ToDoApiController(ToDoRepository repository) {
        this.repository = repository;
    }

    @Timed
    @GetMapping("/todo")
    /**
     * GET /todo URL mapping
     * List all todo's
     */
    List<ToDo> all() {
        Metrics.counter("method.timed", "method", "GET").increment();
        return repository.findAll();
    }

    @PostMapping("/todo")
    /**
     * POST /todo url mapping
     * Insert one task
     */
    ToDo newToDo(@RequestBody ToDo newToDo) {
        Metrics.counter("method.timed", "method", "POST").increment();
        return repository.save(newToDo);
    }

    @GetMapping("/todo/{id}")
    ToDo getToDo(@PathVariable Long id) {
        /**
         * GET url mapping
         * Get a single entry given it's id
         */
        Metrics.counter("method.timed", "method", "GET").increment();
        return repository.findById(id)
                .orElseThrow(() -> new ToDoNotFoundException(id));
    }

    @PutMapping("/todo/{id}")
    ToDo upsertToDo(@RequestBody ToDo newToDo, @PathVariable Long id) {
        /**
         * PUT /todo/id url mapping
         * What is done here is an "UPSERT": update if id exists, insert otherwise...)
         */
        Metrics.counter("method.timed", "method", "PUT").increment();
        return repository.findById(id)
                .map(ToDo -> {
                    ToDo.setTitle(newToDo.getTitle());
                    ToDo.setStatus(newToDo.getStatus());
                    return repository.save(ToDo);
                })
                .orElseGet(() -> {
                    newToDo.setId(id);
                    return repository.save(newToDo);
                });
    }

    @DeleteMapping("/todo/{id}")
    void deleteToDo(@PathVariable Long id) {
        /**
         * DEL /todo/id  url mapping
         * Just delete a given id
         */
        Metrics.counter("method.timed", "method", "DEL").increment();
        try {
            repository.deleteById(id);
        }
        catch (Exception e) {
            throw new ToDoNotFoundException(id);
        }
    }

    /* API Request Logging */
    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new ToDoCustomLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setIncludeHeaders(true);
        return loggingFilter;
    }

    @ControllerAdvice
    public class ToDoNotFoundWarnMsg {
        @ResponseBody
        @ExceptionHandler(ToDoNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        String todoNotFoundHandler(ToDoNotFoundException ex) {
            return ex.getMessage();
        }
    }
}
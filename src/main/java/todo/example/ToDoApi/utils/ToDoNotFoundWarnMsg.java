package todo.example.ToDoApi.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import todo.example.ToDoApi.exceptions.ToDoNotFoundException;

@ControllerAdvice
public class ToDoNotFoundWarnMsg {
    @ResponseBody
    @ExceptionHandler(ToDoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String todoNotFoundHandler(ToDoNotFoundException ex) {
        return ex.getMessage();
    }
}

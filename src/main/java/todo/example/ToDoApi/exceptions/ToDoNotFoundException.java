package todo.example.ToDoApi.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import todo.example.ToDoApi.ToDoApiApplication;

public class ToDoNotFoundException extends RuntimeException {

    public ToDoNotFoundException(long id){

        super("Não foi possível encontrar tarefa id='" + id + "'. Seu acesso foi registrado.");

        // Log error and/or unexpected behavior...
        Logger logger = LoggerFactory.getLogger(ToDoApiApplication.class);
        logger.warn("Customized exception.... ToDo id='" + id + "' not found");
    }
}

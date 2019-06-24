package todo.example.ToDoApi.model;

import org.springframework.data.jpa.repository.JpaRepository;
import todo.example.ToDoApi.model.ToDo;


public interface ToDoRepository extends JpaRepository<ToDo, Long> {

}
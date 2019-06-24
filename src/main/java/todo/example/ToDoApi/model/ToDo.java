package todo.example.ToDoApi.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ToDo {

    private @Id @GeneratedValue Long id;
    private String title;
    private  @Enumerated(EnumType.STRING) ToDoStatus status;

    public ToDo() {}

    public ToDo(String title, ToDoStatus status) {
        this.title = title;
        this.status = status;
    }

    public enum ToDoStatus {
        // Task status must be completed or pending
        completed, pending
    }
}
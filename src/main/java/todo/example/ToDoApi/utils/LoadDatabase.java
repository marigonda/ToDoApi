package todo.example.ToDoApi.utils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import todo.example.ToDoApi.model.ToDo;
import todo.example.ToDoApi.model.ToDoRepository;

@Configuration
@Slf4j
public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(ToDoRepository repository) {
        /**
         * Inicia a base de dados (em memória) com alguns dados para simular persistência
         */
        return args -> {
            log.info("Preloading " + repository.save(new ToDo("Tomar Café", ToDo.ToDoStatus.pending)));
            log.info("Preloading " + repository.save(new ToDo("Iniciar ToDoAPI", ToDo.ToDoStatus.pending)));
        };
    }
}

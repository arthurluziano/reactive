package com.luziano.reactive.controller;

import com.luziano.reactive.controller.data.TaskControllerTestData;
import com.luziano.reactive.model.Task;
import com.luziano.reactive.model.converter.TaskDTOConverter;
import com.luziano.reactive.repository.TaskRepository;
import com.luziano.reactive.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private TaskDTOConverter converter;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskController taskController;

    @Test
    void DADO_task_controller_DEVE_retornar_ok_QUANDO_sucesso() {
        Page<Task> page = TaskControllerTestData.page();

        when(taskService.findPaginated(any())).thenReturn(page);
        when(taskRepository.findAll(any(Pageable.class))).thenReturn(page);

        WebTestClient client = WebTestClient.bindToController(taskController).build();

        client.get()
                .uri("/task")
                .exchange()
                .expectStatus().isOk();
    }
}

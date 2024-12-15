
package com.example.task.controller;

import com.example.task.model.Task;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private List<Task> tasks = new ArrayList<>();

    public TaskController() {
        // Initialize with sample data
        Task task1 = new Task();
        task1.setId("1");
        task1.setName("機種変更");
        task1.setDescription("スマートフォンが古いので機種変更する");
        task1.setDue(LocalDate.of(2024, 12, 31));
        task1.setPri("2");
        task1.setStatus("new");
        tasks.add(task1);
        // Add other sample tasks...
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return tasks;
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable String id) {
        return tasks.stream().filter(task -> task.getId().equals(id)).findFirst().orElse(null);
    }
}

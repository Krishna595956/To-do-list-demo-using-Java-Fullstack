package com.example.todo_list.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.todo_list.model.Todo;
import com.example.todo_list.service.TodoService;

@Controller
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    // Get all tasks and display in a view (HTML page)
    @GetMapping
    public String getAllTasks(Model model) {
        List<Todo> todos = todoService.getAllTodos();
        model.addAttribute("todos", todos);
        return "todo_list";  // Returns the view (todo_list.html)
    }

    // Get a specific task by ID and display its details
    @GetMapping("/{id}")
    public String getTaskById(@PathVariable Long id, Model model) {
        Optional<Todo> todo = todoService.getTodoById(id);
        if (todo.isPresent()) {
            model.addAttribute("todo", todo.get());
            return "todo_detail";  // Returns the view (todo_detail.html)
        } else {
            return "redirect:/todos";  // Redirects to the list of todos if not found
        }
    }

    // Show form to create a new task (GET request)
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("todo", new Todo());  // Create an empty Todo object for the form
        return "todo_form";  // Returns the view (todo_form.html)
    }

    // Create a new task (POST request)
    @PostMapping("/create")
    public String createTask(@ModelAttribute Todo todo) {
        todoService.createTodo(todo);
        return "redirect:/todos";  // Redirects to the list of todos after creation
    }

    // Show form to update an existing task (GET request)
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Optional<Todo> todo = todoService.getTodoById(id);
        if (todo.isPresent()) {
            model.addAttribute("todo", todo.get());
            return "todo_form";  // Returns the view for editing the task (todo_form.html)
        } else {
            return "redirect:/todos";  // Redirects to the list of todos if not found
        }
    }

    // Update an existing task (POST request)
    @PostMapping("/update/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute Todo todoDetails) {
        Optional<Todo> existingTodo = todoService.getTodoById(id);

        if (existingTodo.isPresent()) {
            Todo todo = existingTodo.get();
            todo.setTitle(todoDetails.getTitle());
            todo.setDescription(todoDetails.getDescription());
            todo.setCompleted(todoDetails.isCompleted());
            todoService.updateTodo(id, todo);
            return "redirect:/todos";  // Redirect to the list after updating
        } else {
            return "redirect:/todos";  // Redirect if task is not found
        }
    }

    // Delete a task by ID and return to the task list
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        Optional<Todo> existingTodo = todoService.getTodoById(id);

        if (existingTodo.isPresent()) {
            todoService.deleteTodo(id);
        }
        return "redirect:/todos";  // Redirect to the list after deletion
    }
}

package com.example.todo_list.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.todo_list.model.Todo;
import com.example.todo_list.repository.TodoRepository;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    // Get all todos from the database
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    // Get a specific todo by its ID
    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    // Create a new todo
    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    // Update an existing todo
    public Todo updateTodo(Long id, Todo todoDetails) {
        // Find the todo by ID and update the fields
        Optional<Todo> existingTodo = todoRepository.findById(id);

        if (existingTodo.isPresent()) {
            Todo existing = existingTodo.get();
            existing.setTitle(todoDetails.getTitle());
            existing.setDescription(todoDetails.getDescription());
            existing.setCompleted(todoDetails.isCompleted());
            return todoRepository.save(existing);  // Save the updated todo
        }

        return null;  // If the todo with the given ID doesn't exist
    }

    // Delete a todo by its ID
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
}


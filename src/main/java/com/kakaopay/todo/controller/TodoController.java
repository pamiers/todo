package com.kakaopay.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.todo.dao.TodoItem;
import com.kakaopay.todo.dao.manager.TodoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {
    @Autowired
    private TodoManager todoManager;

    @PostMapping
    public void addTodo(@RequestBody TodoItem todoItem) {
        System.out.println("called");
        todoManager.addTodo(todoItem);
    }

    @PostMapping("/{id}")
    public void updateTodo(@PathVariable Integer id, @RequestBody TodoItem todoItem) {
        System.out.println("update");
        todoManager.updateTodo(id, todoItem);
    }

    @PostMapping("/{id}/complete")
    public boolean complete(@PathVariable Integer id) {
        System.out.println("complete!");
        return todoManager.complete(id);
    }

    @GetMapping
    public ResponseEntity getTodoItems() {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("get Todo!");
        return todoManager.getTodoItems();
    }
}

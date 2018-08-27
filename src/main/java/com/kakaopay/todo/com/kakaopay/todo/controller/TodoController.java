package com.kakaopay.todo.com.kakaopay.todo.controller;

import com.kakaopay.todo.dao.manager.TodoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/todos")
public class TodoController {
    @Autowired
    private TodoManager todoManager;

    @PostMapping
    public void addTodo() {
        System.out.println("called");
    }

    @GetMapping
    public String hello() {
        return "Hello!";
    }
}

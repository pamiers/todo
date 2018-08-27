package com.kakaopay.todo.dao.manager;

import com.kakaopay.todo.dao.TodoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TodoManagerImpl implements TodoManager{
    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void addTodo() {
        System.out.println("add");
    }
}

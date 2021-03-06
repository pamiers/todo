package com.kakaopay.todo.dao.manager;

import com.kakaopay.todo.dao.TodoItem;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TodoManager {
    ResponseEntity addTodo(TodoItem todoItem);
    boolean updateTodo(Integer id, TodoItem todoItem);
    List<TodoItem> getTodoItems();
    boolean complete(Integer id);

}

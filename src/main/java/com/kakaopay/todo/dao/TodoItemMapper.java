package com.kakaopay.todo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface TodoItemMapper {
    void insertTodo(TodoItem todoItem);
    List<TodoItem> getTodoItems();
    TodoItem selectTodoItem(Integer id);
    void updateTodoItem(Integer id, @Param("param") Map<String, Object> param);
}

package com.kakaopay.todo.dao.manager;

import com.kakaopay.todo.dao.TodoItem;
import com.kakaopay.todo.dao.TodoItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class TodoManagerImpl implements TodoManager {
    @Autowired
    private TodoItemMapper itemMapper;

    @Override
    public boolean addTodo(TodoItem todoItem) {
        try {
            itemMapper.insertTodo(todoItem);
            System.out.println("add success!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public boolean updateTodo(Integer id, TodoItem todoItem) {
        try {
            TodoItem origin = itemMapper.selectTodoItem(id);
            if (origin != null) {
                if (todoItem.getContents() != null) {
                    origin.setContents(todoItem.getContents());
                }
                if (todoItem.getRefId() != null) {
                    String [] ref = todoItem.getRefId().split("[,]");
                    for (String r : ref) {
                        if(Integer.parseInt(r) == id) {
                            System.out.println("You can not refer to the same task.");
                            return false;
                        }
                        TodoItem item = itemMapper.selectTodoItem(Integer.parseInt(r));
                        if (item.getCompletedDate() == null && origin.getCompletedDate() != null) {
                            origin.setCompletedDate(null);
                        }
                    }
                    origin.setRefId(todoItem.getRefId());
                }
                origin.setModifiedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                Map<String, Object> param = getMap(origin);
                itemMapper.updateTodoItem(id, param);
                System.out.println("update " + id + " success!!");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public List<TodoItem> getTodoItems() {
        System.out.println("get Todos");
        List<TodoItem> todoItems = itemMapper.getTodoItems();

        for (TodoItem todoItem : todoItems) {
            System.out.println(todoItem.toString());
        }

        return todoItems;
    }

    @Override
    public boolean complete(Integer id) {
        System.out.println("complete called");
        TodoItem todoItem = itemMapper.selectTodoItem(id);

        Optional ref = Optional.ofNullable(todoItem.getRefId());
        if (ref.isPresent()) {
            String[] refArr = ((String) ref.get()).split("[,]");
            for (String refId : refArr) {
                TodoItem refItem = itemMapper.selectTodoItem(Integer.parseInt(refId));
                if (refItem.getCompletedDate() == null) {
                    return false;
                }
            }
        }

        todoItem.setCompletedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        Map<String, Object> param = getMap(todoItem);
        itemMapper.updateTodoItem(id, param);

        return true;
    }

    private Map<String, Object> getMap(TodoItem todoItem) {
        Map<String, Object> param = new HashMap<>();
        Optional.ofNullable(todoItem.getId()).ifPresent(id -> {
            param.put("id", id);
        });

        Optional.ofNullable(todoItem.getContents()).ifPresent(c -> {
            param.put("contents", c);
        });

        Optional.ofNullable(todoItem.getCreatedDate()).ifPresent(d -> {
            param.put("createdDate", d);
        });

        Optional.ofNullable(todoItem.getModifiedDate()).ifPresent(d -> {
            param.put("modifiedDate", d);
        });

        Optional.ofNullable(todoItem.getCompletedDate()).ifPresent(d -> {
            param.put("completedDate", d);
        });

        Optional.ofNullable(todoItem.getRefId()).ifPresent(r -> {
            param.put("refId", r);
        });

        return param;
    }
}

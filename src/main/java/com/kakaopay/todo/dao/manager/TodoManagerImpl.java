package com.kakaopay.todo.dao.manager;

import com.kakaopay.todo.dao.TodoItem;
import com.kakaopay.todo.dao.TodoItemMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TodoManagerImpl implements TodoManager {
    @Autowired
    private TodoItemMapper itemMapper;

    private HttpHeaders responseHeaders;
    public TodoManagerImpl() {
        responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json;charset=UTF-8");
    }

    @Override
    public ResponseEntity addTodo(TodoItem todoItem) {
        JSONObject outputObject = new JSONObject();

        try {
            String contents = todoItem.getContents();
            String [] arr = contents.split("[ ]");
            if (arr.length > 1) {
                String [] sub = arr[1].trim().split("[@]");
                String refId = Stream.of(sub).filter(s -> s != null && !s.isEmpty()).collect(Collectors.joining(","));
                todoItem.setRefId(refId);
                System.out.println("refId: " + refId);
            }
            todoItem.setComplete(false);
            todoItem.setCreatedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            itemMapper.insertTodo(todoItem);
            Map<String, Object> map = getMap(todoItem);
            outputObject.put("meta", map);
            System.out.println("add success!");
            return new ResponseEntity<>(outputObject.toString(), responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(outputObject.toString(), responseHeaders, HttpStatus.BAD_REQUEST);
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
                        if (item.isComplete() == false && origin.isComplete()) {
                            origin.setComplete(false);
                        }
                    }
                    origin.setRefId(todoItem.getRefId());
                }
                origin.setModifiedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                origin.setComplete(false);
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

        JSONArray jsonArray = new JSONArray();
        for (TodoItem todoItem : todoItems) {
            Map<String, Object> map = getMap(todoItem);
            jsonArray.put(map);
            System.out.println(todoItem.toString());
        }

        System.out.println(jsonArray.toString());
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
                if (!refItem.isComplete()) {
                    return false;
                }
            }
        }

        todoItem.setComplete(true);
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

        param.put("complete", todoItem.isComplete());

        Optional.ofNullable(todoItem.getRefId()).ifPresent(r -> {
            param.put("refId", r);
        });

        return param;
    }
}

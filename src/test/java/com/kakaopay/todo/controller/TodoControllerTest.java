package com.kakaopay.todo.controller;

import com.kakaopay.todo.dao.TodoItem;
import com.kakaopay.todo.dao.manager.TodoManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TodoManager todoManager;


    @Test
    public void addTodo() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        Map<String, String> map = new HashMap<>();
        map.put("contents", "hello");
        map.put("createdDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<TodoItem> responseEntity = restTemplate.postForEntity("/todos", request, TodoItem.class);
        System.out.println("add Todo: " + responseEntity);
    }

}

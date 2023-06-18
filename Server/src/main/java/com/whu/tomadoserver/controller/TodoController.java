package com.whu.tomadoserver.controller;

import com.whu.tomadoserver.entity.TodoItem;
import com.whu.tomadoserver.service.TodoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author hiroxzwang
 * @create 2023/6/17 14:42
 */
@Api(description = "待办事项管理")
@RestController
@RequestMapping("todos")
public class TodoController {
    @Autowired
    TodoService todoService;

    // get: localhost:8088/todos/1
    @ApiOperation("根据Id查询待办事项")
    @GetMapping("/{id}")
    public ResponseEntity<TodoItem> getTodo(@ApiParam("待办事项Id")@PathVariable long id){
        TodoItem result = todoService.getTodo(id);
        if(result==null) {
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(result);
        }
    }

    // get: localhost:8088/todos
    // get: localhost:8088/todos?name=作业
    // get: localhost:8088/todos?name=作业&&complete=true
    @ApiOperation("根据条件查询待办事项")
    @GetMapping("")
    public ResponseEntity<List<TodoItem>> findTodos(@ApiParam("待办事项名称")String taskName, @ApiParam("是否完成")Boolean isDone){
        List<TodoItem> result = todoService.findTodos(taskName, isDone);
        return ResponseEntity.ok(result);
    }

    @ApiOperation("添加待办事项")
    @PostMapping("")
    public ResponseEntity<String> addTodo(@RequestBody TodoItem todo){
        try {
            TodoItem result = todoService.addTodo(todo);
            return ResponseEntity.ok(""+result.getId());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @ApiOperation("修改待办事项")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTodo(@PathVariable long id,@RequestBody TodoItem todo){
        todoService.updateTodo(id,todo);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("删除待办事项")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable long id){
        todoService.deleteTodo(id);
        return ResponseEntity.ok().build();
    }
}

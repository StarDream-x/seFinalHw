package com.whu.tomadoserver.controller;

import com.whu.tomadoserver.entity.NodoItem;
import com.whu.tomadoserver.service.NodoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 孔德昱
 * @date 2023/6/18 20:19 星期日
 */

@Api(description = "待办事项管理")
@RestController
@RequestMapping("nodos")
public class NodoController {
    @Autowired
    NodoService nodoService;

    // get: localhost:8088/nodos/1
    @ApiOperation("根据Id查询待办事项")
    @GetMapping("/{id}")
    public ResponseEntity<NodoItem> getNodo(@ApiParam("待办事项Id")@PathVariable long id){
        NodoItem result = nodoService.getNodo(id);
        if(result==null) {
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(result);
        }
    }
    //根据userId查询待办事项
    // get: localhost:8088/nodos/user/1
    @ApiOperation("根据userId查询待办事项")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NodoItem>> getNodoByUserId(@ApiParam("用户Id")@PathVariable long userId){
        List<NodoItem> result = nodoService.getNodoByUserId(userId);
        if(result==null) {
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(result);
        }
    }

    // get: localhost:8088/nodos
    // get: localhost:8088/nodos?name=作业
    // get: localhost:8088/nodos?name=作业&&complete=true
    @ApiOperation("根据条件查询待办事项")
    @GetMapping("")
    public ResponseEntity<List<NodoItem>> findNodos(@ApiParam("待办事项名称")String taskName, @ApiParam("是否完成")Boolean isDone){
        List<NodoItem> result = nodoService.findNodos(taskName, isDone);
        return ResponseEntity.ok(result);
    }

    @ApiOperation("添加待办事项")
    @PostMapping("")
    public ResponseEntity<NodoItem> addNodo(@RequestBody NodoItem nodo){
        try {
            NodoItem result = nodoService.addNodo(nodo);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(nodo);
        }
    }


    @ApiOperation("修改待办事项")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateNodo(@PathVariable long id,@RequestBody NodoItem nodo){
        nodoService.updateNodo(id,nodo);
        return ResponseEntity.ok().build();
    }

    @ApiOperation("删除待办事项")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNodo(@PathVariable long id){
        nodoService.deleteNodo(id);
        return ResponseEntity.ok().build();
    }
}

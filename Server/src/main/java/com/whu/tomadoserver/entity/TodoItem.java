package com.whu.tomadoserver.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author hiroxzwang
 * @create 2023/6/17 14:12
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "待办事项实体")
public class TodoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    long id;

    @Value("0")
    @ApiModelProperty("userId")
    long userId;

    @ApiModelProperty("isDone")
    @Type(type = "boolean")
    private boolean isDone = false;

    @ApiModelProperty("taskName")
    private String taskName;

    @ApiModelProperty("taskTime")
    private String taskTime;

    @ApiModelProperty("taskNotes")
    private String taskNotes;

    @ApiModelProperty("taskCycleTot")
    private int taskCycleTot;

    @ApiModelProperty("taskCycleCount")
    private int taskCycleCount;

    @ApiModelProperty("taskRepeat")
    @Type(type = "boolean")
    private boolean taskRepeat;
}

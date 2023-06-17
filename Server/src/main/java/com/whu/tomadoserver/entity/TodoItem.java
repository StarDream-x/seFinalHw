package com.whu.tomadoserver.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author hiroxzwang
 * @create 2023/6/17 14:12
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description="待办事项实体")
public class TodoItem {

    @Id
    @ApiModelProperty("id")
    long id;

    @ApiModelProperty("isDone")
    private boolean isDone = false;

    @ApiModelProperty("taskName")
    private String taskName;

    @ApiModelProperty("taskTime")
    private String taskTime;

    @ApiModelProperty("taskNotes")
    private String taskNotes;

}

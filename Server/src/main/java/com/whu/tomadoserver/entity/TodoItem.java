package com.whu.tomadoserver.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

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

    @Value("0")
    @ApiModelProperty("userId")
    long userId;

    @ApiModelProperty("isDone")
    private boolean isDone = false;

    @ApiModelProperty("taskName")
    private String taskName;

    @ApiModelProperty("taskTime")
    private String taskTime;

    @ApiModelProperty("taskNotes")
    private String taskNotes;

}

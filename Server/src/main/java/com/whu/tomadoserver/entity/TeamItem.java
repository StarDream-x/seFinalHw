package com.whu.tomadoserver.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author hiroxzwang
 * @create 2023/6/17 16:31
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description="团队信息")
public class TeamItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    long id;

    @ApiModelProperty("团队名")
    String teamName;

    @Value("0")
    @ApiModelProperty("idList")
    String idList;
}

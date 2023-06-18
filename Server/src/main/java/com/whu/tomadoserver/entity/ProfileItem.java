package com.whu.tomadoserver.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;


/**
 * @author 孔德昱
 * @date 2023/6/17 13:13 星期六
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description="论文实体")
public class ProfileItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("ID")
    long userId;

    @ApiModelProperty("用户名")
    String username;

    @ApiModelProperty("密码")
    String password;

    @Value("0")
    @ApiModelProperty("ManageTeams")
    String ManageTeams;

    @Value("0")
    @ApiModelProperty("MemTeams")
    String MemTeams;
}

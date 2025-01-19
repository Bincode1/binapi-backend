package com.bin.binapibackend.model;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * @TableName user
 */
@TableName(value = "user")
@Data
public class UserVO implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 账号
     */
    private String useraccount;

    /**
     * 头像
     */
    private String useravatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户角色：0-普通用户，1-管理员
     */
    private Integer userrole;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 更新时间
     */
    private Date updatetime;
    /**
     * accessKey
     */
    private String accesskey;

//    /**
//     * secretkey
//     */
//    private String secretkey;


    /**
     * 是否删除
     */
    @TableLogic
    private Integer isdelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
package com.bin.binapibackend.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户注册请求体
 */
@Data
public class RegisterRequest implements Serializable {

    /**
     * 用户名
     */
    private String username;

    /**
     * 账号
     */
    private String useraccount;


    /**
     * 密码
     */
    private String userpassword;

    /**
     * 校验密码
     */
    private String checkpassword;


    private static final long serialVersionUID = 1L;

}


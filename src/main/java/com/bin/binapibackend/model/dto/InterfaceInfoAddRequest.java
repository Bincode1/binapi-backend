package com.bin.binapibackend.model.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName interface_info
 */
@TableName(value = "interface_info")
@Data
public class InterfaceInfoAddRequest implements Serializable {
    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;
    /**
     * 请求参数
     */
    private String requestparams;


    /**
     * 创建人
     */
    private Long userid;

    /**
     * 请求头
     */
    private String requestheader;

    /**
     * 响应头
     */
    private String responseheader;


    private static final long serialVersionUID = 1L;
}
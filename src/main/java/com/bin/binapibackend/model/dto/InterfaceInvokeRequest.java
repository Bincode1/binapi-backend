package com.bin.binapibackend.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * 接口调用请求
 */
@TableName(value = "interface_info")
@Data
public class InterfaceInvokeRequest implements Serializable {
    /**
     * 接口id
     */
    private Long id;
    /**
     * 用户请求参数
     */
    private String userRequestparams;


    private static final long serialVersionUID = 1L;
}
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
public class InterfaceCostRequest implements Serializable {
    /**
     * 接口id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
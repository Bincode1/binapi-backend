package com.bin.binapibackend.model.dto.userInterfaceInfo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口调用请求
 */
@TableName(value = "interface_info")
@Data
public class UserInterfaceQueryRequest implements Serializable {
    /**
     * 接口id
     */
    private Long id;

    /**
     * 总调用次数
     */
    private Integer totalnum;

    /**
     * 剩余调用次数
     */
    private Integer leftnum;

    /**
     * 调用用户id
     */
    private Long userid;

    /**
     * 调用接口id
     */
    private Long interfaceinfoid;


    /**
     * 0-正常； 1-禁用
     */
    private Integer status;


    private static final long serialVersionUID = 1L;
}
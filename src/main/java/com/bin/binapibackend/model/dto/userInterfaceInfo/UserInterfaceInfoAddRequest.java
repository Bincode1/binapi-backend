package com.bin.binapibackend.model.dto.userInterfaceInfo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName interface_info
 */
@TableName(value = "interface_info")
@Data
public class UserInterfaceInfoAddRequest implements Serializable {
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



    private static final long serialVersionUID = 1L;
}
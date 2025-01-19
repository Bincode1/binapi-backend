package com.bin.binapibackend.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.bin.bincommon.model.InterfaceInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口信息封装视图
 */
@Data
public class InterfaceInfoVO extends InterfaceInfo implements Serializable {

    private Integer totalNum;


    private static final long serialVersionUID = 1L;
}

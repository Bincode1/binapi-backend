package com.bin.binapibackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页请求参数
 */
@Data
public class PageRequest implements Serializable {

    private static final long serialVersionUID = -441759562761520384L;

    /**
     * 页号
     */
    private long pageNum;

    /**
     * 页面大小
     */
    private long pageSize;
}

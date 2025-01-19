package com.bin.binapibackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 */
@Data
public class IdRequest implements Serializable {


    private static final long serialVersionUID = 1L;

    private Long id;

}

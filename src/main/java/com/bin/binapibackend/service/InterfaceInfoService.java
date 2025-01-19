package com.bin.binapibackend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.binapibackend.model.UserVO;
import com.bin.binapibackend.model.dto.InterfaceInfoUpdateRequest;
import com.bin.bincommon.model.InterfaceInfo;

import java.util.List;

/**
 * @author 26961
 * @description 针对表【interface_info】的数据库操作Service
 * @createDate 2024-10-16 20:34:09
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    List<InterfaceInfo> getAllInterface();
    InterfaceInfo getInterfaceById(long id);

    IPage<InterfaceInfo> getAllInterfaceByPage(long pageNum, long pageSize);

    boolean addInterface(InterfaceInfo interfaceInfo, UserVO loginUser);

    boolean updateInterface(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest, UserVO loginUser);

}

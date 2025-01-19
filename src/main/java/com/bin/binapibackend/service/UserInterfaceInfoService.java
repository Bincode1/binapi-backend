package com.bin.binapibackend.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.binapibackend.model.UserVO;
import com.bin.binapibackend.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;
import com.bin.bincommon.model.UserInterfaceInfo;

import java.util.List;

/**
 * @author 26961
 * @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
 * @createDate 2024-12-26 13:22:27
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    List<UserInterfaceInfo> getAllUserInterface();

    UserInterfaceInfo getUserInterfaceById(long id);

    IPage<UserInterfaceInfo> getAllUserInterfaceByPage(long pageNum, long pageSize);

    boolean addUserInterface(UserInterfaceInfo userInterfaceInfo, UserVO loginUser);

    boolean updateUserInterface(UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest, UserVO loginUser);

    /**
     * 调用接口统计
     *
     * @param interfaceId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceId, long userId);

}

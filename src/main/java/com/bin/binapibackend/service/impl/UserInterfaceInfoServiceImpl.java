package com.bin.binapibackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.binapibackend.common.ErrorCode;
import com.bin.binapibackend.exception.BusinessException;
import com.bin.binapibackend.mapper.UserInterfaceInfoMapper;
import com.bin.binapibackend.model.UserVO;
import com.bin.binapibackend.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;
import com.bin.binapibackend.service.UserInterfaceInfoService;
import com.bin.binapibackend.service.UserService;
import com.bin.bincommon.model.UserInterfaceInfo;
import jakarta.annotation.Resource;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 26961
 * @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service实现
 * @createDate 2024-12-26 13:22:27
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo> implements UserInterfaceInfoService {

    @Autowired
    UserService userService;

    @Resource
    UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Override
    public List<UserInterfaceInfo> getAllUserInterface() {
        List<UserInterfaceInfo> list = this.list();
        return list;
    }

    @Override
    public UserInterfaceInfo getUserInterfaceById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return this.getById(id);
    }

    @Override
    public IPage<UserInterfaceInfo> getAllUserInterfaceByPage(long pageNum, long pageSize) {
        // 创建分页对象
        IPage<UserInterfaceInfo> userInterfacePage = new Page<>(pageNum, pageSize);

        // 使用分页工具进行查询
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        // 这里可以根据需要添加过滤条件

        // 执行分页查询
        page(userInterfacePage, queryWrapper);

        return userInterfacePage;
    }

    @Override
    public boolean addUserInterface(UserInterfaceInfo userInterfaceInfo, UserVO loginUser) {
        // 1. 参数为空不允许创建
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        if (userInterfaceInfo.getInterfaceinfoid() <= 0 || userInterfaceInfo.getUserid() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "借口或用户不存在");
        }
        if (userInterfaceInfo.getLeftnum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不能小于0");
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userInterfaceInfo.getId());
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重复的接口");
        }
        userInterfaceInfo.setUserid(loginUser.getId());
        return this.save(userInterfaceInfo);
    }

    @Override
    public boolean updateUserInterface(UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest, UserVO loginUser) {
        if (userInterfaceInfoUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = userInterfaceInfoUpdateRequest.getId();
        Integer leftnum = userInterfaceInfoUpdateRequest.getLeftnum();
        if (id <= 0 || leftnum < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo oldUserInterfaceInfo = this.getById(id);
        if (oldUserInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "用户接口关系不存在，无法更新");
        }
        UserInterfaceInfo updateInterfaceInfo = new UserInterfaceInfo();
        try {
            BeanUtils.copyProperties(updateInterfaceInfo, userInterfaceInfoUpdateRequest);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return this.updateById(updateInterfaceInfo);
    }


    @Override
    public boolean invokeCount(long interfaceId, long userId) {
        if (interfaceId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 加锁
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceinfoId", interfaceId).eq("userId", userId);
        updateWrapper.gt("leftNum", 0);
        updateWrapper.setSql("leftNum = leftNum - 1 , totalNum = totalNum + 1");

        return this.update(updateWrapper);
    }

}





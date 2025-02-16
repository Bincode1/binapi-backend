package com.bin.binapibackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.binapibackend.common.ErrorCode;
import com.bin.binapibackend.common.RedisUtils;
import com.bin.binapibackend.exception.BusinessException;
import com.bin.binapibackend.mapper.InterfaceInfoMapper;
import com.bin.binapibackend.model.UserVO;
import com.bin.binapibackend.model.dto.InterfaceInfoUpdateRequest;
import com.bin.binapibackend.service.InterfaceInfoService;
import com.bin.binapibackend.service.UserService;
import com.bin.bincommon.model.InterfaceInfo;
import jakarta.annotation.Resource;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author 26961
 * @description 针对表【interface_info】的数据库操作Service实现
 * @createDate 2024-10-16 20:34:09
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo> implements InterfaceInfoService {

    @Autowired
    UserService userService;

    @Autowired
    RedisUtils redisUtils;
    @Override
    public List<InterfaceInfo> getAllInterface() {
        List<InterfaceInfo> list = this.list();
        return list;
    }

    @Override
    public InterfaceInfo getInterfaceById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        return this.getById(id);
    }

    @Override
    public IPage<InterfaceInfo> getAllInterfaceByPage(long pageNum, long pageSize) {
        String key = ""+pageNum;
        Object o = redisUtils.get(key);
        if (o != null) {
            return (IPage<InterfaceInfo>) o;
        }
        // 创建分页对象
        IPage<InterfaceInfo> interfacePage = new Page<>(pageNum, pageSize);

        // 使用分页工具进行查询
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        // 这里可以根据需要添加过滤条件

        // 执行分页查询
        page(interfacePage, queryWrapper);
        redisUtils.save(key, interfacePage);
        return interfacePage;
    }

    @Override
    public boolean addInterface(InterfaceInfo interfaceInfo, UserVO loginUser) {
        // 1. 参数为空不允许创建
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2. 是否登录、未登录不允许创建
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_AUTH, "未登录");
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", interfaceInfo.getName());
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "存在重复的接口");
        }
        interfaceInfo.setUserid(loginUser.getId());
        return this.save(interfaceInfo);
    }

    @Override
    public boolean updateInterface(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest, UserVO loginUser) {
        if (interfaceInfoUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = interfaceInfoUpdateRequest.getId();
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR, "接口不存在，无法更新");
        }
        if (!oldInterfaceInfo.getUserid().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH, "只有接口的创建者才可以修改");
        }
        InterfaceInfo updateInterfaceInfo = new InterfaceInfo();
        try {
            BeanUtils.copyProperties(updateInterfaceInfo, interfaceInfoUpdateRequest);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        return this.updateById(updateInterfaceInfo);
    }


}





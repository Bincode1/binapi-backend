package com.bin.binapibackend.service.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bin.binapibackend.common.ErrorCode;
import com.bin.binapibackend.exception.BusinessException;
import com.bin.binapibackend.mapper.UserMapper;
import com.bin.bincommon.model.User;
import com.bin.bincommon.service.InnerUserService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    UserMapper userMapper;

    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey);
        return userMapper.selectOne(queryWrapper);
    }
}

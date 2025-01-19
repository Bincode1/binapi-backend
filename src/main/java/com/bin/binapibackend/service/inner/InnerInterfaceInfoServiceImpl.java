package com.bin.binapibackend.service.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bin.binapibackend.common.ErrorCode;
import com.bin.binapibackend.exception.BusinessException;
import com.bin.binapibackend.mapper.InterfaceInfoMapper;
import com.bin.bincommon.model.InterfaceInfo;
import com.bin.bincommon.service.InnerInterfaceInfoService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        queryWrapper.eq("method", method);
        return interfaceInfoMapper.selectOne(queryWrapper);

    }
}

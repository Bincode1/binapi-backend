package com.bin.binapibackend.controller;

import com.bin.binapibackend.common.BaseResponse;
import com.bin.binapibackend.common.ResultUtils;
import com.bin.binapibackend.mapper.UserInterfaceInfoMapper;
import com.bin.binapibackend.model.InterfaceInfoVO;
import com.bin.binapibackend.service.InterfaceInfoService;
import com.bin.bincommon.model.InterfaceInfo;
import com.bin.bincommon.model.UserInterfaceInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/top/interface/invoke")
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo() {
        // 获取到 前limit 多调用的 接口信息，包括 interfaceinfoid 和 sum(totallNum)
        List<UserInterfaceInfo> userInterfaceInfoList = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(8);
        userInterfaceInfoList.stream().forEach(System.out::println);

        // 关联查询
        List<InterfaceInfoVO> collect = userInterfaceInfoList.stream().map(userInterfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            Long interfaceinfoid = userInterfaceInfo.getInterfaceinfoid();

            InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceinfoid);
            if (interfaceInfo == null) {
                return null;
            }
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            interfaceInfoVO.setTotalNum(userInterfaceInfo.getTotalnum());
            return interfaceInfoVO;
        }).collect(Collectors.toList());



        return ResultUtils.success(collect);
    }


}

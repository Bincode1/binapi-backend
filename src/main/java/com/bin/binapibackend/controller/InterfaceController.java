package com.bin.binapibackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bin.binapibackend.common.BaseResponse;
import com.bin.binapibackend.common.ErrorCode;
import com.bin.binapibackend.common.ResultUtils;
import com.bin.binapibackend.exception.BusinessException;
import com.bin.binapibackend.mapper.UserInterfaceInfoMapper;
import com.bin.binapibackend.model.UserVO;
import com.bin.binapibackend.model.dto.*;
import com.bin.binapibackend.service.InterfaceInfoService;
import com.bin.binapibackend.service.UserService;
import com.bin.binapiclientsdk.client.BinApiClient;
import com.bin.bincommon.model.InterfaceInfo;
import com.bin.bincommon.model.User;
import com.bin.bincommon.model.UserInterfaceInfo;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interface")
public class InterfaceController {

    @Autowired
    InterfaceInfoService interfaceInfoService;

    @Autowired
    UserService userService;

    @Resource
    BinApiClient binApiClient;
    @Autowired
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    /**
     * 获取所有接口
     *
     * @return
     */
    @GetMapping("/getAllInterface")
    public BaseResponse<List<InterfaceInfo>> getAllInterface() {

        return ResultUtils.success(interfaceInfoService.getAllInterface());
    }

    /**
     * 分页获取所有接口
     *
     * @return
     */
    @GetMapping("/page/list")
    public BaseResponse<IPage<InterfaceInfo>> getAllInterfaceByPage(PageRequest pageRequest) {
        if (pageRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long pageNum = pageRequest.getPageNum();
        long pageSize = pageRequest.getPageSize();
        return ResultUtils.success(interfaceInfoService.getAllInterfaceByPage(pageNum, pageSize));
    }

    /**
     * 获取所有接口
     *
     * @return
     */
    @GetMapping("/getOne")
    public BaseResponse<InterfaceInfo> getInterfaceById(long id) {

        return ResultUtils.success(interfaceInfoService.getInterfaceById(id));
    }

    /**
     * 添加接口
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> AddInterface(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        UserVO loginUser = userService.getCurrentUser(request);
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        try {
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoAddRequest);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return ResultUtils.success(interfaceInfoService.addInterface(interfaceInfo, loginUser));
    }

    /**
     * 更新接口
     *
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> UpdateInterface(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest, HttpServletRequest request) {
        if (interfaceInfoUpdateRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "只有管理员才可以修改");
        }
        UserVO loginUser = userService.getCurrentUser(request);
        return ResultUtils.success(interfaceInfoService.updateInterface(interfaceInfoUpdateRequest, loginUser));
    }

    /**
     * 删除接口
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> DeleteInterface(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        // 必须得是管理员才可以删除接口
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "只有管理员才可以删除");
        }
        InterfaceInfo deleteInterface = interfaceInfoService.getById(deleteRequest.getId());

        // 同时只有接口的拥有者才可以删
        UserVO loginUser = userService.getCurrentUser(request);
        if (!deleteInterface.getUserid().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH, "只有接口的创建者才可以删除");
        }
        return ResultUtils.success(interfaceInfoService.removeById(deleteInterface));
    }

    /**
     * 发布接口
     *
     * @param idRequest
     * @param request
     * @return
     */

    @PostMapping("/online")
    public BaseResponse<Boolean> OnlineInterface(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        // 只有管理员才可以操作
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "只有管理员才可以发布");
        }
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long id = idRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // todo 判断接口是否可以调用
        com.bin.binapiclientsdk.model.User user = new com.bin.binapiclientsdk.model.User();
        user.setUsername("binbin");
        String result = binApiClient.getUsernameByPost(user);
        if (StringUtils.isBlank(result)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口验证失败");
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(1);
        boolean re = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(re);
    }

    /**
     * 下线接口
     *
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/offline")
    public BaseResponse<Boolean> offlineInterface(@RequestBody IdRequest idRequest, HttpServletRequest request) {
        // 只有管理员才可以操作
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "只有管理员才可以发布");
        }
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long id = idRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        InterfaceInfo interfaceInfo = new InterfaceInfo();
        interfaceInfo.setId(id);
        interfaceInfo.setStatus(0);
        boolean re = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(re);
    }


    /**
     * 调用接口
     *
     * @param interfaceInvokeRequest
     * @param request
     * @return
     */
    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterface(@RequestBody InterfaceInvokeRequest interfaceInvokeRequest, HttpServletRequest request) {

        if (interfaceInvokeRequest == null || interfaceInvokeRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long id = interfaceInvokeRequest.getId();
        String userRequestparams = interfaceInvokeRequest.getUserRequestparams();
        // 判断是否存在
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (interfaceInfo.getStatus() != 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口未发布");
        }
        User user = userService.getLoginUser(request);
        QueryWrapper<UserInterfaceInfo> userInterfaceInfoQueryWrapper = new QueryWrapper();
        userInterfaceInfoQueryWrapper.eq("userId", user.getId());
        userInterfaceInfoQueryWrapper.eq("interfaceInfoId", id);
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoMapper.selectOne(userInterfaceInfoQueryWrapper);
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "还未分配次数");
        }
        if (userInterfaceInfo.getLeftnum() < 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口剩余次数不足");
        }
        String accesskey = user.getAccesskey();
        String secretkey = user.getSecretkey();
        // todo 根据用户提供的调用地址来调用不同的方法
        BinApiClient tempClient = new BinApiClient(accesskey, secretkey);
        Gson gson = new Gson();
        com.bin.binapiclientsdk.model.User requestUser = gson.fromJson(userRequestparams, com.bin.binapiclientsdk.model.User.class);

        String usernameByPost = tempClient.getUsernameByPost(requestUser);
        return ResultUtils.success(usernameByPost);
    }
}

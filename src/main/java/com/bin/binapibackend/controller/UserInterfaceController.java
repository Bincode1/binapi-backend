package com.bin.binapibackend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.bin.binapibackend.common.BaseResponse;
import com.bin.binapibackend.common.ErrorCode;
import com.bin.binapibackend.common.ResultUtils;
import com.bin.binapibackend.exception.BusinessException;
import com.bin.binapibackend.mapper.UserInterfaceInfoMapper;
import com.bin.binapibackend.model.UserVO;
import com.bin.binapibackend.model.dto.DeleteRequest;
import com.bin.binapibackend.model.dto.PageRequest;
import com.bin.binapibackend.model.dto.userInterfaceInfo.UserInterfaceInfoAddRequest;
import com.bin.binapibackend.model.dto.userInterfaceInfo.UserInterfaceInfoUpdateRequest;
import com.bin.binapibackend.service.UserInterfaceInfoService;
import com.bin.binapibackend.service.UserService;
import com.bin.bincommon.model.UserInterfaceInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userInterface")
public class UserInterfaceController {

    @Autowired
    UserInterfaceInfoService userInterfaceInfoService;

    @Autowired
    UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Autowired
    UserService userService;

    /**
     * 获取所有接口
     *
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<com.bin.bincommon.model.UserInterfaceInfo>> getAllUserInterface(HttpServletRequest request) {
        UserInterfaceInfo userInterfaceInfoQuery = new UserInterfaceInfo();
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "只有管理员才可以查看用户接口调用关系");
        }
        List<UserInterfaceInfo> list = userInterfaceInfoService.list();
        return ResultUtils.success(list);
    }

    /**
     * 分页获取所有接口
     *
     * @return
     */
    @GetMapping("/page/list")

    public BaseResponse<IPage<UserInterfaceInfo>> getAllUserInterfaceByPage(PageRequest pageRequest, HttpServletRequest request) {
        if (pageRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "只有管理员才可以查看用户接口调用关系");
        }
        long pageNum = pageRequest.getPageNum();
        long pageSize = pageRequest.getPageSize();
        return ResultUtils.success(userInterfaceInfoService.getAllUserInterfaceByPage(pageNum, pageSize));
    }

    /**
     * 根据id获取接口
     *
     * @return
     */
    @GetMapping("/getOne")

    public BaseResponse<UserInterfaceInfo> getUserInterfaceById(long id, HttpServletRequest request) {
        if (userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "只有管理员才可以查看用户接口调用关系");
        }
        return ResultUtils.success(userInterfaceInfoService.getUserInterfaceById(id));

    }

    /**
     * 添加接口
     *
     * @param userInterfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> AddUserInterface(@RequestBody UserInterfaceInfoAddRequest userInterfaceInfoAddRequest, HttpServletRequest request) {
        if (userInterfaceInfoAddRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "只有管理员才可以添加");
        }
        UserVO loginUser = userService.getCurrentUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN, "请先登录");
        }
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        try {
            BeanUtils.copyProperties(userInterfaceInfo, userInterfaceInfoAddRequest);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        return ResultUtils.success(userInterfaceInfoService.addUserInterface(userInterfaceInfo, loginUser));
    }

    /**
     * 更新接口
     *
     * @param userInterfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> UpdateUserInterface(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest, HttpServletRequest request) {
        if (userInterfaceInfoUpdateRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "只有管理员才可以修改");
        }
        UserVO loginUser = userService.getCurrentUser(request);
        return ResultUtils.success(userInterfaceInfoService.updateUserInterface(userInterfaceInfoUpdateRequest, loginUser));
    }

    /**
     * 删除接口
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> DeleteUserInterface(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        // 必须得是管理员才可以删除接口
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "只有管理员才可以删除");
        }
        UserInterfaceInfo deleteUserInterface = userInterfaceInfoService.getById(deleteRequest.getId());

        // 同时只有接口的拥有者才可以删
        UserVO loginUser = userService.getCurrentUser(request);
        if (!deleteUserInterface.getUserid().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH, "只有接口的创建者才可以删除");
        }
        return ResultUtils.success(userInterfaceInfoService.removeById(deleteUserInterface));
    }


    @PostMapping("/test")
    public BaseResponse<Boolean> testInvoke(Long interfaceId, Long userId) {
        return ResultUtils.success(userInterfaceInfoService.invokeCount(interfaceId, userId));
    }

}

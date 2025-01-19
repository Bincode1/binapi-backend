package com.bin.binapibackend.controller;

import com.bin.binapibackend.common.BaseResponse;
import com.bin.binapibackend.common.ErrorCode;
import com.bin.binapibackend.common.ResultUtils;
import com.bin.binapibackend.model.UserVO;
import com.bin.binapibackend.model.dto.LoginRequest;
import com.bin.binapibackend.model.dto.RegisterRequest;
import com.bin.binapibackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public UserService userService;

    /**
     * 用户登录
     *
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<UserVO> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        if (loginRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        UserVO userVO = userService.userLogin(loginRequest, request);
        if (userVO == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userVO);
    }

    /**
     * 用户注册
     *
     * @param registerRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody RegisterRequest registerRequest, HttpServletRequest request) {
        if (registerRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        Long userId = userService.userRegister(registerRequest, request);
        return ResultUtils.success(userId);
    }

    @GetMapping("/current")
    public BaseResponse<UserVO> getCurrentUser(HttpServletRequest request) {
        return ResultUtils.success(userService.getCurrentUser(request));
    }


}

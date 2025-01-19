package com.bin.binapibackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.binapibackend.model.UserVO;
import com.bin.binapibackend.model.dto.LoginRequest;
import com.bin.binapibackend.model.dto.RegisterRequest;
import com.bin.bincommon.model.User;
import jakarta.servlet.http.HttpServletRequest;


/**
 * @author 26961
 * @description 针对表【user】的数据库操作Service
 * @createDate 2024-10-16 19:27:19
 */
public interface UserService extends IService<User> {
    /**
     * 用户信息脱敏
     *
     * @param user
     * @return
     */
    UserVO getSafetyUser(User user);

    /**
     * 用户登录
     *
     * @param loginRequest
     * @param request
     * @return
     */
    UserVO userLogin(LoginRequest loginRequest, HttpServletRequest request);

    Long userRegister(RegisterRequest registerRequest, HttpServletRequest request);

    /**
     * 加密密码
     *
     * @param password
     * @return
     */
    String hashPassword(String password);

    UserVO getCurrentUser(HttpServletRequest request);
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     */

    boolean isAdmin(HttpServletRequest request);
}

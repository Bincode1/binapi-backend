package com.bin.binapibackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.binapibackend.common.ErrorCode;
import com.bin.binapibackend.exception.BusinessException;
import com.bin.binapibackend.mapper.UserMapper;
import com.bin.binapibackend.model.UserVO;
import com.bin.binapibackend.model.dto.LoginRequest;
import com.bin.binapibackend.model.dto.RegisterRequest;
import com.bin.binapibackend.service.UserService;
import com.bin.bincommon.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.regex.Pattern;

import static com.bin.binapibackend.constant.UserConstant.ADMIN_ROLE;
import static com.bin.binapibackend.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author 26961
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-10-16 19:27:19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private static final String SALT = "binbinCrazy";
    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserVO getSafetyUser(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUseraccount(user.getUseraccount());
        userVO.setUseravatar(user.getUseravatar());
        userVO.setUsername(user.getUsername());
        userVO.setUserrole(user.getUserrole());
        userVO.setCreatetime(user.getCreatetime());
        userVO.setUpdatetime(user.getUpdatetime());
        userVO.setIsdelete(user.getIsdelete());
//        userVO.setSecretkey(user.getSecretkey());
        userVO.setAccesskey(user.getAccesskey());

        return userVO;
    }

    @Override
    public UserVO userLogin(LoginRequest loginRequest, HttpServletRequest request) {
        String userAccount = loginRequest.getUseraccount();
        String userPassword = loginRequest.getUserpassword();
        if (StringUtils.isAnyBlank(userPassword, userAccount)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户或密码不能为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户长度不能少于4位");

        }

        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不能少于8位");
        }

        // 账户不能包含特殊字符
//        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        String validPattern = "^[a-zA-Z0-9_]+$";
        boolean matches = Pattern.matches(validPattern, userAccount);
        if (!matches) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不能包含特殊字符");
        }
        //加密
        String securePassword = hashPassword(userPassword);
        //查询数据
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userpassword", securePassword);
        queryWrapper.eq("useraccount", userAccount);
        User user = userMapper.selectOne(queryWrapper);
        UserVO safetyUser = this.getSafetyUser(user);
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    @Override
    public Long userRegister(RegisterRequest registerRequest, HttpServletRequest request) {
        String userAccount = registerRequest.getUseraccount();
        String userPassword = registerRequest.getUserpassword();
        String checkPassword = registerRequest.getCheckpassword();
        if (StringUtils.isAnyBlank(userPassword, userAccount, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户或密码不能为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户长度不能少于4位");

        }

        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码长度不能少于8位");
        }

        if (!checkPassword.equals(userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入密码不一致");
        }

        // 账户不能包含特殊字符
//        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        String validPattern = "^[a-zA-Z0-9_]+$";
        boolean matches = Pattern.matches(validPattern, userAccount);
        if (!matches) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不能包含特殊字符");
        }
        //查询数据
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("useraccount", userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户已存在");
        }
        //加密
        String securePassword = hashPassword(userPassword);
        //随机分配 accessKey 和 secretKey
        String accessKey = SALT + generateSecureHash("accessKey-" + userAccount);
        String secretKey = SALT + generateSecureHash("secretKey-" + userPassword);
        //新增用户
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setUseraccount(userAccount);
        user.setUserpassword(securePassword);
        user.setAccesskey(accessKey);
        user.setSecretkey(secretKey);
        boolean save = this.save(user);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败,数据库错误");
        }
        return user.getId();
    }

    public String hashPassword(String password) {

        try {
            // 创建 SHA-256 MessageDigest 实例
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // 更新摘要，输入数据为密码和盐的组合
            md.update((password + SALT).getBytes());

            // 计算 Hash 值
            byte[] hashBytes = md.digest();

            // 转换为十六进制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    @Override
    public UserVO getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        UserVO currentUser = (UserVO) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        Long userId = currentUser.getId();
        User user = this.getById(userId);
        return getSafetyUser(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        UserVO currentUser = (UserVO) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        Long userId = currentUser.getId();
        return this.getById(userId);
    }

    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        UserVO userVO = (UserVO) userObj;
        return userVO != null && userVO.getUserrole() == ADMIN_ROLE;
    }

    private String generateSecureHash(String input) {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] randomBytes = new byte[16]; // 16 bytes for salt
            secureRandom.nextBytes(randomBytes);
            String salt = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest((input + salt).getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate secure hash", e);
        }
    }
}





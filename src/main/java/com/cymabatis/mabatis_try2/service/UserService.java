package com.cymabatis.mabatis_try2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cymabatis.mabatis_try2.model.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 99537
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2022-07-02 17:24:49
*/
public interface UserService extends IService<User> {
    /**
     * 用户注释
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户id
     */

    public long userRegister(String userRole,String userAccount, String userPassword, String checkPassword,String tmp_class);

    /**
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @return
     */
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    public List<User> selectStudent(String className,String teacherName);

    /**
     * 用户脱敏
     */
    public User getSafeUser(User originUser);

    /**
     * 用户注销
     * @param request
     * @return
     */
    public int userLogout(HttpServletRequest request);
}
package com.cymabatis.mabatis_try2.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cymabatis.mabatis_try2.constant.UserConstant;
import com.cymabatis.mabatis_try2.model.domain.Homework;
import com.cymabatis.mabatis_try2.model.domain.User;
import com.cymabatis.mabatis_try2.model.domain.request.UserLoginRequest;
import com.cymabatis.mabatis_try2.model.domain.request.UserRegisterRequest;
import com.cymabatis.mabatis_try2.service.ClassesService;
import com.cymabatis.mabatis_try2.service.HomeworkService;
import com.cymabatis.mabatis_try2.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.cymabatis.mabatis_try2.constant.UserConstant.ADMIN_ROLE;
import static com.cymabatis.mabatis_try2.constant.UserConstant.USER_LOGIN_STATE;


/**
 * 用户接口
 * @author cy
 */
@Controller
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private ClassesService classesService;
    @Resource
    private HomeworkService homeworkService ;





    @PostMapping(value = {"/register","/register.html"})
    public String userRegister( UserRegisterRequest userRegisterRequest, HttpServletRequest request){
        System.out.println(1);
        if(userRegisterRequest == null){
            request.setAttribute("notice","空对象！");
            return null;
        }
        String userRole = userRegisterRequest.getUserRole();
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String inviteCode = userRegisterRequest.getInviteCode();
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            request.setAttribute("notice","empty");
            return null;
        }
        long notice_back = userService.userRegister(userRole, userAccount, userPassword, checkPassword,inviteCode);
        if(notice_back == -1){
            request.setAttribute("notice","empty");
            return null;
        }
        if(notice_back == -2){
            request.setAttribute("notice","exist");
            return null;
        }
        if(notice_back == -3){
            request.setAttribute("notice","notsame");
            return null;
        }
        if(notice_back == -4){
            request.setAttribute("notice","failed");
            return null;
        }
        if(notice_back == -10){
            request.setAttribute("notice","noclass");
            return null;
        }
        else{
            request.setAttribute("notice","success");
            return "lin";
        }
    }
    @PostMapping("/index")
    public String userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest == null){
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            System.out.println("blank错误");
            request.setAttribute("login_notice","账号或密码错误!");
            return null;
        }
        User user =  userService.userLogin(userAccount, userPassword,request);
        if(user == null){
            System.out.println("user错误");
            request.setAttribute("login_notice","账号或密码错误!");
            return null;
        }
        request.getSession().setAttribute("Loginuser",user);
        request.setAttribute("Loginuser",user);

        if(user.getUserRole().equals("老师")){
            User teacheruser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
            String classTeacher = teacheruser.getUserAccount();
            request.getSession().setAttribute("classTeacher",classTeacher);
            long tmp = classesService.selectClass(request);
            return "teacher";
        }
        else{
            User studentuser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
            System.out.println(USER_LOGIN_STATE);
            System.out.println(studentuser);
            String className = studentuser.getClassName();
            String studentName = studentuser.getUserAccount();
            String teacherName = studentuser.getTeacherName();
            request.getSession().setAttribute("classTeacher",teacherName);
            System.out.println("className登陆时是"+className);
            List<Homework> homeworkList = homeworkService.checkClassHomework(className, studentName,teacherName);
            System.out.println(className+studentName+teacherName+"");
            System.out.println(homeworkList);
            request.getSession().setAttribute("homeworkList",homeworkList);
            return "student";
        }

    }

    @PostMapping("/logout")
    public Integer userLogout(HttpServletRequest request){
        if(request == null) {
            return null;
        }
        return userService.userLogout(request);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username,HttpServletRequest request){

        if(!isAdmin(request)){
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List<User> userList = userService.list(queryWrapper);
        return userList.stream().map(user -> {
            user.setUserPassword(null);
            return userService.getSafeUser(user);
        }).collect(Collectors.toList());
    }
    @GetMapping("/delete")
    public boolean deleteUsers(@RequestBody long id,HttpServletRequest request){
        if(!isAdmin(request)){
            return false;
        }
        if(id < 0){
            return false;
        }

        return userService.removeById(id);
    }

    private boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObj;
        if(user == null || !user.getUserRole().equals(ADMIN_ROLE)){
            return false;
        }
        return true;
    }
}

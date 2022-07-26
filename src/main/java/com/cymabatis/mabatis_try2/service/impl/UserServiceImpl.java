package com.cymabatis.mabatis_try2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cymabatis.mabatis_try2.mapper.ClassesMapper;
import com.cymabatis.mabatis_try2.mapper.HomeworkquestionMapper;
import com.cymabatis.mabatis_try2.mapper.UserMapper;
import com.cymabatis.mabatis_try2.model.domain.Classes;
import com.cymabatis.mabatis_try2.model.domain.Homework;
import com.cymabatis.mabatis_try2.model.domain.Homeworkquestion;
import com.cymabatis.mabatis_try2.model.domain.User;
import com.cymabatis.mabatis_try2.service.HomeworkService;
import com.cymabatis.mabatis_try2.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.cymabatis.mabatis_try2.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author cy
* 用户服务实现类
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    /**
     * 盐值：加密密码
     */
    private static final String SALT = "cy";
    /**
     * 用户登录态键
     */


    @Resource
    private ClassesMapper classesMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private HomeworkquestionMapper homeworkquestionMapper;
    @Resource
    private HomeworkService homeworkService;


    @Override
    public long userRegister(String userRole,String userAccount, String userPassword, String checkPassword,String inviteCode) {
        if(StringUtils.isAnyBlank(userRole,userAccount,userPassword,checkPassword)){
            //todo 修改为自定义异常类
            return -1;
        }
        if(userRole.equals("--请选择你的身份--")){
            return -1;
        }
        if(userRole.equals("学生") && StringUtils.isAnyBlank(inviteCode)){
            return -1;
        }
        // 账户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        long count = this.count(queryWrapper);
        if(count > 0){
            return -2;
        }

        // 密码和校验密码相同
        if(!userPassword.equals(checkPassword)){
            return -3;
        }
        QueryWrapper<Classes> ClassesqueryWrapper = new QueryWrapper<>();
        ClassesqueryWrapper.eq("inviteCode",inviteCode);
        Classes classes = classesMapper.selectOne(ClassesqueryWrapper);

        if (userRole == "学生" && classes == null){
            return -10;
        }

        String className = null;
        String teacherName = null;
        if(userRole.equals("学生")) {
            className = classes.getClassName();
            teacherName = classes.getClassTeacher();
        }
        // 2.加密

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 3.插入数据
        User user = new User();
        user.setUserRole(userRole);
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setClassName(className);
        user.setTeacherName(teacherName);
        boolean saveResult = this.save(user);
        if(!saveResult){
            return -4;
        }

        // 创建班级内全体学生的homework
        QueryWrapper<Homeworkquestion> homeworkquestionQueryWrapper = new QueryWrapper<>();
        homeworkquestionQueryWrapper.eq("teacherName",teacherName);
        homeworkquestionQueryWrapper.eq("className",className);
        List<Homeworkquestion> homeworkquestions = homeworkquestionMapper.selectList(homeworkquestionQueryWrapper);
        if(!homeworkquestions.isEmpty()) {
            ArrayList<Homework> homeworks = new ArrayList<>();
            for (Homeworkquestion homeworkquesiton : homeworkquestions) {
                String homeworkName = homeworkquesiton.getHomeworkName();
                teacherName = homeworkquesiton.getTeacherName();
                String end_Time = homeworkquesiton.getEndTime();
                String funcName = homeworkquesiton.getFuncName();
                Homework homework = new Homework();
                homework.setHomeworkName(homeworkName);
                homework.setStudentName(user.getUserAccount());
                homework.setIsSubmit("否");
                homework.setClassName(className);
                homework.setTeacherName(teacherName);
                homework.setEndTime(end_Time);
                homework.setFuncName(funcName);
                homeworks.add(homework);
            }
            homeworkService.saveBatch(homeworks);
        }
        return user.getId();

    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        // 帐户不能包含特殊字符

        // 2.加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if(user == null){
            System.out.println("suer为空错误");
            log.info("user login failed,userAccount cannot match userPassword");
            return null;
        }


        //4.用户脱敏
        User safeUser = getSafeUser(user);
        //3.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safeUser);

        return safeUser;

    }

    @Override
    public List<User> selectStudent(String className,String teacherName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacherName",teacherName);
        queryWrapper.eq("className",className);
        queryWrapper.eq("userRole","学生");
        List<User> users = userMapper.selectList(queryWrapper);
        return users;
    }

    /**
     * 用户脱敏
     * @param originUser
     * @return
     */
    public User getSafeUser(User originUser){
        User safeUser = new User();
        safeUser.setId(originUser.getId());
        safeUser.setUserAccount(originUser.getUserAccount());
        safeUser.setUserRole(originUser.getUserRole());
        safeUser.setUserStatus(originUser.getUserStatus());
        safeUser.setCreateTime(originUser.getCreateTime());
        safeUser.setClassName(originUser.getClassName());
        safeUser.setIsDelete(originUser.getIsDelete());
        safeUser.setTeacherName(originUser.getTeacherName());
        safeUser.setScore(originUser.getScore());
        return safeUser;
    }

    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}





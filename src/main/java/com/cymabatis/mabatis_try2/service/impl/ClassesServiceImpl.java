package com.cymabatis.mabatis_try2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cymabatis.mabatis_try2.mapper.ClassesMapper;
import com.cymabatis.mabatis_try2.model.domain.Classes;
import com.cymabatis.mabatis_try2.model.domain.User;
import com.cymabatis.mabatis_try2.service.ClassesService;
import com.github.jeffreyning.mybatisplus.service.MppServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;

import static com.cymabatis.mabatis_try2.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author 99537
* @description 针对表【classes】的数据库操作Service实现
* @createDate 2022-07-08 13:03:16
*/
@Service
public class ClassesServiceImpl extends MppServiceImpl<ClassesMapper, Classes>
    implements ClassesService{


    @Resource
    private ClassesMapper classesMapper;

    @Override
    public long createClass(String className, String classTeacher){
        System.out.println("createClass开始执行！");
        QueryWrapper<Classes> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("className",className);
        queryWrapper.eq("classTeacher",classTeacher);
        Classes classes = classesMapper.selectOne(queryWrapper);
        if(classes != null){
            System.out.println("classes已经存在！");
            return -1;
        }

        // 生成邀请码
        char[] chars = {'Q', 'W', 'E', '8', 'S', '2', 'D', 'Z',
                'X', '9', 'C', '7', 'P', '5', 'K', '3',
                'M', 'J', 'U', 'F', 'R', '4', 'V', 'Y',
                'T', 'N', '6', 'B', 'G', 'H', 'A', 'L'};
        Random random = new Random();
        int invite_len = 10;
        char[] inviteChars = new char[invite_len];
        for (int i = 0; i < 10; i++) {
            inviteChars[i] = chars[random.nextInt(chars.length)];
        }
        String inviteCode = String.valueOf(inviteChars);

        Classes newclass = new Classes();
        newclass.setClassName(className);
        newclass.setClassTeacher(classTeacher);
        newclass.setInviteCode(inviteCode);
        boolean saveResult = this.save(newclass);
        if(!saveResult){
            System.out.println("saveResult为假，保存失败！");
            return -2;
        }
        return 1;
    }

    @Override
    public long selectClass(HttpServletRequest request) {
        User teacheruser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        String classTeacher = teacheruser.getUserAccount();
        QueryWrapper<Classes> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("classTeacher",classTeacher);
        List<Classes> classesList = classesMapper.selectList(queryWrapper);
        request.getSession().setAttribute("classesList",classesList);
        return 1;
    }

    @Override
    public String selectClassInviteCode(String className,HttpServletRequest request) {
        QueryWrapper<Classes> queryWrapper = new QueryWrapper<>();
        String classTeacher = (String) request.getSession().getAttribute("classTeacher");
        queryWrapper.eq("className",className);
        queryWrapper.eq("classTeacher",classTeacher);
        Classes classes = classesMapper.selectOne(queryWrapper);
        if(classes == null){
            return null;
        }
        return classes.getInviteCode();
    }


}





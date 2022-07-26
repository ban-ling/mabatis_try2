package com.cymabatis.mabatis_try2.controller;


import com.cymabatis.mabatis_try2.model.domain.Classes;
import com.cymabatis.mabatis_try2.model.domain.User;
import com.cymabatis.mabatis_try2.service.ClassesService;
import com.cymabatis.mabatis_try2.service.HomeworkquestionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.cymabatis.mabatis_try2.constant.UserConstant.USER_LOGIN_STATE;

@Controller
public class classController {

    @Resource
    ClassesService classesService;

    @Resource
    HomeworkquestionService homeworkquestionService;

    @PostMapping("/teacher")
    public String createClass(Classes classes, HttpServletRequest request) {
        System.out.println("Controller的create方法执行！");
        String className = classes.getClassName();
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        String classTeacher = user.getUserAccount();
        long tmp = classesService.createClass(className, classTeacher);

        if (tmp != 1) {
            System.out.println("创建班级失败！");
            return null;
        }


        // 显示修改后的班级名
        long tmp2 = classesService.selectClass(request);

        return "teacher";
    }


    @PostMapping("/class_more")
    public String class_more(Classes classes, HttpServletRequest request){
        System.out.println("Controller的class_more方法执行！");
        String className = classes.getClassName();
        request.getSession().setAttribute("className",className);


        String inviteCode = classesService.selectClassInviteCode(className,request);
        if(inviteCode == null){
            return null;
        }
        request.getSession().setAttribute("inviteCode",inviteCode);
        homeworkquestionService.selectHomeworkQuestion(className,request);

        return "class_more";
    }


}


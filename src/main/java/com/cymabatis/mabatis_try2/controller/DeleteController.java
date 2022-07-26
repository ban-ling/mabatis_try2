package com.cymabatis.mabatis_try2.controller;


import com.cymabatis.mabatis_try2.model.domain.request.DeleteRequest;
import com.cymabatis.mabatis_try2.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class DeleteController {

    @Resource
    private DeleteService deleteService;
    @Resource
    private ClassesService classesService;
    @Resource
    private HomeworkquestionService homeworkquestionService;

    @PostMapping("/deleteClass")
    public String deleteClass(DeleteRequest deleteRequest,HttpServletRequest request){
        System.out.println("deleteClass已经执行");
        String className = deleteRequest.getClassName();
        String classTeacher = (String) request.getSession().getAttribute("classTeacher");
        deleteService.deleteClass(className,classTeacher);
        classesService.selectClass(request);
        return "teacher";
    }



    @PostMapping("/deleteHomework")
    public String deleteHomework(DeleteRequest deleteRequest,HttpServletRequest request){
        System.out.println("deleteHomework已经执行");
        String className = (String) request.getSession().getAttribute("className");
        String teacherName = (String) request.getSession().getAttribute("classTeacher");
        String homeworkName = deleteRequest.getHomeworkName();
        deleteService.deleteHomework(className,homeworkName,teacherName);
        homeworkquestionService.selectHomeworkQuestion(className,request);
        return "class_more";
    }



    @PostMapping("/deleteStudent")
    public String deleteStudent(DeleteRequest deleteRequest,HttpServletRequest request){
        System.out.println("deleteStudent已经执行");
        String className = (String) request.getSession().getAttribute("className");
        String teacherName = (String) request.getSession().getAttribute("classTeacher");
        String studentName = deleteRequest.getStudentName();
        System.out.println("className为"+className);
        System.out.println("studentName为"+studentName);
        deleteService.deleteStudent(className,studentName,teacherName);
        homeworkquestionService.selectHomeworkQuestion(className,request);
        return "class_more";
    }
}

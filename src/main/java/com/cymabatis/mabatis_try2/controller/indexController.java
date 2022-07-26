package com.cymabatis.mabatis_try2.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class indexController {

    @GetMapping(value = {"/lin","/lin.html"})
    public String lin(){return "redirect:index";}

    @GetMapping(value = {"/","index","index.html"})
    public String index(){
        return "index";
    }

    @GetMapping(value = {"/class_more","/class_more.html"})
    public String class_more(){
        return "class_more";
    }

    @GetMapping(value = {"/homeworks","/homeworks.html"})
    public String homeworks(){
        System.out.println("homeworks是get方法执行！");
        return "homeworks";
    }

    @GetMapping(value = {"/register","/register.html"})
    public String register(){return "register";}

    @GetMapping(value = {"/student","/student.html"})
    public String student(){
        System.out.println("执行了student的get方法");
        return "student";
    }

    @GetMapping(value = {"/teacher","/teacher.html"})
    public String teacher(){return "teacher";}

    @GetMapping(value = {"/student_homework","/student_homework.html"})
    public String student_homework(){
        System.out.println("student_homework是get方法执行！");
        return "student_homework";
    }



//    @PostMapping("/index")
//    public String main(S s) {
//        System.out.println("1");
//        if (s.getUserName().equals("teacher") && s.getPasswords().equals("teacher")) {
//            return "redirect:student";
//        }
//        else {
//            return "index";
//        }
//    }
}

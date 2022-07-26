package com.cymabatis.mabatis_try2.controller;



import com.cymabatis.mabatis_try2.model.domain.Homework;
import com.cymabatis.mabatis_try2.model.domain.Homeworkquestion;
import com.cymabatis.mabatis_try2.model.domain.User;
import com.cymabatis.mabatis_try2.service.ClassesService;
import com.cymabatis.mabatis_try2.service.HomeworkService;
import com.cymabatis.mabatis_try2.service.HomeworkquestionService;
import com.cymabatis.mabatis_try2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static com.cymabatis.mabatis_try2.constant.UserConstant.USER_LOGIN_STATE;

@Controller
public class HomeworkController {

    @Resource
    private UserService userService;
    @Resource
    private HomeworkquestionService homeworkquestionService;
    @Resource
    private HomeworkService homeworkService;
    @Resource
    private ClassesService classesService;


    @PostMapping("/createHomework")
    public String createHomework(Homeworkquestion homeworkquestion, HttpServletRequest request){
//        public long createHomework(String className,String teacherName,String homeworkName,String end_Time,String mainContent,String request);

        User teacheruser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        String className = homeworkquestion.getClassName();
        String teacherName = teacheruser.getUserAccount();
        String homeworkName = homeworkquestion.getHomeworkName();
        String end_Time = homeworkquestion.getEndTime();
        String mainContent = homeworkquestion.getMainContent();
        String homework_request = homeworkquestion.getRequest();
        String funcName = homeworkquestion.getFuncName();
        long tmp = homeworkquestionService.createHomework(className,teacherName,homeworkName,end_Time,mainContent,homework_request,funcName);
        long tmp2 = classesService.selectClass(request);

        // 创建班级内全体学生的homework
        List<User> users = userService.selectStudent(className,teacherName);
        ArrayList<Homework> homeworks = new ArrayList<>();
        for(User user:users){
            Homework homework = new Homework();
            homework.setHomeworkName(homeworkName);
            homework.setStudentName(user.getUserAccount());
            homework.setIsSubmit("否");
            homework.setClassName(className);
            homework.setTeacherName(teacherName);
            homework.setEndTime(end_Time);
            homework.setFuncName(funcName);
            homework.setScore(0.0);
            homeworks.add(homework);
        }
        homeworkService.saveBatch(homeworks);

        return "teacher";
    }

    @PostMapping("/student_homework")
    public String student_homework(Homework homework, HttpServletRequest request){
        String homeworkName = homework.getHomeworkName();
        homeworkService.checkStudentHomework(homeworkName,request);
        return "student_homework";
    }

    @PostMapping(value = {"/homeworks.html","/homeworks"})
    public String queryHomeworkMore(Homeworkquestion homeworkquestion, HttpServletRequest request){
        String homeworkName = homeworkquestion.getHomeworkName();
        User studentuser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        String className = studentuser.getClassName();
        Homeworkquestion homeworkquestion1 = homeworkquestionService.questionMore(className, homeworkName, request);
        request.setAttribute("homeworkquestion1",homeworkquestion1);
        return "homeworks";
    }


}

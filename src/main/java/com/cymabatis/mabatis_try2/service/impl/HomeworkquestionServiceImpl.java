package com.cymabatis.mabatis_try2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cymabatis.mabatis_try2.mapper.HomeworkquestionMapper;
import com.cymabatis.mabatis_try2.model.domain.Homeworkquestion;
import com.cymabatis.mabatis_try2.service.HomeworkService;
import com.cymabatis.mabatis_try2.service.HomeworkquestionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 99537
* @description 针对表【homeworkquestion】的数据库操作Service实现
* @createDate 2022-07-11 14:52:15
*/
@Service
public class HomeworkquestionServiceImpl extends ServiceImpl<HomeworkquestionMapper, Homeworkquestion>
    implements HomeworkquestionService{

    @Resource
    HomeworkquestionMapper homeworkquestionMapper;
    @Resource
    HomeworkService homeworkService;


    @Override
    public long createHomework(String className, String teacherName, String homeworkName, String end_Time, String mainContent, String request,String funcName) {
        System.out.println("createHomework开始执行！");
        QueryWrapper<Homeworkquestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("className",className);
        queryWrapper.eq("teacherName",teacherName);
        queryWrapper.eq("homeworkName",homeworkName);
        Homeworkquestion homework = homeworkquestionMapper.selectOne(queryWrapper);
        if(homework != null){
            System.out.println("homework已存在！");
            return -1;
        }
        Homeworkquestion homeworkquestion = new Homeworkquestion();
        homeworkquestion.setClassName(className);
        homeworkquestion.setTeacherName(teacherName);
        homeworkquestion.setHomeworkName(homeworkName);
        homeworkquestion.setEndTime(end_Time);
        homeworkquestion.setMainContent(mainContent);
        homeworkquestion.setRequest(request);
        homeworkquestion.setFuncName(funcName);
        homeworkquestion.setTotalPeople(0);
        homeworkquestion.setPassPeople(0);
        homeworkquestion.setSubmitPeople(0);
        homeworkquestion.setAverageScore(0.0);

        boolean saveResult = this.save(homeworkquestion);
        if(!saveResult){
            System.out.println("保存失败！");
            return -2;
        }
        return 1;
    }

    public long selectHomeworkQuestion(String className, HttpServletRequest request){
        QueryWrapper<Homeworkquestion> queryWrapper = new QueryWrapper<>();
        String classTeacher = (String) request.getSession().getAttribute("classTeacher");
        queryWrapper.eq("className",className);
        queryWrapper.eq("teacherName",classTeacher);
        List<Homeworkquestion> homeworkList = homeworkquestionMapper.selectList(queryWrapper);
        for(Homeworkquestion homeworkquestion:homeworkList){
            String homeworkName = homeworkquestion.getHomeworkName();
            homeworkService.HomeworkTotalData(className,homeworkName,classTeacher,request);
        }
        List<Homeworkquestion> homeworkList2 = homeworkquestionMapper.selectList(queryWrapper);

        request.getSession().setAttribute("homeworkList",homeworkList2);
        return 1;
    }

    @Override
    public Homeworkquestion questionMore(String className, String homeworkName,HttpServletRequest request) {
        QueryWrapper<Homeworkquestion> queryWrapper = new QueryWrapper<>();
        String teacherName = (String) request.getSession().getAttribute("classTeacher");
        queryWrapper.eq("teacherName",teacherName);
        queryWrapper.eq("className",className);
        queryWrapper.eq("homeworkName",homeworkName);
        Homeworkquestion homeworkquestion = homeworkquestionMapper.selectOne(queryWrapper);
        return homeworkquestion;
    }

}





package com.cymabatis.mabatis_try2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cymabatis.mabatis_try2.model.domain.Homeworkquestion;

import javax.servlet.http.HttpServletRequest;

/**
* @author 99537
* @description 针对表【homeworkquestion】的数据库操作Service
* @createDate 2022-07-11 14:52:15
*/
public interface HomeworkquestionService extends IService<Homeworkquestion> {

    public long createHomework(String className,String teacherName,String homeworkName,String end_Time,String mainContent,String request,String funcName);

    public long selectHomeworkQuestion(String className, HttpServletRequest request);

    public Homeworkquestion questionMore(String className, String homeworkName, HttpServletRequest request);

}

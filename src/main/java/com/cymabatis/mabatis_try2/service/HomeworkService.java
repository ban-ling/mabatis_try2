package com.cymabatis.mabatis_try2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cymabatis.mabatis_try2.model.domain.Homework;
import com.cymabatis.mabatis_try2.model.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 99537
* @description 针对表【homework】的数据库操作Service
* @createDate 2022-07-08 15:31:42
*/
public interface HomeworkService extends IService<Homework> {



    public List<Homework> checkStudentHomework(String homeworkName, HttpServletRequest request);

    public List<Homework> checkClassHomework(String className,String studentName,String teacherName);

    public long saveHomework(String homeworkName,String studentName,String filePath,String className,String teacherName);

    public long saveScore(String homeworkName, String studentName, double score,String className,String teacherName);

    public long singleStudentScore(String className, String studentName,String teacherName);

    public long HomeworkTotalData(String className, String homeworkName,String teacherName,HttpServletRequest request);

    public List<User> ClassTotalData(String className,String teacherName);

}

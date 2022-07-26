package com.cymabatis.mabatis_try2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cymabatis.mabatis_try2.mapper.ClassesMapper;
import com.cymabatis.mabatis_try2.mapper.HomeworkMapper;
import com.cymabatis.mabatis_try2.mapper.HomeworkquestionMapper;
import com.cymabatis.mabatis_try2.mapper.UserMapper;
import com.cymabatis.mabatis_try2.model.domain.Classes;
import com.cymabatis.mabatis_try2.model.domain.Homework;
import com.cymabatis.mabatis_try2.model.domain.Homeworkquestion;
import com.cymabatis.mabatis_try2.model.domain.User;
import com.cymabatis.mabatis_try2.service.DeleteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DeleteServiceImpl implements DeleteService {


    @Resource
    private UserMapper userMapper;
    @Resource
    private HomeworkMapper homeworkMapper;
    @Resource
    private HomeworkquestionMapper homeworkquestionMapper;
    @Resource
    private ClassesMapper classesMapper;





    @Override
    public void deleteClass(String className,String classTeacher) {
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<User>();
        queryWrapper1.eq("className",className);
        queryWrapper1.eq("teacherName",classTeacher);

        QueryWrapper<Homework> queryWrapper2 = new QueryWrapper<Homework>();
        queryWrapper2.eq("className",className);
        queryWrapper2.eq("teacherName",classTeacher);

        QueryWrapper<Homeworkquestion> queryWrapper3 = new QueryWrapper<Homeworkquestion>();
        queryWrapper3.eq("className",className);
        queryWrapper3.eq("teacherName",classTeacher);

        QueryWrapper<Classes> queryWrapper4 = new QueryWrapper<Classes>();
        queryWrapper4.eq("className",className);
        queryWrapper4.eq("classTeacher",classTeacher);
        userMapper.delete(queryWrapper1);
        homeworkMapper.delete(queryWrapper2);
        homeworkquestionMapper.delete(queryWrapper3);
        classesMapper.delete(queryWrapper4);
    }

    @Override
    public void deleteHomework(String className, String homeworkName,String classTeacher) {
        QueryWrapper<Homework> queryWrapper1 = new QueryWrapper<>();
        QueryWrapper<Homeworkquestion> queryWrapper2 = new QueryWrapper<>();
        queryWrapper1.eq("teacherName",classTeacher);
        queryWrapper1.eq("className",className);
        queryWrapper1.eq("homeworkName",homeworkName);
        queryWrapper2.eq("teacherName",classTeacher);
        queryWrapper2.eq("className",className);
        queryWrapper2.eq("homeworkName",homeworkName);
        homeworkMapper.delete(queryWrapper1);
        homeworkquestionMapper.delete(queryWrapper2);
    }

    @Override
    public void deleteStudent(String className, String studentName,String classTeacher) {
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("userAccount",studentName);
        userMapper.delete(queryWrapper1);
        QueryWrapper<Homework> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("teacherName",classTeacher);
        queryWrapper2.eq("className",className);
        queryWrapper2.eq("studentName",studentName);
        homeworkMapper.delete(queryWrapper2);
    }
}

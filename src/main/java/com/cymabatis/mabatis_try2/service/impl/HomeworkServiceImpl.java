package com.cymabatis.mabatis_try2.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cymabatis.mabatis_try2.mapper.HomeworkMapper;
import com.cymabatis.mabatis_try2.mapper.HomeworkquestionMapper;
import com.cymabatis.mabatis_try2.mapper.UserMapper;
import com.cymabatis.mabatis_try2.model.domain.Homework;
import com.cymabatis.mabatis_try2.model.domain.Homeworkquestion;
import com.cymabatis.mabatis_try2.model.domain.User;
import com.cymabatis.mabatis_try2.service.HomeworkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 99537
* @description 针对表【homework】的数据库操作Service实现
* @createDate 2022-07-08 15:31:42
*/
@Service
public class HomeworkServiceImpl extends ServiceImpl<HomeworkMapper, Homework>
    implements HomeworkService{

    @Resource
    private HomeworkMapper homeworkMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private HomeworkquestionMapper homeworkquestionMapper;





    @Override
    public List<Homework> checkStudentHomework(String homeworkName,HttpServletRequest request){
        QueryWrapper<Homework> queryWrapper = new QueryWrapper<>();
        String className = (String) request.getSession().getAttribute("className");
        String teacherName = (String) request.getSession().getAttribute("classTeacher");
        queryWrapper.eq("className",className);
        queryWrapper.eq("teacherName",teacherName);
        queryWrapper.eq("homeworkName",homeworkName);
        List<Homework> studentHomeworkList = homeworkMapper.selectList(queryWrapper);
        request.getSession().setAttribute("studentHomeworkList",studentHomeworkList);
        return studentHomeworkList;
    }
    @Override
    public List<Homework> checkClassHomework(String className,String studentName,String teacherName){
        QueryWrapper<Homework> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacherName",teacherName);
        queryWrapper.eq("className",className);
        queryWrapper.eq("studentName",studentName);
        List<Homework> homeworkList = homeworkMapper.selectList(queryWrapper);
        return homeworkList;
    }

    @Override
    public long saveHomework(String homeworkName,String studentName,String filePath,String className,String teacherName){
        UpdateWrapper<Homework> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("className",className);
        updateWrapper.eq("teacherName",teacherName);
        updateWrapper.eq("homeworkName",homeworkName);
        updateWrapper.eq("studentName",studentName);
        updateWrapper.set("filePath",filePath);
        updateWrapper.set("isSubmit","是");
        baseMapper.update(null,updateWrapper);

        return 1;
    }

    @Override
    public long saveScore(String homeworkName, String studentName, double score,String className,String teacherName) {
        UpdateWrapper<Homework> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("className",className);
        updateWrapper.eq("teacherName",teacherName);
        updateWrapper.eq("homeworkName",homeworkName);
        updateWrapper.eq("studentName",studentName);
        updateWrapper.set("score",score);
        baseMapper.update(null,updateWrapper);

        return 1;
    }

    @Override
    public long singleStudentScore(String className, String studentName,String teacherName) {
        QueryWrapper<Homework> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacherName",teacherName);
        queryWrapper.eq("className",className);
        queryWrapper.eq("studentName",studentName);
        List<Homework> homeworkList = homeworkMapper.selectList(queryWrapper);
        double sums = 0;
        double nums = 0;
        for (Homework homework : homeworkList) {
            System.out.println("homework是："+homework);
            Double tmp_score = homework.getScore();
            if (tmp_score == null) {
                nums += 1;
                continue;
            }

            nums += 1;
            sums += tmp_score;
        }
        String correctRate = String.format("%.1f", sums / nums);
        double finalScore = Double.parseDouble(correctRate);
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userAccount",studentName);
        updateWrapper.set("score",finalScore);
        userMapper.update(null,updateWrapper);

        return 1;
    }

    @Override
    public long HomeworkTotalData(String className, String homeworkName,String teacherName,HttpServletRequest request) {
        QueryWrapper<Homework> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacherName",teacherName);
        queryWrapper.eq("className",className);
        queryWrapper.eq("homeworkName",homeworkName);
        List<Homework> homeworkList = homeworkMapper.selectList(queryWrapper);
        double sums = 0;
        double nums = 0;
        int totalPeople = 0;
        int submitPeople = 0;
        int passPeople = 0;

        for(Homework homework:homeworkList){
            totalPeople += 1;
            String isSubmit = homework.getIsSubmit();
            if(isSubmit.equals("否")){
                continue;
            }
            else{
                submitPeople += 1;
            }
            Double tmp_score = homework.getScore();
            if(tmp_score == null){
                continue;
            }
            if(tmp_score >= 60){
                passPeople += 1;
            }
            sums += tmp_score;
        }
        String correctRate = null;
        Double averageScore = null;
        if(submitPeople != 0) {
            correctRate = String.format("%.1f", sums / submitPeople);
            averageScore = Double.parseDouble(correctRate);
        }
        UpdateWrapper<Homeworkquestion> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("teacherName",teacherName);
        updateWrapper.eq("className",className);
        updateWrapper.eq("homeworkName",homeworkName);
        updateWrapper.set("totalPeople",totalPeople);
        updateWrapper.set("submitPeople",submitPeople);
        updateWrapper.set("passPeople",passPeople);
        updateWrapper.set("averageScore",averageScore);
        homeworkquestionMapper.update(null,updateWrapper);


        return 1;
    }

    @Override
    public List<User> ClassTotalData(String className,String teacherName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("teacherName",teacherName);
        queryWrapper.eq("className",className);
        List<User> users = userMapper.selectList(queryWrapper);
        return users;
    }

}





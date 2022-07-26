package com.cymabatis.mabatis_try2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cymabatis.mabatis_try2.model.domain.Classes;
import com.github.jeffreyning.mybatisplus.service.IMppService;

import javax.servlet.http.HttpServletRequest;

/**
* @author 99537
* @description 针对表【classes】的数据库操作Service
* @createDate 2022-07-08 13:03:16
*/
public interface ClassesService extends IMppService<Classes> {


    /**
     * 老师创建班级
     */

    public long createClass(String className,String classTeacher);


    /**
     * 老师查询已存在的班级
     */
    public long selectClass(HttpServletRequest request);


    public String selectClassInviteCode(String className,HttpServletRequest request);


}

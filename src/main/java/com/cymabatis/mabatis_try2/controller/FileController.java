package com.cymabatis.mabatis_try2.controller;


import com.cymabatis.mabatis_try2.model.domain.Homework;
import com.cymabatis.mabatis_try2.model.domain.User;
import com.cymabatis.mabatis_try2.model.domain.request.FileRequest;

import com.cymabatis.mabatis_try2.service.HomeworkService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.cymabatis.mabatis_try2.constant.UserConstant.USER_LOGIN_STATE;



@Controller
public class FileController {

    @Resource
    HomeworkService homeworkService;

    @PostMapping("/upload")
    public String upload(FileRequest fileRequest, HttpServletRequest request) throws IOException {

        User studentuser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        request.setAttribute("Loginuser",studentuser);
        String className = studentuser.getClassName();
        String userAccount = studentuser.getUserAccount();
        String homeworkName = fileRequest.getHomeworkName();
        String teacherName = (String) request.getSession().getAttribute("classTeacher");




        System.out.println("upload方法运行");

        MultipartFile file = fileRequest.getHomeworkFile();

        String oldname = file.getOriginalFilename();//getOriginalFilename()获取文件名带后缀
        //UUID去掉中间的"-",并将原文件后缀名加入新文件
        String newname = UUID.randomUUID().toString().replace("-", "")
                + oldname.substring(oldname.lastIndexOf("."));


        String uploaddate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //设置文件上传保存文件路径：保存在项目运行目录下的uploadFile文件夹+当前日期
        String savepath = request.getSession().getServletContext().getRealPath("/uploadFile/") + uploaddate;
        //创建文件夹,当文件夹不存在时，创建文件夹
        File folder = new File(savepath);
        if (!folder.isDirectory()) {
            folder.mkdir();
        }


        //下面一段程序，是为了建立文件夹，并存放上传的文件
        if (!file.isEmpty()) {
            //重命名上传的文件,为避免重复,我们使用UUID对文件分别进行命名


            //建立每一个文件上传的返回参数
            //文件保存操作
            try {
                File uploadFile = new File(folder, newname);

                FileUtils.copyInputStreamToFile(file.getInputStream(), uploadFile);


                //建立新文件路径,在前端可以直接访问如http://localhost:8080/uploadFile/2021-07-16/新文件名(带后缀)
                String filepath = request.getScheme() + "://" + request.getServerName() + ":" +
                        request.getServerPort() + "/uploadFile/" + uploaddate + "/" + newname;


                homeworkService.saveHomework(homeworkName,userAccount,filepath,className,teacherName);


                List<Homework> homeworkList = homeworkService.checkClassHomework(className, userAccount,teacherName);
                request.getSession().setAttribute("homeworkList",homeworkList);


            } catch (IOException e) {
                e.printStackTrace();
            }




            return "student";
        }
        return "student";
    }
}
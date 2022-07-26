package com.cymabatis.mabatis_try2.controller;


import com.cymabatis.mabatis_try2.model.domain.Homework;
import com.cymabatis.mabatis_try2.model.domain.User;
import com.cymabatis.mabatis_try2.model.domain.request.ScoreRequest;
import com.cymabatis.mabatis_try2.service.HomeworkService;
import com.cymabatis.mabatis_try2.service.HomeworkquestionService;
import org.apache.commons.io.FileUtils;
import org.python.util.PythonInterpreter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class ScoreController {

    @Resource
    private HomeworkService homeworkService;
    @Resource
    private HomeworkquestionService homeworkquestionService;


    @PostMapping("/score")
    public String score(ScoreRequest scoreRequest,HttpServletRequest request) throws IOException{
        String className = (String) request.getSession().getAttribute("className");
        String teacherName = (String) request.getSession().getAttribute("classTeacher");
        System.out.println("score执行了！");
        String homeworkName = scoreRequest.getHomeworkName();
        MultipartFile[] input_txt = scoreRequest.getInput_txt(); //先假设是文件处理
        String filedeal = scoreRequest.getFiledeal();
        MultipartFile output_txt = scoreRequest.getOutput_txt();


        if(filedeal == null){
            filedeal = "";
        }



        //设置当前日期
        String uploaddate= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        //设置文件上传保存文件路径：保存在项目运行目录下的uploadFile文件夹+当前日期
        String savepath = request.getSession().getServletContext().getRealPath("/uploadFile/")+uploaddate;
        //创建文件夹,当文件夹不存在时，创建文件夹
        File folder = new File(savepath);
        if(!folder.isDirectory()){
            folder.mkdir();
        }


        System.out.println(homeworkName);
        List<Homework> homeworkList = homeworkService.checkStudentHomework(homeworkName,request);
        String funcName = homeworkList.get(0).getFuncName();
        if(funcName == null){
            funcName = "";
        }


        System.out.println("homeworkList为："+homeworkList);
        if(!homeworkList.isEmpty()) {
            System.out.println("homeworkList不为空！");

            String message = "";
            int count = 0;



            String linex;

            // 文件处理的答案文件  的处理

            String[] answer_txt_list = {};

            if (!output_txt.isEmpty()) {
                System.out.println("output_txt不为空");

                String answer_all = "";
                String answer_name = UUID.randomUUID().toString().replace("-", "") + output_txt.getOriginalFilename().substring(output_txt.getOriginalFilename().lastIndexOf(".")) + ".txt";
                File answer_File = new File(folder, answer_name);
                String answer_txt_path = request.getScheme() + "://" + request.getServerName() + ":" +
                        request.getServerPort() + "/uploadFile/" + uploaddate + "/" + answer_name;
                FileUtils.copyInputStreamToFile(output_txt.getInputStream(), answer_File);
                System.out.println(answer_txt_path);
                BufferedReader bw_answer = new BufferedReader(new FileReader(answer_File));
                String asas;
                while ((asas = bw_answer.readLine()) != null) {
                    answer_all += asas + ",";
                }
                answer_txt_list = answer_all.split(",");
                for(String s:answer_txt_list){
                    System.out.println("answer_txt_list："+s);
                }

                URL url3 = null;
                InputStream answer_txt_is = null;
                try {
                    url3 = new URL(answer_txt_path);
                } catch (MalformedURLException e) {
                    System.out.println("111x");
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection conn3 = (HttpURLConnection) url3.openConnection();
                    conn3.setDoInput(true);
                    conn3.connect();
                    answer_txt_is = conn3.getInputStream();

                } catch (IOException e) {
                    System.out.println("222x");
                    e.printStackTrace();
                }

            }



            for (Homework homework : homeworkList) {
                System.out.println("homework循环执行！");
                String studentName = homework.getStudentName();
                String filepath = homework.getFilePath();
                if(filepath == null){
                    continue;
                }
                int answer_sums = 0;
                int answer_right = 0;
                float timesum = 0;
                int answer_index = 0;
                count += 1;
                int count2 = 0;
                for (MultipartFile input_txt_single:input_txt) {
                    count2 += 1;

                    //下面一段程序，是为了建立文件夹，并存放上传的文件
                    if (filepath != null) {
                        System.out.println("filepath不为空！");
                        //重命名上传的文件,为避免重复,我们使用UUID对文件分别进行命名
                        String txtnames = input_txt_single.getOriginalFilename();
                        String txtname = UUID.randomUUID().toString().replace("-", "")
                                + txtnames.substring(txtnames.lastIndexOf(".")) + ".txt";

                        //建立每一个文件上传的返回参数
                        //文件保存操作
                        try {
                            File txtFile = new File(folder, txtname);

                            FileUtils.copyInputStreamToFile(input_txt_single.getInputStream(), txtFile);

                            //建立新文件路径,在前端可以直接访问如http://localhost:8080/uploadFile/2021-07-16/新文件名(带后缀)
                            String txtpath = request.getScheme() + "://" + request.getServerName() + ":" +
                                    request.getServerPort() + "/uploadFile/" + uploaddate + "/" + txtname;


                            System.out.println(txtpath);
                            System.out.println(filepath);


                            //下面一段程序，是为了读取已经上传的文件的输入流
                            URL url = null;
                            URL url2 = null;

                            InputStream is = null;
                            InputStream txtis = null;
                            try {
                                url = new URL(filepath);
                                url2 = new URL(txtpath);
                            } catch (MalformedURLException e) {
                                System.out.println("No0");
                                e.printStackTrace();
                            }
                            try {
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();//利用HttpURLConnection对象,我们可以从网络中获取网页数据.
                                HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                                conn.setDoInput(true);
                                conn2.setDoInput(true);
                                conn.connect();
                                conn2.connect();
                                is = conn.getInputStream();    //得到网络返回的输入流
                                txtis = conn2.getInputStream();

                            } catch (IOException e) {
                                System.out.println("No!");
                                e.printStackTrace();
                            }


                            //下面一段程序，是真正执行python程序并判断运行时间,并判断对错的。
                            try {
                                PythonInterpreter pythonInterpreter = new PythonInterpreter();

                                pythonInterpreter.exec("# -*- coding: utf-8 -*-");
                                pythonInterpreter.exec("#!/usr/bin/env python");


                                //数据文件不为空，而且勾选框指明数据文件本身就是用例
                                if (input_txt_single != null && filedeal.equals("文件处理")) {
                                    System.out.println("还没打开文件");
                                    pythonInterpreter.exec("s = open(\"qqq.txt\",\'w\')");
                                    System.out.println("打开了文件");
                                    BufferedReader bw1 = new BufferedReader(new FileReader(txtFile));
                                    String tmp;
                                    while ((tmp = bw1.readLine()) != null) {
                                        System.out.println("读取了信息：" + tmp);
                                        pythonInterpreter.exec("s.write(\"" + tmp + "\\n\")");
                                        System.out.println("s.write(\"" + tmp + "\\n\")");
                                    }
                                    pythonInterpreter.exec("s.close()");

                                    StringWriter realoutput = new StringWriter();
                                    pythonInterpreter.setOut(realoutput);
                                    pythonInterpreter.execfile(is);
                                    long starttime = System.currentTimeMillis();
                                    pythonInterpreter.exec("print(" + funcName + "(\"qqq.txt\"))");
                                    long endtime = System.currentTimeMillis();

                                    System.out.println("print(" + funcName + "(\"qqq.txt\"))");



                                    answer_sums += 1;

                                    int time = (int) (endtime - starttime);
                                    String realoutput_stra = realoutput.toString().trim();
                                    String realoutput_str = new String(realoutput_stra.getBytes("ISO-8859-1"), "utf-8");

                                    if (!output_txt.isEmpty()) {
                                        linex = answer_txt_list[answer_index];
                                        System.out.println("分割线");
                                        System.out.println(linex);
                                        System.out.println(realoutput_str);
                                        System.out.println("a");
                                        if (realoutput_str.equals(linex)) {
                                            answer_right += 1;
                                        }
                                    } else {
                                        message = "未上传答案文件！";
                                    }
                                    answer_index += 1;




                                    message += "已批改！";





                                }


                                //数据文件不为空，上传数据文件
                                else if (input_txt_single.equals("") == false) {


                                    BufferedReader bw = new BufferedReader(new FileReader(txtFile));
                                    String line;
                                    String nextline;
                                    int inputsums = 0;
                                    int inputnum = 0;
                                    pythonInterpreter.execfile(is);
                                    while ((line = bw.readLine()) != null) {
                                        inputsums += 1;
                                        nextline = bw.readLine();
                                        System.out.println(line);
                                        System.out.println(nextline);

                                        StringWriter realoutput = new StringWriter();
                                        pythonInterpreter.setOut(realoutput);

                                        long starttime = System.currentTimeMillis();
                                        pythonInterpreter.exec("print(" + funcName + "(" + line + "))");
                                        long endtime = System.currentTimeMillis();
                                        float time = (float) (endtime - starttime);
                                        timesum += time;
                                        String realoutput_str = realoutput.toString().trim();
                                        System.out.println(realoutput_str + "s");
                                        System.out.println(nextline + "a");
                                        if (realoutput_str.equals(nextline)) {
                                            inputnum += 1;
                                        }
                                    }
                                    bw.close();

                                    float inputRightnumx = (float) inputnum;
                                    float inputNumsumsx = (float) inputsums;
                                    String correctRate = String.format("%.1f", inputRightnumx / inputNumsumsx * 100);
                                    System.out.println(inputRightnumx);
                                    System.out.println(inputNumsumsx);
                                    System.out.println(correctRate);
                                    double storeScore = Double.parseDouble(correctRate);

                                    homeworkService.saveScore(homeworkName,studentName,storeScore,className,teacherName);


                                    int runtime = (int) (timesum / inputNumsumsx);
                                    System.out.println("timesum是"+timesum);
                                    System.out.println("是"+inputNumsumsx);
                                    message += "已批改！";
                                }



//                                }
                            } catch (Exception e) {
                                System.out.println(filepath);
                                System.out.println("NO1");
                                message = "上传失败！";
                                e.printStackTrace();
                            }


                            //这一小段程序，是处理有部分失败的情况。
                        } catch (IOException ex) {
                            //操作失败报错并写入返回参数
                            System.out.println("NO2");
                            ex.printStackTrace();
                            System.out.println("上传失败！");
                        }


                    } else {
                        message = "您尚未上传文件！";
                    }
                }

            try {
                if (!output_txt.isEmpty()) {
                    float answerRightnumx = (float) answer_right;
                    float answerNumsumsx = (float) answer_sums;
                    String correctRate = String.format("%.1f", answerRightnumx / answerNumsumsx * 100);
                    double storeScore = Double.parseDouble(correctRate);
                    homeworkService.saveScore(homeworkName, studentName, storeScore,className,teacherName);

                }
            }catch (Exception ea) {
                //操作失败报错并写入返回参数
                System.out.println("NO2");
                ea.printStackTrace();
                System.out.println("上传失败！");
            }



            }
            request.getSession().setAttribute("notice", message);

        }
        homeworkService.HomeworkTotalData(className,homeworkName,teacherName,request);
        homeworkquestionService.selectHomeworkQuestion(className,request);
        homeworkService.checkStudentHomework(homeworkName,request);
        return "class_more";
    }


    @PostMapping(value={"student_score","student_score.html"})
    public String student_score(HttpServletRequest request){
        String className = (String) request.getSession().getAttribute("className");
        String teacherName = (String) request.getSession().getAttribute("classTeacher");
        List<User> users = homeworkService.ClassTotalData(className,teacherName);
        for(User user:users){
            String userAccount = user.getUserAccount();
            homeworkService.singleStudentScore(className,userAccount,teacherName);
        }
        users = homeworkService.ClassTotalData(className,teacherName);
        request.getSession().setAttribute("users",users);
        return "student_score";
    }
}

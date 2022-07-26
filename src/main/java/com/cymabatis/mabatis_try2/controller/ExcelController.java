package com.cymabatis.mabatis_try2.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import com.cymabatis.mabatis_try2.model.domain.Homework;
import com.cymabatis.mabatis_try2.model.domain.User;
import com.cymabatis.mabatis_try2.service.HomeworkService;
import com.cymabatis.mabatis_try2.service.UserService;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
@Controller
public class ExcelController {

    @Resource
    private UserService userService;
    @Resource
    private HomeworkService homeworkService;



    @PostMapping("/excel")
    public String excel(HttpServletRequest request) {

        String className = (String) request.getSession().getAttribute("className");
        String teacherName = (String) request.getSession().getAttribute("classTeacher");

        ArrayList<Map<String, Object>> rows = CollUtil.newArrayList(data(className,teacherName));
        ExcelWriter writer = null;

        try {
            String uploaddate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            //设置文件上传保存文件路径：保存在项目运行目录下的uploadFile文件夹+当前日期
            String savepath = request.getSession().getServletContext().getRealPath("/uploadFile/") + uploaddate;
            //创建文件夹,当文件夹不存在时，创建文件夹
            File folder = new File(savepath);
            if (!folder.isDirectory()) {
                folder.mkdir();
            }

            String excelName = "Hutool" + System.currentTimeMillis() + ".xlsx";

            File uploadFile = new File(folder, excelName);
            //建立新文件路径,在前端可以直接访问如http://localhost:8080/uploadFile/2021-07-16/新文件名(带后缀)
            String path = request.getScheme() + "://" + request.getServerName() + ":" +
                    request.getServerPort() + "/uploadFile/" + uploaddate + "/" + excelName;

            request.setAttribute("path",path);
            System.out.println(path);



            // 通过工具类创建writer,固定的文件输出路径
            writer = ExcelUtil.getWriter(uploadFile);

            // 定义第一行合并单元格样式
            CellStyle headCellStyle = writer.getHeadCellStyle();
            // 设置内容字体
            Font font = writer.createFont();
            // 字体加粗
            font.setBold(true);
            // 字体颜色
            font.setColor(Font.COLOR_RED);
            headCellStyle.setFont(font);

            // 设置第 0 列的单元格的宽度，列数从零开始计算
            writer.setColumnWidth(0, 20);
            writer.setColumnWidth(1, 20);
            writer.setColumnWidth(2, 20);

            // 定义数据行的样式
            StyleSet style = writer.getStyleSet();
            // 设置单元格文本内容自动换行
            style.setWrapText();

            // 合并单元格后的标题行（第一行），使用默认标题样式
            writer.merge(rows.get(0).size() - 1, "成绩详细数据");
            // 一次性写出内容，使用默认样式，强制输出标题
            writer.write(rows, true);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                // 记住关闭 writer，释放内存
                writer.close();
            }
        }

        return "student_score";
    }


    /**
     * 构造 导出的数据
     *
     * @return
     */
    public List<Map<String, Object>> data(String className,String teacherName) {
        // 导出的数据
        ArrayList<Map<String, Object>> rows = new ArrayList<>();

        List<User> users = userService.selectStudent(className,teacherName);

        for(User user:users){
            String studentName = user.getUserAccount();
            List<Homework> homeworkList = homeworkService.checkClassHomework(className, studentName,teacherName);
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("学生姓名学号", studentName);
            for(Homework homework:homeworkList){
                String homeworkName = homework.getHomeworkName();
                Double score = homework.getScore();
                if(score == null){
                    score = 0.0;
                }
                row.put(homeworkName, score);
            }
            row.put("总成绩",user.getScore());
            rows.add(row);
        }

        return rows;
    }
}

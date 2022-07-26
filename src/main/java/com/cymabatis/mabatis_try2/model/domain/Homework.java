package com.cymabatis.mabatis_try2.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName homework
 */
@TableName(value ="homework")
@Data
public class Homework implements Serializable {
    /**
     * 
     */
    private String endTime;

    /**
     * 
     */
    private String funcName;

    /**
     * 
     */
    private String homeworkName;

    /**
     * 
     */
    private String studentName;

    /**
     * 
     */
    private String isSubmit;

    /**
     * 
     */
    private String filePath;

    /**
     * 
     */
    private Double score;

    /**
     * 
     */
    private String className;

    /**
     * 
     */
    private String teacherName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
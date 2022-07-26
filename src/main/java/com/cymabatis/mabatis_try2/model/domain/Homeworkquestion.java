package com.cymabatis.mabatis_try2.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName homeworkquestion
 */
@TableName(value ="homeworkquestion")
@Data
public class Homeworkquestion implements Serializable {
    /**
     * 
     */
    private String homeworkName;

    /**
     * 
     */
    private String mainContent;

    /**
     * 
     */
    private String request;

    /**
     * 
     */
    private String teacherName;

    /**
     * 
     */
    private String className;

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
    private Integer totalPeople;

    /**
     * 
     */
    private Integer submitPeople;

    /**
     * 
     */
    private Integer passPeople;

    /**
     * 
     */
    private Double averageScore;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
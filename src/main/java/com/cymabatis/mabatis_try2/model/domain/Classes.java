package com.cymabatis.mabatis_try2.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import lombok.Data;

/**
 * 
 * @TableName classes
 */
@TableName(value ="classes")
@Data
public class Classes implements Serializable {
    /**
     * 
     */
    @MppMultiId
    @TableField
    private String className;

    /**
     * 
     */
    @MppMultiId
    @TableField
    private String classTeacher;

    /**
     * 
     */
    private String inviteCode;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
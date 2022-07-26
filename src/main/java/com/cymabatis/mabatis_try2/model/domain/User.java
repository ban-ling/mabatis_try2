package com.cymabatis.mabatis_try2.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 用户角色
普通用户为0
管理员为1
     */
    private String userRole;

    /**
     * 状态
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 
     */
    private Integer isDelete;

    /**
     * 
     */
    private String className;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 学生成绩
     */
    private Double score;

    /**
     * 
     */
    private String teacherName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
package com.cymabatis.mabatis_try2.model.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 305654286658608077L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String userRole;
    private String inviteCode;
}

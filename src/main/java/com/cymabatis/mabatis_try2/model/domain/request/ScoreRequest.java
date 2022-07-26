package com.cymabatis.mabatis_try2.model.domain.request;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class ScoreRequest implements Serializable {

    private static final long serialVersionUID = 305654286658608070L;

    private String homeworkName;
    private String funcName;
    private MultipartFile[] input_txt;
    private MultipartFile output_txt;
    private String filedeal;
}

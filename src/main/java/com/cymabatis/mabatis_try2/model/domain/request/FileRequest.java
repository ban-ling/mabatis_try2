package com.cymabatis.mabatis_try2.model.domain.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.Serializable;


@Data
public class FileRequest implements Serializable {
    private static final long serialVersionUID = -8810925958988725014L;
    private MultipartFile homeworkFile;
    private String studentName;
    private String homeworkName;
}

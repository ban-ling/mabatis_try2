package com.cymabatis.mabatis_try2.model.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class DeleteRequest implements Serializable {
    private static final long serialVersionUID = -8810925958988725010L;

    private String className;

    private String homeworkName;

    private String studentName;
}

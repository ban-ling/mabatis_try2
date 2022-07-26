package com.cymabatis.mabatis_try2.service;

public interface DeleteService {

    public void deleteClass(String className,String classTeacher);

    public void deleteHomework(String className, String homeworkName,String classTeacher);

    public void deleteStudent(String className, String studentName,String classTeacher);

}

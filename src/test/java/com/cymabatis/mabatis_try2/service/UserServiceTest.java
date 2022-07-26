//package com.cymabatis.mabatis_try2.service;
//import java.util.Date;
//
//import com.cymabatis.mabatis_try2.model.domain.S;
//import com.cymabatis.mabatis_try2.model.domain.User;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import javax.annotation.Resource;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * 用户服务测试
// * @author cy
// */
//@SpringBootTest
//class UserServiceTest {
//    @Resource
//    private UserService userService;
//    @Test
//    public void testAddUser(){
//        User user = new User();
//        user.setUsername("cy");
//        user.setUserAccount("123") ;
//        user.setAvatarUrl("https://article-images.zsxq.com/Fs0nX73cCjsZMWQzq12sY6DWBHVE");
//
//        user.setUserPassword("xxx");
//        boolean result = userService.save(user);
//        System.out.println(user.getId());
//        assertTrue(result);
//    }
//
//    @Test
//    void userRegister() {
//        String userRole = "student";
//        String tmp_class = "1班";
//        String userAccount = "cy";
//        String userPassword = "";
//        String checkPassword = "123456";
//        long result = userService.userRegister(userRole,userAccount,userPassword,checkPassword,tmp_class);
//        Assertions.assertEquals(-1,result);
//        userAccount = "c";
//        result = userService.userRegister(userRole,userAccount,userPassword,checkPassword,tmp_class);
//        Assertions.assertEquals(-1,result);
//        userAccount = "cyzz";
//        userPassword = "12345678";
//        result = userService.userRegister(userRole,userAccount,userPassword,checkPassword,tmp_class);
//        Assertions.assertEquals(-1,result);
//        userAccount = "c y8";
//        result = userService.userRegister(userRole,userAccount,userPassword,checkPassword,tmp_class);
//        Assertions.assertEquals(-1,result);
//        userPassword = "12345678";
//        result = userService.userRegister(userRole,userAccount,userPassword,checkPassword,tmp_class);
//        Assertions.assertEquals(-1,result);
//        checkPassword = "123456789";
//        userAccount = "lalalalalalas2";
//        userPassword = "123456789";
//        result = userService.userRegister(userRole,userAccount,userPassword,checkPassword,tmp_class);
//        Assertions.assertTrue(result > 0);
//
//
//    }
//}
package generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cymabatis.mabatis_try2.model.domain.User;
import generator.service.UserService;
import com.cymabatis.mabatis_try2.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 99537
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2022-07-24 13:22:16
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}





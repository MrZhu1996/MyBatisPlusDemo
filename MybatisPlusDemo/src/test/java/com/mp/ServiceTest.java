package com.mp;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mp.entiy.User;
import com.mp.service.UserInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceTest {

    @Autowired
    private UserInfoService userInfoService;

    @Test
    public void getOne(){
        User one = userInfoService.getOne(Wrappers.<User>lambdaQuery().gt(User::getAge,25),false);
        System.out.println(one);
    }

}

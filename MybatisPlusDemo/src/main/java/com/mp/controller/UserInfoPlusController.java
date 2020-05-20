package com.mp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mp.entiy.User;
import com.mp.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/userInfoPlus")
public class UserInfoPlusController {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/getInfoListPlus")
    public Map<String,Object> getInfoListPage(){
        //初始化返回类
        Map<String,Object> result = new HashMap<>();
        //查询年龄等于25岁的用户
        //等价SQL:SELECT id,name,age,email,create_time from mp_user where age=25
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().eq(User::getAge,25);
        List<User> userList1 = userInfoService.list(queryWrapper1);
        result.put("userAge25",userList1);
        return result;
    }
}

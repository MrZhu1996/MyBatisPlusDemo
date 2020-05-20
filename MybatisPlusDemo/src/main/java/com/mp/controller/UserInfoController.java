package com.mp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mp.dao.UserMapper;
import com.mp.entiy.User;
import com.mp.service.UserInfoService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

import static java.time.LocalDate.now;

@RestController
@RequestMapping("/userinfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserMapper userMapper;

    /**
     * 根据ID获取用户信息
     * @Author zhukf
     * @CreateTime 2020/1/6
     * @param userId
     * @return User 用户实体
     */
    @GetMapping("/getInfo")
    public User getInfo(@RequestParam String userId){
        User user = userInfoService.getById(userId);
        return user;
    }

    /**
     * 查询全部信息
     * @Author zhukf
     * @CreateTime 2020/1/6
     * @Return List<User> 用户实体集合
     */
    @GetMapping("/getList")
    public List<User> getList(){
        List<User> userList = userInfoService.list();
        return userList;
    }

    /**
     * 分页查询全部数据
     * @author zhukf
     * @CreateTime 2020/1/6
     * @Return IPage<Useer>  分页数据
     */
    @GetMapping("/getInfoListPage")
    public IPage<User> getInfoListPage(){
        //需要在Config配置类中配置分页插件
        IPage<User> page = new Page<>();
        page.setCurrent(1);//当前页
        page.setSize(5);//每页条数
        page = userInfoService.page(page);
        return page;
    }

    /**
     * 根据指定字段查询用户信息集合
     */
    @GetMapping("/getListMap")
    public void getListMap(){
        Map<String,Object> map = new HashMap<>();
        //key是字段名，value是字段值
        map.put("age",25);
        Collection<User> userList =userInfoService.listByMap(map);
        for (User user : userList) {
            System.out.println(user);
        }
    }

    /**
     * 新增用户信息
     */
    @PostMapping("/saveInfo")
    public void saveInfo(){
        User user = new User();
        user.setName("张无忌");
        user.setAge(23);
        user.setEmail("zwj@baomidou.com");
        user.setCreateTime(LocalDateTime.now());
        user.setManagerId(1088248166370832385L);
        user.setRemark("我是测试新增方法用户");
        userInfoService.save(user);
    }

    /**
     * 批量新增用户信息
     */
    @PostMapping("/saveInfoList")
    public void saveInfoList(){
        //创建对象
        User user = new User();
        user.setName("张帅");
        user.setAge(26);
        user.setRemark("我是测试批量新增方法用户1");

        User user1 = new User();
        user1.setName("张帅");
        user1.setAge(26);
        user1.setRemark("我是测试批量新增方法用户2");

        //批量保存
        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user1);
        userInfoService.saveBatch(userList);
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/updateInfo")
    public void updateInfo(){
        //根据实体中的ID去更新,其他字段如果值为null则不会更新该字段,参考yml配置文件
        User user = new User();
        user.setId(1087982257332887553L);
        user.setAge(45);
        userInfoService.updateById(user);
    }

    /**
     * 新增或者更新用户信息
     */
    @PostMapping("/saveOrUpdateInfo")
    public void saveOrUpdate(){
        //传入的实体类user中ID为null就会新增(ID自增)
        //实体类ID值存在,如果数据库存在ID就会更新,如果不存在就会新增
        User user = new User();
        user.setId(1L);
        user.setAge(20);
        userInfoService.saveOrUpdate(user);  
    }

    /**
     * 根据ID删除用户信息
     */
    @DeleteMapping("/deleteInfo")
    public void deleteInfo(String userId){
        userInfoService.removeById(userId);
    }

    /**
     * 根据ID批量删除用户信息
     */
    @DeleteMapping("/deleteInfoList")
    public void deleteInfoList(){
        List<String> userIdlist = new ArrayList<>();
        userIdlist.add("12");
        userIdlist.add("13");
        userInfoService.removeByIds(userIdlist);
    }

    /**
     * 根据指定字段删除用户信息
     */
    @DeleteMapping("/deleteInfoMap")
    public void deleteInfoMap(){
        //kay是字段名 value是字段值
        Map<String,Object> map = new HashMap<>();
        map.put("name","向北");
        map.put("age",26);
        userInfoService.removeByMap(map);
    }


}

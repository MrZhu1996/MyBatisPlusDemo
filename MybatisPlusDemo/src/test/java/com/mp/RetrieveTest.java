package com.mp;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.mp.dao.UserMapper;
import com.mp.entiy.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;
import java.text.DateFormat;
import java.util.*;
import java.util.function.Predicate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RetrieveTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void selectById(){
        User user = userMapper.selectById(1088250446457389058L);
        System.out.println(user);
    }

    @Test
    public void selectIds(){
        List<Long> idList = Arrays.asList(1088248166370832385L,
                1094592041087729666L,1094590409767661570L);
        List<User> userList = userMapper.selectBatchIds(idList);
        userList.forEach(System.out :: println);
    }

    @Test
    public void selectByMap(){
        //map.put("name","王天风")
        //map.put("age",30)
        //where name="王天风" and age=30
        Map<String,Object> columnMap = new HashMap<>();
        //columnMap.put("name","王天风");
        //列age为数据库中的列名，不是实体类中的属性名
        columnMap.put("age",27);
        List<User> userList = userMapper.selectByMap(columnMap);
        userList.forEach(System.out::println);
    }

    //以下为以条件构造器为参数的查询

    /**
     * 1、名字中包含雨并且年龄小于40
     * name like '%雨%' and age<40
     */
    @Test
    public void selectByWrapper(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        //QueryWrapper<User> query = Wrappers.<User>query();
        queryWrapper.like("name","雨").lt("age",40);
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 名字中包含雨年并且龄大于等于20且小于等于40并且email不为空
     * name like '%雨%' and age between 20 and 40 and email is not null
     */
    @Test
    public void selectByWrapper2(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.like("name","雨").between("age",20,40).isNotNull("email");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 名字为王姓或者年龄大于等于25，按照年龄降序排列，年龄相同按照id升序排列
     * name like '王%' or age>=25 order by age desc,id asc
     */
    @Test
    public void selectByWrapper3(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.likeRight("name","王").or().ge("age",25).orderByDesc("age").orderByAsc("id");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 创建日期为2019年2月14日并且直属上级为名字为王姓
     * date_format(create_time,'%Y-%m-%d')='2019-02-14' and manager_id in (select id from user where name like '王%')
     * 采用这种方式防止sql注入：queryWrapper.apply("date_format(create_time,'%Y-%m-%d')={0}","2019-02-14")
     */
    @Test
    public void selectByWrapper4(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("date_format(create_time,'%Y-%m-%d')={0}","2019-02-14")
                .inSql("manager_id","select id from mp_user where name like '王%'");
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 名字为王姓并且（年龄小于40或邮箱不为空）
     * name like '王%' and (age<40 or email is not null)
     */
    @Test
    public void selectByWrapper5(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("name","王").and(wq->wq.lt("age",40).or().isNotNull("email"));
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 名字为王姓或者（年龄小于40并且年龄大于20并且邮箱不为空）
     * name like '王%' or (age<40 and age>20 and email is not null)
     */
    @Test
    public void selectByWrapper6(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("name","王").or(qw->qw.lt("age",40).gt("age",20).isNotNull("email"));
        List<User> userList = userMapper.selectList(queryWrapper);
        for (User user : userList) {
            System.out.println(user);
        }
    }

    /**
     * （年龄小于40或邮箱不为空）并且名字为王姓
     *  (age<40 or email is not null) and name like '王%'
     */
    @Test
    public void selectByWrapper7(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.nested(wq->wq.lt("age",40).or().isNotNull("email")).likeRight("name","王");
        List<User> userList = userMapper.selectList(queryWrapper);
        for (User user : userList) {
            System.out.println(user);
        }
    }

    /**
     * 年龄为30、31、34、35
     * age in (30、31、34、35)
     */
    @Test
    public void selectByWrapper8(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("age",Arrays.asList(30,31,34,35));
        //queryWrapper.in("age",30,31,34,35);
        List<User> userList = userMapper.selectList(queryWrapper);
        for (User user : userList) {
            System.out.println(user);
        }
    }

    /**
     * 9、只返回满足条件的其中一条语句即可
     * limit 1
     *
     * 无视优化规则直接拼接到 sql 的最后
     * 只能调用一次,多次调用以最后一次为准 有sql注入的风险,请谨慎使用
     */
    @Test
    public void selectByWrapper9(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("age",Arrays.asList(30,31,34,35)).last("limit 1");
        List<User> userList = userMapper.selectList(queryWrapper);
        for (User user : userList) {
            System.out.println(user);
        }
    }

    /**
     * 不列出全部字段
     * 10、名字中包含雨并且年龄小于40(需求1加强版)
     * 第一种情况：select id,name
     * 	           from user
     * 	           where name like '%雨%' and age<40
     */
    @Test
    public void selectByWrapperSupper(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","name").like("name","雨").lt("age",40);
        List<User> userList = userMapper.selectList(queryWrapper);
        for (User user : userList) {
            System.out.println(user);
        }
    }

    /**
     * 名字中包含雨并且年龄小于40(需求1加强版)
     * 第二种情况：select id,name,age,email
     * 	           from user
     * 	           where name like '%雨%' and age<40
     */
    @Test
    public void selectByWrapperSupper2(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name","雨").lt("age",40)
                .select(User.class,info->!info.getColumn().equals("create_time")&&
                        !info.getColumn().equals("manager_id"));
        List<User> userList = userMapper.selectList(queryWrapper);
        for (User user : userList) {
            System.out.println(user);
        }
    }

    @Test
    public void testCondition(){
        String name="";
        String email="x";
        condition(name,email);
    }

    private void condition(String name,String email){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        if(StringUtils.isNotEmpty(name)){
//            queryWrapper.like("name",name);
//        }
//        if(StringUtils.isNotEmpty(email)){
//            queryWrapper.like("email",email);
//        }

        queryWrapper.like(StringUtils.isNotEmpty(name),"name",name)
                .like(StringUtils.isNotEmpty(email),"email",email);
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);

    }

    @Test
    public void selectByWrapperEntity(){
        User whereUser = new User();
        whereUser.setName("刘红雨");
        whereUser.setAge(32);
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>(whereUser);
        List<User> userList = userMapper.selectList(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 11、按照直属上级分组，查询每组的平均年龄、最大年龄、最小年龄。
     *     并且只取年龄总和小于500的组
     *
     *     select avg(age) avg_age,min(age) min_age,max(age) max_age
     *     from user
     *     group by manager_id
     *     having sum(age) <500
     */

    @Test
    public void selectByWrapperMaps2(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.select("avg(age) avg_age","min(age) min_age","max(age) max_age")
                .groupBy("manager_id")
                .having("sum(age)<{0}",500);

        List<Map<String,Object>> userList =userMapper.selectMaps(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 注意:selectObjs返回结果只返回一列
     */
    @Test
    public void selectByWrapperObjs(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.select("id","name").like("name","雨")
                .lt("age",40);
        List<Object> userList = userMapper.selectObjs(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 查询记录数
     */
    @Test
    public void selectByWrapperCount(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name","雨").lt("age",40);

        Integer count = userMapper.selectCount(queryWrapper);
        System.out.println("总记录数" + count);
    }

    @Test
    public void selectByWrapperOne(){

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name","红").lt("age",40);
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }

    /**
     * lambda条件构造器可以防误写
     * 以下展示了lambda条件构造器的三种用法
     *
     */
    @Test
    public void selectLambda(){
//        LambdaQueryWrapper<User> lambda = new QueryWrapper<User>().lambda();
//        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> LambdaQuery = Wrappers.lambdaQuery();
        LambdaQuery.like(User::getName,"雨").lt(User::getAge,40);
        //where name like '%雨%'
        List<User> userList = userMapper.selectList(LambdaQuery);
        userList.forEach(System.out::println);
    }

    /**
     * 名字为王姓并且（年龄小于40或邮箱不为空）
     * name like '王%' and (age<40 or email is not null)
     */
    @Test
    public void selectLambda2(){
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.likeRight(User::getName,"王")
                .and(qw->qw.lt(User::getAge,40).or().isNotNull(User::getEmail));
        List<User> userList = userMapper.selectList(lambdaQuery);
        userList.forEach(System.out::println);
    }

    /**
     * 3.0.7新增条件构造器
     */
    public void selectLambda3(){
        List<User> userList = new LambdaQueryChainWrapper<>(userMapper)
                .like(User::getName,"雨")
                .ge(User::getAge,20).list();
        userList.forEach(System.out::println);
    }

    @Test
    public void selectPage(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.ge("age",26);

        Page<User> page = new Page<User>(1,2,false);
//        IPage<User> iPage = userMapper.selectPage(page,queryWrapper);
//        System.out.println("总页数" + iPage.getPages());
//        System.out.println("总记录数" + iPage.getTotal());
//        List<User> userList = iPage.getRecords();

        IPage<Map<String,Object>> iPage = userMapper.selectMapsPage(page,queryWrapper);
        System.out.println("总页数" + iPage.getPages());
        System.out.println("总记录数" + iPage.getTotal());
        List<Map<String,Object>> userList = iPage.getRecords();
        userList.forEach(System.out::println);
    }



}

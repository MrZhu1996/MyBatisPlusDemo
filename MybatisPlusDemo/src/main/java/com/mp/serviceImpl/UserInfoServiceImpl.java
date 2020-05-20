package com.mp.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mp.service.UserInfoService;
import com.mp.dao.UserMapper;
import com.mp.entiy.User;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserMapper, User> implements UserInfoService {
}

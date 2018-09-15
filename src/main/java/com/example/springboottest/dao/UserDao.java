package com.example.springboottest.dao;

import com.example.springboottest.bean.UserData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserDao {
    public UserData findByUserAccount(UserData user);
    public UserData findByUserId(UserData user);
    public void insert(UserData user);
    public void updatePassword(UserData user);

}

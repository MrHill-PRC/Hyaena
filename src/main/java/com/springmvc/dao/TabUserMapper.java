package com.springmvc.dao;

import com.springmvc.entity.TabUser;

import java.util.List;

public interface TabUserMapper {
    int deleteByPrimaryKey(String sId);

    int insert(TabUser record);

    int insertSelective(TabUser record);

    TabUser selectByPrimaryKey(String sId);

    int updateByPrimaryKeySelective(TabUser record);

    int updateByPrimaryKey(TabUser record);

    List<TabUser> selctByPrimary(TabUser record);
}
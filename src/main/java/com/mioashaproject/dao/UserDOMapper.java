package com.mioashaproject.dao;

import com.mioashaproject.dataobject.UserDO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserDO record);

    int insertSelective(UserDO record);

    UserDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserDO record);

    int updateByPrimaryKey(UserDO record);

    UserDO selectByTelphone(String telphone);
}
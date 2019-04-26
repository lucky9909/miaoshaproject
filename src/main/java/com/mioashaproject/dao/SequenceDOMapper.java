package com.mioashaproject.dao;

import com.mioashaproject.dataobject.SequenceDO;
import org.springframework.stereotype.Repository;

@Repository
public interface SequenceDOMapper {
    int deleteByPrimaryKey(String name);

    int insert(SequenceDO record);

    int insertSelective(SequenceDO record);

    SequenceDO selectByPrimaryKey(String name);

    int updateByPrimaryKeySelective(SequenceDO record);

    int updateByPrimaryKey(SequenceDO record);

    SequenceDO getSequenceByName(String name);
}
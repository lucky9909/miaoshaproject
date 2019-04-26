package com.mioashaproject.dao;

import com.mioashaproject.dataobject.ItemDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemDOMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ItemDO record);

    int insertSelective(ItemDO record);

    ItemDO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ItemDO record);

    int updateByPrimaryKey(ItemDO record);

    //商品列表浏览
    List<ItemDO> listItem();

    //商品销量增加
    int increaseSales(@Param("id") Integer id,@Param("amount") Integer amount);
}
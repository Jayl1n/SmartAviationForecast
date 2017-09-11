package edu.cap.dao;

import edu.cap.model.po.EventRaw;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EventRawMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(EventRaw record);

    int insertSelective(EventRaw record);

    EventRaw selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EventRaw record);

    int updateByPrimaryKey(EventRaw record);
}
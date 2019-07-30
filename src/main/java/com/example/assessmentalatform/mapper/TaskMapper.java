package com.example.assessmentalatform.mapper;

import com.example.assessmentalatform.bean.Task;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TaskMapper {

    public int insertTask(Task task);
}

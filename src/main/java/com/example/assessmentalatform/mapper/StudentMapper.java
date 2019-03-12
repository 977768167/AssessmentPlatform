package com.example.assessmentalatform.mapper;

import com.example.assessmentalatform.bean.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;

@Mapper
public interface StudentMapper {

    public int insertStu(Student student);

    public int selectStuNumber();

    public Collection<Student> selectStuByPage(Integer page);

    public int deleteStuById(Integer id);
}

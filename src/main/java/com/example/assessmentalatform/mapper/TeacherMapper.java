package com.example.assessmentalatform.mapper;

import com.example.assessmentalatform.bean.Teacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;

@Mapper
public interface TeacherMapper {

    public int insertTeacher(Teacher teacher);

    public int selectTeacherNumber();

    public Collection<Teacher> selectTeacherByPage(Integer page);

    public Teacher selectTeacherById(Integer id);

    public int deleteTeacherById(Integer id);

    public Teacher selectTeacherByTeacherId(String teacherId);
}

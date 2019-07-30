package com.example.assessmentalatform.mapper;

import com.example.assessmentalatform.bean.Course;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;

@Mapper
public interface CourseMapper {

    public int insertCourse(Course course);

    public int selectCourseNumber();

    public Collection<Course> selectCourseByPage(Integer page);

    public int deleteCourseById(Integer id);

    public int selectCourseByTeacherIdNumber(String TeacherId);

    public Collection<Course> selectCourseByTeacherId(String TeacherId,Integer page);

}

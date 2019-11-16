package com.example.assessmentalatform.mapper;

import com.example.assessmentalatform.bean.Curriculum;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;

@Mapper
public interface CurriculumMapper {

    public int insertCurriculum(Curriculum curriculum);

    public int selectCurriculumNumber();

    public Collection<Curriculum> selectCurriculumByPage(Integer page);

    public int deleteCurriculumById(Integer id);

    public Collection<Curriculum> selectCurriculumByTeacherAndTime(String teacherId,String schoolYear,String term);

    public Collection<Curriculum> selectCurriculumByTeacherAndTimeAndPage(String teacherId,String schoolYear,String term,Integer page);

    public int selectCurriculumNumberByTeacherAndTime(String teacherId,String schoolYear,String term);

    public Collection<Curriculum> selectCurriculumByTeacherAndTimeAndClass(String teacherId,String schoolYear,String term,String speciality,String classes);
    //去重
    public Integer getCurriculumByTeacherAndTime(String teacherId,String schoolYear,String term);

}

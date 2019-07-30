package com.example.assessmentalatform.mapper;

import com.example.assessmentalatform.bean.Scores;
import com.example.assessmentalatform.bean.ScoresTwo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;

@Mapper
public interface ScoresMapper {
    public int insertScores(ScoresTwo scores);

    public int deleteScores(Integer id);
    //查询全部学生的某门课程每天的平均分
    public Collection<Scores> alleveryDayAvgScores(String courseId);
    //查询全部学生的某门课程每月的平均分
    public Collection<Scores> alleveryMonthAvgScores(String courseId);
    //查询学生某门课程每天的平均分
    public Collection<Scores> studenteveryDayAvgScores(String studentId, String courseId);
    //查询学生某门课程每月的平均分
    public Collection<Scores> studenteveryMonthAvgScores(String studentId, String courseId);
}

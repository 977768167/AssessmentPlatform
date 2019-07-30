package com.example.assessmentalatform.mapper;

import com.example.assessmentalatform.bean.MultipleChoice;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;

@Mapper
public interface MultipleChoiceMapper {

    //插入操作
    public int insertMultipleChoice(MultipleChoice multipleChoice);
    //查询题数量
    public int selectMultipleChoiceNumber();
    //根据页数查询题
    public Collection<MultipleChoice> selcetMultipleChoiceByPage(Integer page);
    //根据教师信息和科目查询题
    public Collection<MultipleChoice> selectMultipleChoiceByTeacherAndcourse(Integer teacher_nubmer,String course_name);
    //根据id删除题
    public int deleteMultipleChoiceById(Integer id);
    //根据教师id查询题
    public Collection<MultipleChoice> selectMultipleChoiceByTeacherId(String TeacherId);
    //随机抽题
    public Collection<MultipleChoice> selectMultipleChoiceByRand(Integer difficulty,Integer number);
}

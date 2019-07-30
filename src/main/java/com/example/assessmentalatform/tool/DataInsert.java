package com.example.assessmentalatform.tool;

import com.example.assessmentalatform.bean.*;
import com.example.assessmentalatform.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DataInsert {

    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    CurriculumMapper curriculumMapper;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    MultipleChoiceMapper multipleChoiceMapper;
    @Bean
    public DataInsert getDataInsert(){
        return new DataInsert();
    }
    public void insertData(ArrayList<ArrayList<Object>> result,Integer no){
        if (no==1) {
            Teacher teacher = new Teacher();
            for (int i = 0; i < result.size(); i++) {
                teacher.setTeacherNumber(result.get(i).get(0).toString());
                teacher.setTeacherName(result.get(i).get(1).toString());
                teacher.setBranch(result.get(i).get(2).toString());
                teacher.setPassword(result.get(i).get(3).toString());
                teacherMapper.insertTeacher(teacher);
            }
        }
        else if(no==2){
            Student student=new Student();
            for (int i = 0 ;i < result.size() ;i++){
                student.setSno(result.get(i).get(0).toString());
                student.setsName(result.get(i).get(1).toString());
                student.setBranch(result.get(i).get(2).toString());
                student.setSpeciality(result.get(i).get(3).toString());
                student.setClasses(result.get(i).get(4).toString());
                student.setPassword(result.get(i).get(5).toString());
                studentMapper.insertStu(student);
            }
        }
        else if(no==3){
            Course course=new Course();
            for (int i = 0 ;i < result.size() ;i++){
                course.setCourseId(result.get(i).get(0).toString());
                course.setCourseName(result.get(i).get(1).toString());
                course.setCourseType(result.get(i).get(2).toString());
                course.setCredit(result.get(i).get(3).toString());
                course.setCourseHour(result.get(i).get(4).toString());
                courseMapper.insertCourse(course);
            }
        }
        else if (no==4){
            Curriculum curriculum=new Curriculum();
            for (int i=0;i<result.size();i++){
                curriculum.setCourseId(result.get(i).get(0).toString());
                curriculum.setCourseName(result.get(i).get(1).toString());
                curriculum.setTeacherId(result.get(i).get(2).toString());
                curriculum.setTeacherName(result.get(i).get(3).toString());
                curriculum.setSpeciality(result.get(i).get(4).toString());
                curriculum.setClasses(result.get(i).get(5).toString());
                curriculum.setSchoolYear(result.get(i).get(6).toString());
                curriculum.setTerm(result.get(i).get(7).toString());
                curriculumMapper.insertCurriculum(curriculum);
            }
        }
        else {
            MultipleChoice multipleChoice=new MultipleChoice();
            for (int i=0;i<result.size();i++){
                multipleChoice.setQuestion(result.get(i).get(0).toString());
                multipleChoice.setA(result.get(i).get(1).toString());
                multipleChoice.setB(result.get(i).get(2).toString());
                multipleChoice.setC(result.get(i).get(3).toString());
                multipleChoice.setD(result.get(i).get(4).toString());
                multipleChoice.setAnswer(result.get(i).get(5).toString());
                multipleChoice.setDifficulty(result.get(i).get(6).toString());
                multipleChoice.setUploadTeacherNumber(result.get(i).get(7).toString());
                multipleChoice.setCourseName(result.get(i).get(8).toString());
                multipleChoiceMapper.insertMultipleChoice(multipleChoice);
            }
        }
    }
}

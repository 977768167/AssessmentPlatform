package com.example.assessmentalatform.controller;

import com.example.assessmentalatform.bean.*;
import com.example.assessmentalatform.mapper.CourseMapper;
import com.example.assessmentalatform.mapper.CurriculumMapper;
import com.example.assessmentalatform.mapper.StudentMapper;
import com.example.assessmentalatform.mapper.TeacherMapper;
import com.example.assessmentalatform.tool.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;


@Controller
public class AdminController {

    @Autowired
    TeacherMapper teacherMapper;
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    CurriculumMapper curriculumMapper;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    Transmission transmission;
    @Autowired
    ExcelUtil excelUtil;
    @Autowired
    FileWrite fileWrite;
    @Autowired
    DataInsert dataInsert;
    @Autowired
    Time time;
    String admin_menu="admin_menu";
    @GetMapping("/admin/insert")
    public String insertAdmin(@ModelAttribute Administrator administrator){
        //administratorMapper.insertAdmin(administrator);
        return "index-administrator";
    }

    @PostMapping("/admin/data/insert")//文件上传数据
    public String adminInsertData(@RequestParam("file") MultipartFile file,@RequestParam("no") Integer no){

        if(!file.isEmpty()){
            fileWrite.fileWritePath(file,file.getOriginalFilename());
        }
        File readFile=new File(file.getOriginalFilename());
        ArrayList<ArrayList<Object>> result = ExcelUtil.readExcel(readFile);
        dataInsert.insertData(result,no);
//        readFile.delete();
        return "file-request";
    }

    @GetMapping("/admin/data/page")//数据分页
    public String selectTeacherByPage(@RequestParam String object,@RequestParam("page") Integer page, Model model){

        if (object.equals("teacher")) {
            transmission.setPage(page);
            transmission.boundaryJudgment(teacherMapper.selectTeacherNumber());
            Collection<Teacher> teachers = teacherMapper.selectTeacherByPage((transmission.getPage() - 1) * 10);
            model.addAttribute("teachers", teachers);
            model.addAttribute("transmission", transmission);
            return "teacher-information";
        }
        else if(object.equals("stu")){
            transmission.setPage(page);
            transmission.boundaryJudgment(studentMapper.selectStuNumber());
            Collection<Student> students=studentMapper.selectStuByPage((transmission.getPage()-1)*10);
            model.addAttribute("students",students);
            model.addAttribute("transmission",transmission);
            model.addAttribute("url","/admin/data/page");
            return "student-information";
        }
        else if (object.equals("course")){
            transmission.setPage(page);
            transmission.boundaryJudgment(courseMapper.selectCourseNumber());
            Collection<Course> courses=courseMapper.selectCourseByPage((transmission.getPage()-1)*10);
            model.addAttribute("courses",courses);
            model.addAttribute("transmission",transmission);
            return "course-information";
        }
        else {
           transmission.setPage(page);
           transmission.boundaryJudgment(curriculumMapper.selectCurriculumNumber());
           Collection<Curriculum> curriculums=curriculumMapper.selectCurriculumByPage((transmission.getPage()-1)*10);
           model.addAttribute("curriculums",curriculums);
           model.addAttribute("transmission",transmission);
           return "curriculum-information";
        }
    }

    @GetMapping("/admin/teacher/information")//查询详细信息
    public String selectTeacherById(@RequestParam Integer id,Model model){

        Teacher teacher=teacherMapper.selectTeacherById(id);
        Collection<Curriculum> curriculums=curriculumMapper.selectCurriculumByTeacherAndTime(teacher.getTeacherNumber().toString(),time.schoolYearJudge(),time.termJudge());
        transmission.setMenu(admin_menu);
        model.addAttribute("transmission",transmission);
        model.addAttribute("curriculums",curriculums);
        model.addAttribute("teacher",teacher);
        return "teacher-detailed-information";
    }

    @GetMapping("/admin/data/delete")
    public String deleteTeacherById(@RequestParam String object,@RequestParam Integer id,@RequestParam("page") Integer page, Model model){

        if (object.equals("teacher")) {
            teacherMapper.deleteTeacherById(id);
            transmission.setPage(page);
            model.addAttribute("transmission",transmission);
            transmission.boundaryJudgment(teacherMapper.selectTeacherNumber());
            Collection<Teacher> teachers = teacherMapper.selectTeacherByPage((transmission.getPage() - 1) * 10);
            model.addAttribute("teachers", teachers);
            return "teacher-information";
        }
        else if (object.equals("stu")){
            studentMapper.deleteStuById(id);
            transmission.setPage(page);
            model.addAttribute("transmission",transmission);
            transmission.boundaryJudgment(studentMapper.selectStuNumber());
            Collection<Student> students=studentMapper.selectStuByPage((transmission.getPage() - 1) * 10);
            model.addAttribute("students",students);
            return "student-information";
        }
        else if (object.equals("course")){
            courseMapper.deleteCourseById(id);
            transmission.setPage(page);
            model.addAttribute("transmission",transmission);
            transmission.boundaryJudgment(courseMapper.selectCourseNumber());
            Collection<Course> courses=courseMapper.selectCourseByPage((transmission.getPage() - 1) * 10);
            model.addAttribute("courses",courses);
            return "course-information";
        }
        else {
            curriculumMapper.deleteCurriculumById(id);
            transmission.setPage(page);
            model.addAttribute("transmission",transmission);
            transmission.boundaryJudgment(curriculumMapper.selectCurriculumNumber());
            Collection<Curriculum> curriculums=curriculumMapper.selectCurriculumByPage((transmission.getPage()-1)*10);
            model.addAttribute("curriculums",curriculums);
            return "curriculum-information";
        }


    }
}

package com.example.assessmentalatform.controller;

import com.example.assessmentalatform.bean.*;
import com.example.assessmentalatform.mapper.*;
import com.example.assessmentalatform.tool.LoginCheck;
import com.example.assessmentalatform.tool.Time;
import com.example.assessmentalatform.tool.Transmission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;

@Controller
public class HTMLController {

    @Autowired
    Transmission transmission;
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
    String admin_menu="admin_menu";
    @Autowired
    LoginCheck loginCheck;
    @Autowired
    Time time;
    @GetMapping("/login")
    public String getLogin(){

        return "login";
    }
    @PostMapping("/index")
    public String getIndexAdmin(@RequestParam String adminName, @RequestParam String password, @RequestParam Integer status, Model model, HttpServletRequest request, HttpSession httpSession){//主界面

            User user=new User();
           user.setUserName(adminName);
           user.setPassword(password);
           //账号密码正确存session
           if (loginCheck.loginCheck(request,user,status)){
               httpSession.setAttribute("loginUser",user.getUserName());
           }
//管理员登录
           if (status==1) {
               Integer teacherNumber=teacherMapper.selectTeacherNumber();
               Integer courseNumber=courseMapper.selectCourseNumber();
               Integer stuNumber=studentMapper.selectStuNumber();
               Integer multipleNumber=multipleChoiceMapper.selectMultipleChoiceNumber();
               model.addAttribute("teacherNumber",teacherNumber);
               model.addAttribute("courseNumber",courseNumber);
               model.addAttribute("stuNumber",stuNumber);
               model.addAttribute("multipleNumber",multipleNumber);
               transmission.setMenu(admin_menu);
               return "index-administrator";
           }
           //教师登录
           else if (status==2) {
               Integer curriculumNumber=curriculumMapper.getCurriculumByTeacherAndTime(user.getUserName(),time.schoolYearJudge(),time.termJudge());
               Integer classNumber=curriculumMapper.selectCurriculumNumberByTeacherAndTime(user.getUserName(),time.schoolYearJudge(),time.termJudge());
               Integer myQuestionNumber=multipleChoiceMapper.selectMultipleChoiceByTeacherId(user.getUserName()).size();
               Integer questionNumber=multipleChoiceMapper.selectMultipleChoiceNumber();
               model.addAttribute("curriculumNumber",curriculumNumber);
               model.addAttribute("classNumber",classNumber);
               model.addAttribute("myQuestionNumber",myQuestionNumber);
               model.addAttribute("questionNumber",questionNumber);
               return "index-teacher";
           }


        return "login";
    }
    @GetMapping("/admin/teacher")
    public String getTeacherByAdmin(Model model){

        transmission.initialization(teacherMapper.selectTeacherNumber());
        model.addAttribute("transmission",transmission);
        Collection<Teacher> teachers=teacherMapper.selectTeacherByPage((transmission.getPage()-1)*10);
        model.addAttribute("teachers",teachers);
        return "teacher-information";
    }
    @GetMapping("/admin/stu")
    public String getStuByAdmin(Model model){

        transmission.initialization(studentMapper.selectStuNumber());
        transmission.setMenu(admin_menu);
        model.addAttribute("transmission",transmission);
        Collection<Student> students=studentMapper.selectStuByPage((transmission.getPage()-1)*10);
        model.addAttribute("students",students);
        model.addAttribute("url","/admin/data/page");
        return "student-information";
    }
    @GetMapping("/admin/curriculum")
    public  String getCurriculumByadmin(Model model){
        transmission.initialization(curriculumMapper.selectCurriculumNumber());
        transmission.setMenu(admin_menu);
        model.addAttribute("transmission",transmission);
        Collection<Curriculum> curriculums=curriculumMapper.selectCurriculumByPage((transmission.getPage()-1)*10);
        model.addAttribute("curriculums",curriculums);
        return "curriculum-information";
    }
    @GetMapping("/admin/course")
    public String getClassByAdmin(Model model){
        transmission.initialization(courseMapper.selectCourseNumber());
        model.addAttribute("transmission",transmission);
        Collection<Course> courses=courseMapper.selectCourseByPage((transmission.getPage()-1)*10);
        model.addAttribute("courses",courses);
        return "course-information";
    }
    @GetMapping("/admin/file/request")
    public String getClassFileByAdmin(){
        return "file-request";
    }
    @GetMapping("/admin/file/upload")
    public String goFileUoload(@RequestParam("no") Integer no, Model model){
        model.addAttribute("no",no);
        if (no==1)
            model.addAttribute("classification","教师");
        else if(no==2)
            model.addAttribute("classification","学生");
        else if (no==3)
            model.addAttribute("classification","课目");
        else
            model.addAttribute("classification","课表");
        return "file-uoload";
    }
    ///////////////////////////////////////////////////////////////////////
    @GetMapping("/stu/index")
    public String getIndexStu(){
        return "index-student";
    }

    @GetMapping("/admin/index")
    public String getIndexAdmin(Model model){

        Integer teacherNumber=teacherMapper.selectTeacherNumber();
        Integer courseNumber=courseMapper.selectCourseNumber();
        Integer stuNumber=studentMapper.selectStuNumber();
        Integer multipleNumber=multipleChoiceMapper.selectMultipleChoiceNumber();
        model.addAttribute("teacherNumber",teacherNumber);
        model.addAttribute("courseNumber",courseNumber);
        model.addAttribute("stuNumber",stuNumber);
        model.addAttribute("multipleNumber",multipleNumber);
        transmission.setMenu(admin_menu);
        return "index-administrator";
    }

    @GetMapping("/teacher/index")
    public String getIndexTeacher(Model model,HttpSession httpSession){
        Integer curriculumNumber=curriculumMapper.getCurriculumByTeacherAndTime(httpSession.getAttribute("loginUser").toString(),time.schoolYearJudge(),time.termJudge());
        Integer classNumber=curriculumMapper.selectCurriculumNumberByTeacherAndTime(httpSession.getAttribute("loginUser").toString(),time.schoolYearJudge(),time.termJudge());
        Integer myQuestionNumber=multipleChoiceMapper.selectMultipleChoiceByTeacherId(httpSession.getAttribute("loginUser").toString()).size();
        Integer questionNumber=multipleChoiceMapper.selectMultipleChoiceNumber();
        model.addAttribute("curriculumNumber",curriculumNumber);
        model.addAttribute("classNumber",classNumber);
        model.addAttribute("myQuestionNumber",myQuestionNumber);
        model.addAttribute("questionNumber",questionNumber);

        return "index-teacher";
    }
}

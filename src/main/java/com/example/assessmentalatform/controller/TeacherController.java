package com.example.assessmentalatform.controller;

import com.example.assessmentalatform.bean.*;
import com.example.assessmentalatform.mapper.*;
import com.example.assessmentalatform.tool.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

@Controller
public class TeacherController {

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
    @Autowired
    ScoresMapper scoresMapper;
    @Autowired
    TaskMapper taskMapper;
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
    @Autowired
    EchartsDataProcessing echartsDataProcessing;
    String teacher_menu="teacher_menu";
    @GetMapping("/teacher/multiplechoice")
    public String getMultipleChoiceByAdmin(Model model){
        transmission.initialization(multipleChoiceMapper.selectMultipleChoiceNumber());
        model.addAttribute("transmission",transmission);
        Collection<MultipleChoice> multipleChoices=multipleChoiceMapper.selcetMultipleChoiceByPage((transmission.getPage()-1)*10);
        model.addAttribute("multipleChoices",multipleChoices);
        return "teacher-multiple-choice";
    }

    @GetMapping("/teacher/question-request")
    public String goInsertData(){
        return "question-request";
    }
    @PostMapping("/teacher/data/insert")//文件上传数据(题)
    public String teacherInsertData(@RequestParam("file") MultipartFile file){

        if(!file.isEmpty()){
            fileWrite.fileWritePath(file,file.getOriginalFilename());
        }
        File readFile=new File(file.getOriginalFilename());
        ArrayList<ArrayList<Object>> result = ExcelUtil.readExcel(readFile);
        dataInsert.insertData(result,5);
//        readFile.delete();
        return "question-request";
    }

    @GetMapping("/teacher/student")
    public String getStuByTeacher(Model model, HttpSession httpSession){

        transmission.initialization(studentMapper.selectStuByTeacherIdNumber(httpSession.getAttribute("loginUser").toString()));
        transmission.setMenu(teacher_menu);
        model.addAttribute("transmission",transmission);
        Collection<Student> students1=studentMapper.selectTeacherIdFindStu(httpSession.getAttribute("loginUser").toString(),(transmission.getPage()-1)*10);
        model.addAttribute("students",students1);
        model.addAttribute("url","/teacher/data/page");
        return "student-information";
    }

    @GetMapping("/teacher/curriculum")
    public String getClassByTeacher(Model model,HttpSession httpSession){

        transmission.initialization(curriculumMapper.selectCurriculumNumberByTeacherAndTime(httpSession.getAttribute("loginUser").toString(),time.schoolYearJudge(),time.termJudge()));
        transmission.setMenu(teacher_menu);
        model.addAttribute("transmission",transmission);
        Collection<Curriculum> curriculums=curriculumMapper.selectCurriculumByTeacherAndTime(httpSession.getAttribute("loginUser").toString(),time.schoolYearJudge(),time.termJudge());
        model.addAttribute("curriculums",curriculums);
        return "curriculum-information";
    }

    @GetMapping("/teacher/student/information")//查询详细信息
    public String selectTeacherById(@RequestParam Integer id,Model model){

        Student student=studentMapper.selectStuById(id);
        transmission.setMenu(teacher_menu);
        model.addAttribute("transmission",transmission);
        Collection<Curriculum> curriculums=curriculumMapper.selectCurriculumByTeacherAndTimeAndClass("2014",time.schoolYearJudge(),time.termJudge(),student.getSpeciality(),student.getClasses());
        model.addAttribute("curriculums",curriculums);
        model.addAttribute("student",student);
        return "student-detailed-information";
    }

    @GetMapping("/teacher/student/chart")//显示图表
    public String showChart(@RequestParam String studentId, @RequestParam String courseId,Model model){
        transmission.setMenu(teacher_menu);
        model.addAttribute("studentId",studentId);
        model.addAttribute("courseId",courseId);
        model.addAttribute("transmission",transmission);
        return "scores-chart";
    }
    @ResponseBody
    @GetMapping("/teacher/student/chart/data")//获得详细图表数据
    public ScoresThree getChartData(@RequestParam String studentId, @RequestParam String courseId){
        Collection<Scores> scores=scoresMapper.studenteveryDayAvgScores(studentId,courseId);
        return echartsDataProcessing.getJsonData(scores);
    }

    @GetMapping("/teacher/generate-papers")//难度选择界面
    public String getGeneratePapers(Model model,HttpSession httpSession){
        transmission.setMenu(teacher_menu);
        Collection<Curriculum> courses=curriculumMapper.selectCurriculumByTeacherAndTime(httpSession.getAttribute("loginUser").toString(),time.schoolYearJudge(),time.termJudge());
        model.addAttribute("courses",courses);
        model.addAttribute("transmission",transmission);
        return "generate-papers";
    }

    @PostMapping("/teacher/generate-questions")//题目展示页面
    public String getGenerateQuestions(@ModelAttribute ProblemDifficulty problemDifficulty, Model model,HttpSession httpSession){

        Collection<MultipleChoice> testQuestions=multipleChoiceMapper.selectMultipleChoiceByRand(1,problemDifficulty.getSimple(),problemDifficulty.getSubject());
        testQuestions.addAll(multipleChoiceMapper.selectMultipleChoiceByRand(2,problemDifficulty.getMedium(),problemDifficulty.getSubject()));
        testQuestions.addAll(multipleChoiceMapper.selectMultipleChoiceByRand(3,problemDifficulty.getDifficulty(),problemDifficulty.getSubject()));

        Collection<Curriculum> curriculums=curriculumMapper.selectCurriculumByTeacherAndTime(httpSession.getAttribute("loginUser").toString(),time.schoolYearJudge(),time.termJudge());
        ArrayList<String> specialityClass=echartsDataProcessing.getSpecialityClass(curriculums);
        ArrayList<Integer> questionId=new ArrayList<>();
        Iterator<MultipleChoice> iterator=testQuestions.iterator();
        while (iterator.hasNext()){
            questionId.add(iterator.next().getQuestionId());
        }
        model.addAttribute("subject",problemDifficulty.getSubject());
        model.addAttribute("questionId",questionId);
        model.addAttribute("specialityClasses",specialityClass);
        model.addAttribute("testQuestions",testQuestions);
        transmission.setMenu(teacher_menu);
        model.addAttribute("transmission",transmission);
        return "problem";
    }
    @PostMapping("/teacher/generate-questions/confirm")
    public String confirmGenerateQuestions(@RequestParam String[] specialityClass,@RequestParam String[] questionId,@RequestParam String subject){

        Task task=new Task();
        for (int i=0;i<specialityClass.length;i++) {
            task.setCourseId(courseMapper.getCourseIdByName(subject));
            task.setCourseName(subject);
            task.setSpeciality(echartsDataProcessing.resolveSpecialityClass(specialityClass[i])[0]);
            task.setClasses(echartsDataProcessing.resolveSpecialityClass(specialityClass[i])[1]);
            task.setQuestion(Arrays.toString(questionId));
            taskMapper.insertTask(task);
        }
        return "index-teacher";
    }
    @GetMapping("/teacher/data/page")//数据分页
    public String teacherSelectByPage(@RequestParam String object,@RequestParam("page") Integer page, Model model,HttpSession httpSession){

        transmission.setPage(page);
        transmission.setMenu(teacher_menu);
        if (object.equals("curriculum")) {

            transmission.boundaryJudgment(curriculumMapper.selectCurriculumNumberByTeacherAndTime(httpSession.getAttribute("loginUser").toString(),time.schoolYearJudge(),time.termJudge()));
            model.addAttribute("transmission",transmission);
            Collection<Curriculum> curriculums=curriculumMapper.selectCurriculumByTeacherAndTimeAndPage(httpSession.getAttribute("loginUser").toString(),time.schoolYearJudge(),time.termJudge(),(transmission.getPage()-1)*10);
            model.addAttribute("curriculums",curriculums);
            return "curriculum-information";
        }
        else if(object.equals("stu")){

            transmission.boundaryJudgment(studentMapper.selectStuByTeacherIdNumber(httpSession.getAttribute("loginUser").toString()));
            Collection<Student> students=studentMapper.selectTeacherIdFindStu(httpSession.getAttribute("loginUser").toString(),(transmission.getPage()-1)*10);
            model.addAttribute("students",students);
            model.addAttribute("transmission",transmission);
            model.addAttribute("url","/teacher/data/page");
            return "student-information";
        }
        else {

            transmission.boundaryJudgment(multipleChoiceMapper.selectMultipleChoiceNumber());
            Collection<MultipleChoice> multipleChoices=multipleChoiceMapper.selcetMultipleChoiceByPage((transmission.getPage()-1)*10);
            model.addAttribute("multipleChoices",multipleChoices);
            model.addAttribute("transmission",transmission);
            return "teacher-multiple-choice";
        }
    }
    @GetMapping("/c")
    public String c(Model model){
        transmission.setMenu(teacher_menu);
        model.addAttribute("transmission",transmission);
        int[] data1={10,12,53};
        EchartsDataProcessing echartsDataProcessing=new EchartsDataProcessing();
        String data=echartsDataProcessing.getFormatData(data1);
        model.addAttribute("data",data);
        return "problem";
    }
//数据测试
    @ResponseBody
    @GetMapping("/aa")
    public JsonQuestBean getSt(){

        QuestBean questBean=new QuestBean();
        questBean.setId(1);
        questBean.setQ_type(1);
        questBean.setTitle("1");
        questBean.setOptionA("1");
        questBean.setOptionB("1");
        questBean.setOptionC("1");
        questBean.setOptionD("1");
        questBean.setTips("1");
        questBean.setExplain("1");
        questBean.setAnswer("A");
        questBean.setMyanswer("1");

        QuestBean questBean1=new QuestBean();
        questBean1.setId(2);
        questBean1.setQ_type(1);
        questBean1.setTitle("2");
        questBean1.setOptionA("2");
        questBean1.setOptionB("2");
        questBean1.setOptionC("2");
        questBean1.setOptionD("2");
        questBean1.setTips("2");
        questBean1.setExplain("2");
        questBean1.setAnswer("A");
        questBean1.setMyanswer("2");


        JsonQuestBean jsonQuestBean=new JsonQuestBean();
        jsonQuestBean.setStatus("ok");
        jsonQuestBean.setCode("200");
        List list=new ArrayList();
        list.add(questBean);
        list.add(questBean1);
        jsonQuestBean.setMessages(list);


        return jsonQuestBean;
    }
}

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
    String teacherId="2014";
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
    public String getStuByTeacher(Model model){

        transmission.initialization(studentMapper.selectStuByTeacherIdNumber(teacherId));
        transmission.setMenu(teacher_menu);
        model.addAttribute("transmission",transmission);
        Collection<Student> students1=studentMapper.selectTeacherIdFindStu(teacherId,(transmission.getPage()-1)*10);
        model.addAttribute("students",students1);
        model.addAttribute("url","/teacher/data/page");
        return "student-information";
    }

    @GetMapping("/teacher/curriculum")
    public String getClassByTeacher(Model model){

        transmission.initialization(curriculumMapper.selectCurriculumNumberByTeacherAndTime(teacherId,time.schoolYearJudge(),time.termJudge()));
        transmission.setMenu(teacher_menu);
        model.addAttribute("transmission",transmission);
        Collection<Curriculum> curriculums=curriculumMapper.selectCurriculumByTeacherAndTime(teacherId,time.schoolYearJudge(),time.termJudge());
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
    public ScoresThree getChartData(@RequestParam String studentId, @RequestParam String courseId,Model model){
        Collection<Scores> scores=scoresMapper.studenteveryDayAvgScores(studentId,courseId);
        return echartsDataProcessing.getJsonData(scores);
    }

    @GetMapping("/teacher/generate-papers")//难度选择界面
    public String getGeneratePapers(Model model){
        transmission.setMenu(teacher_menu);
        model.addAttribute("transmission",transmission);
        return "generate-papers";
    }

    @PostMapping("/teacher/generate-questions")//题目展示页面
    public String getGenerateQuestions(@ModelAttribute ProblemDifficulty problemDifficulty, Model model, HttpSession httpSession){

        Collection<MultipleChoice> testQuestions=multipleChoiceMapper.selectMultipleChoiceByRand(1,problemDifficulty.getSimple());
        testQuestions.addAll(multipleChoiceMapper.selectMultipleChoiceByRand(2,problemDifficulty.getMedium()));
        testQuestions.addAll(multipleChoiceMapper.selectMultipleChoiceByRand(3,problemDifficulty.getDifficulty()));

        Collection<Curriculum> curriculums=curriculumMapper.selectCurriculumByTeacherAndTime(teacherId,time.schoolYearJudge(),time.termJudge());
        ArrayList<String> specialityClass=echartsDataProcessing.getSpecialityClass(curriculums);
        ArrayList<Integer> questionId=new ArrayList<>();
        Iterator<MultipleChoice> iterator=testQuestions.iterator();
        while (iterator.hasNext()){
            questionId.add(iterator.next().getQuestionId());
        }
        httpSession.setAttribute("questionId",questionId);
        model.addAttribute("questionId",questionId);
        model.addAttribute("specialityClasses",specialityClass);
        model.addAttribute("testQuestions",testQuestions);
        transmission.setMenu(teacher_menu);
        model.addAttribute("transmission",transmission);
        return "problem";
    }
    @PostMapping("/teacher/generate-questions/confirm")
    public String confirmGenerateQuestions(@ModelAttribute String[] specialityClass,@ModelAttribute Integer[] questionId){

        System.out.println(specialityClass);
        System.out.println(questionId);
        return "index-teacher";
    }
    @GetMapping("/teacher/data/page")//数据分页
    public String teacherSelectByPage(@RequestParam String object,@RequestParam("page") Integer page, Model model){

        transmission.setPage(page);
        transmission.setMenu(teacher_menu);
        if (object.equals("curriculum")) {

            transmission.boundaryJudgment(curriculumMapper.selectCurriculumNumberByTeacherAndTime(teacherId,time.schoolYearJudge(),time.termJudge()));
            model.addAttribute("transmission",transmission);
            Collection<Curriculum> curriculums=curriculumMapper.selectCurriculumByTeacherAndTimeAndPage(teacherId,time.schoolYearJudge(),time.termJudge(),(transmission.getPage()-1)*10);
            model.addAttribute("curriculums",curriculums);
            return "curriculum-information";
        }
        else if(object.equals("stu")){

            transmission.boundaryJudgment(studentMapper.selectStuByTeacherIdNumber(teacherId));
            Collection<Student> students=studentMapper.selectTeacherIdFindStu(teacherId,(transmission.getPage()-1)*10);
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
    @ResponseBody
    @GetMapping("/a")
    public String[] geddd(){

        int[] data={10,12,53};
        String[] data1={"一月","二月","三月","四月","五月"};
        Collection<Scores> scores=scoresMapper.studenteveryDayAvgScores("201613052","1111");
        System.out.println(scores.iterator().next().getDateTime());
        EchartsDataProcessing echartsDataProcessing=new EchartsDataProcessing();
        return data1;

    }
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

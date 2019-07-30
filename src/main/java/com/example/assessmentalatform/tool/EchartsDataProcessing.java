package com.example.assessmentalatform.tool;

import com.example.assessmentalatform.bean.Curriculum;
import com.example.assessmentalatform.bean.Scores;
import com.example.assessmentalatform.bean.ScoresThree;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

@Service
public class EchartsDataProcessing {

    public String getFormatData(int[] array){

        StringBuilder data=new StringBuilder();
        data.append("[");
        for (int i=0;i<array.length;i++){
            data.append(array[i]);
            data.append(",");
        }
        data.delete(data.length()-1,data.length());
        data.append("]");
        return data.toString();
    }

    public String getFormatData(String[] array){

        StringBuilder data=new StringBuilder();
        data.append("[");
        for (int i=0;i<array.length;i++){
            data.append("\"");
            data.append(array[i]);
            data.append("\"");
            data.append(",");
        }
        data.delete(data.length()-1,data.length());
        data.append("]");
        return data.toString();
    }

    public ScoresThree getJsonData(Collection<Scores> scores){//将collection对象转成ScoresThree对象

        ScoresThree scoresThree=new ScoresThree();
        Iterator<Scores> iterator=scores.iterator();
        String[] time=new String[scores.size()];
        Integer[] score=new Integer[scores.size()];
        for (int i=0;i<scores.size();i++){
            Scores scores1=iterator.next();
            time[i]=scores1.getDateTime();
            score[i]=scores1.getScores();
        }
        scoresThree.setDateTime(time);
        scoresThree.setScores(score);

        return scoresThree;
    }

    public ArrayList<String> getSpecialityClass(Collection<Curriculum> curriculums){

        Iterator<Curriculum> curriculumIterator=curriculums.iterator();
        ArrayList<String> specialityClass=new ArrayList<>();
        Curriculum curriculum;
        HashSet<String> hashSet=new HashSet<>();
        while (curriculumIterator.hasNext()){
            curriculum=curriculumIterator.next();
            hashSet.add(curriculum.getSpeciality()+curriculum.getClasses());
        }

        specialityClass.addAll(hashSet);
        return specialityClass;
    }
}

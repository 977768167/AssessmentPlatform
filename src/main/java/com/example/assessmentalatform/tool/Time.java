package com.example.assessmentalatform.tool;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.Calendar;
@Service
public class Time {

    Calendar c=Calendar.getInstance();
    @Bean
    public Time getTime(){
        return new Time();
    }

    public String schoolYearJudge(){//年份返回


        return String.valueOf(c.get(Calendar.YEAR));
    }

    public String termJudge(){//学期返回

        if (c.get(Calendar.MONTH)+1>=3&&c.get(Calendar.MONTH)+1<9){
            return "下";
        }
        else{
            return "上";
        }

    }
}

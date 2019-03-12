package com.example.assessmentalatform;

import com.example.assessmentalatform.tool.Time;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AssessmentPlatformApplicationTests {


    @Autowired
    Time time;
    @Test
    public void contextLoads() {

        System.out.println(time.schoolYearJudge());
        System.out.println();
        System.out.println(time.termJudge());
    }

}

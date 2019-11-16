package com.example.assessmentalatform.tool;

import com.example.assessmentalatform.bean.User;
import com.example.assessmentalatform.mapper.AdministratorMapper;
import com.example.assessmentalatform.mapper.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
@Service
public class LoginCheck {

    @Autowired
    AdministratorMapper adminMapper;
    @Autowired
    TeacherMapper teacherMapper;
    @Bean
    public LoginCheck getLoginCheck(){
        return new LoginCheck();
    }

    public boolean loginCheck(HttpServletRequest request, User user,Integer status){

        if (CodeUtil.checkVerifyCode(request)) {
            if (status==1) {
                if (user.getPassword().equals(adminMapper.getAdminByAdmin(user.getUserName()).getPassword())) {
                    return true;
                }
            }
            else if (status==2){
                if (user.getPassword().equals(teacherMapper.selectTeacherByTeacherId(user.getUserName()).getPassword())){
                    return true;
                }
            }
        }
            return false;
    }
}

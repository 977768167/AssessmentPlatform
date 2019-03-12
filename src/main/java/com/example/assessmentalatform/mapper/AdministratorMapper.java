package com.example.assessmentalatform.mapper;

import com.example.assessmentalatform.bean.Administrator;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdministratorMapper {

    public Administrator getAdminByAdmin(String admin);

    public int insertAdmin(Administrator administrator);

}

package com.example.assessmentalatform.tool;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
@Service
public class FileWrite {
    public void fileWritePath(MultipartFile file, String path){

        try {
            byte[] bytes = file.getBytes();
            Files.write(Paths.get(path),bytes);
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    @Bean
    public FileWrite getFileWrite(){
        return new FileWrite();
    }
}

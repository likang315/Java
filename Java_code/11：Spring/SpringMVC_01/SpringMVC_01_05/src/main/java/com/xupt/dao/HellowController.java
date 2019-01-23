package com.xupt.dao;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * Author: likang
 * Email: 13571887352@163.com
 * GitHub：https://github.com/likang315
 * CreateTime： 2019/1/21 10:00
 */

/**
 * MultipartFile,上传文件
 */
@Controller
@RequestMapping("/admin")
public class HellowController {

    @RequestMapping(value = "/hi", method = RequestMethod.POST)
    public String handleFormUpload(@RequestParam("name") String name, @RequestParam("pic") MultipartFile file) {

        if (!file.isEmpty())
        {
            try {
                byte[] bytes = file.getBytes();
                file.transferTo(new File("D:\\MMMM.JP"));

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return "hello";
        } else {
            return "redirect:http://www.baidu.com";
        }
    }


}

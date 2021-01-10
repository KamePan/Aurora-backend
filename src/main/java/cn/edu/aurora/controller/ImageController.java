package cn.edu.aurora.controller;

import cn.edu.aurora.entity.Feature;
import cn.edu.aurora.entity.Image;
import cn.edu.aurora.entity.Meta;
import cn.edu.aurora.entity.Thumb;
import cn.edu.aurora.service.AuroraService;
import cn.edu.aurora.service.ImageService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private AuroraService auroraService;

    @GetMapping
    public JSONArray findAllImages() throws IOException {
        String picDest = "D:\\Pan\\IDEAWorkspace\\AuroraManagement\\src\\main\\resources\\thumb\\";
        //List<Image> images = imageService.findAllImages();
        /*List<Image> images = imageService.findAllImages();
        for (Image image : images) {
            byte[] pic = image.getRawpic().getData();
            File file = new File(picDest + image.getName() + ".jpg");
            FileOutputStream fileOuputStream = new FileOutputStream(file);
            fileOuputStream.write(pic);
        }*/

        List<Thumb> thumbs = imageService.findAllThumbs();
        for (Thumb thumb : thumbs) {
            byte[] pic = thumb.getThumb().getData();
            BASE64Encoder encoder = new BASE64Encoder();
            String encodedPic = encoder.encode(pic);
            System.out.println(encodedPic);
            //File file = new File(picDest + thumb.getName() + ".jpg");
            //FileOutputStream fileOuputStream = new FileOutputStream(file);
            //fileOuputStream.write(pic);
        }

        return null;//JSONArray.parseArray(JSON.toJSONString(images));
    }





}

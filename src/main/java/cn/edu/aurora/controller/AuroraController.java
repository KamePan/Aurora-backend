package cn.edu.aurora.controller;

import cn.edu.aurora.entity.Feature;
import cn.edu.aurora.entity.Meta;
import cn.edu.aurora.service.AuroraService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/aurora")
public class AuroraController {

    @Autowired
    private AuroraService auroraService;

    @GetMapping("/meta")
    public JSONArray findAuroraWithOptions(@RequestParam(required = false) String start,
                                           @RequestParam(required = false) String end,
                                           @RequestParam(required = false) String band,
                                           @RequestParam(required = false) String type) {
        // 需要返回的数据包括 meta、原图、缩略图、Keogram 生成的图
        List<Meta> meta = null;
        try {
            meta = auroraService.findAuroraWithOption(start, end, band, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSONArray.parseArray(JSON.toJSONString(meta));
    }

    @GetMapping("/feature")
    public JSONArray findAuroraLikeSomePic(@RequestParam MultipartFile pic) {
        List<Feature> features = null;
        try {
            features = auroraService.findAuroraLikeSomePic(pic.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSONArray.parseArray(JSON.toJSONString(features));
    }
}

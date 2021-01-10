package cn.edu.aurora.controller;

import cn.edu.aurora.entity.Feature;
import cn.edu.aurora.entity.Meta;
import cn.edu.aurora.service.AuroraService;
import cn.edu.aurora.vo.AuroraVO;
import cn.edu.aurora.vo.FeatureVO;
import cn.edu.aurora.vo.KeogramVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Api(tags = "极光信息控制器")
@RestController
@RequestMapping("/aurora")
public class AuroraController {

    @Autowired
    private AuroraService auroraService;

    @ApiOperation("通过参数返回相应的极光数据")
    @GetMapping("/meta")
    public JSONArray findAuroraWithOptions(@RequestParam(required = false) String start,
                                           @RequestParam(required = false) String end,
                                           @RequestParam(required = false) String band,
                                           @RequestParam(required = false) String type) {
        System.out.println("成功");
        // 需要返回的数据包括 meta、原图、缩略图、Keogram 生成的图
        List<AuroraVO> auroraList = null;
        try {
            auroraList = auroraService.findAuroraWithOption(start, end, band, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSONArray.parseArray(JSON.toJSONString(auroraList));
    }

    @ApiOperation("通过参数返回相应的极光数据图 Keogram")
    @GetMapping("/keogram")
    public JSONObject findKeogramWithOptions(@RequestParam(required = false) String start,
                                             @RequestParam(required = false) String end,
                                             @RequestParam(required = false) String band,
                                             @RequestParam(required = false) String type) {
        // 返回 Keogram 生成的图
        KeogramVO keogramVO = null;
        try {
            String keogram = auroraService.findKeogramWithOptions(start, end, band, type);
            keogramVO = new KeogramVO().setKeogram(keogram);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (JSONObject) JSON.toJSON(keogramVO);
    }

    @ApiOperation("通过图片字节流查询相似度高的极光数据")
    @GetMapping("/feature")
    public JSONArray findAuroraLikeSomePic(@RequestParam MultipartFile pic) {
        List<FeatureVO> auroraList = null;
        try {
            auroraList = auroraService.findAuroraLikeSomePic(pic.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSONArray.parseArray(JSON.toJSONString(auroraList));
    }

    @ApiOperation("插入极光图片")
    @PostMapping
    public JSONObject insertAuroraLikeSomePic(@RequestParam MultipartFile pic) {
        return null;
    }
}

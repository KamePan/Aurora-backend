package cn.edu.aurora.controller;

import cn.edu.aurora.dao.MetaDao;
import cn.edu.aurora.entity.Feature;
import cn.edu.aurora.entity.Image;
import cn.edu.aurora.entity.Meta;
import cn.edu.aurora.entity.Thumb;
import cn.edu.aurora.service.AuroraService;
import cn.edu.aurora.util.ImageUtil;
import cn.edu.aurora.vo.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.bson.types.Binary;
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


    @ApiOperation("通过参数返回相应的极光缩略图")
    @GetMapping("/thumb")
    public JSONArray findThumbListWithOptions(@RequestParam(required = false) String start,
                                             @RequestParam(required = false) String end,
                                             @RequestParam(required = false) String band,
                                             @RequestParam(required = false) String type) {
        // 需要返回的数据包括 meta、原图、缩略图、Keogram 生成的图
        List<ThumbVO> thumbVOList = null;
        try {
            thumbVOList = auroraService.findThumbWithOption(start, end, band, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSONArray.parseArray(JSON.toJSONString(thumbVOList));
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

    @ApiOperation("通过参数返回相应的极光数据图")
    @GetMapping("/image")
    public JSONArray findImageWithOptions(@RequestParam(required = false) String start,
                                          @RequestParam(required = false) String end,
                                          @RequestParam(required = false) String band) {
        // 需要返回的数据包括 meta、原图、缩略图、Keogram 生成的图
        List<ImageVO> imageVOList = null;
        try {
            imageVOList = auroraService.findImageWithOption(start, end, band);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSONArray.parseArray(JSON.toJSONString(imageVOList));
    }

    @ApiOperation("通过图片字节流查询相似度高的极光数据")
    @PostMapping("/feature")
    public JSONArray findAuroraLikeSomePic(@RequestParam MultipartFile pic) {
        List<FeatureVO> featureVOList = null;
        try {
            featureVOList = auroraService.findAuroraLikeSomePic(pic.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSONArray.parseArray(JSON.toJSONString(featureVOList));
    }


    @ApiOperation("根据 name 查询图片")
    @GetMapping("/image/name/{name}")
    public JSONObject findAuroraImageByName(@PathVariable String name) {
        ImageVO imageVO = auroraService.findImageByName(name);
        return (JSONObject) JSON.toJSON(imageVO);
    }

    @ApiOperation("插入极光图片")
    @PostMapping
    public JSONObject insertAurora(@RequestParam MultipartFile pic) {
        try {
            String originalFilename = pic.getOriginalFilename();
            byte[] bytes = pic.getBytes();
            String name = originalFilename.substring(0, originalFilename.lastIndexOf('.'));
            // 得到图片的名字，如果名字不符合规范，则抛出异常，否则解析改名字
            auroraService.insertImage(bytes, name);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @ApiOperation("标记极光图片类型")
    @PutMapping
    public JSONObject updateMetaByName(@RequestBody Meta meta) {
        auroraService.updateMetaByName(meta);
        return null;
    }
}

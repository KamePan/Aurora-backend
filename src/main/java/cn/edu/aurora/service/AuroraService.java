package cn.edu.aurora.service;

import cn.edu.aurora.dao.FeatureDao;
import cn.edu.aurora.dao.ImageDao;
import cn.edu.aurora.dao.MetaDao;
import cn.edu.aurora.dao.ThumbDao;
import cn.edu.aurora.entity.Feature;
import cn.edu.aurora.entity.Meta;
import cn.edu.aurora.util.ImageUtil;
import cn.edu.aurora.util.KeogramUtil;
import cn.edu.aurora.vo.AuroraVO;
import cn.edu.aurora.vo.FeatureVO;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
public class AuroraService {

    @Autowired
    private FeatureDao featureDao;

    @Autowired
    private MetaDao metaDao;

    @Autowired
    private ThumbDao thumbDao;

    @Autowired
    private ImageDao imageDao;

    public List<AuroraVO> findAuroraWithOption(String start, String end, String band, String type) throws IOException {
        List<AuroraVO> auroraList = new ArrayList<>();
        List<Meta> metaList = metaDao.findMetaWithOption(start, end, band, type);
        System.out.println(metaList);
        // 根据查询结果配置返回列表
        for (Meta meta: metaList) {
            AuroraVO aurora = new AuroraVO();
            BASE64Encoder encoder = new BASE64Encoder();
            byte[] thumbBytes = thumbDao.findThumbByName(meta.getName()).getThumb().getData();
            byte[] imageBytes = imageDao.findImageByName(meta.getName()).getRawpic().getData();
            aurora.setName(meta.getName())
                    .setBand(meta.getBand())
                    .setTime(meta.getTime())
                    .setManualtype(meta.getManualtype())
                    .setThumb(encoder.encode(thumbBytes))
                    .setRawpic(encoder.encode(imageBytes));
            auroraList.add(aurora);
        }
        System.out.println(auroraList);
        return auroraList;
    }

    /**
     * 根据海明距离得到排序以后的 feature 列表
     * @param pic 图片字节流参数
     * @return
     * @throws IOException
     */
    public List<FeatureVO> findAuroraLikeSomePic(byte[] pic) throws IOException {

        String srcfeat = ImageUtil.produceFingerPrint(pic);
        List<Feature> features = featureDao.findAllFeatures();
        // 根据相关值进行排序
        features.sort(Comparator.comparingInt((Feature o) -> ImageUtil.hammingDistance(srcfeat, o.getFeature())));
        int size = features.size();
        for (int i = size - 1; i >= 0; i--) {
            if (ImageUtil.hammingDistance(srcfeat, features.get(i).getFeature()) >= 10) { //大于 10 说明完全不匹配
                features.remove(features.get(i));
            }
        }

        // 配置返回列表
        List<FeatureVO> auroraList = new ArrayList<>();
        for (Feature feature : features) {
            FeatureVO aurora = new FeatureVO();
            BASE64Encoder encoder = new BASE64Encoder();
            byte[] thumbBytes = thumbDao.findThumbByName(feature.getName()).getThumb().getData();
            byte[] imageBytes = imageDao.findImageByName(feature.getName()).getRawpic().getData();
            Meta meta = metaDao.findMetaByName(feature.getName());
            aurora.setName(meta.getName())
                    .setBand(meta.getBand())
                    .setTime(meta.getTime())
                    .setManualtype(meta.getManualtype())
                    .setThumb(encoder.encode(thumbBytes))
                    .setRawpic(encoder.encode(imageBytes))
                    .setSimilarity(ImageUtil.hammingDistance(srcfeat, feature.getFeature()));
            auroraList.add(aurora);
        }
        return auroraList;
    }

    public String findKeogramWithOptions(String start, String end, String band, String type) throws IOException {
        List<Meta> metas = metaDao.findMetaWithOption(start, end, band, type);

        //生成 keogram 图像代码
        List<String> names = new ArrayList<>();
        for (Meta meta : metas) {
            names.add(meta.getName());
        }
        // 通过 name 属性查询数据库得到 keogramList
        List<DBObject> keogramlist = KeogramUtil.getKeogramList(names);
        // 通过 Keogram 对象列表得到通过 keogram 中的条列生成的图像，每个 keogram 对象都有 440 个像素点组成的 keogram 列表属性
        BufferedImage keogramList = KeogramUtil.getKeogram(keogramlist);
        // 使用 BufferedImage 类型的数据流生成图像
        ByteArrayOutputStream keo=new ByteArrayOutputStream();
        ImageIO.write(keogramList, "jpg",keo);
        byte[] keogrambyte = keo.toByteArray();
        //System.out.println(Arrays.toString(keogrambyte));

        return new BASE64Encoder().encode(keogrambyte);
    }

}

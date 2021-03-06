package cn.edu.aurora.service;

import cn.edu.aurora.dao.FeatureDao;
import cn.edu.aurora.dao.ImageDao;
import cn.edu.aurora.dao.MetaDao;
import cn.edu.aurora.dao.ThumbDao;
import cn.edu.aurora.entity.Feature;
import cn.edu.aurora.entity.Image;
import cn.edu.aurora.entity.Meta;
import cn.edu.aurora.entity.Thumb;
import cn.edu.aurora.util.ImageUtil;
import cn.edu.aurora.util.KeogramUtil;
import cn.edu.aurora.vo.AuroraVO;
import cn.edu.aurora.vo.FeatureVO;
import cn.edu.aurora.vo.ImageVO;
import cn.edu.aurora.vo.ThumbVO;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

@CacheConfig(cacheNames = "Aurora")
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
        for (Feature feature : features) {
            System.out.println(ImageUtil.hammingDistance(srcfeat, feature.getFeature()));
        }
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
            Meta meta = metaDao.findMetaByName(feature.getName());
            aurora.setName(meta.getName())
                    .setBand(meta.getBand())
                    .setTime(meta.getTime())
                    .setManualtype(meta.getManualtype())
                    .setThumb(encoder.encode(thumbBytes))
                    .setSimilarity(ImageUtil.hammingDistance(srcfeat, feature.getFeature()));
            System.out.println(ImageUtil.hammingDistance(srcfeat, feature.getFeature()));
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
        ByteArrayOutputStream keo = new ByteArrayOutputStream();
        ImageIO.write(keogramList, "jpg",keo);
        byte[] keogrambyte = keo.toByteArray();

        return new BASE64Encoder().encode(keogrambyte);
    }

    @Cacheable(key = "targetClass + #name")
    public ImageVO findImageByName(String name) {
        Image image = imageDao.findImageByName(name);
        ImageVO imageVO = new ImageVO();
        imageVO.setImage(new BASE64Encoder().encode(image.getRawpic().getData()));
        return imageVO;
    }

    public List<ThumbVO> findThumbWithOption(String start, String end, String band, String type) throws IOException {
        List<ThumbVO> thumbVOList = new ArrayList<>();
        List<Meta> metaList = metaDao.findMetaWithOption(start, end, band, type);
        // 根据查询结果配置返回列表
        for (Meta meta: metaList) {
            ThumbVO thumbVO = new ThumbVO();
            BASE64Encoder encoder = new BASE64Encoder();
            byte[] thumbBytes = thumbDao.findThumbByName(meta.getName()).getThumb().getData();
            thumbVO.setName(meta.getName())
                    .setBand(meta.getBand())
                    .setTime(meta.getTime())
                    .setManualtype(meta.getManualtype())
                    .setThumb(encoder.encode(thumbBytes));
            thumbVOList.add(thumbVO);
        }
        return thumbVOList;
    }

    public List<ImageVO> findImageWithOption(String start, String end, String band) throws IOException {
        List<ImageVO> imageVOList = new ArrayList<>();
        List<Meta> metaList = metaDao.findMetaNeedsMarkedWithOption(start, end, band);
        // 根据查询结果配置返回列表
        for (Meta meta: metaList) {
            ImageVO imageVO = new ImageVO();
            BASE64Encoder encoder = new BASE64Encoder();
            byte[] imageBytes = imageDao.findImageByName(meta.getName()).getRawpic().getData();
            imageVO.setName(meta.getName())
                    .setBand(meta.getBand())
                    .setTime(meta.getTime())
                    .setManualtype(meta.getManualtype())
                    .setImage(encoder.encode(imageBytes));
            imageVOList.add(imageVO);
        }
        return imageVOList;
    }

    @CacheEvict(key = "targetClass + #p0.name")
    public void updateMetaByName(Meta meta) {
        metaDao.updateMetaByName(meta);
    }

    @CacheEvict(key = "targetClass + #name")
    public void insertImage(byte[] bytes, String name) throws IOException {
        ImageUtil.handleImage(bytes, name);
    }
}

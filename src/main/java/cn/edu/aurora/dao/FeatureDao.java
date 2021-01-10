package cn.edu.aurora.dao;

import cn.edu.aurora.entity.*;
import cn.edu.aurora.util.ImageUtil;
import cn.edu.aurora.util.KeogramUtil;
import com.mongodb.*;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Repository
public class FeatureDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     *
     * @param pic 图片字节流参数
     * @return 根据海明距离排序以后的 feature 列表
     * @throws IOException
     */
    public List<Feature> findFeatureLikeSomePic(byte[] pic) throws IOException {
        String srcfeat = ImageUtil.produceFingerPrint(pic);
        List<Feature> features = findAllFeatures();
        // 根据相关值进行排序
        features.sort(Comparator.comparingInt((Feature o) -> ImageUtil.hammingDistance(srcfeat, o.getFeature())));
        int size = features.size();
        for (int i = size - 1; i >= 0; i--) {
            if (ImageUtil.hammingDistance(srcfeat, features.get(i).getFeature()) >= 10) { //大于 10 说明完全不匹配
                features.remove(features.get(i));
            }
        }
        for (Feature feature : features) {
            System.out.println(ImageUtil.hammingDistance(srcfeat, feature.getFeature()));
        }
        return features;
    }


    private List<Feature> findAllFeatures() {
        List<Feature> features = mongoTemplate.findAll(Feature.class);
        return features;
    }

}

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

    public List<Feature> findAllFeatures() {
        List<Feature> features = mongoTemplate.findAll(Feature.class);
        return features;
    }

}

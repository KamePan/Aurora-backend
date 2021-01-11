package cn.edu.aurora.dao;

import cn.edu.aurora.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
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

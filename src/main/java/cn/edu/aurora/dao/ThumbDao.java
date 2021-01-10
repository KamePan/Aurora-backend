package cn.edu.aurora.dao;

import cn.edu.aurora.entity.Image;
import cn.edu.aurora.entity.Thumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;


@Repository
public class ThumbDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Thumb findThumbByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        Thumb thumb = mongoTemplate.findOne(query, Thumb.class);
        return thumb;
    }

}

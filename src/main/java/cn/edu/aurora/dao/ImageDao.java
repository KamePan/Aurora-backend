package cn.edu.aurora.dao;


import cn.edu.aurora.entity.Image;
import cn.edu.aurora.entity.Thumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ImageDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Image> findAllImages() {
        List<Image> images = mongoTemplate.findAll(Image.class);
        return images;
    }

    public List<Thumb> findAllThumbs() {
        List<Thumb> thumbs = mongoTemplate.findAll(Thumb.class);
        return thumbs;
    }
}

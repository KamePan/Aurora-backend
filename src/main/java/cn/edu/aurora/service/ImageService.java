package cn.edu.aurora.service;

import cn.edu.aurora.dao.ImageDao;
import cn.edu.aurora.dao.ThumbDao;
import cn.edu.aurora.entity.Image;
import cn.edu.aurora.entity.Thumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {

    @Autowired
    private ImageDao imageDao;

    public List<Image> findAllImages() {
        return imageDao.findAllImages();
    }
}

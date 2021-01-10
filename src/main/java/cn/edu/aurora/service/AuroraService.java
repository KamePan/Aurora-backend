package cn.edu.aurora.service;

import cn.edu.aurora.dao.FeatureDao;
import cn.edu.aurora.dao.MetaDao;
import cn.edu.aurora.dao.ThumbDao;
import cn.edu.aurora.entity.Feature;
import cn.edu.aurora.entity.Meta;
import cn.edu.aurora.vo.AuroraVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AuroraService {

    @Autowired
    private FeatureDao featureDao;

    @Autowired
    private MetaDao metaDao;

    @Autowired
    private ThumbDao thumbDao;

    public List<Meta> findAuroraWithOption(String start, String end, String band, String type) throws IOException {
        List<Meta> metaList = metaDao.findMetaWithOption(start, end, band, type);
        for (Meta meta: metaList) {
            ;
        }
        return metaList;
    }

    public List<Feature> findAuroraLikeSomePic(byte[] pic) throws IOException {
        List<Feature> featureList = featureDao.findFeatureLikeSomePic(pic);
        return featureList;
    }
}

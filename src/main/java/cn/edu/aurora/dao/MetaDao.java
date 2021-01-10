package cn.edu.aurora.dao;

import cn.edu.aurora.entity.Meta;
import cn.edu.aurora.util.KeogramUtil;
import com.mongodb.DBObject;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public class MetaDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 根据极光类型查询 Aurora
     * @param start 开始时间
     * @param end 结束时间
     * @param band 极光波段
     * @param type 极光类型
     * @return
     * @throws IOException
     */
    public List<Meta> findMetaWithOption(String start, String end, String band, String type) throws IOException {
        Query query = new Query();
        if (start != null) query.addCriteria(Criteria.where("time").gte(start));
        if (end != null) query.addCriteria(Criteria.where("time").lte(end));
        if (band != null) query.addCriteria(Criteria.where("band").is(band));
        if (type != null) query.addCriteria(Criteria.where("type").is(type));

        List<Meta> metas = mongoTemplate.find(query, Meta.class);

        return metas;
    }

    public Meta findMetaByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.findOne(query, Meta.class);
    }

}

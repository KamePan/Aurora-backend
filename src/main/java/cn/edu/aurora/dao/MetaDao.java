package cn.edu.aurora.dao;

import cn.edu.aurora.entity.Meta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
        Criteria criteria = new Criteria();
        if (start != null && end != null) {
            criteria.andOperator(criteria.where("time").gte(start),
                    criteria.where("time").lte(end));
        } else if (start != null) {
            criteria.and("time").gte(start);
        } else if (end != null) {
            criteria.and("time").lte(end);
        }
        query.addCriteria(criteria);

        if (band != null) query.addCriteria(Criteria.where("band").is(band));
        if (type != null) query.addCriteria(Criteria.where("type").is(type));

        List<Meta> metas = mongoTemplate.find(query, Meta.class);

        return metas;
    }

    public List<Meta> findMetaNeedsMarkedWithOption(String start, String end, String band) throws IOException {
        Query query = new Query();
        Criteria criteria = new Criteria();
        if (start != null && end != null) {
            criteria.andOperator(criteria.where("time").gte(start),
                    criteria.where("time").lte(end));
        } else if (start != null) {
            criteria.and("time").gte(start);
        } else if (end != null) {
            criteria.and("time").lte(end);
        }
        query.addCriteria(criteria);
        if (band != null) query.addCriteria(Criteria.where("band").is(band));
        query.addCriteria(Criteria.where("type").is("0"));

        List<Meta> metas = mongoTemplate.find(query, Meta.class);

        return metas;
    }

    public Meta findMetaByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        return mongoTemplate.findOne(query, Meta.class);
    }

    public void updateMetaByName(Meta meta) {
        Query query = new Query(Criteria.where("name").is(meta.getName()));
        Update update = new Update().set("manualtype", meta.getManualtype());
        System.out.println(meta);
        mongoTemplate.upsert(query, update, "Aurora.Meta");
        System.out.println(mongoTemplate.find(query, Meta.class));
    }
}

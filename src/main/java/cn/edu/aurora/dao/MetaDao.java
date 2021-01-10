package cn.edu.aurora.dao;

import cn.edu.aurora.entity.Meta;
import cn.edu.aurora.util.KeogramUtil;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
        Criteria criteria = new Criteria();
        if (start != null) criteria.where("time").gte(start);
        if (end != null) criteria.where("time").lte(end);
        if (band != null) criteria.where("band").is(band);
        if (band != null) criteria.where("type").is(type);

        Query query = new Query();
        List<Meta> metas = mongoTemplate.find(query, Meta.class);

        //---------------------------以下属于生成 keogram 图像代码-----------------------------------
        System.out.println(metas);
        List<String> names = new ArrayList<>();
        for (Meta meta : metas) {
            names.add(meta.getName());
        }
        System.out.println(names);
        // 通过 name 属性查询数据库得到 imageList
        //List<Image> imagelist = findImagesWithNames(names);
        // 通过 name 属性查询数据库得到 keogramList
        List<DBObject> keogramlist = KeogramUtil.getKeogramList(names);
        // 通过 Image 对象列表得到图像的字节码数组
        //byte[][] image = queryPic.getImage(imagelist);
        // 通过 Keogram 对象列表得到通过 keogram 中的条列生成的图像，每个 keogram 对象都有 440 个像素点组成的 keogram 列表属性
        BufferedImage keogramList = KeogramUtil.getKeogram(keogramlist);
        // 使用 BufferedImage 类型的数据流生成图像
        ByteArrayOutputStream keo=new ByteArrayOutputStream();
        ImageIO.write(keogramList, "jpg",keo);
        byte[] keogrambyte = keo.toByteArray();
        System.out.println(Arrays.toString(keogrambyte));

        String path = "D:\\Pan\\IDEAWorkspace\\AuroraManagement\\src\\main\\resources\\keogram\\";
        //File keoPic = new File(path);
        FileOutputStream fileOuputStream = new FileOutputStream(path + "b.jpg");
        fileOuputStream.write(keogrambyte);
        //---------------------------以上属于生成 keogram 图像代码-----------------------------------

        return metas;
    }

}

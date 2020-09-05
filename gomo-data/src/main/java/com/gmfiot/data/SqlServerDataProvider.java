package com.gmfiot.data;

import com.gmfiot.core.model.BaseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ThinkPad
 */
@Component
public class SqlServerDataProvider implements DataProvider {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    /**
     * test only
     * @param id
     * @param <E>
     * @return
     */
    @Override
    public <E extends BaseModel> E select(long id) {
        var sql = "insert into tb_article(title,summary,user_id,create_time,public_time,update_time,status) " +
                "values(:title,:summary,:userId,:createTime,:publicTime,:updateTime,:status)";
        Map<String, Object> param = new HashMap<>(6);
        param.put("title","the article title");
        param.put("summary", "article.getSummary()");
        param.put("userId", "article.getUserId()");
        param.put("status", "article.getStatus()");
        param.put("createTime", "article.getCreateTime()");
        param.put("publicTime", "article.getPublicTime()");
        param.put("updateTime", "article.getUpdateTime()");
        int effectRow = namedParameterJdbcTemplate.update(sql,param);
        return null;
    }

    @Override
    public Long getId(){
        var sql = "SELECT NEXT VALUE FOR Ids";
        Map map = null;
        return namedParameterJdbcTemplate.queryForObject(sql,map,Long.class);
    }
}

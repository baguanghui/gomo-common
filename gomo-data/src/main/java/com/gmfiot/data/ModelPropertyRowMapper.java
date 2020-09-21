package com.gmfiot.data;

import com.gmfiot.core.util.ReflectionUtil;
import com.gmfiot.data.sql.ColumnInfo;
import com.gmfiot.data.sql.SqlMappingData;
import com.gmfiot.data.sql.TableInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.lang.Nullable;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author BaGuangHui
 */
public class ModelPropertyRowMapper<T> implements RowMapper<T> {

    protected final Log logger = LogFactory.getLog(this.getClass());

    private Class<T> mappedClass;

    public ModelPropertyRowMapper() {
    }

    public ModelPropertyRowMapper(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
    }

//    protected String underscoreName(String name) {
//        if (!StringUtils.hasLength(name)) {
//            return "";
//        } else {
//            StringBuilder result = new StringBuilder();
//            result.append(this.lowerCaseName(name.substring(0, 1)));
//
//            for(int i = 1; i < name.length(); ++i) {
//                String s = name.substring(i, i + 1);
//                String slc = this.lowerCaseName(s);
//                if (!s.equals(slc)) {
//                    result.append("_").append(slc);
//                } else {
//                    result.append(s);
//                }
//            }
//            return result.toString();
//        }
//    }

    @Override
    public T mapRow(ResultSet rs, int i) throws SQLException {
        //Assert.state(this.mappedClass != null, "Mapped class was not specified");
        var rsMetaData = rs.getMetaData();
        var columnCount = rsMetaData.getColumnCount();
        TableInfo tableInfo = SqlMappingData.getTableInfo(mappedClass);
        T mappedObject = ReflectionUtil.newInstance(mappedClass);
        var columnInfoList = tableInfo.getFieldColumnMap().values();
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            var columnName = rsMetaData.getColumnName(columnIndex);
            var columnInfo = columnInfoList.stream().filter(p -> p.getName().equalsIgnoreCase(columnName)).findFirst().orElse(null);
            if(columnInfo == null){
                continue;
            }
            var columnValue = SqlMappingData.getColumnValue(rs,columnIndex,columnInfo);
            if(columnValue == null){
                continue;
            }
            columnInfo.setValue(mappedObject,columnValue);
        }
        return mappedObject;
    }
}

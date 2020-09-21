package com.gmfiot.data;

import com.gmfiot.core.util.ReflectionUtil;
import com.gmfiot.data.sql.SqlMappingData;
import com.gmfiot.data.sql.TableInfo;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author BaGuangHui
 */
public class ModelResultSetExtractor<T> implements ResultSetExtractor<List<T>> {

    private Class<T> mappedClass;

    public ModelResultSetExtractor(Class<T> mappedClass){
        this.mappedClass = mappedClass;
    }

    @Override
    public List<T> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<T> modelList = null;

        try {
            modelList = new ArrayList<>();
            var rsMetaData = resultSet.getMetaData();
            var columnCount = rsMetaData.getColumnCount();
            TableInfo tableInfo = SqlMappingData.getTableInfo(mappedClass);
            var columnInfoList = tableInfo.getFieldColumnMap().values();

            while (resultSet.next()){
                T mappedObject = ReflectionUtil.newInstance(mappedClass);
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    var columnName = rsMetaData.getColumnName(columnIndex);
                    var columnInfo = columnInfoList.stream().filter(p -> p.getName().equalsIgnoreCase(columnName)).findFirst().orElse(null);
                    if(columnInfo == null){
                        continue;
                    }
                    var columnValue = SqlMappingData.getColumnValue(resultSet, columnIndex,columnInfo);
                    if(columnValue == null){
                        continue;
                    }
                    columnInfo.setValue(mappedObject,columnValue);
                }
                modelList.add(mappedObject);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return  modelList;
    }
}

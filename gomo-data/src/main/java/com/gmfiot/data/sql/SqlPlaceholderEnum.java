package com.gmfiot.data.sql;

/**
 * @author BaGuangHui
 */
public enum SqlPlaceholderEnum {
    QUESTION_MARK("?","PreparedStatement,jdbcTemplate sql占位符"),
    COLON(":","NamedParameterJdbcTemplate,占位符"),
    HASH_SIGN("#","MyBatis sql占位符"),
    RAW("","无sql占位符");

    private String sign;
    private String description;

    SqlPlaceholderEnum(String sign, String description){
        this.sign = sign;
        this.description = description;
    }

    public String getName() {
        return sign;
    }

    public String getDescription() {
        return description;
    }

}

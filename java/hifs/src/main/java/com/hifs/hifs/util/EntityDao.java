package com.hifs.hifs.util;

import org.apache.commons.lang3.StringUtils;

public class EntityDao {

    public String getSearchSql(String qarams, String tableName, String join , String condition, String pageNumber, String pageSize, String grd, String ord) {
        String sql = "Select "+qarams+" from "+tableName;

        if(StringUtils.isNotBlank(join)){
            sql+= join;
        }

        if(StringUtils.isNotBlank(condition)){
            sql+= " where "+condition;
        }
        if(StringUtils.isNotBlank(pageNumber)&&StringUtils.isNotBlank(pageSize)){
            int pn = Integer.parseInt(pageNumber);
            int ps = Integer.parseInt(pageSize);
            sql+= " limit "+((pn-1)*ps)+","+ps;
        }
        if(StringUtils.isNotBlank(grd)){
            sql+=" group by "+grd;
        }
        if(StringUtils.isNotBlank(ord)){
            sql+=" order by "+ord;
        }
        return sql;
    }

    public String getSearchTotalSql(String qarams, String tableName, String join, String condition, String grd) {
        String sql = "Select COUNT( "+qarams+" ) as total from "+tableName;

        if(StringUtils.isNotBlank(join)){
            sql+= join;
        }

        if(StringUtils.isNotBlank(condition)){
            sql+= " where "+condition;
        }
        if(StringUtils.isNotBlank(grd)){
            sql+=" group by "+grd;
        }
        return sql;
    }
}

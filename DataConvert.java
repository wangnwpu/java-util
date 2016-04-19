package com.cwang.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class DataConvert {
	
	/**
	 * 将字节数组转换为16进制字符串
	 * @param src	字节数组
	 * @return
	 */
	public static String bytesToHexString(Vector<Byte> src){   
	    StringBuilder stringBuilder = new StringBuilder("");   
	    Iterator<Byte> it = src.iterator();
	    if (src == null || src.size() <= 0) {   
	        return null;   
	    }   
	    while(it.hasNext()){
	    	int v = it.next() & 0xFF;   
	        String hv = Integer.toHexString(v);   
	        if (hv.length() < 2) {   
	            stringBuilder.append(0);   
	        }   
	        stringBuilder.append(hv);   
	    }	     
	    return stringBuilder.toString();   
	} 
	
	
	/**
	 * 将结果集ResultSet转为List<Map>
	 * @param rs 数据库查询结果集
	 * @return	List<Map>
	 * @throws java.sql.SQLException
	 */
	public static List<Map> resultSetToList(ResultSet rs) throws java.sql.SQLException {   
        if (rs == null)   
            return Collections.EMPTY_LIST;   
        ResultSetMetaData md = rs.getMetaData(); //得到结果集(rs)的结构信息，比如字段数、字段名等   
        int columnCount = md.getColumnCount(); //返回此 ResultSet 对象中的列数   
        List<Map> list = new ArrayList<Map>();   
        Map rowData = new HashMap();   
        while (rs.next()) {   
         rowData = new HashMap(columnCount);   
         for (int i = 1; i <= columnCount; i++) {   
                 rowData.put(md.getColumnName(i), rs.getObject(i));   
         }   
         list.add(rowData);   
        }   
        return list;   
	} 
}

package com.hifs.hifs.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.hifs.hifs.entity.Monitor;
import com.hifs.hifs.entity.Record;
import com.hifs.hifs.service.record.IRecordService;
import com.hifs.hifs.util.EntityDao;
import com.hifs.hifs.util.Tool;
import com.hifs.hifs.util.Youtu;

@RestController
@RequestMapping(value="record")
public class RecordController {

    private EntityDao ed = new EntityDao();

    private Tool tool = new Tool();

    public static final String APP_ID = "10135334";
    public static final String SECRET_ID = "AKID42pTFudfpnZ9XjnXBev0tALLBHFEwMBt";
    public static final String SECRET_KEY = "jGclOtGWcGpCOOGHY3dMbljV5TfeR7bt";
    public static final String USER_ID = "1009371919";
    
    @Autowired
    private EntityManagerFactory emf;
    
    @Autowired
    private IRecordService rs;
    
    @PostMapping(value = "/getRecordsByPage")
    public Map<String,Object> getMonitorsByPage(Record record, String pageSize, String pageNumber){

        String params = " r.*, r.illegal_type AS illegalType, r.monitor_id AS monitorId, r.people_name AS peopleName, r.begin_date AS beginDate, r.end_date AS endDate, m.`name` AS monitorName, a. NAME AS areaName ";

        String condition = " 1=1 ";
        
        String join = " LEFT JOIN monitor m ON m.id = r.monitor_id LEFT JOIN area a ON m.area_id = a.id ";

        if(StringUtils.isNotBlank(record.getPeopleName())){
            condition += " and r.people_name like '%"+record.getPeopleName()+"%' ";
        }

        if(StringUtils.isNotBlank(record.getUno())){
            condition += " and r.uno = '"+record.getUno()+"' ";
        }

        String sql = ed.getSearchSql(params, "record r", join, condition, pageNumber, pageSize, null, null);

        String totalSql = ed.getSearchTotalSql("*", "record r", join, condition, null);

        Query query  = getData(sql);
        Query queryTotal  = getData(totalSql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        Map<String,Object> result = new HashMap<String,Object>();

        result.put("total", queryTotal.getResultList().get(0));
        result.put("data", query.getResultList());


        return result;
    }

    @PostMapping(value="save")
    public Record saveRecord(@RequestParam(value = "file") MultipartFile file, Record record) throws IOException{
    	String imgName = tool.updateFile(file, "img");
    	if(StringUtils.isNotBlank(imgName)){
        	record.setEvidence(imgName);
    	}
        return rs.save(record);
    }

    

    @PostMapping(value="saveByImg")
    public Object saveByImg(@RequestParam(value = "file") MultipartFile file, Record record) throws IOException{
        try {
        	String imgName = tool.updateFile(file, "img");
        	if(StringUtils.isNotBlank(imgName)){
            	record.setEvidence(imgName);
        	}
        	JSONArray arr = new JSONArray();
			Youtu faceYoutu = new Youtu(APP_ID, SECRET_ID, SECRET_KEY,Youtu.API_YOUTU_END_POINT,USER_ID);
			arr.add(faceYoutu.PlateOcrUrl("http://images.pccoo.cn/store/20120721/20120721105409831.jpg").toString());
			arr.add(faceYoutu.CarClassifyUrl("http://images.pccoo.cn/store/20120721/20120721105409831.jpg").toString());
	        record.setIllegalType("在高速公路上载运危险品未经审批或者未按规定行驶的");
	        record.setCarno("晋ED3131");
	        record.setUno("513623199605258612");
	        record.setPhone("17772450134");
	        record.setPeopleName("顾小琳");
	        rs.save(record);
	        return arr;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "";
    }

    @PostMapping(value="saveNoFile")
    public Record saveNoFile( Record record) throws IOException{
        return rs.save(record);
    }

    @PostMapping(value="deleteRecord")
    public Object deleteRecord(String records){
        JSONArray jsonArray=JSONArray.fromObject(records);
        JSONObject jsonObject=null;
        List<Record> list=new ArrayList<Record>();
        for(int i=0;i<jsonArray.size();i++){
            jsonObject = jsonArray.optJSONObject(i);
            Record record = new Record();
            record.setId(Integer.parseInt(jsonObject.optString("id")));
            record.setIllegalType(jsonObject.optString("illegalType"));
            record.setPeopleName(jsonObject.optString("peopleName"));
            record.setUno(jsonObject.optString("uno"));
            record.setPhone(jsonObject.optString("phone"));
            record.setMonitorId(jsonObject.optString("monitorId"));
            record.setBeginDate(jsonObject.optString("beginDate"));
            record.setEndDate(jsonObject.optString("endDate"));
            record.setEvidence(jsonObject.optString("evidence"));
            list.add(record);
        }
        rs.deleteInBatch(list);
        return null;
    }
    
    
    @GetMapping(value = "/getPieRecords")
    public Object getPieRecords(@RequestParam(value = "year",required = false, defaultValue = "") String year,
    		@RequestParam(value = "monitorId",required = false, defaultValue = "") String monitorId,
    		@RequestParam(value = "uNo",required = false, defaultValue = "") String uNo){

        String params = " illegal_type as name, COUNT(illegal_type) as value ";

        String condition = " 1=1 ";

        if(StringUtils.isNotBlank(year)){
            condition += " and date_format(STR_TO_DATE(r.begin_date, '%Y'), '%Y') = "+year+" ";
        }

        if(StringUtils.isNotBlank(monitorId)){
            condition += " and r.monitor_id = '"+monitorId+"' ";
        }

        if(StringUtils.isNotBlank(uNo)){
            condition += " and r.uNo = '"+uNo+"' ";
        }
        
        String group = " illegal_type ";

        String sql = ed.getSearchSql(params, "record r", null, condition, null, null, group, null);

        Query query  = getData(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


        return query.getResultList();
    }
    
    @PostMapping(value = "/getTRecords")
    public Object getTRecords(String monitorId, String year, String uNo){

        String params = " area.`name` AS area, m. NAME AS monitor, COUNT(illegal_type) AS totalType ";

        String condition = " 1=1 ";

        if(StringUtils.isNotBlank(monitorId)){
            condition += " and r.monitor_id = '"+monitorId+"' ";
        }

        if(StringUtils.isNotBlank(year)){
            condition += " and date_format(STR_TO_DATE(r.begin_date, '%Y'), '%Y') = "+year+" ";
        }

        if(StringUtils.isNotBlank(uNo)){
            condition += " and r.uno = '"+uNo+"' ";
        }
        
        String group = " monitor_id ";
        
        String join = " LEFT JOIN monitor m ON m.id = r.monitor_id LEFT JOIN area ON area.id = m.area_id ";

        String sql = ed.getSearchSql(params, "record r", join, condition, null, null, group, null);

        Query query  = getData(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);



        Map<String,Object> result = new HashMap<String,Object>();

        result.put("total", 0);
        result.put("data", query.getResultList());
        return result;
    }
    
    @GetMapping(value = "/getLineRecords")
    public Object getLineRecords(@RequestParam(value = "year",required = false, defaultValue = "") String year,
    @RequestParam(value = "monitorId",required = false, defaultValue = "") String monitorId){
    	
    	String params1 = " m.*, m.area_id AS areaId, m.is_run AS isRun, m.mtor_describe AS mtorDescribe, a. NAME AS areaName ";

        String condition1 = " 1=1 and m.is_run = '1' and a.is_monitor = '1' ";

        if(StringUtils.isNotBlank(monitorId)){
            condition1 += " and m.id = '"+monitorId+"' ";
        }
        
        String join1 = " LEFT JOIN area a ON a.id = m.area_id ";
        
    	String sql1 = ed.getSearchSql(params1, "monitor m", join1, condition1, null, null, null, null);

        Query query1  = getData(sql1);
        query1.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        
        List<Map<String,Object>> list = query1.getResultList();
        
        List result = new ArrayList<Map<String,Object>>();
        
        for(int i = 0; i<list.size();i++){
        	Map<String, Object> map = new HashMap<String, Object>();
        	map.put("monitor", list.get(i).get("areaName")+"/"+list.get(i).get("name"));
        	String params = " COUNT(illegal_type) AS num, date_format( STR_TO_DATE(r.begin_date, '%Y-%m'), '%m' ) as month ";

            String condition = " 1=1 and monitor_id = "+list.get(i).get("id")+" ";

            if(StringUtils.isNotBlank(year)){
                condition += " and date_format(STR_TO_DATE(r.begin_date, '%Y'), '%Y') = "+year+" ";
            }
            
            String group = " date_format( STR_TO_DATE(r.begin_date, '%Y-%m'), '%m' ) ";

            String order = " date_format( STR_TO_DATE(r.begin_date, '%Y-%m'), '%m' ) asc ";
            
            String sql = ed.getSearchSql(params, "record r", null, condition, null, null, group, order);

            Query query  = getData(sql);
            query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            
            map.put("data", query.getResultList());
            result.add(map);
        }
        
        return result;
    }

    
    
    @PostMapping(value = "/getSelfTRecords")
    public Object getSelfTRecords(String uNo, String monitorId, String year){

        String params = " r.*, r.illegal_type AS illegalType, r.monitor_id AS monitorId, r.people_name AS peopleName, r.begin_date AS beginDate, r.end_date AS endDate, m.`name` AS monitorName, a. NAME AS area  ";

        String condition = " 1=1 ";

        if(StringUtils.isNotBlank(monitorId)){
            condition += " and r.monitor_id = '"+monitorId+"' ";
        }

        if(StringUtils.isNotBlank(year)){
            condition += " and date_format(STR_TO_DATE(r.begin_date, '%Y'), '%Y') = "+year+" ";
        }

        if(StringUtils.isNotBlank(uNo)){
            condition += " and r.uno = '"+uNo+"' ";
        }
        
        String join = " LEFT JOIN monitor m ON m.id = r.monitor_id LEFT JOIN area a ON a.id = m.area_id ";

        String sql = ed.getSearchSql(params, "record r", join, condition, null, null, null, null);

        Query query  = getData(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);



        Map<String,Object> result = new HashMap<String,Object>();

        result.put("total", 0);
        result.put("data", query.getResultList());
        return result;
    }

    
    
    @GetMapping(value = "/getHighMonitorInfo")
    public Object getHighMonitorInfo(@RequestParam(value = "year",required = false, defaultValue = "") String year){

        String params = " area.`name` AS area, m. NAME AS monitor, monitor_id as monitorId, COUNT(illegal_type) AS totalType ";

        String condition = " 1=1 ";

        if(StringUtils.isNotBlank(year)){
            condition += " and date_format( STR_TO_DATE(r.begin_date, '%Y-%m'), '%Y' ) = "+year+" ";
        }
        String group = " monitor_id HAVING totalType > 1 ";
        
        String join = " LEFT JOIN monitor m ON m.id = r.monitor_id LEFT JOIN area ON area.id = m.area_id ";

        String sql = ed.getSearchSql(params, "record r", join, condition, null, null, group, null);

        Query query  = getData(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }
    

    private Query getData(String sql){
        EntityManager em = emf.createEntityManager();

        return em.createNativeQuery(sql);
    }

}

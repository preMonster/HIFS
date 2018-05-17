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

@RestController
@RequestMapping(value="record")
public class RecordController {

    private EntityDao ed = new EntityDao();

    private Tool tool = new Tool();
    
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
    

    private Query getData(String sql){
        EntityManager em = emf.createEntityManager();

        return em.createNativeQuery(sql);
    }

}

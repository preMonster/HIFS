package com.hifs.hifs.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.hifs.hifs.entity.Area;
import com.hifs.hifs.entity.Monitor;
import com.hifs.hifs.service.area.IAreaService;
import com.hifs.hifs.service.monitor.IMonitorService;
import com.hifs.hifs.util.EntityDao;

@RestController
@RequestMapping(value="monitor")
public class MonitorController {

    private EntityDao ed = new EntityDao();
    
    @Autowired
    private EntityManagerFactory emf;
    
    @Autowired
    private IMonitorService ms;
    
    @PostMapping(value = "/getMonitorsByPage")
    public Map<String,Object> getMonitorsByPage(Monitor monitor, String pageSize, String pageNumber){

        String params = " m.*, m.area_id AS areaId, m.is_run AS isRun, m.mtor_describe AS mtorDescribe, a. NAME AS areaName ";

        String condition = " 1=1 ";
        
        String join = " LEFT JOIN area a ON a.id = m.area_id ";

        if(StringUtils.isNotBlank(monitor.getName())){
            condition += " and m.name like '%"+monitor.getName()+"%' ";
        }

        if(StringUtils.isNotBlank(monitor.getAreaId())){
            condition += " and m.area_id = '"+monitor.getAreaId()+"' ";
        }

        String sql = ed.getSearchSql(params, "monitor m", join, condition, pageNumber, pageSize, null, null);

        String totalSql = ed.getSearchTotalSql("*", "monitor m", join, condition, null);

        Query query  = getData(sql);
        Query queryTotal  = getData(totalSql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        Map<String,Object> result = new HashMap<String,Object>();

        result.put("total", queryTotal.getResultList().get(0));
        result.put("data", query.getResultList());


        return result;
    }

    @PostMapping(value="save")
    public Monitor saveMonitor(Monitor monitor){
        return ms.save(monitor);
    }

    @PostMapping(value="deleteMonitor")
    public Object deleteMonitor(String monitors){
        JSONArray jsonArray=JSONArray.fromObject(monitors);
        JSONObject jsonObject=null;
        List<Monitor> list=new ArrayList<Monitor>();
        for(int i=0;i<jsonArray.size();i++){
            jsonObject = jsonArray.optJSONObject(i);
            Monitor monitor = new Monitor();
            monitor.setId(Integer.parseInt(jsonObject.optString("id")));
            monitor.setName(jsonObject.optString("name"));
            monitor.setIsRun(jsonObject.optString("isRun"));
            monitor.setMtorDescribe(jsonObject.optString("mtorDescribe"));
            monitor.setAreaId(jsonObject.optString("areaId"));
            list.add(monitor);
        }
        ms.deleteInBatch(list);
        return null;
    }

    @GetMapping(value="getRunningMonitor")
    public Object getRunningMonitor(){

        String params = " m.*, m.area_id AS areaId, m.is_run AS isRun, m.mtor_describe AS mtorDescribe, a. NAME AS areaName ";

        String condition = " 1=1 and m.is_run = '1' and a.is_monitor = '1' ";
        
        String join = " LEFT JOIN area a ON a.id = m.area_id ";
        
    	String sql = ed.getSearchSql(params, "monitor m", join, condition, null, null, null, null);

        Query query  = getData(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }
    


    @GetMapping(value="getAreaMonitor")
    public Object getAreaMonitor(){
    	
        String params = " id as value, name as label ";

        String condition = " is_monitor = 1 ";
        
    	String sql = ed.getSearchSql(params, "area", null, condition, null, null, null, null);

        Query query  = getData(sql);
        
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        
        List<Map<String, Object>> list = query.getResultList();
        
        for(int i=0;i<list.size();i++){

            String params1 = " id as value, name as label ";

            String condition1 = " is_run = 1 AND area_id = "+list.get(i).get("value")+" ";
            
        	String sql1 = ed.getSearchSql(params1, "monitor", null, condition1, null, null, null, null);

            Query query1  = getData(sql1);
            query1.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            
            list.get(i).put("children", query1.getResultList());
        }
        return list;
    }
    

    private Query getData(String sql){
        EntityManager em = emf.createEntityManager();

        return em.createNativeQuery(sql);
    }

}

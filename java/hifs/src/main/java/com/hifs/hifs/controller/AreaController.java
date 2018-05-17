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
import com.hifs.hifs.service.area.IAreaService;
import com.hifs.hifs.util.EntityDao;

@RestController
@RequestMapping(value="area")
public class AreaController {

    private EntityDao ed = new EntityDao();
    
    @Autowired
    private EntityManagerFactory emf;
    
    @Autowired
    private IAreaService as;
    
    @PostMapping(value = "/getAreasByPage")
    public Map<String,Object> getAreasByPage(Area area, String pageSize, String pageNumber){

        String params = " *, area_describe AS areaDescribe, is_monitor AS isMonitor ";

        String condition = " 1=1 ";

        if(StringUtils.isNotBlank(area.getName())){
            condition += " and name like '%"+area.getName()+"%' ";
        }

        String sql = ed.getSearchSql(params, "area", null, condition, pageNumber, pageSize, null, null);

        String totalSql = ed.getSearchTotalSql("*", "area", null, condition, null);

        Query query  = getData(sql);
        Query queryTotal  = getData(totalSql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        Map<String,Object> result = new HashMap<String,Object>();

        result.put("total", queryTotal.getResultList().get(0));
        result.put("data", query.getResultList());


        return result;
    }

    @PostMapping(value="save")
    public Area saveArea(Area area){
        return as.save(area);
    }

    @PostMapping(value="deleteArea")
    public Object deleteArea(String areas){
        JSONArray jsonArray=JSONArray.fromObject(areas);
        JSONObject jsonObject=null;
        List<Area> list=new ArrayList<Area>();
        for(int i=0;i<jsonArray.size();i++){
            jsonObject = jsonArray.optJSONObject(i);
            Area area = new Area();
            area.setId(Integer.parseInt(jsonObject.optString("id")));
            area.setName(jsonObject.optString("name"));
            area.setIsMonitor(jsonObject.optString("isMonitor"));
            area.setAreaDescribe(jsonObject.optString("areaDescribe"));
            list.add(area);
        }
        as.deleteInBatch(list);
        return null;
    }
    
    @GetMapping(value="getRunningArea")
    public List<Area> getRunningArea(){
        return as.findByIsMonitor("1");
    }

    private Query getData(String sql){
        EntityManager em = emf.createEntityManager();

        return em.createNativeQuery(sql);
    }

}

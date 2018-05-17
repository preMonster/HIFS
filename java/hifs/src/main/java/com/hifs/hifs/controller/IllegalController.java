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

import com.hifs.hifs.entity.Illegal;
import com.hifs.hifs.service.illegal.IIllegalService;
import com.hifs.hifs.util.EntityDao;

@RestController
@RequestMapping(value="illegal")
public class IllegalController {

    private EntityDao ed = new EntityDao();

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    private IIllegalService is;
    
    @PostMapping(value = "getIllegalsByPage")
    public Map<String,Object> getRolesByPage(Illegal illegal, String pageNumber, String pageSize){

        String params = "*";

        String condition = " 1=1 ";

        if(StringUtils.isNotBlank(illegal.getName())){
            condition += " and name like '%"+illegal.getName()+"%' ";
        }

        String sql = ed.getSearchSql(params, "illegal", null, condition, pageNumber, pageSize, null, null);

        String totalSql = ed.getSearchTotalSql("*", "illegal", null, condition, null);

        Query query  = getData(sql);
        Query queryTotal  = getData(totalSql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        Map<String,Object> result = new HashMap<String,Object>();

        result.put("total", queryTotal.getResultList().get(0));
        result.put("data", query.getResultList());

        return result;
    }

    @PostMapping(value="save")
    public Illegal saveIllegal(Illegal illegal){
        return is.save(illegal);
    }

    @PostMapping(value="deleteIllegal")
    public Object deleteIllegal(String illegals){
        JSONArray jsonArray=JSONArray.fromObject(illegals);
        JSONObject jsonObject=null;
        List<Illegal> list=new ArrayList<Illegal>();
        for(int i=0;i<jsonArray.size();i++){
            jsonObject = jsonArray.optJSONObject(i);
            Illegal illegal = new Illegal();
            illegal.setId(Integer.parseInt(jsonObject.optString("id")));
            illegal.setName(jsonObject.optString("name"));
            illegal.setPunish(jsonObject.optString("punish"));
            list.add(illegal);
        }
        is.deleteInBatch(list);
        return null;
    }

    @GetMapping(value = "getIllegalList")
    public List<Illegal> getIllegalList(){
        return is.findAll();
    }

    private Query getData(String sql){
        EntityManager em = emf.createEntityManager();

        return em.createNativeQuery(sql);
    }

}

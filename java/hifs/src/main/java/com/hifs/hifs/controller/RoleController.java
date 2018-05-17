package com.hifs.hifs.controller;

import com.hifs.hifs.entity.Role;
import com.hifs.hifs.entity.RoleAssign;
import com.hifs.hifs.service.role.IRoleServer;
import com.hifs.hifs.service.roleAssign.IRoleAssignService;
import com.hifs.hifs.util.EntityDao;
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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/role")
public class RoleController {

    private EntityDao ed = new EntityDao();

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    private IRoleServer rs;

    @Autowired
    private IRoleAssignService ras;

    @PostMapping(value = "getRolesByPage")
    public Map<String,Object> getRolesByPage(Role role, String pageNumber, String pageSize){

        String params = "*, hifs_describe AS 'describe'";

        String condition = " 1=1 ";

        if(StringUtils.isNotBlank(role.getName())){
            condition += " and name like '%"+role.getName()+"%' ";
        }

        String sql = ed.getSearchSql(params, "role", null, condition, pageNumber, pageSize, null, null);

        String totalSql = ed.getSearchTotalSql("*", "role", null, condition, null);

        Query query  = getData(sql);
        Query queryTotal  = getData(totalSql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        Map<String,Object> result = new HashMap<String,Object>();

        result.put("total", queryTotal.getResultList().get(0));
        result.put("data", query.getResultList());

        return result;
    }

    @PostMapping(value="save")
    public Role saveRole(Role role){
        return rs.save(role);
    }

    @PostMapping(value="deleteRole")
    public Object deleteRole(String roles){
        JSONArray jsonArray=JSONArray.fromObject(roles);
        JSONObject jsonObject=null;
        List<Role> list=new ArrayList<Role>();
        for(int i=0;i<jsonArray.size();i++){
            jsonObject = jsonArray.optJSONObject(i);
            Role role = new Role();
            role.setId(Integer.parseInt(jsonObject.optString("id")));
            role.setName(jsonObject.optString("name"));
            role.setHifs_describe(jsonObject.optString("describe"));
            list.add(role);

            List<RoleAssign> roleAssigns = ras.findByRoleId(role.getId());
            ras.deleteInBatch(roleAssigns);
        }
        rs.deleteInBatch(list);
        return null;
    }

    @GetMapping(value = "getRoleList")
    public List<Role> getRoleList(){
        return rs.findAll();
    }

    private Query getData(String sql){
        EntityManager em = emf.createEntityManager();

        return em.createNativeQuery(sql);
    }
}

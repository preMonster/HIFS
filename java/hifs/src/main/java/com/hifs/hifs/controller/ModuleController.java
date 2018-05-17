package com.hifs.hifs.controller;

import com.hifs.hifs.entity.Module;
import com.hifs.hifs.entity.RoleAssign;
import com.hifs.hifs.service.roleAssign.IRoleAssignService;
import com.hifs.hifs.util.EntityDao;
import com.hifs.hifs.service.module.IModuleService;
import com.hifs.hifs.util.Tool;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/module")
public class ModuleController {

    private EntityDao ed = new EntityDao();

    private Tool tool = new Tool();

    @Autowired
    private EntityManagerFactory emf;
    
    @Autowired
    private IModuleService ms;

    @Autowired
    private IRoleAssignService ras;

    @PostMapping(value = "/getModulesByPage")
    public Map<String,Object> getModulesByPage(Module module, String pageSize, String pageNumber){

        String params = " module.*, icon_name AS iconName, pm. NAME AS parentName ";

        String condition = " 1=1 ";

        String join = " LEFT JOIN (SELECT m. NAME, m. CODE FROM module m ) pm ON module.parent_code = pm. CODE ";

        if(StringUtils.isNotBlank(module.getParent_code())){
            condition += " and parent_code = '"+module.getParent_code()+"' ";
        }else{
            condition += " and parent_code = '' ";
        }

        if(StringUtils.isNotBlank(module.getCode())){
            condition += " and module.code like '%"+module.getCode()+"%' ";
        }

        if(StringUtils.isNotBlank(module.getName())){
            condition += " and module.name like '%"+module.getName()+"%' ";
        }

        String sql = ed.getSearchSql(params, "module", join, condition, pageNumber, pageSize, null, null);

        String totalSql = ed.getSearchTotalSql("*", "module", join, condition, null);

        Query query  = getData(sql);
        Query queryTotal  = getData(totalSql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        Map<String,Object> result = new HashMap<String,Object>();

        result.put("total", queryTotal.getResultList().get(0));
        result.put("data", query.getResultList());


        return result;
    }


    @GetMapping(value="getModuleTrees")
    public Map<String,Object> getModuleTrees(){
        String params = " id, code, name as title ";
        String condition = " 1=1 and parent_code = '' ";
        String sql = ed.getSearchSql(params, "module", null, condition, null, null, null, null);
        Query query  = getData(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();


        Map<String,Object> result = new HashMap<String, Object>();
        result.put("code", "");
        result.put("title", "全部模块");
        result.put("children", list);

        for(int i=0;i<list.size();i++){
            String params1 = " id, code, name as title ";
            String condition1 = " 1=1 and parent_code = '"+list.get(i).get("code")+"' ";
            String sql1 = ed.getSearchSql(params1, "module", null, condition1, null, null, null, null);
            Query query1  = getData(sql1);
            query1.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Map<String, Object>> list1 = query1.getResultList();

            list.get(i).put("children", list1);
        }

        return result;
    }

    @PostMapping(value="save")
    public Module saveModule(Module module){
        String parentCode = module.getParent_code();
        if(StringUtils.isBlank(parentCode)){
            parentCode = "m";
        }
        if(StringUtils.isBlank(module.getCode())){
            String code = tool.getCode(parentCode);
            module.setCode(code);
        }
        return ms.save(module);
    }

    @PostMapping(value="deleteModule")
    public Object deleteModule(String modules){
        JSONArray jsonArray=JSONArray.fromObject(modules);
        JSONObject jsonObject=null;
        List<Module> list=new ArrayList<Module>();
        for(int i=0;i<jsonArray.size();i++){
            jsonObject = jsonArray.optJSONObject(i);
            Module moudle = new Module();
            moudle.setId(Integer.parseInt(jsonObject.optString("id")));
            moudle.setName(jsonObject.optString("name"));
            moudle.setCode(jsonObject.optString("code"));
            moudle.setParent_code(jsonObject.optString("parent_code"));
            moudle.setLevel(jsonObject.optString("level"));
            moudle.setIcon_name(jsonObject.optString("icon_name"));
            moudle.setAddres(jsonObject.optString("addres"));
            list.add(moudle);

            List<RoleAssign> roleAssigns = ras.findByModuleId(moudle.getId());
            ras.deleteInBatch(roleAssigns);
        }
        ms.deleteInBatch(list);
        return null;
    }

    @GetMapping(value = "getModulesByRoleId")
    public List<Map<String, Object>> getModulesByRoleId(@RequestParam(value = "roleId") String roleId){
         String params = " code, name as title ";
         String join = " LEFT JOIN role_assign ra ON ra.module_id = m.id ";
         String condition = " ra.role_id = "+roleId+" ";

        String sql = ed.getSearchSql(params, "module m", join, condition, null, null, null, null);
        Query query  = getData(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        return list;
    }


    @GetMapping(value = "getModulesTreeByuserId")
    public List<Map<String, Object>> getModulesTreeByuserId(@RequestParam(value = "userId") String userId){
         String params = " m.NAME as name, m.icon_name as iconName, m.code as code, m.addres as addres, m.id as id ";
         String join = " LEFT JOIN role_assign ra ON m.id = ra.module_id LEFT JOIN role r on r.id = ra.role_id LEFT JOIN user u on u.role_id = r.id ";
         String condition = " m.parent_code = '' ";

        String sql = ed.getSearchSql(params, "module m", null, condition, null, null, null, null);
        Query query  = getData(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();
        
        for(int i=0;i<list.size();i++){
        	String code = list.get(i).get("code").toString();
            String condition1 = " m.parent_code = '"+code+"' and u.id = "+userId+" ";

            String sql1 = ed.getSearchSql(params, "module m", join, condition1, null, null, null, null);
            Query query1  = getData(sql1);
            query1.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Map<String, Object>> list1 = query1.getResultList();
            
            list.get(i).put("children", list1);
        }

        return list;
    }

    private Query getData(String sql){
        EntityManager em = emf.createEntityManager();

        return em.createNativeQuery(sql);
    }

}

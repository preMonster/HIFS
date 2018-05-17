package com.hifs.hifs.controller;

import com.hifs.hifs.entity.RoleAssign;
import com.hifs.hifs.service.roleAssign.IRoleAssignService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@RestController
@RequestMapping(value = "/roleAssign")
public class RoleAssignController {

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    private IRoleAssignService ras;

    

    @PostMapping(value = "changeRoleAssign")
    public Object changeRoleAssign(String roleId, String moduleIds){
        deleteRoleAssign(roleId);
        JSONArray jsonArray=JSONArray.fromObject(moduleIds);
        JSONObject jsonObject=null;
        for(int i=0;i<jsonArray.size();i++){
            jsonObject = jsonArray.optJSONObject(i);
            RoleAssign roleAssign = new RoleAssign();
            roleAssign.setRoleId(Integer.parseInt(roleId));
            if(StringUtils.isNotBlank(jsonObject.optString("moduleId"))){
                roleAssign.setModuleId(Integer.parseInt(jsonObject.optString("moduleId")));
                ras.save(roleAssign);
            }
        }
        return null;
    }

    @PostMapping(value = "saveRoleAssign")
    public Object saveRoleAssign(String roleId, String moduleIds){
        JSONArray jsonArray=JSONArray.fromObject(moduleIds);
        JSONObject jsonObject=null;
        for(int i=0;i<jsonArray.size();i++){
            jsonObject = jsonArray.optJSONObject(i);
            RoleAssign roleAssign = new RoleAssign();
            roleAssign.setRoleId(Integer.parseInt(roleId));
            roleAssign.setModuleId(Integer.parseInt(jsonObject.optString("moduleId")));
            ras.save(roleAssign);
        }
        return null;
    }


    @PostMapping(value = "deleteAllByoleId")
    public Object deleteRoleAssign(String roleId){
        List<RoleAssign> list = ras.findByRoleId(Integer.parseInt(roleId));
        ras.deleteInBatch(list);
        return null;
    }
}

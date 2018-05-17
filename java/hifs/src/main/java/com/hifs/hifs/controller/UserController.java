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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hifs.hifs.entity.Role;
import com.hifs.hifs.entity.RoleAssign;
import com.hifs.hifs.entity.User;
import com.hifs.hifs.service.role.IRoleServer;
import com.hifs.hifs.service.user.IUserService;
import com.hifs.hifs.util.EntityDao;
import com.hifs.hifs.util.Tool;

@RestController
@RequestMapping(value = "user")
public class UserController {


    private EntityDao ed = new EntityDao();

    private Tool tool = new Tool();

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    private IRoleServer rs;

    @Autowired
    private IUserService us;
    
    @PostMapping(value = "getUsersByPage")
    public Map<String,Object> getUsersByPage(User user, String pageNumber, String pageSize){

        String params = " u.*, u.user_name as userName, u.role_Id as roleId, u.real_name as realName , r.name as roleName ";

        String condition = " 1=1 ";
        
        String join = " LEFT JOIN role r ON r.id = u.role_id ";

        if(StringUtils.isNotBlank(user.getUserName())){
            condition += " and u.user_name like '%"+user.getUserName()+"%' ";
        }

        if(StringUtils.isNotBlank(user.getUno())){
            condition += " and u.uno = '"+user.getUno()+"' ";
        }

        String sql = ed.getSearchSql(params, " user u ", join, condition, pageNumber, pageSize, null, null);

        String totalSql = ed.getSearchTotalSql("*", " user u ", join, condition, null);

        Query query  = getData(sql);
        Query queryTotal  = getData(totalSql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        Map<String,Object> result = new HashMap<String,Object>();

        result.put("total", queryTotal.getResultList().get(0));
        result.put("data", query.getResultList());

        return result;
    }

    @PostMapping(value="save")
    public User saveUser(User user){
    	if(StringUtils.isNotBlank(user.getId()+"")){
        	String password = user.getPassword();
        	String md5Password = tool.MD5(password);
        	user.setPassword(md5Password);
    	}
        return us.save(user);
    }

    @PostMapping(value="deleteUser")
    public Object deleteUser(String users){
        JSONArray jsonArray=JSONArray.fromObject(users);
        JSONObject jsonObject=null;
        List<User> list=new ArrayList<User>();
        for(int i=0;i<jsonArray.size();i++){
            jsonObject = jsonArray.optJSONObject(i);
            User user = new User();
            user.setId(Integer.parseInt(jsonObject.optString("id")));
            user.setUserName(jsonObject.optString("userName"));
            user.setPassword(jsonObject.optString("password"));
            user.setRoleId(jsonObject.optString("roleId"));
            user.setUno(jsonObject.optString("uno"));
            user.setRealName(jsonObject.optString("realName"));
            user.setSex(Integer.parseInt(jsonObject.optString("sex")));
            user.setPhone(jsonObject.optString("phone"));
            user.setEmail(jsonObject.optString("email"));
            list.add(user);
        }
        us.deleteInBatch(list);
        return null;
    }

    @PostMapping(value="checkUser")
    public Object checkUser(User user){
    	String userName = user.getUserName();
    	List<User> list = us.findByUserName(userName);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("flag", false);
    	if(list.size()>0){
    		if(tool.MD5(user.getPassword()).equals(list.get(0).getPassword())){
    			map.put("flag", true);
    			map.put("user", list.get(0));
    		}else{
    			map.put("error", "密码错误");
    		}
    	}else{
			map.put("error", "用户不存在");
    	}

		return map;
    }
    
    @GetMapping(value="getUserById")
    public Object getUserById(@RequestParam(value = "userId") String userId){
    	String params = " u.*, u.user_name as userName, u.role_Id as roleId, u.real_name as realName , r.name as roleName ";
        
        String join = " LEFT JOIN role r ON r.id = u.role_id ";
        
        String condition = " u.id = "+userId+" ";

    	String sql = ed.getSearchSql(params, " user u ", join, condition, null, null, null, null);
    	
        Query query  = getData(sql);
        
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        
        return query.getResultList().get(0);
    }

    private Query getData(String sql){
        EntityManager em = emf.createEntityManager();

        return em.createNativeQuery(sql);
    }

}

package com.hifs.hifs.controller;

//import org.springframework.beans.factory.annotation.Value;
//import com.hifs.hifs.entity.Role;
//import com.hifs.hifs.service.IRoleServer;
import com.hifs.hifs.util.EntityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import  java.util.List;

@RestController
@RequestMapping(value = "/helloC")
public class HelloController {
//    @Value("${testString}")
//    private String testString;

    private EntityDao ed = new EntityDao();

    @Autowired
    private EntityManagerFactory emf;

//    @Autowired
//    private IRoleServer roleServer;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String say(@RequestParam(value = "id",required = false, defaultValue = "000") Integer id){
        return "id:"+id;
    }

//    @GetMapping(value = "/getRole")
//    public List<Role> roleList(){
//        return roleServer.findAll();
//    }
//
//    @PostMapping(value = "/addRole")
//    public Role addRole(Role role){
//        return roleServer.save(role);
//    }
//
//    @GetMapping(value = "/findByName/{name}")
//    public List<Role> getRoleByName(@PathVariable("name") String name){
//        return roleServer.findByName(name);
//    }

//    @GetMapping(value = "/getRoleBySql")
//    public  List<Role> getRoleBySql(){
//        EntityManager em = emf.createEntityManager();
//
//        String sql = ed.getSearchSql("*", "role", null, null, null, null, null, null);
//
//        Query query  = em.createNativeQuery(sql);
//
//        return query.getResultList();
//    }
}

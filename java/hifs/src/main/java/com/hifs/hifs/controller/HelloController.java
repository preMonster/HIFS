package com.hifs.hifs.controller;

//import org.springframework.beans.factory.annotation.Value;
//import com.hifs.hifs.entity.Role;
//import com.hifs.hifs.service.IRoleServer;
import com.hifs.hifs.util.EntityDao;
import com.hifs.hifs.util.Youtu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import java.util.HashMap;
import  java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "/helloC")
public class HelloController {
//    @Value("${testString}")
//    private String testString;

    private EntityDao ed = new EntityDao();

    @Autowired
    private EntityManagerFactory emf;
    
    public static final String APP_ID = "10135334";
    public static final String SECRET_ID = "AKID42pTFudfpnZ9XjnXBev0tALLBHFEwMBt";
    public static final String SECRET_KEY = "jGclOtGWcGpCOOGHY3dMbljV5TfeR7bt";
    public static final String USER_ID = "1009371919";

//    @Autowired
//    private IRoleServer roleServer;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String say(@RequestParam(value = "id",required = false, defaultValue = "000") Integer id){
        return "id:"+id;
    }


    @RequestMapping(value = "/say", method = RequestMethod.GET)
    public Object say1(){
    	
		try {
			JSONArray arr = new JSONArray();
			Youtu faceYoutu = new Youtu(APP_ID, SECRET_ID, SECRET_KEY,Youtu.API_YOUTU_END_POINT,USER_ID);
			JSONObject response = new JSONObject();
			//response= faceYoutu.FaceCompareUrl("http://open.youtu.qq.com/content/img/slide-1.jpg","http://open.youtu.qq.com/content/img/slide-1.jpg");
			//response = faceYoutu.DetectFace("test.jpg",1);
			//response = faceYoutu.ImageTerrorismUrl("http://open.youtu.qq.com/app/img/experience/terror/img_terror01.jpg");
			response = faceYoutu.CarClassifyUrl("http://images.pccoo.cn/store/20120721/20120721105409831.jpg");
			arr.put(response);
			JSONObject response1 = new JSONObject();
			//response = faceYoutu.BizLicenseOcrUrl("http://open.youtu.qq.com/app/img/experience/char_general/ocr_yyzz_01.jpg");
			//response = faceYoutu.CreditCardOcrUrl("http://open.youtu.qq.com/app/img/experience/char_general/ocr_card_1.jpg");
			response1 = faceYoutu.PlateOcrUrl("http://images.pccoo.cn/store/20120721/20120721105409831.jpg");
			arr.put(response1);
            //get respose
            return arr.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
        return "123";
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

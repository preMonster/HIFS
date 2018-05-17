package com.hifs.hifs.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.web.multipart.MultipartFile;

public class Tool {

    public Boolean isEmpty(String s){
        return false;
    }

    public static String getCode(String symbol){

        Calendar now = Calendar.getInstance();

        String dateStr = now.get(Calendar.YEAR)+
                        (now.get(Calendar.MONTH) + 1) + ""+
                         now.get(Calendar.DAY_OF_MONTH)+
                         now.get(Calendar.HOUR_OF_DAY)+
                         now.get(Calendar.MINUTE)+
                         now.get(Calendar.SECOND);
        return symbol+"-"+dateStr;

    }
    
 // MD5加码。32位  
    public static String MD5(String inStr) {  
     MessageDigest md5 = null;  
     try {  
      md5 = MessageDigest.getInstance("MD5");  
     } catch (Exception e) {  
      System.out.println(e.toString());  
      e.printStackTrace();  
      return "";  
     }  
     char[] charArray = inStr.toCharArray();  
     byte[] byteArray = new byte[charArray.length];  
     
     for (int i = 0; i < charArray.length; i++)  
      byteArray[i] = (byte) charArray[i];  
     
     byte[] md5Bytes = md5.digest(byteArray);  
     
     StringBuffer hexValue = new StringBuffer();  
     
     for (int i = 0; i < md5Bytes.length; i++) {  
      int val = ((int) md5Bytes[i]) & 0xff;  
      if (val < 16)  
       hexValue.append("0");  
      hexValue.append(Integer.toHexString(val));  
     }  
     
     return hexValue.toString();  
    }  

    // 可逆的加密算法   
    public   static  String KL(String inStr) {  
     // String s = new String(inStr);   
     char [] a = inStr.toCharArray();  
     for  ( int  i =  0 ; i < a.length; i++) {  
      a[i] = (char ) (a[i] ^  't' );  
     }  
     String s = new  String(a);  
     return  s;  
    }  

    
    
    // 加密后解密   
    public   static  String JM(String inStr) {  
     char [] a = inStr.toCharArray();  
     for  ( int  i =  0 ; i < a.length; i++) {  
      a[i] = (char ) (a[i] ^  't' );  
     }  
     String k = new  String(a);  
     return  k;  
    }
    


	public static synchronized String updateFile(MultipartFile myFile,String imgName) throws IOException{
		
		if(myFile != null){
			
			File f = null;
			try {
			    f=File.createTempFile("tmp", null);
			    myFile.transferTo(f);
			    f.deleteOnExit();
			} catch (IOException e) {
			    e.printStackTrace();
			}
			String s1 = "G:/xiangmu/HIFS/static/image/";
			BufferedImage image = ImageIO.read(f);//根据你实际情况改文件路径吧
			String newImgName = getCode(imgName)+".png";
			
			ImageIO.write(image, "png", new File(s1+newImgName));
			return "/static/image/"+newImgName;
		}else{
			return "";
		}
	}


}

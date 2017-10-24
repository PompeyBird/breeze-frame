package org.bird.breeze.web.member.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.bird.breeze.web.member.bean.MemberBean;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


public class MemberUtil {
	
	private static Object lock = new Object();
	
	private static Map<String, MemberBean> memberMap = new HashMap<String, MemberBean>();
	
	private static String path;
	
	public static String USER_NAME = "user_name";
	public static String PASSWORD = "password";
	public static String LAST_LOGIN_TIME = "last_login_time";
	public static String PASS_CHANGE_TIME = "pass_change_time";
	
	static{
		String pathStr = MemberUtil.class.getClassLoader().getResource("").getPath();
		int index = pathStr.indexOf("WEB-INF");
		path = pathStr.substring(0, index);
	}
	
	@SuppressWarnings("unchecked")
	public static List<MemberBean> getMembers(){
		
		List<MemberBean> list = new ArrayList<MemberBean>();
		
		synchronized(lock){
			if(null != memberMap && !memberMap.isEmpty()){
	        	list.addAll(memberMap.values());
	        	return list;
	        }
		}
		
        try {
        	
        	Document document = readXmlDocument();
        	
			if(null != document){
				Element root = document.getRootElement();
				List<Element> users = root.elements("user");
				for(Element user : users){
					
					MemberBean member = elementToMember(user);
					synchronized(lock){
						memberMap.put(member.getUserName(), member);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
        list.addAll(memberMap.values());
        
        return list;
	}
	
	@SuppressWarnings("unchecked")
	public static void updateMember(MemberBean member){
		
		try {
			
			Document document = readXmlDocument();
			
			if(null != document){
				Element root = document.getRootElement();
				List<Element> users = root.elements("user");
				for(Element user : users){
					String userName = user.element(USER_NAME).getText();
					if(userName.equals(member.getUserName())){
						memberToElement(member, user);
						break;
					}
				}
			}
			
			writeXmlDocument(document);
			
			synchronized(lock){
				memberMap.put(member.getUserName(), member);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static void addMember(MemberBean member){
		
		try {

			Document document = readXmlDocument();
			
			Element root = document.getRootElement();
			Element user = root.addElement("user");
			if(!StringUtils.isEmpty(member.getUserName())){
				user.addElement(USER_NAME).setText(member.getUserName());
			}
			if(!StringUtils.isEmpty(member.getPassword())){
				user.addElement(PASSWORD).setText(member.getPassword());
			}
			if(null != member.getLastLoginTime()){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				user.addElement(LAST_LOGIN_TIME).setText(sdf.format(member.getLastLoginTime()));
			}
			if(null != member.getPasswordUpdatedTime()){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				user.addElement(PASS_CHANGE_TIME).setText(sdf.format(member.getPasswordUpdatedTime()));
			}
			
//			System.out.println("==============================");
//			System.out.println(path+"/login_info1.xml");
//			System.out.println("==============================");
//			
//			System.out.println("==============================");
//			String docXmlText=document.asXML();
//			System.out.println(docXmlText);
//			System.out.println("==============================");
			
			writeXmlDocument(document);
			
			synchronized(lock){
				memberMap.put(member.getUserName(), member);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static MemberBean getMember(String userName, String password){
		MemberBean member = memberMap.get(userName);
		if(null != member){
			if(!member.getPassword().equals(password)){
				member = null;
			}
		}
		return member;
	}
	
	
	private static Document readXmlDocument() throws Exception{
		
		Document document = null;
		
		synchronized(lock){
			File file = new File(path+"/WEB-INF/login_info.xml");
			if(file.exists())    
			{    
				SAXReader reader = new SAXReader();
				document = reader.read(file);
			}    
			else{
				document = DocumentHelper.parseText("<users></users>");
			}
			
		}
		
		return  document;
	}
	
	private static void writeXmlDocument(Document document) throws Exception{
		
		// 排版缩进的格式  
		OutputFormat format = OutputFormat.createPrettyPrint();  
		// 设置编码  
		format.setEncoding("UTF-8");  
		
		synchronized(lock){
			XMLWriter writer = new XMLWriter(new OutputStreamWriter(  
			        new FileOutputStream(new File(path+"/WEB-INF//login_info.xml")), "UTF-8"), format);  
			
			// 写入  
			writer.write(document);  
			// 立即写入  
			writer.flush();  
			// 关闭操作  
			writer.close();
		}
	}
	
	private static Element memberToElement(MemberBean member, Element user){

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(!StringUtils.isEmpty(member.getUserName())){
			user.addElement(USER_NAME).setText(member.getUserName());
		}
		if(!StringUtils.isEmpty(member.getPassword())){
			user.addElement(PASSWORD).setText(member.getPassword());
		}
		if(null != member.getLastLoginTime()){
			user.addElement(LAST_LOGIN_TIME).setText(sdf.format(member.getLastLoginTime()));
		}
		if(null != member.getPasswordUpdatedTime()){
			user.addElement(PASS_CHANGE_TIME).setText(sdf.format(member.getPasswordUpdatedTime()));
		}
		return user;
	}
	
	private static MemberBean elementToMember(Element user) throws Exception{
		
		String userName = user.element(USER_NAME).getText();
		String password = user.element(PASSWORD).getText();
		String lastLoginTime = user.element(LAST_LOGIN_TIME).getText();
		String passwordUpdatedTime = user.element(PASS_CHANGE_TIME).getText();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		MemberBean member = new MemberBean();
		member.setUserName(userName);
		member.setPassword(password);
		if(!StringUtils.isEmpty(lastLoginTime)){
			member.setLastLoginTime(new Timestamp(sdf.parse(lastLoginTime).getTime()));
		}
		if(!StringUtils.isEmpty(passwordUpdatedTime)){
			member.setPasswordUpdatedTime(new Timestamp(sdf.parse(passwordUpdatedTime).getTime()));
		}
		
		return member;
	}
}

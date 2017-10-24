package org.bird.breeze.web.member.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bird.breeze.web.member.bean.MenuBean;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class MenuUtil {
	
	private static Object lock = new Object();
	
	private static Map<String, MenuBean> menuMap = new HashMap<String, MenuBean>();
	
	private static List<MenuBean> menuList = new ArrayList<MenuBean>();
	
	private static String path;
	
	public static String NODE_ROOT = "menus";
	public static String NODE_MENU = "menu";
//	public static String NODE_SUB_MENU = "sub_menu";
	public static String ATTR_TITLE = "title";
	public static String ATTR_CODE = "code";
	public static String ATTR_SRC = "src";
	public static String ATTR_HIDDEN = "hidden";
	
	static{
		String pathStr = MenuUtil.class.getClassLoader().getResource("").getPath();
		int index = pathStr.indexOf("WEB-INF");
		path = pathStr.substring(0, index);
	}
	
	public static void init() {
		
		try {
			
			Document document = readXmlDocument();
	    	
			if(null != document){
				Element root = document.getRootElement();
				menuList.addAll(elementToMenu(root, null));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static List<MenuBean> getMenus(){
		if(null == menuList || menuList.isEmpty()){
			init();
        }
		return menuList;
	}
	
	
	private static Document readXmlDocument() throws Exception{
		
		Document document = null;
		
		synchronized(lock){
			File file = new File(path+"/WEB-INF/menu_info.xml");
			if(file.exists())    
			{    
				SAXReader reader = new SAXReader();
				document = reader.read(file);
			}    
			else{
				document = DocumentHelper.parseText("<"+NODE_ROOT+"></"+NODE_ROOT+">");
			}
			
		}
		
		return  document;
	}
	
	
	@SuppressWarnings("unchecked")
	private static List<MenuBean> elementToMenu(Element root, MenuBean parent) throws Exception{
		
		List<MenuBean> list = new ArrayList<MenuBean>();
		
		List<Element> menus = root.elements(NODE_MENU);
		
		if(null != menus && menus.size() > 0){
			
			for(Element menu : menus){
				
				MenuBean bean = new MenuBean();
				
				String title = menu.attributeValue(ATTR_TITLE);
				String code = menu.attributeValue(ATTR_CODE);
				String src = menu.attributeValue(ATTR_SRC);
				bean.setCode(code);
				bean.setTitle(title);
				bean.setSrc(src);
				
				if(null != menu.attribute(ATTR_HIDDEN)){
					bean.setHidden(Boolean.valueOf(menu.attributeValue(ATTR_HIDDEN)));
				}
				else{
					bean.setHidden(false);
				}
				
				if(null == bean.getNavigation()){
					bean.setNavigation(new ArrayList<String>());
				}
				
				if(null != parent){
					bean.setParentCode(root.attributeValue(ATTR_CODE));
					bean.getNavigation().addAll(parent.getNavigation());
				}
				bean.getNavigation().add(title);
				
				bean.setSubMenus(elementToMenu(menu, bean));
				
				list.add(bean);
				
				if(null != src){
					menuMap.put(src, bean);
				}
			}
		}
		
		return list;
	}
	
	public static MenuBean getMenuBeanBySrc(String src){
		
		if(null == menuMap || menuMap.isEmpty()){
			init();
        }
		
		return menuMap.get(src);
	}
}

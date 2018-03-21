package com.bestpay;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class Struts {


    //private final static Configuration cfg = new Configuration("struts.xml");

    private static ConfigParser configParser = new ConfigParser("src\\main\\resources\\struts.xml");

    public static View runAction(String actionName, Map<String, String> parameters) {

        /*
         
		0. 读取配置文件struts.xml
 		
 		1. 根据actionName找到相对应的class ， 例如LoginAction,   通过反射实例化（创建对象）
		据parameters中的数据，调用对象的setter方法， 例如parameters中的数据是 
		("name"="test" ,  "password"="1234") ,     	
		那就应该调用 setName和setPassword方法
		
		2. 通过反射调用对象的exectue 方法， 并获得返回值，例如"success"
		
		3. 通过反射找到对象的所有getter方法（例如 getMessage）,  
		通过反射来调用， 把值和属性形成一个HashMap , 例如 {"message":  "登录成功"} ,  
		放到View对象的parameters
		
		4. 根据struts.xml中的 <result> 配置,以及execute的返回值，  确定哪一个jsp，  
		放到View对象的jsp字段中。
        
        */



        //反射实例化对象并调用方法

        try {
            Class clazz = Class.forName(configParser.getClassName(actionName));
            Object o = clazz.newInstance();
            //调用所有的set方法
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String fieldName = entry.getKey();
                String fieldValue = entry.getValue();
                Method setMethod = clazz.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), String.class);
                setMethod.invoke(o, fieldValue);
            }
            //调用execute
            Method executeMethod = clazz.getMethod("execute");
            String resultName = (String) executeMethod.invoke(o);
            //调用所有的get方法
            Map<String, Object> returnParam = new HashMap<>();
            for (Method method : clazz.getMethods()) {
                if (method.getName().startsWith("get")) {
                    returnParam.put((method.getName().substring(3, 4).toLowerCase()) + method.getName().substring(4), method.invoke(o));
                }
            }

            View view = new View();
            view.setJsp(configParser.getResultView(actionName,resultName));
            view.setParameters(returnParam);
            return view;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;


    }

}

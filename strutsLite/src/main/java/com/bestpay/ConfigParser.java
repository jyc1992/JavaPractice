package com.bestpay;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author 蒋雨辰 on 2018-03-21.
 */
public class ConfigParser {

    private Map<String, ActionConfig> beanMap = new HashMap<>();

    public ConfigParser(String fileName) {

        SAXReader reader = new SAXReader();


        // 通过reader对象的read方法加载books.xml文件,获取document对象。

        Document document = null;
        try {
            document = reader.read(fileName);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        // 通过document对象获取根节点bookstore
        Element struts = document.getRootElement();
        // 通过element对象的elementIterator方法获取迭代器
        Iterator it = struts.elementIterator();
        while (it.hasNext()) {
            Element element = (Element) it.next();
            ActionConfig actionConfig = new ActionConfig(element.attribute("class").getValue());

            Iterator childItr = element.elementIterator();
            while (childItr.hasNext()) {
                Element childElement = (Element) childItr.next();
                actionConfig.result.put(childElement.attribute("name").getValue(), childElement.getStringValue());
            }

            beanMap.put(element.attribute("name").getValue(), actionConfig);

        }
    }

    public String getClassName(String action) {
        ActionConfig ac = this.beanMap.get(action);
        if (ac == null) {
            return null;
        }
        return ac.className;
    }

    public String getResultView(String action, String resultName) {
        ActionConfig ac = this.beanMap.get(action);
        if (ac == null) {
            return null;
        }
        return ac.result.get(resultName);

    }
}

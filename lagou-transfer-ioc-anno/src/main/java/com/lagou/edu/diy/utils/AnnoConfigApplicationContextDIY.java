package com.lagou.edu.diy.utils;


import com.lagou.edu.annocations.Autowired;
import com.lagou.edu.annocations.Service;
import com.lagou.edu.annocations.Transactional;
import com.lagou.edu.diy.factory.ProxyFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xulin
 * @date 2020/4/11 20:53
 * @description 自定义注解配置上下文工厂类
 */
public class AnnoConfigApplicationContextDIY {
    /**
     * 扫包范围
     */
    private String packageName;
    /**
     * 存放bean的容器
     */
    private static ConcurrentHashMap<String,Object> beans=null;



    public AnnoConfigApplicationContextDIY(String packageName) throws Exception {
        this.packageName = packageName;
        beans=new ConcurrentHashMap<String,Object>();//初始化容器
        initBean();

    }
    /**
     * 初始化对象
     * @throws Exception
     */
    public void initBean() throws Exception{
        //1 使用java的反射机制扫包 获取包下所有类
        List<Class<?>> classes= ClassUtil.getClasses(packageName);
        //2 判断类上面是否存在注入bean的注解
        ConcurrentHashMap<String,Object> classExistAnnotation= findClassExistAnnotation(classes);
        if(classExistAnnotation==null||classExistAnnotation.isEmpty()){
            throw	new Exception("该包下没有任何类使用需要扫描的注解");
        }

    }

    public ConcurrentHashMap<String,Object> findClassExistAnnotation(List<Class<?>> classes) throws IllegalAccessException, InstantiationException {
        for(Class<?> classInfo:classes){
            //只扫描固定注解的类到自定义容器中(自定义服务类），
           Service component= classInfo.getAnnotation(Service.class);
            if (component!=null){
                //bean id 默认类名首字母小写
                String className=classInfo.getSimpleName();
                String beanId=toLowerCaseFirstOne(className);
                //如果指定了value的值得话id按照实际value的值
                if (!"".equals(component.value()))
                {
                    beanId=component.value();
                }
                beans.put(beanId, classInfo.newInstance());
            }


        }
        //依赖注入
        for (String s : beans.keySet()) {
            Object obj=beans.get(s);
            Class clz=obj.getClass();
            //循环遍历所有属性，含有autowired注解的，依赖注入进入
           Field[] fields= clz.getDeclaredFields();
            for (Field field : fields) {
                if(field.isAnnotationPresent(Autowired.class))
                {
                    //从第一次加载进来的对象中注入--先不考虑泛型
                   Class clz_prop=field.getType();
                    for (String s1 : beans.keySet()) {
                        Object obj_item=beans.get(s1);
                        if(obj_item.getClass()==clz_prop||(obj_item.getClass().getInterfaces().length>0&&obj_item.getClass().getInterfaces()[0]==clz_prop))
                        {
                            //暴力访问
                            field.setAccessible(true);
                            field.set(obj,obj_item);
                        }

                    }
                }
            }
        }

        //事务增强aop--动态代理---容器内对象类含有事务注解的全部利用动态代理管理事务
        //依赖注入
        for (String s : beans.keySet()) {
            Object obj=beans.get(s);
            Class clz=obj.getClass();
            //循环遍历所有属性，含有transaction注解的，依赖注入进入
           if(clz.isAnnotationPresent(Transactional.class))
           {
               //1:从容器中获取代理工厂对象
               ProxyFactory pf= ((ProxyFactory)beans.get("proxyFactory"));
               //2：有接口--jdk动态代理，否则cglib
               if(clz.getInterfaces()!=null&&clz.getInterfaces().length>0)
               {
                   beans.put(s,pf.getJdkProxy(obj)) ;
               }
               else {
                   beans.put(s,pf.getCglibProxy(obj));
               }
           }
        }

        return beans;

    }

    public Object getBean(String beanId) throws Exception{
        if(beanId==null||beanId.isEmpty()){
            throw new Exception("bean id 不能为空!");
        }
        //使用容器获取bean
        Object classInfo=beans.get(beanId);
        if(classInfo==null){
            throw new Exception("class not foud!");
        }
        return classInfo;
    }

    // 首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

}

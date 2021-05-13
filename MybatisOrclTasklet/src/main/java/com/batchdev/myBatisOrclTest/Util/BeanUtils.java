package com.batchdev.myBatisOrclTest.Util;

import org.springframework.context.ApplicationContext;

public class BeanUtils {
//    public static Object getBean(String beanName) {
//        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
//        return applicationContext.getBean(beanName);
//    }

    public static Object getBean(Class<?> classType) {
        ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
        return applicationContext.getBean(classType);
    }

}

package com.ajaxproject.warehouse.beanpostprocessor

import com.ajaxproject.warehouse.annotation.RateLimit
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.cglib.proxy.Proxy
import org.springframework.stereotype.Component

@Component
class RateLimitAnnotationBeanPostProcessor : BeanPostProcessor { // todo

    val annotatedBeansMap: HashMap<String, Class<Any>> = HashMap()

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
        val beanClass = bean.javaClass
        if (beanClass.methods.any { it.isAnnotationPresent(RateLimit::class.java) }) {
            annotatedBeansMap[beanName] = beanClass
        }
        return bean
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        val beanClass = annotatedBeansMap[beanName] ?: return bean

        return Proxy.newProxyInstance(
            beanClass.classLoader,
            beanClass.interfaces,
            RateLimitInvocationHandler(bean, beanClass)
        )
    }
}

package com.ajaxproject.warehouse.beanpostprocessor

import com.ajaxproject.warehouse.annotation.RateLimit
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.cglib.proxy.Proxy
import org.springframework.stereotype.Component

@Component
class RateLimitAnnotationBeanPostProcessor : BeanPostProcessor {

    val annotatedBeansMap: MutableMap<String, Class<Any>> = mutableMapOf()

    val beanAnnotatedMethods: MutableMap<String, Map<String, Int>> = mutableMapOf()

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
        val beanClass = bean.javaClass

        val annotatedMethods: Map<String, Int> = beanClass.methods.asSequence()
            .filter { it.isAnnotationPresent(RateLimit::class.java) }
            .map { it.name to it.getAnnotation(RateLimit::class.java).value }
            .toMap()

        if (annotatedMethods.isNotEmpty()) {
            annotatedBeansMap[beanName] = beanClass
            beanAnnotatedMethods[beanName] = annotatedMethods
        }

        return bean
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        val beanClass = annotatedBeansMap[beanName] ?: return bean

        return Proxy.newProxyInstance(
            beanClass.classLoader,
            beanClass.interfaces,
            RateLimitInvocationHandler(
                bean,
                beanAnnotatedMethods[beanName] ?: mapOf()
            )
        )
    }
}

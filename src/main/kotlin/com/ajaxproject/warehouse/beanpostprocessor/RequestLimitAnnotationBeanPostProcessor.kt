package com.ajaxproject.warehouse.beanpostprocessor

import com.ajaxproject.warehouse.annotation.RequestLimit
import com.ajaxproject.warehouse.exception.MethodRequestLimitExceededException
import com.ajaxproject.warehouse.service.ProductService
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.cglib.proxy.InvocationHandler
import org.springframework.cglib.proxy.Proxy
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController
import java.lang.reflect.Method
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KClass

@Component
class RequestLimitAnnotationBeanPostProcessor : BeanPostProcessor {

    val annotatedBeansMap: HashMap<String, Class<Any>> = HashMap()

    override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any? {
        val beanClass = bean.javaClass
        if (beanClass.isAnnotationPresent(Service::class.java)) {   // TODO ask about interfaces and other
            if (beanClass.methods.any { it.isAnnotationPresent(RequestLimit::class.java) }) {
                annotatedBeansMap[beanName] = beanClass
            }
        }
        return bean
    }

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
        val beanClass = annotatedBeansMap[beanName] ?: return bean

        return Proxy.newProxyInstance(
            beanClass.classLoader,
            beanClass.interfaces,
            RequestLimitInvocationHandler(bean, beanClass)
        )
    }
}

class RequestLimitInvocationHandler(
    private val bean: Any,
    private val beanClass: Class<Any>
) : InvocationHandler {

    private val methodRequests: HashMap<String, AtomicInteger> = HashMap()

    @Suppress("SpreadOperator")
    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {

        val methodArguments = args ?: emptyArray()

        val methodName: String = method.name

        val containsAnnotation: Boolean = beanClass.methods.asSequence()
            .filter { it.name == methodName }
            .filter { it.isAnnotationPresent(RequestLimit::class.java) }
            .toList()
            .isNotEmpty()

        if (!containsAnnotation) {
            return method.invoke(bean, *methodArguments)
        }

        var methodRequestCounter: AtomicInteger? = methodRequests[methodName]
        if (methodRequestCounter == null) {
            methodRequestCounter = AtomicInteger(0)
            methodRequests[methodName] = methodRequestCounter
        }

        if (methodRequestCounter.get() >= METHOD_REQUEST_LIMIT) {
            throw MethodRequestLimitExceededException("Method $methodName is too busy now")
        }

        methodRequestCounter.incrementAndGet()
        val returnValue = method.invoke(bean, *methodArguments)
        methodRequestCounter.decrementAndGet()

        return returnValue
    }

    companion object {
        private const val METHOD_REQUEST_LIMIT: Int = 10
    }
}

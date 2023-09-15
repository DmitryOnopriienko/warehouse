package com.ajaxproject.warehouse.beanpostprocessor

import com.ajaxproject.warehouse.annotation.RateLimit
import com.ajaxproject.warehouse.exception.MethodRateLimitExceededException
import org.springframework.cglib.proxy.InvocationHandler
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class RateLimitInvocationHandler(
    private val bean: Any,
    private val beanClass: Class<Any>
) : InvocationHandler {

    private val methodRequests: ConcurrentHashMap<String, AtomicInteger> = ConcurrentHashMap()

    @Suppress("SpreadOperator")
    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {

        val methodArguments = args ?: emptyArray()

        val methodName: String = method.name
        // TODO make limit settable, smth like: (val value: Int = 10)

        val containsAnnotation: Boolean = beanClass.methods.any {
            it.name == methodName && it.isAnnotationPresent(RateLimit::class.java)
        }

        if (!containsAnnotation) {
            return method.invoke(bean, *methodArguments)
        }

        var methodRequestCounter: AtomicInteger? = methodRequests[methodName]
        if (methodRequestCounter == null) {
            methodRequestCounter = AtomicInteger(0)
            methodRequests[methodName] = methodRequestCounter
        }

        if (methodRequestCounter.get() >= METHOD_REQUEST_LIMIT) {
            throw MethodRateLimitExceededException("Method $methodName is too busy now")
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

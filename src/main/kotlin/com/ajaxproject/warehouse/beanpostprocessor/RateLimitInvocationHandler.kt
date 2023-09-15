package com.ajaxproject.warehouse.beanpostprocessor

import com.ajaxproject.warehouse.exception.MethodRateLimitExceededException
import org.springframework.cglib.proxy.InvocationHandler
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class RateLimitInvocationHandler(
    private val bean: Any,
    private val annotatedMethods: Map<String, Int>
) : InvocationHandler {

    private val methodRequests: ConcurrentHashMap<String, AtomicInteger> = ConcurrentHashMap()

    @Suppress("SpreadOperator")
    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any {
        val methodArguments = args ?: emptyArray()
        val methodName: String = method.name

        val limit: Int = annotatedMethods[methodName]
            ?: return method.invoke(bean, *methodArguments)

        var methodRequestCounter: AtomicInteger? = methodRequests[methodName]
        if (methodRequestCounter == null) {
            methodRequestCounter = AtomicInteger(0)
            methodRequests[methodName] = methodRequestCounter
        }

        if (methodRequestCounter.get() >= limit) {
            throw MethodRateLimitExceededException("Method $methodName is too busy now")
        }

        methodRequestCounter.incrementAndGet()
        val returnValue = method.invoke(bean, *methodArguments)
        methodRequestCounter.decrementAndGet()

        return returnValue
    }
}

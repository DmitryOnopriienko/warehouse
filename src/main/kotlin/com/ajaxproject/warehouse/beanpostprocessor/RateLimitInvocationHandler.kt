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

        val methodRequestCounter: AtomicInteger = methodRequests.computeIfAbsent(methodName) {
            AtomicInteger(0)
        }

        if (methodRequestCounter.get() >= limit) {
            throw MethodRateLimitExceededException("Method $methodName is too busy now")
        }

        return try {
            methodRequestCounter.incrementAndGet()
            method.invoke(bean, *methodArguments)
        } finally {
            methodRequestCounter.decrementAndGet()
        }
    }
}

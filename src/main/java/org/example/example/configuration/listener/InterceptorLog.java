package org.example.example.configuration.listener;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import jakarta.interceptor.Interceptor;
import jakarta.security.enterprise.SecurityContext;


@Log
@Interceptor
@Priority(1000)
public class InterceptorLog {
    @Inject
    private SecurityContext securityContext;

    @AroundInvoke
    public Object aroundInvoke(final InvocationContext invocationContext) throws Exception {
        String username = securityContext.getCallerPrincipal() != null ? securityContext.getCallerPrincipal().getName() : "ANONYMOUS";

        String methodName = invocationContext.getMethod().getName();
        String resourceTitle = "unknown";

        Object[] params = invocationContext.getParameters();
        if (params != null && params.length > 0) {
            try {
                Object param = params[0];
                resourceTitle = param.getClass().getMethod("getTitle").invoke(param).toString();
            } catch (Exception ignored){}
        }

        System.out.println("User: " + username + " Method: " + methodName + " Resource title: " + resourceTitle);

        return invocationContext.proceed();
    }
}

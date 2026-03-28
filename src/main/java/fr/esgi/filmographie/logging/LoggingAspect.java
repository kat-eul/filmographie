package fr.esgi.filmographie.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static fr.esgi.filmographie.logging.LoggingUtils.getClassName;
import static fr.esgi.filmographie.logging.LoggingUtils.isController;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingAspect.class);

    private final long slowThresholdMs;

    public LoggingAspect(@Value("${logging.aspect.slow-threshold-ms:200}") long slowThresholdMs) {
        this.slowThresholdMs = slowThresholdMs;
    }

    private static String threadTag() {
        return "Thread[" + Thread.currentThread().threadId() + "]";
    }

    @Around("(within(fr.esgi.filmographie..*) && ("
            + "within(@org.springframework.web.bind.annotation.RestController *)"
            + " || within(@org.springframework.stereotype.Service *)"
            + " || within(@org.springframework.stereotype.Repository *)"
            + ")"
            + " && !execution(* get*())"
            + " && !execution(boolean is*())"
            + " && !execution(java.lang.Boolean is*())"
            + " && !execution(* set*(*))"
            + " && !execution(* getClass())"
            + ")")
    public Object logAround(final ProceedingJoinPoint pjp) throws Throwable {
        final Signature sig = pjp.getSignature();
        final Object target = pjp.getTarget();
        
        final String className = getClassName(target, sig);
        final String methodName = sig.getName();
        final boolean isController = isController(target);
        final long startNs = System.nanoTime();
        Throwable thrown = null;

        try {
            logBeforeMethod(pjp, isController, className, methodName);
            return pjp.proceed();
        } catch (Throwable t) {
            thrown = t;
            throw t;
        } finally {
            logAfterMethod(startNs, thrown, className, methodName, isController);
        }
    }

    private static void logBeforeMethod(ProceedingJoinPoint pjp, boolean isController, String className, String methodName) {
        if (isController) {
            LOG.info("{} http-in class={} method={}", threadTag(), className, methodName);
        } else if (LOG.isDebugEnabled()) {
            LOG.debug("{} start class={} method={} args={}",
                    threadTag(), className, methodName, Arrays.toString(pjp.getArgs()));
        }
    }

    private void logAfterMethod(long startNs, Throwable thrown, String className, String methodName, boolean isController) {
        final long tookMs = (System.nanoTime() - startNs) / 1_000_000;

        if (thrown != null) {
            LOG.error("{} error class={} method={} tookMs={} exType={}",
                    threadTag(), className, methodName, tookMs, thrown.getClass().getSimpleName(), thrown);
        } else if (tookMs >= slowThresholdMs) {
            LOG.warn("{} slow class={} method={} tookMs={}",
                    threadTag(), className, methodName, tookMs);
        } else if (isController) {
            LOG.info("{} http-out class={} method={} tookMs={}",
                    threadTag(), className, methodName, tookMs);
        } else if (LOG.isDebugEnabled()) {
            LOG.debug("{} end class={} method={} tookMs={}",
                    threadTag(), className, methodName, tookMs);
        }
    }
}
package fr.esgi.filmographie.logging;

import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;

public class LoggingUtils {
    public static String getClassName(Object target, Signature sig) {
        final Class<?> targetClass = (target != null) ? AopUtils.getTargetClass(target) : null;

        final String className;
        if (targetClass != null) {
            className = targetClass.getSimpleName();
        } else if (sig instanceof MethodSignature ms) {
            className = ms.getDeclaringType().getSimpleName();
        } else {
            className = sig.getDeclaringTypeName();
        }
        return className;
    }

     public static boolean isController(Object target) {
         if (target == null) return false;
         return AopUtils.getTargetClass(target)
                 .isAnnotationPresent(
                         org.springframework.web.bind.annotation.RestController.class
                 );
     }
}

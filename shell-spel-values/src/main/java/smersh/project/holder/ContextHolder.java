package smersh.project.holder;

import org.springframework.expression.EvaluationContext;

/**
 * ContextHolder для хранения EvaluationContext в потоке и работе с единым контекстом в разных методах
 */
public class ContextHolder {

    private static final ThreadLocal<EvaluationContext> CONTEXT_HOLDER = new ThreadLocal<>();

    public static void setContext(EvaluationContext context) {
        CONTEXT_HOLDER.set(context);
    }

    public static EvaluationContext getContext() {
        return CONTEXT_HOLDER.get();
    }

    public static void clearContext() {
        CONTEXT_HOLDER.remove();
    }
}

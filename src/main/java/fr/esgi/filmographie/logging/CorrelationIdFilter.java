package fr.esgi.filmographie.logging;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationIdFilter extends OncePerRequestFilter {

    public static final String MDC_KEY = "correlationId";
    public static final String HEADER = "X-Correlation-Id";

    private static final int MAX_LEN = 64;
    private static final Pattern ALLOWED = Pattern.compile("^[A-Za-z0-9\\-]{1," + MAX_LEN + "}$");

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String incoming = request.getHeader(HEADER);

        String correlationId = Optional.ofNullable(incoming)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .filter(s -> s.length() <= MAX_LEN)
                .filter(s -> ALLOWED.matcher(s).matches())
                .orElse(UUID.randomUUID().toString());

        MDC.put(MDC_KEY, correlationId);
        if (!response.containsHeader(HEADER)) {
            response.setHeader(HEADER, correlationId);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_KEY);
        }
    }
}
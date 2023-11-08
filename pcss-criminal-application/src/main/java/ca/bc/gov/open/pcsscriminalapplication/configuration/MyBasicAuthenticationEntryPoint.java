package ca.bc.gov.open.pcsscriminalapplication.configuration;

import ca.bc.gov.open.pcsscriminalapplication.properties.SecurityProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableConfigurationProperties(SecurityProperties.class)
public class MyBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public MyBasicAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authEx)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        Map<String, Object> resp = new HashMap<>();
        resp.put("path", request.getRequestURI());
        resp.put("error", "Unauthorized");
        resp.put("status", HttpStatus.UNAUTHORIZED.value());
        resp.put("timestamp", Instant.now().toString());

        log.warn("Unauthorized request to the api received");
        writer.println(objectMapper.writeValueAsString(resp));
    }

    @Override
    public void afterPropertiesSet() {
        setRealmName("PCSS");
        super.afterPropertiesSet();
    }
}

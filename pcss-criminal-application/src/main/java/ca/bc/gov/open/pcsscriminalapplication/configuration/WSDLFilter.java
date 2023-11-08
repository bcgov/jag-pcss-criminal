package ca.bc.gov.open.pcsscriminalapplication.configuration;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class WSDLFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if ("wsdl".equalsIgnoreCase(httpRequest.getQueryString())) {
            HttpServletRequestWrapper requestWrapper =
                    new HttpServletRequestWrapper(httpRequest) {
                        @Override
                        public String getQueryString() {
                            return null;
                        }

                        @Override
                        public String getRequestURI() {
                            return super.getRequestURI() + ".wsdl";
                        }
                    };
            chain.doFilter(requestWrapper, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}

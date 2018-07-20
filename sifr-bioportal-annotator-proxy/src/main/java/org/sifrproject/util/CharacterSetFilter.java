package org.sifrproject.util;

import javax.servlet.*;
import java.io.IOException;

public class CharacterSetFilter implements Filter {

    // ...

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {

    }

    @SuppressWarnings("HardcodedFileSeparator")
    @Override
    public void doFilter(
            final ServletRequest request,
            final ServletResponse response,
            final FilterChain next) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        next.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    // ...
}

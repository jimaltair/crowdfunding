package ru.pcs.crowdfunding.client.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ExceptionInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, @Nullable Exception ex) throws Exception {
        if (ex != null) {
            logException(request, ex);
            writeResponse(response, ex);
        }
    }

    private void logException(HttpServletRequest request, Exception ex) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String requestString = method + " " + uri + ((query != null) ? (" " + query) : "");
        log.error("Exception while processing request: {}", requestString, ex);
    }

    private void writeResponse(HttpServletResponse response, Exception ex) throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.getWriter().println("<html>");

        response.getWriter().println("<head><title>Ошибка</title></head>");

        response.getWriter().println("<body>");
        response.getWriter().println("<h1>Ой, что-то пошло не так</h1>");
        response.getWriter().println("<p>Ошибка:</p>");
        response.getWriter().println("<p>" + ex.getMessage() + "</p>");
        response.getWriter().println("<img src=\"/img/crying_cat.jpg\" style=\"max-width: 512px; max-height: 512px; width: auto; height: auto;\"/>");
        response.getWriter().println("</body>");

        response.getWriter().println("</html>");
    }
}

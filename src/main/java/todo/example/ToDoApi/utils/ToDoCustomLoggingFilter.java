package todo.example.ToDoApi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import todo.example.ToDoApi.ToDoApiApplication;
import javax.servlet.http.HttpServletRequest;

public class ToDoCustomLoggingFilter extends CommonsRequestLoggingFilter {

    // SL4J logger
    Logger logger = LoggerFactory.getLogger(ToDoApiApplication.class);

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        super.beforeRequest(request, message);
        /**
         * Here we can log a request BEFORE processing....
         * We can also do some security checks and/or validations
         */

        // Will persist some metrics here
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        super.afterRequest(request, message);

        long startTime = (Long)request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long execTime = endTime - startTime;

        log(startTime, endTime, execTime);
    }

    private void log(long startTime, long endTime, long execTime) {
        logger.info("startTime: {}, endTime: {}, totalTime: {} ms", startTime, endTime, execTime);
    }
}
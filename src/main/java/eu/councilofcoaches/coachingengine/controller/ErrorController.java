package eu.councilofcoaches.coachingengine.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.councilofcoaches.coachingengine.exception.HttpException;
import eu.woolplatform.utils.AppComponents;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This controller handles all errors in the application. It looks at the
 * request attributes for a status code, error message and exception. It
 * distinguishes the following cases:
 * 
 * <p><ul>
 * <li>The request has no exception. It sets the status code and returns the
 * error message or an empty string.</li>
 * <li>The exception has a {@link ResponseStatus ResponseStatus} annotation,
 * such as a {@link HttpException HttpException}. It sets the status code and
 * returns the exception message.</li>
 * <li>The exception is a {@link ServletException ServletException}, which is
 * usually the result of Spring validation. It sets the status code and
 * returns the error message.</li>
 * <li>Any other exception. It logs the exception, sets the status code to
 * 500 Internal Server Error and returns "Internal Server Error". This means
 * that any details about the exception, which may contain sensitive
 * information, are hidden for the client.</li>
 * </ul></p>
 * 
 * @author Dennis Hofs (RRD)
 */
@RestController
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
	private static final String LOGTAG = ErrorController.class.getSimpleName();

	@RequestMapping("/error")
	public Object error(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Logger logger = AppComponents.getLogger(LOGTAG);
		int statusCode = (Integer)request.getAttribute(
				"javax.servlet.error.status_code");
		Object obj = request.getAttribute(
				"org.springframework.web.servlet.DispatcherServlet.EXCEPTION");
		Throwable exception = null;
		if (obj instanceof Throwable)
			exception = (Throwable)obj;
		String message = (String)request.getAttribute(
				"javax.servlet.error.message");
		if (message == null)
			message = "";
		if (exception == null) {
			logger.error("Error " + statusCode + ": " + message);
			response.setStatus(statusCode);
			return "Unknown error";
		}
		ResponseStatus status = exception.getClass().getAnnotation(
				ResponseStatus.class);
		if (status != null && exception instanceof HttpException) {
			HttpException httpEx = (HttpException)exception;
			response.setStatus(status.value().value());
			return httpEx.getError();
		} else if (status != null) {
			response.setStatus(status.value().value());
			return exception.getMessage();
		} else if (exception instanceof ServletException) {
			response.setStatus(statusCode);
			return message;
		}
		Throwable cause = null;
		if (exception instanceof HttpMessageNotReadableException) {
			cause = exception.getCause();
		}
		if (cause instanceof JsonProcessingException) {
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			return cause.getMessage();
		} else {
			String msg = "Internal Server Error";
			logger.error(msg + ": " + exception.getMessage(), exception);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return msg;
		}
	}

	@Override
	public String getErrorPath() {
		return "/error";
	}
}

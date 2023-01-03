package com.limbo.light.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/**
 * All Http Post data will save in InputStream of ServletRequest object base on tomcat.
 * SpringMVC 针对不同的Content-Type, InputStream会被封装进行特殊处理，如：
 *  1. QueryString no need to process.
 *  2. form-data  get body by request.getParameter() (InputStream convert to)
 *  3. json get body by request.getInputStream() or getReader()
 *
 *  Problems: 在拦截器或Controller中重复读取参数时出现异常(null, or HttpMessageNotReadable, or can't be called after getReader())
 *  Why: 由于InputStream流数据特征：内部通过指针移动来读取字节，当读完一遍后，指针不会reset，导致下次读时出问题
 *  Resolve: 将InputStream流数据缓存起来
 *
 * @author limbo
 * @since 2022/10/21 11:11
 */
@Slf4j
@WebFilter(urlPatterns = "/*")
public class CachingContentFilter implements Filter {

    private static final String FORM_CONTENT_TYPE = MediaType.MULTIPART_FORM_DATA_VALUE;

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            String contentType = request.getContentType();
            if (contentType != null && contentType.contains(FORM_CONTENT_TYPE)) {
                filterChain.doFilter(request, response);
            } else {
                ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
                filterChain.doFilter(requestWrapper, response);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }

    public static String getRequestBody(HttpServletRequest request) {
        String reqBody = StringUtils.EMPTY;
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (Objects.nonNull(wrapper)) {
            reqBody = IOUtils.toString(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
        }
        return reqBody;
    }
}

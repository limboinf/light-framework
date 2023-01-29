package com.limbo.light.web.api;

import cn.hutool.core.date.DateUtil;
import org.springframework.beans.PropertyValuesEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.Date;

/**
 * 通用web基类Controller
 *
 * @author limbo
 * @date 2023/01/29
 */
public class BaseController extends ApiController {

    /**
     * 解决前端传字符串转date问题
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new PropertyValuesEditor() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(DateUtil.parse(text));
            }
        });
    }

}

package com.limbo.light.core.test.enhance;

import com.limbo.light.core.enhance.SpELProcessor;
import com.limbo.light.core.enhance.impl.DefaultSpELProcessor;
import com.limbo.light.core.test.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * SpEL解析器单测
 *
 * @author limbo
 * @since 2022/12/28 16:30
 */
class SpELProcessorTest {

    @Test
    @DisplayName("测试简单case")
    public void testSimple() {
        // 变量定义通过EvaluationContext接口的setVariable(variableName, value)方法定义；
        // 在表达式中使用"#variableName"引用
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        standardEvaluationContext.setVariable("name", "limbo");
        DefaultSpELProcessor<String> defaultSpELProcessor = new DefaultSpELProcessor<>(standardEvaluationContext);
        String res = defaultSpELProcessor.parse("#name", String.class);
        System.out.println(res);
        Assertions.assertEquals("limbo", res);
    }

    @Test
    @DisplayName("测试boolean case")
    public void testSimple2() {
        Boolean res = SpELProcessor.PARSER.parseExpression("2>1 && 3>2").getValue(boolean.class);
        Assertions.assertEquals(Boolean.TRUE, res);
    }

    @Test
    @DisplayName("测试简单case")
    public void testSimple3() {
        // 引用根对象及当前上下文对象，使用"#root"引用根对象，使用"#this"引用当前上下文对象；
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext(100);
        DefaultSpELProcessor<Integer> defaultSpELProcessor = new DefaultSpELProcessor<>(standardEvaluationContext);
        int res1 = defaultSpELProcessor.parse("#root", int.class);
        int res2 = defaultSpELProcessor.parse("#this", int.class);
        System.out.println(res1);
        System.out.println(res2);
        Assertions.assertEquals(100, res1);
        Assertions.assertEquals(100, res2);
    }

    @Test
    @DisplayName("测试 bean引用")
    public void testSimple4() {
        // SpEL支持使用“@”符号来引用Bean
        User user = new User(100, "limbo");
        // 注册bean
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton("limboUser", user);
        // BeanResolver接口实现来查找Bean
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        standardEvaluationContext.setBeanResolver(new BeanFactoryResolver(beanFactory));

        DefaultSpELProcessor<User> defaultSpELProcessor = new DefaultSpELProcessor<>(standardEvaluationContext);
        User res = defaultSpELProcessor.parse("@limboUser", User.class);
        System.out.println(res);
        Assertions.assertEquals(user, res);
    }

    @Test
    @DisplayName("MethodBasedEvaluationContext测试")
    public void testSimple5() throws NoSuchMethodException {
        Method method = this.getClass().getDeclaredMethod("m1", Integer.class, User.class);
        Object[] parameterValues = new Object[]{100, new User(1, "limbo")};

        // MethodBasedEvaluationContext
        // EvaluationContext context = new MethodBasedEvaluationContext(null, method, parameterValues, SpELProcessor.NAME_DISCOVERER);
        EvaluationContext context = new MethodBasedEvaluationContext("根对象abc", method, parameterValues, SpELProcessor.NAME_DISCOVERER);

        // 解析方法参数值 id
        DefaultSpELProcessor<Integer> defaultSpELProcessor = new DefaultSpELProcessor<>(context);
        Integer id = defaultSpELProcessor.parse("#id", int.class);
        System.out.println(id);
        Assertions.assertEquals(100, id);

        // 解析方法参数值 User对象实例
        DefaultSpELProcessor<User> defaultSpELProcessor2 = new DefaultSpELProcessor<>(context);
        User user = defaultSpELProcessor2.parse("#user", User.class);
        System.out.println(user);
        Assertions.assertNotNull(user);

        // 解解析方法参数值 User对象实例的属性
        DefaultSpELProcessor<String> defaultSpELProcessor3 = new DefaultSpELProcessor<>(context);
        // 使用安全访问符号?.，可以规避null错误
        String userName = defaultSpELProcessor3.parse("#user?.name", String.class);
        System.out.println(userName);
        Assertions.assertEquals("limbo", userName);

        // 根对象
        DefaultSpELProcessor<String> defaultSpELProcessor4 = new DefaultSpELProcessor<>(context);
        String res1 = defaultSpELProcessor4.parse("#root", String.class);
        String res2 = defaultSpELProcessor4.parse("#this", String.class);
        System.out.println(res1);
        System.out.println(res2);
        Assertions.assertEquals("根对象abc", res1);
        Assertions.assertEquals("根对象abc", res2);
    }

    private void m1(Integer id, User user) {

    }

}

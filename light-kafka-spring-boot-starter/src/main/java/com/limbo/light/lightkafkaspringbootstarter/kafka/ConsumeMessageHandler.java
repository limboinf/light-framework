package com.limbo.light.lightkafkaspringbootstarter.kafka;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

/**
 * 具体消费业务处理接口
 *
 * @author limbo
 * @since 2022/12/6 11:52
 */
public interface ConsumeMessageHandler<T> {

    /**
     * 消息单条处理
     *
     * @param message 消息
     */
    default void process(T message) {

    }

    /**
     * 消息批量处理
     *
     * @param messageList 消息列表
     */
    default void processBatch(List<T> messageList) {

    }

    Validator validator = Validation.byProvider(HibernateValidator.class)
                .configure()
                .failFast(true) // 快速失败，一旦校验字段有问题则失败
                .buildValidatorFactory().getValidator();

    default void checkValidate(T message) {
        Set<ConstraintViolation<T>> validate = validator.validate(message);
        // 如果校验通过，validate为空；否则，validate包含未校验通过项
        if (!validate.isEmpty()) {
            StringBuilder sb = new StringBuilder("校验失败");
            for (ConstraintViolation<T> constraintViolation : validate) {
                sb.append(constraintViolation.getPropertyPath().toString())
                        .append(": ").append(constraintViolation.getMessage());
            }

            throw new ValidationException(String.format("event valid fail, msg: %s", sb));
        }
    }
}

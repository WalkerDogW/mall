package site.javaee.mall.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author shkstart
 * @create 2020-09-10 22:02
 */
@Constraint(validatedBy = {ListValueConstraintValidator.class})
@Target({FIELD})
@Retention(RUNTIME)
public @interface ListValue {
    String message() default "{site.javaee.mall.common.valid.ListValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] vals() default {};
}

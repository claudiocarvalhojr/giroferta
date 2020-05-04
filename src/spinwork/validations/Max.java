package spinwork.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Max {
	int value();
	String message() default "Quantidade de dígitos/caracteres esta acima do limite máximo para o campo %s!";
}

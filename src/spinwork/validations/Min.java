package spinwork.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Min {
	int value();
	String message() default "Quantidade de dígitos/caracteres esta abaixo do limite mínimo para o campo %s!";
}

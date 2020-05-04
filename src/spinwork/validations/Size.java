package spinwork.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Size {
	int min();
	int max();
	String message() default "Quantidade de d�gitos/caracteres esta fora do intervalo definido para o campo %s!";
}

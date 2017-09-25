package br.com.wcf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldReader {

	public enum ListLocation {
		/**
		 * Indica que a lista esta contida no posicional. Neste caso, o tamanho
		 * da lista deve ser conhecido
		 * <p>
		 * e o parâmetro <b>listSize</b> deve ser informado.
		 */
		INNER,

		/**
		 * Indica que a lista será obtida através do Map. Neste caso, o tamanho
		 * da lista não é conhecido.
		 * <p>
		 * e o parâmetro <b>listKey</b> deve ser informado.
		 */
		OUTER;
	}

	int length() default 0;

	int precision() default 0;

	boolean calculate() default false;

	String datePattern() default "dd/MM/yyyy HH:mm:ss";

	ListLocation listLocation() default ListLocation.OUTER;

	Class<?> listType() default Object.class;

	int listSize() default 0;
	
	String listKey() default "";
}

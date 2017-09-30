package br.com.wcf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldReader {

	public enum DataLocation {
		/**
		 * Indica que os dados est�o contidos no posicional. No caso de uma
		 * lista, seu tamanho deve ser conhecido
		 * <p>
		 * e o par�metro <b>listSize</b> deve ser informado.
		 */
		INNER,

		/**
		 * Indica que os dados devem ser obtidos a partir do Map e o par�metro
		 * <b>dataKey</b> deve ser informado.. No caso de uma lista, o tamanho
		 * n�o � conhecido.
		 */
		OUTER,
		
		/**
		 * Valor default indicando que os dados n�o foram fornecidos.
		 */
		NONE;
	}

	int length() default 0;

	int precision() default 0;

	String datePattern() default "dd/MM/yyyy HH:mm:ss";

	DataLocation dataLocation() default DataLocation.NONE;

	Class<?> listType() default Object.class;

	int listSize() default 0;

	String dataKey() default "";
}

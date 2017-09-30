package br.com.wcf.reader;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.wcf.annotation.FieldReader;
import br.com.wcf.exception.PositionalReaderException;

/**
 * Classe que realiza a conversão de uma linha posicional.
 * 
 * @author willcf
 *
 */
public class PositionalReader {

	/**
	 * Converte uma linha posicional em objeto.
	 * 
	 * @param classType
	 *            - Tipo de classe a ser convertida.
	 * @param line
	 *            - Linha do posicional.
	 * @param data
	 *            - Map com as outras linhas ou lista de linhas a serem
	 *            convertidas para o classType informado.
	 * @return
	 * @throws Exception
	 */
	public <E> E parse(Class<E> classType, String line, Map<String, Object> data) throws Exception {
		return this.reader(classType, line, data);
	}

	public <E> E parse(Class<E> classType, String line) throws Exception {
		return this.reader(classType, line, null);
	}

	private <E> E reader(Class<E> classType, String line, Map<String, Object> data) throws Exception {
		E object = classType.newInstance();
		for (Field field : classType.getDeclaredFields()) {

			if (!field.isAnnotationPresent(FieldReader.class)) {
				continue;
			}

			FieldReader annotation = field.getAnnotation(FieldReader.class);
			int length = annotation.length();
			if (annotation.dataLocation().equals(FieldReader.DataLocation.INNER)) {
				length = this.calculate(field, annotation);
			}

			if (!(line.length() >= length)) {
				throw new PositionalReaderException(String.format("Fim prematuro da linha. Class: %s Field: %s",
						classType.getName(), field.getName()));
			}
			String aux = line.substring(0, length);
			Object value = this.parseField(field.getType(), aux, annotation, data, field);
			this.setInternalField(field.getName(), object, value);

			line = line.substring(length);
		}

		return (E) object;
	}

	@SuppressWarnings("unchecked")
	private <E> E parseField(Class<E> type, String value, FieldReader annotation, Map<String, Object> data, Field field)
			throws Exception {

		if (type.equals(String.class)) {
			return value.isEmpty() ? null : (E) value.trim();
		}

		if (type.equals(Integer.class)) {
			try {
				return value.isEmpty() ? null : (E) new Integer(value);
			} catch (Exception e) {
				throw new PositionalReaderException(String.format("Erro ao converter %s em Integer.", value));
			}
		}

		if (type.equals(Long.class)) {
			try {
				return value.isEmpty() ? null : (E) new Long(value);
			} catch (Exception e) {
				throw new PositionalReaderException(String.format("Erro ao converter %s em Long.", value));
			}
		}

		if (type.equals(Double.class)) {
			try {

				if (value.isEmpty()) {
					return null;
				}

				String aux = value.substring(0, annotation.precision());
				String aux2 = value.substring(annotation.precision());
				aux = aux + "." + aux2;
				return (E) new Double(aux);
			} catch (Exception e) {
				throw new PositionalReaderException(String.format("Erro ao converter %s em Double.", value));
			}
		}

		if (type.equals(Date.class)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(annotation.datePattern());
				return value.isEmpty() ? null : (E) sdf.parse(value);
			} catch (Exception e) {
				throw new PositionalReaderException(String.format("Erro ao converter %s em Date.", value));
			}
		}

		if (type.equals(Boolean.class)) {
			try {
				return value.isEmpty() ? null : (E) new Boolean(value);
			} catch (Exception e) {
				throw new PositionalReaderException(String.format("Erro ao converter %s em Boolean.", value));
			}
		}

		if (type.equals(List.class)) {
			if (annotation.listType().equals(Object.class)) {
				throw new PositionalReaderException(
						String.format("Tipo de lista não definido para o atributo %s.", field.getName()));
			}

			if (annotation.dataLocation().equals(FieldReader.DataLocation.INNER)) {
				return (E) this.parseInnerList(annotation.listType(), annotation, value);
			}

			if (data != null) {
				return (E) this.parseOuterList(annotation.listType(), annotation,
						(List<String>) data.get(annotation.dataKey()));
			}
		}

		if (annotation.dataLocation().equals(FieldReader.DataLocation.INNER)) {
			return this.reader(type, value, data);
		}

		if (annotation.dataLocation().equals(FieldReader.DataLocation.OUTER)) {
			return this.reader(type, (String) data.get(annotation.dataKey()), data);
		}

		throw new PositionalReaderException(
				String.format("Tipo de dado não definido para o atributo %s.", field.getName()));
	}

	@SuppressWarnings("unchecked")
	private <E> List<E> parseInnerList(Class<?> classType, FieldReader annotation, String line) throws Exception {
		if (line.isEmpty()) {
			return null;
		}

		List<E> list = new ArrayList<>();
		for (int i = 0; i < annotation.listSize(); i++) {
			list.add((E) this.reader(classType, line, null));
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	private <E> List<E> parseOuterList(Class<?> classType, FieldReader annotation, List<String> data) throws Exception {

		if (!(data != null && !data.isEmpty())) {
			return null;
		}

		List<E> list = new ArrayList<>();
		for (String line : data) {
			list.add((E) this.reader(classType, line, null));
		}

		return list;

	}

	private int calculate(Field field, FieldReader annotation) throws Exception {

		if (field.getType().equals(List.class) && annotation.dataLocation().equals(FieldReader.DataLocation.INNER)) {
			if (annotation.listSize() == 0) {
				throw new PositionalReaderException(
						String.format("Tamanho da lista não definido para o atributo %s.", field.getName()));
			}

			return this.calculate(annotation.listType()) * annotation.listSize();
		}

		return this.calculate(field.getType());
	}

	private int calculate(Class<?> classType) throws Exception {
		try {
			Field[] fields = classType.getDeclaredFields();
			int length = 0;
			for (Field field : fields) {
				if (!field.isAnnotationPresent(FieldReader.class)) {
					continue;
				}

				FieldReader annotation = field.getAnnotation(FieldReader.class);
				if (annotation.dataLocation().equals(FieldReader.DataLocation.INNER)) {
					length += this.calculate(field.getClass());
					continue;
				}

				length += annotation.length();
			}
			return length;
		} catch (Exception e) {
			throw e;
		}
	}

	private void setInternalField(final String fieldName, final Object target, final Object value) throws Exception {
		try {
			AccessController.doPrivileged(new PrivilegedAction<Object>() {
				public Object run() {
					Object result = null;
					java.lang.reflect.Field field = null;
					try {
						field = target.getClass().getDeclaredField(fieldName);
						field.setAccessible(true);
						field.set(target, value);
					} catch (Exception e1) {
						e1.printStackTrace();
						return null;
					}
					return result;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
}

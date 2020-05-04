package spinwork;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import connection.Connection;
import spinwork.util.Utils;
import spinwork.util.Validations;

public final class Manager {

	public static Object setObject(Object object, HttpServletRequest request, Connection connection) {
		
		boolean isValid = true;
		ArrayList<String> messages = new ArrayList<String>();
		String nameOfObject = object.getClass().getSimpleName().substring(0, object.getClass().getSimpleName().length() -1).toLowerCase();
		Map<String, String[]> methodsAndValue = new HashMap<String, String[]>();
		Map<String, String[]> parameters = request.getParameterMap();
		Utils util = new Utils();
		
		for (Map.Entry<String, String[]> e : parameters.entrySet()) {
			if (e.getKey().startsWith(nameOfObject) && e.getKey().contains(".")) {
				String[] aux = e.getKey().split("\\.");
				String nameOfAttribute = aux[aux.length-1];
				String nameOfMethod = "set" + util.firstLetterUppercase(nameOfAttribute);
				methodsAndValue.put(nameOfMethod, e.getValue());
			}
		}
		
		for (Method method : object.getClass().getDeclaredMethods()) {
			if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
				for (Class<?> paramType : method.getParameterTypes()) {
					
//					System.out.println("Método: " + method.getName() + " - Tipo: " + paramType.getSimpleName());
					
					for (Map.Entry<String, String[]> map : methodsAndValue.entrySet()) {
						if (method.getName().compareTo(map.getKey()) == 0) {
							try {
								// STRING
								if (paramType.getSimpleName().equals("String")) {
									Field field = object.getClass().getDeclaredField(map.getKey().substring(3, map.getKey().length()).toLowerCase());
									Annotation[] annotations = field.getAnnotations();
									boolean isChecked = true;
									for (Annotation annotation : annotations) {
										if (!map.getValue()[0].equals("") && map.getValue()[0] != null) {
											// MIN
											if (annotation.annotationType().getSimpleName().equals("Min")) {
												String message = "";
												for (Method met : annotation.annotationType().getMethods()) {
													if (met.getName().equals("value")) {
														int size = (Integer) met.invoke(annotation);
														if (map.getValue()[0].length() < size) {
															isValid = false;
															isChecked = false;
														}
													}
													else if (met.getName().equals("message")) {														
														message = met.invoke(annotation).toString();
														if (!isChecked)
															messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
													}
												}	
											}
											// MAX
											if (annotation.annotationType().getSimpleName().equals("Max")) {
												String message = "";
												for (Method met : annotation.annotationType().getMethods()) {
													if (met.getName().equals("value")) {
														int size = (Integer) met.invoke(annotation);
														if (map.getValue()[0].length() > size) {
															isValid = false;
															isChecked = false;
														}
													}
													else if (met.getName().equals("message")) {														
														message = met.invoke(annotation).toString();
														if (!isChecked)
															messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
													}
												}	
											}
											// SIZE
											if (annotation.annotationType().getSimpleName().equals("Size")) {
												int min = 0;
												int max = 0;
												String message = "";
												for (Method met : annotation.annotationType().getMethods()) {
													if (met.getName().equals("min")) {
														min = (Integer) met.invoke(annotation);
														if (map.getValue()[0].length() < min) {
															isValid = false;
															isChecked = false;
														}
													}
													else if (met.getName().equals("max")) {
														max = (Integer) met.invoke(annotation);
														if (map.getValue()[0].length() > max) {
															isValid = false;
															isChecked = false;
														}
													}
													else if (met.getName().equals("message")) {														
														message = met.invoke(annotation).toString();
														if (!isChecked)
															messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
													}
												}
											}
											if (isChecked) {
												Validations validations = new Validations();
												// CPF
												if (annotation.annotationType().getSimpleName().equals("Cpf")) {
													isChecked = validations.isCpf(map.getValue()[0]);
													if (!isChecked) {
												        String message = "";
														for (Method met : annotation.annotationType().getMethods()) {
															if (met.getName().equals("message"))
																message = met.invoke(annotation).toString();
														}
														messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
														isValid = false;
													}
												}
												// CNPJ
												else if (annotation.annotationType().getSimpleName().equals("Cnpj")) {
													isChecked = validations.isCnpj(map.getValue()[0]);
													if (!isChecked) {
												        String message = "";
														for (Method met : annotation.annotationType().getMethods()) {
															if (met.getName().equals("message"))
																message = met.invoke(annotation).toString();
														}
														messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
														isValid = false;
													}
												}
												// EMAIL
												else if (annotation.annotationType().getSimpleName().equals("Email")) {
													isChecked = validations.isEmail(map.getValue()[0]);
													if (!isChecked) {
												        String message = "";
														for (Method met : annotation.annotationType().getMethods()) {
															if (met.getName().equals("message"))
																message = met.invoke(annotation).toString();
														}
														messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
														isValid = false;
													}
												}
												// URL
												else if (annotation.annotationType().getSimpleName().equals("Url")) {
													isChecked = validations.isUrl(map.getValue()[0]);
													if (!isChecked) {
												        String message = "";
														for (Method met : annotation.annotationType().getMethods()) {
															if (met.getName().equals("message"))
																message = met.invoke(annotation).toString();
														}
														messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
														isValid = false;
													}
												}
												// PASSWORD
												else if (annotation.annotationType().getSimpleName().equals("Password")) {
													isChecked = validations.isPassword(map.getValue()[0]);
													if (!isChecked) {
												        String message = "";
														for (Method met : annotation.annotationType().getMethods()) {
															if (met.getName().equals("message"))
																message = met.invoke(annotation).toString();
														}
														messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
														isValid = false;
													}
												}
												if (isChecked) {
													// Compare
													if (annotation.annotationType().getSimpleName().equals("Compare")) {
														String message = "";
														String compareField = "";
														for (Method met : annotation.annotationType().getMethods()) {
															if (met.getName().equals("field")) {
																compareField = met.invoke(annotation).toString();
																Field[] fields = object.getClass().getDeclaredFields();
																for (Field fieldAux : fields) {
																	if (compareField.compareTo(fieldAux.getName()) == 0) {
																		Field fieldTemp = object.getClass().getDeclaredField(fieldAux.getName());
																		fieldTemp.setAccessible(true);
																		String valueField = (String) fieldTemp.get(object);
//																		System.out.println("Comparar: " + map.getValue()[0] + " Com: " + valueField);
																		if (map.getValue()[0].compareTo(valueField) != 0) {
																			isValid = false;
																			isChecked = false;
																		}
																	}
																}
															}
															if (met.getName().equals("message")) {														
																message = met.invoke(annotation).toString();
																if (!isChecked)
																	messages.add(String.format(message, compareField.toUpperCase(), map.getKey().substring(3, map.getKey().length()).toUpperCase()));
															}
														}
													}
												}
//												validations = null;
											}
											if (isChecked) {
												// DUPLICATE
												if (annotation.annotationType().getSimpleName().equals("Duplicate")) {
													Map<String, Object> parameter = new HashMap<String, Object>();
											        parameter.put(map.getKey().substring(3, map.getKey().length()).toLowerCase(), map.getValue()[0]);
											        String namedQuery = "";
											        String message = "";
													for (Method met : annotation.annotationType().getMethods()) {
														if (met.getName().equals("message"))
															message = met.invoke(annotation).toString();
														else if (met.getName().equals("namedQuery")) {
															Object obj = met.invoke(annotation);
															namedQuery = obj.toString();
														}
													}
													if (connection.find(namedQuery, parameter) != null) {
														messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
														isValid = false;
													}
												}
											}
											method = object.getClass().getMethod(map.getKey(), java.lang.String.class);
											method.invoke(object, new Object[] { map.getValue()[0] });
											// ENCRYPT
//											if (annotation.annotationType().getSimpleName().equals("Encrypt")) {
//												System.out.println("Aqui!!!");										
//												String valorEncrypt = Encode.encrypt(map.getValue()[0]); 
//												System.out.println("Campo: " + field.getName() + " Valor: " + map.getValue()[0] + " - Crypto: " + valorEncrypt);
//												
//												method = object.getClass().getMethod(map.getKey(), java.lang.String.class);
//												method.invoke(object, new Object[] { valorEncrypt });
//												
//												Field fieldTemp = object.getClass().getDeclaredField(map.getKey().substring(3, map.getKey().length()).toLowerCase());
//												fieldTemp.setAccessible(true);
//												fieldTemp.set(map.getValue()[0], valorEncrypt);
//												
//											}
										}
										else {
											// NOTNULL
											if (annotation.annotationType().getSimpleName().equals("NotNullWithException")) {
										        String message = "";
										        boolean isException = false;
												for (Method met : annotation.annotationType().getMethods()) {
													if (met.getName().equals("message"))
														message = met.invoke(annotation).toString();
													if (met.getName().equals("exception")) {
//														System.out.println("TYPE: " + request.getParameter("typePerson") + " - EXCEPTION: " + met.invoke(annotation).toString() + " - FIELD: " + field.getName());
														if (request.getParameter("typePerson") != null) {
															if (met.invoke(annotation).toString().compareTo(request.getParameter("typePerson")) != 0)
																isException = true;
														}
														else {
															messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
															isValid = false;
														}
													}
												}
												if (isException) {
													messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
													isValid = false;
												}
											}
											if (annotation.annotationType().getSimpleName().equals("NotNull")) {
										        String message = "";
												for (Method met : annotation.annotationType().getMethods()) {
													if (met.getName().equals("message"))
														message = met.invoke(annotation).toString();
												}
												messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
												isValid = false;
											}
											method = object.getClass().getMethod(map.getKey(), java.lang.String.class);
											method.invoke(object, new Object[] { map.getValue()[0] });
										}
									}
								}
								// INTEGER
								else if (paramType.getSimpleName().equals("Integer")) {
									Field field = object.getClass().getDeclaredField(map.getKey().substring(3, map.getKey().length()).toLowerCase());
									Annotation[] annotations = field.getAnnotations();
									for (Annotation annotation : annotations) {
										if (map.getValue()[0].equals("0") || map.getValue()[0].equals("") || map.getValue()[0] == null) {
											// NotNull
											if (annotation.annotationType().getSimpleName().equals("NotNull")) {
										        String message = "";
												for (Method met : annotation.annotationType().getMethods()) {
													if (met.getName().equals("message"))
														message = met.invoke(annotation).toString();
												}
												messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
											}												
											method = object.getClass().getMethod(map.getKey(), java.lang.Integer.class);
											method.invoke(object, new Object[] { Integer.parseInt("0") });
											isValid = false;
										}												
										else {
											// MIN
											if (annotation.annotationType().getSimpleName().equals("Min")) {
												String message = "";
												boolean isChecked = true;
												for (Method met : annotation.annotationType().getMethods()) {
													if (met.getName().equals("value")) {
														int size = (Integer) met.invoke(annotation);
														if (map.getValue()[0].length() < size) {
															isValid = false;
															isChecked = false;
														}
													}
													else if (met.getName().equals("message")) {														
														message = met.invoke(annotation).toString();
														if (!isChecked)
															messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
													}
												}	
											}
											// MAX
											if (annotation.annotationType().getSimpleName().equals("Max")) {
												String message = "";
												boolean isChecked = true;
												for (Method met : annotation.annotationType().getMethods()) {
													if (met.getName().equals("value")) {
														int size = (Integer) met.invoke(annotation);
														if (map.getValue()[0].length() > size) {
															isValid = false;
															isChecked = false;
														}
													}
													else if (met.getName().equals("message")) {														
														message = met.invoke(annotation).toString();
														if (!isChecked)
															messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
													}
												}	
											}
											// SIZE
											if (annotation.annotationType().getSimpleName().equals("Size")) {
												int min = 0;
												int max = 0;
												String message = "";
												boolean isChecked = true;
												for (Method met : annotation.annotationType().getMethods()) {
													if (met.getName().equals("min")) {
														min = (Integer) met.invoke(annotation);
														if (map.getValue()[0].length() < min) {
															isValid = false;
															isChecked = false;
														}
													}
													else if (met.getName().equals("max")) {
														max = (Integer) met.invoke(annotation);
														if (map.getValue()[0].length() > max) {
															isValid = false;
															isChecked = false;
														}
													}
													else if (met.getName().equals("message")) {														
														message = met.invoke(annotation).toString();
														if (!isChecked)
															messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
													}
												}
											}
											method = object.getClass().getMethod(map.getKey(), java.lang.Integer.class);
											method.invoke(object, new Object[] { Integer.parseInt(map.getValue()[0]) });
										}
									}
								}
								// INT
								else if (paramType.getSimpleName().equals("int")) {
									Field field = object.getClass().getDeclaredField(map.getKey().substring(3, map.getKey().length()).toLowerCase());
									Annotation[] annotations = field.getAnnotations();
									for (Annotation annotation : annotations) {
										if (map.getValue()[0].equals("0") || map.getValue()[0].equals("") || map.getValue()[0] == null) {
											// NotNull
											if (annotation.annotationType().getSimpleName().equals("NotNull")) {
										        String message = "";
												for (Method met : annotation.annotationType().getMethods()) {
													if (met.getName().equals("message"))
														message = met.invoke(annotation).toString();
												}
												messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
											}
											method = object.getClass().getMethod(map.getKey(), int.class);
											method.invoke(object, new Object[] { Integer.parseInt("0") });
											isValid = false;
										}
										else {
											// MIN
											if (annotation.annotationType().getSimpleName().equals("Min")) {
												String message = "";
												boolean isChecked = true;
												for (Method met : annotation.annotationType().getMethods()) {
													if (met.getName().equals("value")) {
														int size = (Integer) met.invoke(annotation);
														if (map.getValue()[0].length() < size) {
															isValid = false;
															isChecked = false;
														}
													}
													else if (met.getName().equals("message")) {														
														message = met.invoke(annotation).toString();
														if (!isChecked)
															messages.add(message);
													}
												}	
											}
											// MAX
											if (annotation.annotationType().getSimpleName().equals("Max")) {
												String message = "";
												boolean isChecked = true;
												for (Method met : annotation.annotationType().getMethods()) {
													if (met.getName().equals("value")) {
														int size = (Integer) met.invoke(annotation);
														if (map.getValue()[0].length() > size) {
															isValid = false;
															isChecked = false;
														}
													}
													else if (met.getName().equals("message")) {														
														message = met.invoke(annotation).toString();
														if (!isChecked)
															messages.add(message);
													}
												}	
											}
											// SIZE
											if (annotation.annotationType().getSimpleName().equals("Size")) {
												int min = 0;
												int max = 0;
												String message = "";
												boolean isChecked = true;
												for (Method met : annotation.annotationType().getMethods()) {
													if (met.getName().equals("min")) {
														min = (Integer) met.invoke(annotation);
														if (map.getValue()[0].length() < min) {
															isValid = false;
															isChecked = false;
														}
													}
													else if (met.getName().equals("max")) {
														max = (Integer) met.invoke(annotation);
														if (map.getValue()[0].length() > max) {
															isValid = false;
															isChecked = false;
														}
													}
													else if (met.getName().equals("message")) {														
														message = met.invoke(annotation).toString();
														if (!isChecked)
															messages.add(message);
													}
												}
											}
											method = object.getClass().getMethod(map.getKey(), int.class);
											method.invoke(object, new Object[] { Integer.parseInt(map.getValue()[0]) });
										}
									}
								}
								// DATE - criar função para validar data
								else if (paramType.getSimpleName().equals("Date")) {
									boolean isException = false;
									Field field = object.getClass().getDeclaredField(map.getKey().substring(3, map.getKey().length()).toLowerCase());
									Annotation[] annotations = field.getAnnotations();
									for (Annotation annotation : annotations) {
										if (map.getValue()[0].equals("  /  /    ") || map.getValue()[0].equals("") || map.getValue()[0] == null) {
											// NotNull
											if (annotation.annotationType().getSimpleName().equals("NotNull")) {
										        String message = "";
												for (Method met : annotation.annotationType().getMethods()) {
													if (met.getName().equals("message"))
														message = met.invoke(annotation).toString();
													if (met.getName().equals("exception")) {
//														System.out.println("TYPE: " + request.getParameter("typePerson") + " - EXCEPTION: " + met.invoke(annotation).toString() + " - FIELD: " + field.getName());
														if (request.getParameter("typePerson") != null) {
															if (met.invoke(annotation).toString().compareTo(request.getParameter("typePerson")) != 0)
																isException = true;
														}
														else {
															messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
															isValid = false;
														}
													}
												}
												if (isException)
													messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
											}
											if (isException)
												isValid = false;
										}
										else {
											method = object.getClass().getMethod(map.getKey(), java.util.Date.class);
											method.invoke(object, new Object[] { util.converteDate(map.getValue()[0]) });
										}
									}
								}
								// BIGDECIMAL
								else if (paramType.getSimpleName().equals("BigDecimal")) {
									Field field = object.getClass().getDeclaredField(map.getKey().substring(3, map.getKey().length()).toLowerCase());
									Annotation[] annotations = field.getAnnotations();
									for (Annotation annotation : annotations) {
										if (map.getValue()[0].equals("") || map.getValue()[0] == null) {
											// NotNull
											if (annotation.annotationType().getSimpleName().equals("NotNull")) {
										        String message = "";
												for (Method met : annotation.annotationType().getMethods()) {
													if (met.getName().equals("message"))
														message = met.invoke(annotation).toString();
												}
												messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
												isValid = false;
											}
											else {
												method = object.getClass().getMethod(map.getKey(), java.math.BigDecimal.class);
												method.invoke(object, new Object[] { util.converteBigDecimal("0,00") });
											}
										}
										else {
											method = object.getClass().getMethod(map.getKey(), java.math.BigDecimal.class);
											method.invoke(object, new Object[] { util.converteBigDecimal(map.getValue()[0]) });
										}
									}
								}
								// DOUBLE
								else if (paramType.getSimpleName().equals("Double")) {
									Field field = object.getClass().getDeclaredField(map.getKey().substring(3, map.getKey().length()).toLowerCase());
									Annotation[] annotations = field.getAnnotations();
									for (Annotation annotation : annotations) {
										if (map.getValue()[0].equals("") || map.getValue()[0] == null) {
											// NotNull
											if (annotation.annotationType().getSimpleName().equals("NotNull")) {
										        String message = "";
												for (Method met : annotation.annotationType().getMethods()) {
													if (met.getName().equals("message"))
														message = met.invoke(annotation).toString();
												}
												messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
											}
											isValid = false;
										}
										else {
											method = object.getClass().getMethod(map.getKey(), java.lang.Double.class);
											method.invoke(object, new Object[] { util.converteDouble(map.getValue()[0]) });
										}
									}
								}
								// FLOAT
								else if (paramType.getSimpleName().equals("Float")) {
									Field field = object.getClass().getDeclaredField(map.getKey().substring(3, map.getKey().length()).toLowerCase());
									Annotation[] annotations = field.getAnnotations();
									for (Annotation annotation : annotations) {
										if (map.getValue()[0].equals("") || map.getValue()[0] == null) {
											// NotNull
											if (annotation.annotationType().getSimpleName().equals("NotNull")) {
										        String message = "";
												for (Method met : annotation.annotationType().getMethods()) {
													if (met.getName().equals("message"))
														message = met.invoke(annotation).toString();
												}
												messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
											}
											isValid = false;
										}
										else {
											method = object.getClass().getMethod(map.getKey(), java.lang.Float.class);
											method.invoke(object, new Object[] { util.converteFloat(map.getValue()[0]) });
										}
									}
								}
								// BOOLEAN
								else if (paramType.getSimpleName().equals("Boolean")) {
									Field field = object.getClass().getDeclaredField(map.getKey().substring(3, map.getKey().length()).toLowerCase());
									Annotation[] annotations = field.getAnnotations();
									for (Annotation annotation : annotations) {
										if (map.getValue()[0].equals("") || map.getValue()[0] == null) {
											// NotNull
											if (annotation.annotationType().getSimpleName().equals("NotNull")) {
										        String message = "";
												for (Method met : annotation.annotationType().getMethods()) {
													if (met.getName().equals("message"))
														message = met.invoke(annotation).toString();
												}
												messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
											}
											isValid = false;
										}
										else {
											method = object.getClass().getMethod(map.getKey(), java.lang.Boolean.class);
											method.invoke(object, new Object[] { util.converteBoolean(map.getValue()[0]) });
										}
									}
								}
								// CHARACTER
								else if (paramType.getSimpleName().equals("Character")) {
									Field field = object.getClass().getDeclaredField(map.getKey().substring(3, map.getKey().length()).toLowerCase());
									Annotation[] annotations = field.getAnnotations();
									for (Annotation annotation : annotations) {
										if (map.getValue()[0].equals("") || map.getValue()[0] == null) {
											// NotNull
											if (annotation.annotationType().getSimpleName().equals("NotNull")) {
										        String message = "";
												for (Method met : annotation.annotationType().getMethods()) {
													if (met.getName().equals("message"))
														message = met.invoke(annotation).toString();
												}
												messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
											}
											isValid = false;
										}
										else {
											method = object.getClass().getMethod(map.getKey(), java.lang.Character.class);
											method.invoke(object, new Object[] { util.converteCharacter(map.getValue()[0]) });
										}
									}
								}
								else {
									// OTHER CUSTOM CLASSES
									if (paramType.getName().startsWith("entity")) {
										
//										System.out.println("Manager.java - aqui!!! - Valor: " + map.getValue()[0]);
										
										Field field = object.getClass().getDeclaredField(map.getKey().substring(3, map.getKey().length()).toLowerCase());
										Annotation[] annotations = field.getAnnotations();
										for (Annotation annotation : annotations) {
											if (map.getValue()[0].equals("0") || map.getValue()[0].equals("") || map.getValue()[0] == null) {
												// NotNull
												if (annotation.annotationType().getSimpleName().equals("NotNull")) {
											        String message = "";
													for (Method met : annotation.annotationType().getMethods()) {
														if (met.getName().equals("message"))
															message = met.invoke(annotation).toString();
													}
													messages.add(String.format(message, map.getKey().substring(3, map.getKey().length()).toUpperCase()));
												}
												isValid = false;
											}
											else {
												method = object.getClass().getMethod(map.getKey(), Class.forName(paramType.getName()).newInstance().getClass());
												method.invoke(object, connection.find(Class.forName(paramType.getName()).newInstance().getClass(), Integer.parseInt(map.getValue()[0])));
											}
										}
									}
								}
								
							} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | ClassNotFoundException | NoSuchFieldException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
		}
		
		request.setAttribute("isValid", isValid);
		request.setAttribute("messages", messages);
		request.setAttribute(nameOfObject, object);
		
//		methodsAndValue = null;
//		messages = null;
//		parameters = null;
//		util = null;
		
		return object;
		
	}
	
}

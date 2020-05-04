package spinwork.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validations {

	public boolean isCpf(String value) {
		if (isInvalid(value))
			return false;
		value = onlyNumbers(value);
		int soma = 0;
		try {
			Long.parseLong(value);
		} catch (Exception e) {
			return false;
		}
		if (value.length() == 11) {
			for (int i = 0; i < 9; i++)
				soma += (10 - i) * (value.charAt(i) - '0');
			soma = 11 - (soma % 11);
			if (soma > 9)
				soma = 0;
			if (soma == (value.charAt(9) - '0')) {
				soma = 0;
				for (int i = 0; i < 10; i++)
					soma += (11 - i) * (value.charAt(i) - '0');
				soma = 11 - (soma % 11);
				if (soma > 9)
					soma = 0;
				if (soma == (value.charAt(10) - '0'))
					return true;
			}
		}
		return false;
	}
	
	public boolean isCnpj(String value) { 
		if (isInvalid(value))
			return false;
		value = onlyNumbers(value);
		int soma = 0;
		try {
			Long.parseLong(value);
		} catch (Exception e) {
			return false;
		}
		if (value.length() == 14) {
			for (int i = 0, j = 5; i < 12; i++) {
				soma += j-- * (value.charAt(i) - '0');
				if (j < 2)
					j = 9;
			}
			soma = 11 - (soma % 11);
			if (soma > 9)
				soma = 0;
			if (soma == (value.charAt(12) - '0')) {
				soma = 0;
				for (int i = 0, j = 6; i < 13; i++) {
					soma += j-- * (value.charAt(i) - '0');
					if (j < 2)
						j = 9;
				}
				soma = 11 - (soma % 11);
				if (soma > 9)
					soma = 0;
				if (soma == (value.charAt(13) - '0'))
					return true;
			}
		}
		return false;
	}

	public boolean isEmail(String email) {
		Pattern p = Pattern.compile("^([0-9a-z]+([_.-]?[0-9a-z]+)*@[0-9a-z]+([_.-]?[0-9a-z]+)*(.){1}[a-z]{2,4})+$");
		Matcher m = p.matcher(email);
		return m.matches();
	}  
	
	public boolean isUrl(String url) {
		if (url.length() < 2)
			return false;
		Pattern p = Pattern.compile("[a-z0-9]+((\\.[a-z0-9]+)*(-[a-z0-9]+)*(_[a-z0-9]+)*)*");
		Matcher m = p.matcher(url);
		return m.matches();
	}  

	public boolean isPassword(String password) {
		if (password.length() < 8 || password.length() > 20)
			return false;
		Pattern p = Pattern.compile("([a-zA-Z]*([0-9]+[a-zA-Z]+)|([a-zA-Z]+[0-9]+)[0-9]*)+");
		Matcher m = p.matcher(password);
		return m.matches();
	}  

	public boolean isDigit(String value) {
		boolean isDigit = false;
		for (int i = 0; i < value.length(); i++) {
			isDigit = Character.isDigit(value.charAt(i)); 
			if (!isDigit)
				break;
		}
		return isDigit;
	}
	
	public String onlyNumbers(String valor) {
		valor = valor.replace("(", "");
		valor = valor.replace(")", "");
		valor = valor.replace("[", "");
		valor = valor.replace("]", "");
		valor = valor.replace("{", "");
		valor = valor.replace("}", "");
		valor = valor.replace(".", "");
		valor = valor.replace(",", "");
		valor = valor.replace("+", "");
		valor = valor.replace("-", "");
		valor = valor.replace("*", "");
		valor = valor.replace("=", "");
		valor = valor.replace("/", "");
		valor = valor.replace("\\", "");
		valor = valor.replace("_", "");
		valor = valor.replace(":", "");
		valor = valor.replace(";", "");
		valor = valor.replace("&", "");
		valor = valor.replace("%", "");
		valor = valor.replace("#", "");
		valor = valor.replace("@", "");
		valor = valor.replace("$", "");
		valor = valor.replace("|", "");
		valor = valor.replace("?", "");
		valor = valor.replace("!", "");
		valor = valor.replace("\"", "");
		valor = valor.replace("'", "");
		valor = valor.replace("ª", "");
		valor = valor.replace("º", "");
		valor = valor.replace("¹", "");
		valor = valor.replace("²", "");
		valor = valor.replace("³", "");
		valor = valor.replace("¬", "");
		valor = valor.replace("¢", "");
		valor = valor.replace("£", "");
		valor = valor.replace("§", "");
		valor = valor.replace(" ", "");
		return valor;
	}

	public boolean isInvalid(String value) {		
		if (value.equals("00000000000"))
			return true;
		else if (value.equals("11111111111"))
			return true;
		else if (value.equals("22222222222"))
			return true;
		else if (value.equals("33333333333"))
			return true;
		else if (value.equals("44444444444"))
			return true;
		else if (value.equals("55555555555"))
			return true;
		else if (value.equals("66666666666"))
			return true;
		else if (value.equals("77777777777"))
			return true;
		else if (value.equals("88888888888"))
			return true;
		else if (value.equals("99999999999"))
			return true;		
		else if (value.equals("00000000000000"))
			return true;		
		return false;
	}
	
}
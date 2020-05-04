package spinwork.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Utils {
	
	public String firstLetterUppercase(String nomeCorrigido) {
		String alterado = ""; 
		int tamanho = 0;
		tamanho = nomeCorrigido.length();
		alterado = nomeCorrigido.substring(0, 1).toUpperCase();
		alterado = alterado + nomeCorrigido.substring(1, tamanho);
		return alterado;
	}
	
	public String convertDateToFormatPtBR(Date data) {
		String dataFormatada = null;
		try {
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			dataFormatada = formato.format(data);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return dataFormatada;
	}

	public Date convertDateToFormatBD(String data) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date date = null;
		try {
			date = new Date(format.parse(data).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public Date converteDate(String data) {
		Date date = null;
		String MaskData = "dd/MM/yyyy";
		String MaskHora = "HH:mm:ss";
		try {
			if (data.length() == 10)
				date = new SimpleDateFormat(MaskData).parse(data);
			else
				date = new SimpleDateFormat(MaskData + " " + MaskHora).parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;									
	}
	
	public Double converteDouble(String valor) {
		valor = valor.replaceAll(",", ".");
		return new Double(valor);
	}
	
	public Float converteFloat(String valor) {
		valor = valor.replaceAll(",", ".");
		return new Float(valor);
	}
	
	public BigDecimal converteBigDecimal(String valor) {
		if (valor.contains(",")) {
			valor = valor.replace(".", "");
			valor = valor.replace(",", ".");
		}
		return new BigDecimal(valor).setScale(2, RoundingMode.UNNECESSARY);
	}
	
	public Boolean converteBoolean(String valor) {
		return new Boolean(valor);
	}
	
	public Character converteCharacter(String valor) {
		return valor.charAt(0);
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
		valor = valor.replace(".", "");
		valor = valor.replace(",", ".");
		valor = valor.replace("A", "");
		valor = valor.replace("B", "");
		valor = valor.replace("C", "");
		valor = valor.replace("D", "");
		valor = valor.replace("E", "");
		valor = valor.replace("F", "");
		valor = valor.replace("G", "");
		valor = valor.replace("H", "");
		valor = valor.replace("I", "");
		valor = valor.replace("J", "");
		valor = valor.replace("K", "");
		valor = valor.replace("L", "");
		valor = valor.replace("M", "");
		valor = valor.replace("N", "");
		valor = valor.replace("O", "");
		valor = valor.replace("P", "");
		valor = valor.replace("Q", "");
		valor = valor.replace("R", "");
		valor = valor.replace("S", "");
		valor = valor.replace("T", "");
		valor = valor.replace("U", "");
		valor = valor.replace("V", "");
		valor = valor.replace("X", "");
		valor = valor.replace("W", "");
		valor = valor.replace("Y", "");
		valor = valor.replace("Z", "");
		valor = valor.replace("Ç", "");
		valor = valor.replace(" ", "");
		return valor;
	}

}

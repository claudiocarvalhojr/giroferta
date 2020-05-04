package utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tomcat.util.http.fileupload.IOUtils;

public final class Utilities {
	
	// PRINT LISTA NOME E VALOR
	public void printNameValue(Map<String, String[]> parametros, boolean isPrint) {
		if (isPrint) {
			printLinha(isPrint);
			System.out.println("LOG - " +(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(new Date()));
			for (Map.Entry<String, String[]> e : parametros.entrySet())
				System.out.printf("PARAMETRO: %-25s VALOR: %s %n", e.getKey(), e.getValue()[0]);
			printLinha(isPrint);
		}
	}
	
	// PRINT VALOR
	public void print(String valor, boolean isPrint) {
		if (isPrint)
			System.out.print(valor);
	}
		
	// PRINT VALOR C/ NOVA LINHA
	public void println(String valor, boolean isPrint) {
		if (isPrint)
			System.out.println(valor);
	}
		
	// PRINT VALOR C/ NOVA LINHA
	public void printDataHora(String valor, boolean isPrint) {
		if (isPrint)
			System.out.println((new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(new Date()) + valor);
	}
		
	// PRINT LINHA
	public void printLinha(boolean isPrint) {
		if (isPrint)
			System.out.println("********************************************************************************");
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
	
	public String converDate(Calendar calendar, String mask) {
		return (new SimpleDateFormat(mask)).format(calendar.getTime());
	}
	
	public String converDate(Date date, String mask) {
		return (new SimpleDateFormat(mask)).format(date);
	}
	
	public String converCalendar(Calendar calendar) {
		return (new SimpleDateFormat("yyyy-MM-dd")).format(calendar.getTime());
	}
	
	public String converDate(Date date) {
		return (new SimpleDateFormat("dd/MM/yyyy HH:mm:ss")).format(date);
	}
	
	public byte[] toBytes(InputStream stream) {
		try {			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(stream, baos);
			return baos.toByteArray();			
		}
		catch (Exception e) {
			return null;
		}										
	}

	public BigDecimal converteBigDecimal(String valor) {
		if (valor.contains(",")) {
			valor = valor.replace(".", "");
			valor = valor.replace(",", ".");
		}
		return new BigDecimal(valor).setScale(2, RoundingMode.UNNECESSARY);
	}
	
	// CONFIGURA ESCALA IMAGEM ENVIADA (IMAGENS) ******************************
	public BufferedImage resizeImage(BufferedImage original, int width, int height) {		
		BufferedImage alterada = new BufferedImage(width, height, original.getType()); // original.getType() ou BufferedImage.TYPE_INT_RGB									
		Graphics2D g2d = alterada.createGraphics();
		g2d.drawImage(original.getScaledInstance(width, height, 10000), 0, 0, null);
		g2d.dispose();		
		return alterada;	   
	}
	
	public BufferedImage cropImage(BufferedImage original, int x, int y, int width, int height) {		
		BufferedImage alterada = original.getSubimage(x, y, width, height);	
		return alterada;	   
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
	
	public boolean isPassword(String password) {
		if (password.length() < 8 || password.length() > 20)
			return false;
		Pattern p = Pattern.compile("([a-zA-Z]*([0-9]+[a-zA-Z]+)|([a-zA-Z]+[0-9]+)[0-9]*)+");
		Matcher m = p.matcher(password);
		return m.matches();
	}  

	public boolean isEmail(String email) {
		Pattern p = Pattern.compile("^([0-9a-z]+([_.-]?[0-9a-z]+)*@[0-9a-z]+([_.-]?[0-9a-z]+)*(.){1}[a-z]{2,4})+$");
		Matcher m = p.matcher(email);
		return m.matches();
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

	public String configUrl(String title) {
		String url = title.toLowerCase();
		url = url.replaceAll(" ", "-");
		url = url.replaceAll("á", "a");
		url = url.replaceAll("à", "a");
		url = url.replaceAll("â", "a");
		url = url.replaceAll("ã", "a");
		url = url.replaceAll("ä", "a");
		url = url.replaceAll("é", "e");
		url = url.replaceAll("è", "e");
		url = url.replaceAll("ê", "e");
		url = url.replaceAll("ë", "e");
		url = url.replaceAll("í", "i");
		url = url.replaceAll("ì", "i");
		url = url.replaceAll("î", "i");
		url = url.replaceAll("ï", "i");
		url = url.replaceAll("ó", "o");
		url = url.replaceAll("ò", "o");
		url = url.replaceAll("ô", "o");
		url = url.replaceAll("õ", "o");
		url = url.replaceAll("ö", "o");
		url = url.replaceAll("ú", "u");
		url = url.replaceAll("ù", "u");
		url = url.replaceAll("û", "u");
		url = url.replaceAll("ü", "u");
		url = url.replaceAll("ç", "c");

		url = url.replaceAll("\\.", "-");
		url = url.replaceAll(",", "-");
		url = url.replaceAll("\\*", "");
		
//		url = url.replaceAll("ª", "-");
//		url = url.replaceAll("º", "-");
		url = url.replaceAll("!", "-");
//		url = url.replaceAll("@", "-");
		url = url.replaceAll("#", "-");
//		url = url.replaceAll("$", "-");
//		url = url.replaceAll("%", "-");
//		url = url.replaceAll("&", "-");
//		url = url.replaceAll(";", "-");
//		url = url.replaceAll(":", "-");
//		url = url.replaceAll("|", "-");
		url = url.replaceAll("/", "-");
//		url = url.replaceAll("<", "-");
//		url = url.replaceAll(">", "-");
//		url = url.replaceAll("'", "-");
//		url = url.replaceAll("\"", "-");
//		url = url.replaceAll("=", "-");
//		url = url.replaceAll("~", "-");
//		url = url.replaceAll("^", "-");
//		url = url.replaceAll("´", "-");
//		url = url.replaceAll("`", "-");

//		url = url.replaceAll("\\?", "-");
//		url = url.replaceAll("\\_", "-");
//		url = url.replaceAll("\\\", "-");
//		url = url.replaceAll("\\+", "-");
//		url = url.replaceAll("\\§", "-");
//		url = url.replaceAll("\\(", "-");
//		url = url.replaceAll("\\)", "-");
//		url = url.replaceAll("\\[", "-");
//		url = url.replaceAll("\\]", "-");
//		url = url.replaceAll("\\{", "-");
//		url = url.replaceAll("\\}", "-");
		
		url = url.replaceAll("\\(", "-");
		url = url.replaceAll("\\)", "-");
		
		url = url.replaceAll("’", "-"); 
		url = url.replaceAll("™", "-"); 
		url = url.replaceAll("ª", "-"); 
		url = url.replaceAll("“", "-");
		url = url.replaceAll("”", "-");
		url = url.replaceAll("´", "-");
		url = url.replaceAll("`", "-");
		url = url.replaceAll("'", "-");
		url = url.replaceAll("\"", "-");
		url = url.replaceAll("\\+", "-");
		
		url = url.replaceAll("-----", "-");
		url = url.replaceAll("----", "-");
		url = url.replaceAll("---", "-");
		url = url.replaceAll("--", "-");
		
		if (url.endsWith("--"))
			url = url.substring(0, url.length()-2);
		
		if (url.endsWith("-"))
			url = url.substring(0, url.length()-1);
		
		return url + "/";
	}
	
}

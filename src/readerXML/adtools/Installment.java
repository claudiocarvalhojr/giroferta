package readerXML.adtools;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "installment")
public class Installment {

	@Element(name = "months", required = false)
	private int months;
	@Element(name = "amount", required = false)
	private String amount;

	public int getMonths() {
		return months;
	}

	public void setMonths(int months) {
		this.months = months;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}

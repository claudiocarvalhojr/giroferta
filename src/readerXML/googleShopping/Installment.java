package readerXML.googleShopping;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "installment")
public class Installment {

	@Element(name = "months", required = false)
	private int months;
	@Element(name = "amount", required = false)
	private double amount;

	public int getMonths() {
		return months;
	}

	public void setMonths(int months) {
		this.months = months;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}

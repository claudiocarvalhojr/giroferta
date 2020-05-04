package readerXML.googleShopping;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import readerXML.Link;

@Root(name = "entry")
public class Entry {

	@Element(name = "id", required = false)
	private String id;
	@Element(name = "title", required = false)
	private String title;
	@Element(name = "description", required = false)
	private String description;
	@Element(name = "product_type", required = false)
	private String product_type;
	@Element(name = "link", required = false)
	private Link link;
	@Element(name = "image_link", required = false)
	private String image_link;
	@ElementList(name= "additional_image_link", inline = true, entry = "additional_image_link", required = false)
	private List<String> additional_image_link;
	@Element(name = "condition", required = false)
	private String condition;
	@Element(name = "availability", required = false)
	private String availability;
	@Element(name = "price", required = false)
	private String price;
	@Element(name = "sale_price", required = false)
	private String sale_price;
	@Element(name = "brand", required = false)
	private String brand;
	@Element(name = "mpn", required = false)
	private String mpn;
	@ElementList(name = "installment", required = false, inline = true)
	private List<Installment> installment;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProduct_type() {
		return product_type;
	}

	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}

	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public String getImage_link() {
		return image_link;
	}

	public void setImage_link(String image_link) {
		this.image_link = image_link;
	}

	public List<String> getAdditional_image_link() {
		return additional_image_link;
	}

	public void setAdditional_image_link(List<String> additional_image_link) {
		this.additional_image_link = additional_image_link;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSale_price() {
		return sale_price;
	}

	public void setSale_price(String sale_price) {
		this.sale_price = sale_price;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getMpn() {
		return mpn;
	}

	public void setMpn(String mpn) {
		this.mpn = mpn;
	}

	public List<Installment> getInstallment() {
		return installment;
	}

	public void setInstallment(List<Installment> installment) {
		this.installment = installment;
	}

}

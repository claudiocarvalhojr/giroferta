package readerXML.rss;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "entry")
public class Entry {

	@Element(name = "id", required = false)
	private String id;
	@Element(name = "title", required = false)
	private String title;
	@Element(name = "summary", required = false)
	private String summary;
	@Element(name = "product_type", required = false)
	private String product_type;
	@Element(name = "google_product_category", required = false)
	private String google_product_category;
	@Element(name = "link", required = false)
	private String link;
	@Element(name = "image_link", required = false)
	private String image_link;
	@ElementList(name = "additional_image_link", inline = true, entry = "additional_image_link", required = false)
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
	@Element(name = "gtin", required = false)
	private String gtin;
	@Element(name = "shipping_label", required = false)
	private String shipping_label;
	@Element(name = "custom_label_0", required = false)
	private String custom_label_0;
	@Element(name = "item_group_id", required = false)
	private String item_group_id;
	@ElementList(name = "installment", inline = true, required = false)
	private List<Installment> installment;
	@ElementList(name = "specs", inline = true, required = false)
	private List<Specs> specs;

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

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getProduct_type() {
		return product_type;
	}

	public void setProduct_type(String product_type) {
		this.product_type = product_type;
	}

	public String getGoogle_product_category() {
		return google_product_category;
	}

	public void setGoogle_product_category(String google_product_category) {
		this.google_product_category = google_product_category;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
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

	public String getGtin() {
		return gtin;
	}

	public void setGtin(String gtin) {
		this.gtin = gtin;
	}

	public String getShipping_label() {
		return shipping_label;
	}

	public void setShipping_label(String shipping_label) {
		this.shipping_label = shipping_label;
	}

	public String getCustom_label_0() {
		return custom_label_0;
	}

	public void setCustom_label_0(String custom_label_0) {
		this.custom_label_0 = custom_label_0;
	}

	public String getItem_group_id() {
		return item_group_id;
	}

	public void setItem_group_id(String item_group_id) {
		this.item_group_id = item_group_id;
	}

	public List<Installment> getInstallment() {
		return installment;
	}

	public void setInstallment(List<Installment> installment) {
		this.installment = installment;
	}

	public List<Specs> getSpecs() {
		return specs;
	}

	public void setSpecs(List<Specs> specs) {
		this.specs = specs;
	}

}

package readerXML;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "link")
public class Link {

	@Attribute(name = "href", required = false)	
	private String href;
	
	@Attribute(name = "ref", required = false)
	private String ref;
	
	@Attribute(name = "rel", required = false)
	private String rel;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

}

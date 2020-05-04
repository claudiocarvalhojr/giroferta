package writerXML;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "urlset")
public class UrlSet {

	@Attribute(name = "xmlns", required = true)
	private String xmlns; 
	@ElementList(name = "url", inline = true, required = true, empty = false)
	private List<Url> listUrl;
	
	public UrlSet(String xmlns) {
		this.setXmlns(xmlns);
	}
	
	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public List<Url> getListUrl() {
		return listUrl;
	}

	public void setListUrl(List<Url> listUrl) {
		this.listUrl = listUrl;
	}

}

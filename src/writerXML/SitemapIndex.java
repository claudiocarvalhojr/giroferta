package writerXML;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "sitemapindex")
public class SitemapIndex {
	
	@Attribute(name = "xmlns", required = true)
	private String xmlns; 
	@ElementList(name = "sitemap", inline = true, required = true, empty = false)
	private List<Sitemap> listSitemap;

	public SitemapIndex(String xmlns) {
		this.setXmlns(xmlns);
	}

	public String getXmlns() {
		return xmlns;
	}

	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public List<Sitemap> getListSitemap() {
		return listSitemap;
	}

	public void setListSitemap(List<Sitemap> listSitemap) {
		this.listSitemap = listSitemap;
	}
	
}

package writerXML;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "sitemap")
public class Sitemap {

	@Element(name = "loc", required = true)
	private String loc;
	@Element(name = "lastmod", required = false)
	private String lastmod;
	
	public Sitemap(String loc) {
		this.setLoc(loc);
	}

	public Sitemap(String loc, String lastmod) {
		this.setLoc(loc);
		this.setLastmod(lastmod);
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public String getLastmod() {
		return lastmod;
	}

	public void setLastmod(String lastmod) {
		this.lastmod = lastmod;
	}
	
}

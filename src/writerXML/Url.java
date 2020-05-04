package writerXML;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "url")
public class Url {
	
	@Element(name = "loc", required = true)
	private String loc;
	@Element(name = "lastmod", required = false)
	private String lastmod;
	@Element(name = "changefreq", required = false)
	private String changefreq;
	@Element(name = "priority", required = true)
	private double priority;
	
	public Url() {
	}

	public Url(String loc) {
		this.setLoc(loc);
	}

	public Url(String loc, double priority) {
		this.setLoc(loc);
		this.setPriority(priority);
	}

	public Url(String loc, String changefreq, double priority) {
		this.setLoc(loc);
		this.setLastmod(lastmod);
		this.setChangefreq(changefreq);
		this.setPriority(priority);
	}

	public Url(String loc, String lastmod, String changefreq, double priority) {
		this.setLoc(loc);
		this.setLastmod(lastmod);
		this.setChangefreq(changefreq);
		this.setPriority(priority);
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

	public String getChangefreq() {
		return changefreq;
	}

	public void setChangefreq(String changefreq) {
		this.changefreq = changefreq;
	}

	public double getPriority() {
		return priority;
	}

	public void setPriority(double priority) {
		this.priority = priority;
	}
	
}

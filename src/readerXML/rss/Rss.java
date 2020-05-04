package readerXML.rss;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import readerXML.Link;

@Root
public class Rss {
	
	@Attribute(name = "version", required = false)
	public String version;
	@Element(name = "title", required = false)
	private String title;
	@Element(name = "link", required = false)
	private Link link;
	@Element(name = "updated", required = false)
	private String updated;
	@ElementList(name = "entry", inline = true)
	private List<Entry> entry;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public List<Entry> getEntry() {
		return entry;
	}

	public void setEntry(List<Entry> entry) {
		this.entry = entry;
	}

}
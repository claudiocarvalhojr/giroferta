package readerXML.adtools;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import readerXML.Author;
import readerXML.Link;

@Root(name = "feed")
public class Feed {

	@Element(name = "title", required = false)
	private String title;
	@Element(name = "link", required = false)
	private Link link;
	@Element(name = "updated", required = false)
	private String updated;
	@Element(name = "author", required = false)
	private Author author;
	@Element(name = "id", required = false)
	private String id;
	@ElementList(name = "entry", inline = true)
	private List<Entry> entry;

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

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Entry> getEntry() {
		return entry;
	}

	public void setEntry(List<Entry> entry) {
		this.entry = entry;
	}

}

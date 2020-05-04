package readerXML;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "author")
public class Author {

	@Element(name = "name", required = false)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

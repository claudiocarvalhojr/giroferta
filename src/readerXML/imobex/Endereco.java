package readerXML.imobex;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "endereco")
public class Endereco {

	@Element(name = "endereco", data=true, required=false)
	String endereco;
	
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

}

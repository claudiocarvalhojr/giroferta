package readerXML.imobex;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "imobex")
public class Imobex {
	
	@ElementList(name = "imovel", inline = true, entry = "imovel", required = false)
	private List<Imovel> imovel;

	public List<Imovel> getImovel() {
		return imovel;
	}

	public void setImovel(List<Imovel> imovel) {
		this.imovel = imovel;
	}

}

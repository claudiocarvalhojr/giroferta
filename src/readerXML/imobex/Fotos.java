package readerXML.imobex;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "fotos")
public class Fotos {

	@ElementList(name = "foto", inline = true, entry = "foto", required = false)
	private List<Foto> foto;

	public List<Foto> getFoto() {
		return foto;
	}

	public void setFoto(List<Foto> foto) {
		this.foto = foto;
	}

}

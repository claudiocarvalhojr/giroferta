package readerXML.imobex;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "foto")
public class Foto {

	@Element(name = "foto_url", data=true, required=false)
	private String foto_url;
	@Element(name = "foto_legenda", data=true, required=false)
	private String foto_legenda;
	@Element(name = "foto_principal", data=true, required=false)
	private String foto_principal;
	@Element(name = "foto_ordem", data=true, required=false)
	private String foto_ordem;
	
	public String getFoto_url() {
		return foto_url;
	}
	public void setFoto_url(String foto_url) {
		this.foto_url = foto_url;
	}
	public String getFoto_legenda() {
		return foto_legenda;
	}
	public void setFoto_legenda(String foto_legenda) {
		this.foto_legenda = foto_legenda;
	}
	public String getFoto_principal() {
		return foto_principal;
	}
	public void setFoto_principal(String foto_principal) {
		this.foto_principal = foto_principal;
	}
	public String getFoto_ordem() {
		return foto_ordem;
	}
	public void setFoto_ordem(String foto_ordem) {
		this.foto_ordem = foto_ordem;
	}
	
}

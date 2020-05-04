package readerXML.imobex;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "imovel")
public class Imovel {

	@Element(name = "id", data=true, required=false)
	private String id;
	@Element(name = "ref", data=true, required=false)
	private String ref;
	@Element(name = "url", data=true, required=false)
	private String url;
	@Element(name = "anunciante", data=true, required=false)
	private String anunciante;
	@Element(name = "dominio", data=true, required=false)
	private String dominio;
	@Element(name = "anunciante_fone", data=true, required=false)
	private String anunciante_fone;
	@Element(name = "anunciante_email", data=true, required=false)
	private String anunciante_email;
	@Element(name = "anunciante_endereco", data=true, required=false)
	private String anunciante_endereco;
	@Element(name = "anunciante_bairro", data=true, required=false)
	private String anunciante_bairro;
	@Element(name = "anunciante_cidade", data=true, required=false)
	private String anunciante_cidade;
	@Element(name = "anunciante_contato", data=true, required=false)
	private String anunciante_contato;
	@Element(name = "transacao", data=true, required=false)
	private String transacao;
	@Element(name = "valor", data=true, required=false)
	private String valor;
	@Element(name = "pessoas", data=true, required=false)
	private String pessoas;
	@Element(name = "descricao", data=true, required=false)
	private String descricao;
	@Element(name = "tipoimovel", data=true, required=false)
	private String tipoimovel;
	@Element(name = "subtipoimovel", data=true, required=false)
	private String subtipoimovel;
	@Element(name = "area_privativa", data=true, required=false)
	private String area_privativa;
	@Element(name = "area_util", data=true, required=false)
	private String area_util;
	@Element(name = "area_total", data=true, required=false)
	private String area_total;
	@Element(name = "dormitorios", data=true, required=false)
	private String dormitorios;
	@Element(name = "banheiro", data=true, required=false)
	private String banheiro;
	@Element(name = "suites", data=true, required=false)
	private String suites;
	@Element(name = "vagas", data=true, required=false)
	private String vagas;
	@Element(name = "cep", data=true, required=false)
	private String cep;
	@Element(name = "numero", data=true, required=false)
	private String numero;
	@Element(name = "complemento", data = true, required = false)
	private String complemento;
	@ElementList(name = "endereco", inline = true, entry = "endereco", required = false)
	private List<Endereco> endereco;
	@Element(name = "ponto_referencia", data=true, required=false)
	private String ponto_referencia;
	@Element(name = "bairro", data=true, required=false)
	private String bairro;
	@Element(name = "cidade", data=true, required=false)
	private String cidade;
	@Element(name = "estado", data=true, required=false)
	private String estado;
	@Element(name = "pais", data=true, required=false)
	private String pais;
	@Element(name = "latitude", data=true, required=false)
	private String latitude;
	@Element(name = "longitude", data=true, required=false)
	private String longitude;
	@ElementList(name = "fotos", inline = true, entry = "fotos", required = false)
	private List<Fotos> fotos;
	@Element(name = "data", data=true, required=false)
	String data;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAnunciante() {
		return anunciante;
	}
	public void setAnunciante(String anunciante) {
		this.anunciante = anunciante;
	}
	public String getDominio() {
		return dominio;
	}
	public void setDominio(String dominio) {
		this.dominio = dominio;
	}
	public String getAnunciante_fone() {
		return anunciante_fone;
	}
	public void setAnunciante_fone(String anunciante_fone) {
		this.anunciante_fone = anunciante_fone;
	}
	public String getAnunciante_email() {
		return anunciante_email;
	}
	public void setAnunciante_email(String anunciante_email) {
		this.anunciante_email = anunciante_email;
	}
	public String getAnunciante_endereco() {
		return anunciante_endereco;
	}
	public void setAnunciante_endereco(String anunciante_endereco) {
		this.anunciante_endereco = anunciante_endereco;
	}
	public String getAnunciante_bairro() {
		return anunciante_bairro;
	}
	public void setAnunciante_bairro(String anunciante_bairro) {
		this.anunciante_bairro = anunciante_bairro;
	}
	public String getAnunciante_cidade() {
		return anunciante_cidade;
	}
	public void setAnunciante_cidade(String anunciante_cidade) {
		this.anunciante_cidade = anunciante_cidade;
	}
	public String getAnunciante_contato() {
		return anunciante_contato;
	}
	public void setAnunciante_contato(String anunciante_contato) {
		this.anunciante_contato = anunciante_contato;
	}
	public String getTransacao() {
		return transacao;
	}
	public void setTransacao(String transacao) {
		this.transacao = transacao;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getPessoas() {
		return pessoas;
	}
	public void setPessoas(String pessoas) {
		this.pessoas = pessoas;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getTipoimovel() {
		return tipoimovel;
	}
	public void setTipoimovel(String tipoimovel) {
		this.tipoimovel = tipoimovel;
	}
	public String getSubtipoimovel() {
		return subtipoimovel;
	}
	public void setSubtipoimovel(String subtipoimovel) {
		this.subtipoimovel = subtipoimovel;
	}
	public String getArea_privativa() {
		return area_privativa;
	}
	public void setArea_privativa(String area_privativa) {
		this.area_privativa = area_privativa;
	}
	public String getArea_util() {
		return area_util;
	}
	public void setArea_util(String area_util) {
		this.area_util = area_util;
	}
	public String getArea_total() {
		return area_total;
	}
	public void setArea_total(String area_total) {
		this.area_total = area_total;
	}
	public String getDormitorios() {
		return dormitorios;
	}
	public void setDormitorios(String dormitorios) {
		this.dormitorios = dormitorios;
	}
	public String getBanheiro() {
		return banheiro;
	}
	public void setBanheiro(String banheiro) {
		this.banheiro = banheiro;
	}
	public String getSuites() {
		return suites;
	}
	public void setSuites(String suites) {
		this.suites = suites;
	}
	public String getVagas() {
		return vagas;
	}
	public void setVagas(String vagas) {
		this.vagas = vagas;
	}
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getComplemento() {
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public List<Endereco> getEndereco() {
		return endereco;
	}
	public void setEndereco(List<Endereco> endereco) {
		this.endereco = endereco;
	}
	public String getPonto_referencia() {
		return ponto_referencia;
	}
	public void setPonto_referencia(String ponto_referencia) {
		this.ponto_referencia = ponto_referencia;
	}
	public String getBairro() {
		return bairro;
	}
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public List<Fotos> getFotos() {
		return fotos;
	}
	public void setFotos(List<Fotos> fotos) {
		this.fotos = fotos;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}

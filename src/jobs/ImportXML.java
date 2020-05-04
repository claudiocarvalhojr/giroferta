package jobs;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import connection.Connection;
import entity.AdvertisementImages;
import entity.Advertisements;
import entity.Categories;
import entity.GoogleCategories;
import entity.Users;
import readerXML.imobex.Foto;
import readerXML.imobex.Fotos;
import readerXML.imobex.Imobex;
import readerXML.rss.Rss;
import utils.Utilities;

public class ImportXML extends TimerTask {
	
	private Connection connection = null;
	
	private int idUser = 0;
	
	int countNewImages = 0;
	
	public ImportXML(Connection connection, int idUser) {
		this.connection = connection;
		this.idUser = idUser;
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
		long initialTime = System.currentTimeMillis();
		int cutOff = 20;
		int countNewAdvertisements = 0;
		int countTotalNewAdvertisements = 0;
		int countImagesAdvertisement = 0;
		int countTotalAdvertisement = 0;
		boolean isNew = false;
		boolean isPrint = true;
		Utilities utilities = new Utilities();
		List<Users> listUsers = null;
		Map<String, Object> param = new HashMap<String, Object>();
		Date dataAtual = null;
		URL url = null;
		HttpURLConnection httpURLConnection = null;
		readerXML.googleShopping.Feed feedGS = null;
		readerXML.adtools.Feed feedAdtools = null;
		Rss rss = null;
		Imobex imobex = null;
		Serializer serializer = null;
		Advertisements advertisement = null;
		GoogleCategories googleCategories = null;
		String valor = null; 
		try {
			utilities.printDataHora(" - IMPORT XML => INICIO ...", isPrint);
			connection.beginTransaction();
			// ADVERTISEMENTS
			googleCategories = (GoogleCategories) connection.find(GoogleCategories.class, 3690);
			if (idUser == 0)
				listUsers = (List<Users>) connection.list(Users.QUERY_IMPORT_XML);
			else {
				param.put("user_id", idUser);
				listUsers = (List<Users>) connection.list(Users.QUERY_IMPORT_XML, param);
				param.clear();
			}
			for (Users user : listUsers) {
//				utilities.printDataHora(" - IMPORT XML => USER/EMAIL/XML: " + user.getId() + " | " + user.getEmail() + " | " + user.getUrl_xml(), isPrint);
				countNewAdvertisements = 0;
				feedGS = null;
				feedAdtools = null;
				rss = null;
				url = new URL(user.getUrl_xml());
				httpURLConnection = (HttpURLConnection) url.openConnection();
				serializer = new Persister();
				if (user.getType_xml() == 1) {
					feedGS = serializer.read(readerXML.googleShopping.Feed.class, httpURLConnection.getInputStream());
					httpURLConnection.disconnect();
					if (feedGS.getEntry() != null) {
						for (readerXML.googleShopping.Entry entry : feedGS.getEntry()) {
							advertisement = null;
							isNew = false;
							dataAtual = new Date();
							countImagesAdvertisement = 0;
							countTotalAdvertisement++;
							// ADVERTISEMENT
							param.put("user_id", user.getId());
							param.put("reference", entry.getId());
							advertisement = (Advertisements) connection.find(Advertisements.QUERY_BY_REFERENCE, param);					
							param.clear();
							if (advertisement == null) {
								advertisement = new Advertisements();
								isNew = true;
							}
							advertisement.setUser_id(user);
							advertisement.setCategory_id((Categories) connection.find(Categories.class, 4));
							advertisement.setGoogle_category_id(googleCategories);
							advertisement.setReference(entry.getId());
							advertisement.setTitle(entry.getTitle());
							advertisement.setDescription(entry.getDescription());
							if (entry.getSale_price() != null) {
								if (entry.getSale_price().endsWith("BRL"))
									advertisement.setPrice(new BigDecimal(entry.getSale_price().substring(0, (entry.getSale_price().length()-4))));
								else
									advertisement.setPrice(new BigDecimal(entry.getSale_price()));
							}
							else {
								if (entry.getPrice().endsWith("BRL"))
									advertisement.setPrice(new BigDecimal(entry.getPrice().substring(0, (entry.getPrice().length()-4))));
								else
									advertisement.setPrice(new BigDecimal(entry.getPrice()));
							}
							if (entry.getBrand() != null)
								advertisement.setBrand(entry.getBrand());
							else
								advertisement.setBrand("");
							if (entry.getCondition().equals("new"))
								advertisement.setItem_state("NOVO");
							else if (entry.getCondition().equals("used"))
								advertisement.setItem_state("USADO");
							else if (entry.getCondition().equals("refurbished"))
								advertisement.setItem_state("RECONDICIONADO	");
							else
								advertisement.setItem_state("0");
							advertisement.setStatus(2);
							if (entry.getAvailability().equals("in stock"))
								advertisement.setAvailability("EM ESTOQUE");
							else if (entry.getAvailability().equals("preorder"))
								advertisement.setAvailability("PRÉ-VENDA");
							else if (entry.getAvailability().equals("out of stock")) {
								advertisement.setAvailability("SEM ESTOQUE");
								advertisement.setStatus(1);
							}
							else
								advertisement.setAvailability("0");
							advertisement.setLink(entry.getLink().getHref());
							advertisement.setUpdated_at(dataAtual);
							if (isNew) {
//								if (user.getCount_stores() > 1)
									advertisement.setAll_stores(1);
//								else
//									advertisement.setAll_stores(0);
								advertisement.setBalance(new BigDecimal(-1));
								advertisement.setQtda_images(0);
								if (user.getType_person().equals("F"))
									advertisement.setMax_images(4);
								else
									advertisement.setMax_images(8);
								advertisement.setUrl("");
								advertisement.setGross_click(0);
								advertisement.setCount_click(0);
								advertisement.setCreated_at(dataAtual);
								countNewAdvertisements++;
								countTotalNewAdvertisements++;
								connection.save(advertisement);
								advertisement.setUrl("advertisement/" + advertisement.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
								connection.update(advertisement);
							}
							else {
//								if (advertisement.getAvailability().compareTo(entry.getAvailability()) != 0) {
//								countImagesAdvertisement = advertisement.getQtda_images();
									advertisement.setUrl("advertisement/" + advertisement.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
									connection.update(advertisement);
//								}
							}
							if (countTotalAdvertisement % cutOff  == 0) {
								connection.getSession().flush();
								connection.getSession().clear();
							}
							// IMAGES
							if (countImagesAdvertisement < advertisement.getMax_images()) {
								if (entry.getImage_link() != null) {
									countImagesAdvertisement++;
									advertisement = setImages(advertisement, entry.getAvailability(), entry.getImage_link(), countImagesAdvertisement, dataAtual);
									if (entry.getAdditional_image_link() != null) {
										for (String aditionalImage : entry.getAdditional_image_link()) {
											countImagesAdvertisement++;
											if (countImagesAdvertisement <= advertisement.getMax_images()) {
												advertisement = setImages(advertisement, entry.getAvailability(), aditionalImage, countImagesAdvertisement, dataAtual);
											}
										}
									}
									connection.update(advertisement);
								}
							}
						}
					}
					if (countNewAdvertisements > 0) {
						user.setCount_advertisements(user.getCount_advertisements() + countNewAdvertisements);
						connection.update(user);
					}
				}
				else if (user.getType_xml() == 2) {
					rss = serializer.read(Rss.class, httpURLConnection.getInputStream());
					httpURLConnection.disconnect();
					if (rss.getEntry() != null) {
						for (readerXML.rss.Entry entry : rss.getEntry()) {
							advertisement = null;
							isNew = false;
							dataAtual = new Date();
							countImagesAdvertisement = 0;
							countTotalAdvertisement++;
							// ADVERTISEMENT
							param.put("user_id", user.getId());
							param.put("reference", entry.getId());
							advertisement = (Advertisements) connection.find(Advertisements.QUERY_BY_REFERENCE, param);					
							param.clear();
							if (advertisement == null) {
								advertisement = new Advertisements();
								isNew = true;
							}
							advertisement.setUser_id(user);
							advertisement.setCategory_id((Categories) connection.find(Categories.class, 4));
							advertisement.setGoogle_category_id(googleCategories);
							advertisement.setReference(entry.getId());
							advertisement.setTitle(entry.getTitle());
							advertisement.setDescription(entry.getSummary());
							if (entry.getSale_price() != null) {
								if (entry.getSale_price().endsWith("BRL"))
									advertisement.setPrice(new BigDecimal(entry.getSale_price().substring(0, (entry.getSale_price().length()-4))));
								else
									advertisement.setPrice(new BigDecimal(entry.getSale_price()));
							}
							else {
								if (entry.getPrice().endsWith("BRL"))
									advertisement.setPrice(new BigDecimal(entry.getPrice().substring(0, (entry.getPrice().length()-4))));
								else
									advertisement.setPrice(new BigDecimal(entry.getPrice()));
							}
							if (entry.getBrand() != null)
								advertisement.setBrand(entry.getBrand());
							else
								advertisement.setBrand("");
							if (entry.getCondition().equals("new"))
								advertisement.setItem_state("NOVO");
							else if (entry.getCondition().equals("used"))
								advertisement.setItem_state("USADO");
							else if (entry.getCondition().equals("refurbished"))
								advertisement.setItem_state("RECONDICIONADO	");
							else
								advertisement.setItem_state("0");
							advertisement.setStatus(2);
							if (entry.getAvailability().equals("in stock"))
								advertisement.setAvailability("EM ESTOQUE");
							else if (entry.getAvailability().equals("preorder"))
								advertisement.setAvailability("PRÉ-VENDA");
							else if (entry.getAvailability().equals("out of stock")) {
								advertisement.setAvailability("SEM ESTOQUE");
								advertisement.setStatus(1);
							}
							else
								advertisement.setAvailability("0");
							advertisement.setLink(entry.getLink());
							advertisement.setUpdated_at(dataAtual);
							if (isNew) {
//								if (user.getCount_stores() > 1)
									advertisement.setAll_stores(1);
//								else
//									advertisement.setAll_stores(0);
								advertisement.setBalance(new BigDecimal(-1));
								advertisement.setQtda_images(0);
								if (user.getType_person().equals("F"))
									advertisement.setMax_images(4);
								else
									advertisement.setMax_images(8);
								advertisement.setUrl("");
								advertisement.setGross_click(0);
								advertisement.setCount_click(0);
								advertisement.setCreated_at(dataAtual);
								countNewAdvertisements++;
								countTotalNewAdvertisements++;
								connection.save(advertisement);
								advertisement.setUrl("advertisement/" + advertisement.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
								connection.update(advertisement);
							}
							else {
//								if (advertisement.getAvailability().compareTo(entry.getAvailability()) != 0) {
//									countImagesAdvertisement = advertisement.getQtda_images();
									advertisement.setUrl("advertisement/" + advertisement.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
									connection.update(advertisement);
//								}
							}
							if (countTotalAdvertisement % cutOff  == 0) {
								connection.getSession().flush();
								connection.getSession().clear();
							}
							// IMAGES
							if (countImagesAdvertisement < advertisement.getMax_images()) {
								if (entry.getImage_link() != null) {
									countImagesAdvertisement++;
									advertisement = setImages(advertisement, entry.getAvailability(), entry.getImage_link(), countImagesAdvertisement, dataAtual);
									if (entry.getAdditional_image_link() != null) {
										for (String aditionalImage : entry.getAdditional_image_link()) {
											countImagesAdvertisement++;
											if (countImagesAdvertisement <= advertisement.getMax_images()) {
												advertisement = setImages(advertisement, entry.getAvailability(), aditionalImage, countImagesAdvertisement, dataAtual);
											}
										}
									}
									connection.update(advertisement);
								}
							}
						}
					}
					if (countNewAdvertisements > 0) {
						user.setCount_advertisements(user.getCount_advertisements() + countNewAdvertisements);
						connection.update(user);
					}
				}
				// IMOBEX...
				else if (user.getType_xml() == 3) {
					imobex = serializer.read(Imobex.class, httpURLConnection.getInputStream());
					httpURLConnection.disconnect();
					if (imobex.getImovel() != null) {
						for (readerXML.imobex.Imovel imovel : imobex.getImovel()) {
							advertisement = null;
							isNew = false;
							dataAtual = new Date();
							countImagesAdvertisement = 0;
							countTotalAdvertisement++;
							// ADVERTISEMENT
							param.put("user_id", user.getId());
							param.put("reference", imovel.getId().trim());
							advertisement = (Advertisements) connection.find(Advertisements.QUERY_BY_REFERENCE, param);					
							param.clear();
							if (advertisement == null) {
								advertisement = new Advertisements();
								isNew = true;
							}
							advertisement.setUser_id(user);
							advertisement.setCategory_id((Categories) connection.find(Categories.class, 3));
							advertisement.setGoogle_category_id(googleCategories);
							advertisement.setReference(imovel.getId().trim());
							advertisement.setTitle(imovel.getTipoimovel() + " no bairro " + imovel.getBairro() + " em " + imovel.getCidade() + " (" + imovel.getEstado() + ")");
							advertisement.setDescription(imovel.getDescricao());
							
							valor = imovel.getValor(); 
							
							if (valor != null) {
								if (imovel.getValor().startsWith("R$ "))
									valor = imovel.getValor().substring(3, (imovel.getValor().length()));
								else
									valor = imovel.getValor().trim();
							}
							
							if (valor == null || valor.length() > 11)
								valor = "0.00";
							advertisement.setPrice(new BigDecimal(valor));
							
							advertisement.setBrand("");
							advertisement.setItem_state("");
							advertisement.setAvailability("0");
							advertisement.setStatus(2);
							
							advertisement.setLink(imovel.getUrl());
							advertisement.setUpdated_at(dataAtual);
							if (isNew) {
//								if (user.getCount_stores() > 1)
									advertisement.setAll_stores(1);
//								else
//									advertisement.setAll_stores(0);
								advertisement.setBalance(new BigDecimal(-1));
								advertisement.setQtda_images(0);
								if (user.getType_person().equals("F"))
									advertisement.setMax_images(4);
								else
									advertisement.setMax_images(8);
								advertisement.setUrl("");
								advertisement.setGross_click(0);
								advertisement.setCount_click(0);
								advertisement.setCreated_at(dataAtual);
								countNewAdvertisements++;
								countTotalNewAdvertisements++;
								connection.save(advertisement);
								advertisement.setUrl("advertisement/" + advertisement.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
								connection.update(advertisement);
							}
							else {
//								countImagesAdvertisement = advertisement.getQtda_images();
								advertisement.setUrl("advertisement/" + advertisement.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
								connection.update(advertisement);
							}
							if (countTotalAdvertisement % cutOff  == 0) {
								connection.getSession().flush();
								connection.getSession().clear();
							}
							// IMAGES
							if (countImagesAdvertisement < advertisement.getMax_images()) {
								if (imovel.getFotos() != null) {
									for (Fotos fotos : imovel.getFotos()) {
										for (Foto foto : fotos.getFoto()) {
											countImagesAdvertisement++;
											if (countImagesAdvertisement <= advertisement.getMax_images()) {
												advertisement = setImages(advertisement, "", foto.getFoto_url(), countImagesAdvertisement, dataAtual);
											}
										}
									}
									connection.update(advertisement);
								}
							}
						}
					}
					if (countNewAdvertisements > 0) {
						user.setCount_advertisements(user.getCount_advertisements() + countNewAdvertisements);
						connection.update(user);
					}
				}
				else if (user.getType_xml() == 4) {
					feedAdtools = serializer.read(readerXML.adtools.Feed.class, httpURLConnection.getInputStream());
					httpURLConnection.disconnect();
					if (feedAdtools.getEntry() != null) {
						for (readerXML.adtools.Entry entry : feedAdtools.getEntry()) {
							advertisement = null;
							isNew = false;
							dataAtual = new Date();
							countImagesAdvertisement = 0;
							countTotalAdvertisement++;
							// ADVERTISEMENT
							param.put("user_id", user.getId());
							param.put("reference", entry.getId());
							advertisement = (Advertisements) connection.find(Advertisements.QUERY_BY_REFERENCE, param);
							param.clear();
							if (advertisement == null) {
								advertisement = new Advertisements();
								isNew = true;
							}
							advertisement.setUser_id(user);
							advertisement.setCategory_id((Categories) connection.find(Categories.class, 4));
							advertisement.setGoogle_category_id(googleCategories);
							advertisement.setReference(entry.getId());
							advertisement.setTitle(entry.getTitle());
							advertisement.setDescription(entry.getSummary());
							if (entry.getSale_price() != null) {
								if (entry.getSale_price().endsWith("BRL"))
									advertisement.setPrice(new BigDecimal(entry.getSale_price().substring(0, (entry.getSale_price().length()-4))));
								else
									advertisement.setPrice(new BigDecimal(entry.getSale_price()));
							}
							else {
								if (entry.getPrice().endsWith("BRL"))
									advertisement.setPrice(new BigDecimal(entry.getPrice().substring(0, (entry.getPrice().length()-4))));
								else
									advertisement.setPrice(new BigDecimal(entry.getPrice()));
							}
							if (entry.getBrand() != null)
								advertisement.setBrand(entry.getBrand());
							else
								advertisement.setBrand("");
							if (entry.getCondition().equals("new"))
								advertisement.setItem_state("NOVO");
							else if (entry.getCondition().equals("used"))
								advertisement.setItem_state("USADO");
							else if (entry.getCondition().equals("refurbished"))
								advertisement.setItem_state("RECONDICIONADO	");
							else
								advertisement.setItem_state("0");
							advertisement.setStatus(2);
							if (entry.getAvailability().equals("in stock"))
								advertisement.setAvailability("EM ESTOQUE");
							else if (entry.getAvailability().equals("preorder"))
								advertisement.setAvailability("PRÉ-VENDA");
							else if (entry.getAvailability().equals("out of stock")) {
								advertisement.setAvailability("SEM ESTOQUE");
								advertisement.setStatus(1);
							}
							else
								advertisement.setAvailability("0");
							advertisement.setLink(entry.getLink());
							advertisement.setUpdated_at(dataAtual);
							if (isNew) {
//								if (user.getCount_stores() > 1)
									advertisement.setAll_stores(1);
//								else
//									advertisement.setAll_stores(0);
								advertisement.setBalance(new BigDecimal(-1));
								advertisement.setQtda_images(0);
								if (user.getType_person().equals("F"))
									advertisement.setMax_images(4);
								else
									advertisement.setMax_images(8);
								advertisement.setUrl("");
								advertisement.setGross_click(0);
								advertisement.setCount_click(0);
								advertisement.setCreated_at(dataAtual);
								countNewAdvertisements++;
								countTotalNewAdvertisements++;
								connection.save(advertisement);
								advertisement.setUrl("advertisement/" + advertisement.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
								connection.update(advertisement);
							}
							else {
//								if (advertisement.getAvailability().compareTo(entry.getAvailability()) != 0) {
//									countImagesAdvertisement = advertisement.getQtda_images();
									advertisement.setUrl("advertisement/" + advertisement.getId() + "/" + utilities.configUrl(advertisement.getTitle()));
									connection.update(advertisement);
//								}
							}
							if (countTotalAdvertisement % cutOff  == 0) {
								connection.getSession().flush();
								connection.getSession().clear();
							}
							// IMAGES
							if (countImagesAdvertisement < advertisement.getMax_images()) {
								if (entry.getImage_link() != null) {
									countImagesAdvertisement++;
									advertisement = setImages(advertisement, entry.getAvailability(), entry.getImage_link(), countImagesAdvertisement, dataAtual);
									if (entry.getAdditional_image_link() != null) {
										for (String aditionalImage : entry.getAdditional_image_link()) {
											countImagesAdvertisement++;
											if (countImagesAdvertisement <= advertisement.getMax_images()) {
												advertisement = setImages(advertisement, entry.getAvailability(), aditionalImage, countImagesAdvertisement, dataAtual);
											}
										}
									}
									connection.update(advertisement);
								}
							}
						}
					}
					if (countNewAdvertisements > 0) {
						user.setCount_advertisements(user.getCount_advertisements() + countNewAdvertisements);
						connection.update(user);
					}
				}
			}
			connection.commit();
			utilities.printDataHora(" - IMPORT XML => ... FIM | ADS: " + countTotalNewAdvertisements + " | IMG: "+ countNewImages + " | TEMPO TOTAL: " + ((System.currentTimeMillis() - initialTime) / 1000) + "s.", isPrint);
		} catch (Exception e) {
			e.printStackTrace();
			connection.rollback();
		} finally {
			connection.closeTransaction();		
			isNew = false;
			isPrint = true;
			utilities = null;
			listUsers = null;
			param = null;
			dataAtual = null;
			url = null;
			httpURLConnection = null;
			feedGS = null;
			feedAdtools = null;
			rss = null;
			imobex = null;
			serializer = null;
			advertisement = null;
			googleCategories = null;
			valor = null;
//			System.gc();
		}
	}

	private Advertisements setImages(Advertisements advertisement, String availability, String urlImage, int countImages, Date dataAtual) {
		AdvertisementImages advertisementImages = null;
		Map<String, Object> param = new HashMap<String, Object>();
		boolean isNewImage = false;
		String ext = "";
		String nameImage = String.valueOf(countImages);
		String[] tokensUrl = urlImage.split("\\.");
		int qtyTokens = tokensUrl.length;
		try {
			if (qtyTokens > 1) {
				ext = tokensUrl[qtyTokens-1].toLowerCase();
				if (ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")) {
					nameImage += "." + ext;
					isNewImage = false;
//					param.put("link", urlImage);									
//					advertisementImages = (AdvertisementImages) connection.find(AdvertisementImages.QUERY_BY_LINK, param);
//					param.clear();
					param.put("file_name", "advertisements/" + advertisement.getId() + "/" + countImages + "." + ext);									
					advertisementImages = (AdvertisementImages) connection.find(AdvertisementImages.QUERY_BY_FILE_NAME, param);
					param.clear();
					if (advertisementImages == null) {
						advertisementImages = new AdvertisementImages();
						isNewImage = true;
					}
					advertisementImages.setAdvertisement_id(advertisement);
					advertisementImages.setImage_file_type(ext);
					advertisementImages.setLink(urlImage);
					advertisementImages.setStatus(1);
					advertisementImages.setUpdated_at(dataAtual);
					if (!advertisementImages.getLink().startsWith("http://") && !advertisementImages.getLink().startsWith("https://"))
						advertisementImages.setLink("http://" + advertisementImages.getLink());
					if (isNewImage) {
						countNewImages++;
						advertisementImages.setImage_file_name("advertisements/" + advertisement.getId() + "/" + nameImage);
						advertisementImages.setCreated_at(dataAtual);
//						advertisement.setQtda_images(advertisement.getQtda_images() + 1);
						connection.save(advertisementImages);
					}
					else  {
//						if (advertisement.getAvailability().compareTo(availability) != 0)
							connection.update(advertisementImages);
					}
				}
			}
		}
		finally {
			advertisementImages = null;
			param = null;
			nameImage = null;
			ext = null;
			tokensUrl = null;
//			System.gc();
		}
		return advertisement;
	}
	
}

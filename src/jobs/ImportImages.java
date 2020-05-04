package jobs;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import connection.Connection;
import entity.AdvertisementImages;
import entity.Advertisements;
import utils.Utilities;

public class ImportImages extends TimerTask {

	private Connection connection = null;
	
	private String pathImages = null;
	
	private int idUser = 0;
	
	private boolean isReImport; 
	
	private int countImportImages = 0; 
	
	public String getPathImages() {
		return pathImages;
	}

	public void setPathImages(String pathImages) {
		this.pathImages = pathImages;
	}

	public ImportImages(Connection connection, String pathImages, int idUser, boolean isReImport) {
		this.connection = connection;
		this.setPathImages(pathImages);
		this.idUser = idUser;
		this.isReImport = isReImport;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		int cutOff = 20;
		long initialTime = System.currentTimeMillis();
		boolean isPrint = true;
		Utilities utilities = new Utilities();
		Date dataAtual = new Date();
		int limitQueryAdvImages = 100;
		int qtdaAtualQueryAdvImages = 0;
		int countImages = 0;
		int totalAdvImages = 0;
		List<AdvertisementImages> listAdvImages = null;
		Map<String, Object> param = null;
		try {
			utilities.printDataHora(" - IMPORT IMAGES => INICIO ...", isPrint);
			countImportImages = 0;
			connection.beginTransaction();
			if (idUser == 0) {
				totalAdvImages = connection.count(AdvertisementImages.QUERY_COUNT_ALL);
				if (totalAdvImages > limitQueryAdvImages) {					
					totalAdvImages /= limitQueryAdvImages;
					for (int i = 0; i <= totalAdvImages; i++) {					
						listAdvImages = (List<AdvertisementImages>) connection.list(AdvertisementImages.QUERY_ALL, qtdaAtualQueryAdvImages, limitQueryAdvImages);
						for (AdvertisementImages advImages : listAdvImages) {
							if (!advImages.getLink().equals("")) {
								countImages++;
								downloadImage(initialTime, advImages, dataAtual, countImages);						
								if (countImages % cutOff  == 0) {
									connection.getSession().flush();
									connection.getSession().clear();
								}
							}
						}						
						qtdaAtualQueryAdvImages += limitQueryAdvImages;
					}
				}
				else {
					listAdvImages = (List<AdvertisementImages>) connection.list(AdvertisementImages.QUERY_ALL);
					for (AdvertisementImages advImages : listAdvImages) {
						if (!advImages.getLink().equals("")) {
							countImages++; 
							downloadImage(initialTime, advImages, dataAtual, countImages);						
							if (countImages % cutOff  == 0) {
								connection.getSession().flush();
								connection.getSession().clear();
							}
						}
					}						
				}
			}
			else {
				param = new HashMap<String, Object>();
				param.put("user_id", idUser);
				totalAdvImages = connection.count(AdvertisementImages.QUERY_COUNT_ALL_BY_USER, param);
				if (totalAdvImages > limitQueryAdvImages) {					
					totalAdvImages /= limitQueryAdvImages;
					for (int i = 0; i <= totalAdvImages; i++) {					
						listAdvImages = (List<AdvertisementImages>) connection.list(AdvertisementImages.QUERY_ALL_BY_USER, param, qtdaAtualQueryAdvImages, limitQueryAdvImages);
						for (AdvertisementImages advImages : listAdvImages) {
							if (advImages.getAdvertisement_id().getUser_id().getId() == idUser && !advImages.getLink().equals("")) {
								countImages++;
								downloadImage(initialTime, advImages, dataAtual, countImages);						
								if (countImages % cutOff  == 0) {
									connection.getSession().flush();
									connection.getSession().clear();
								}
							}
						}						
						qtdaAtualQueryAdvImages += limitQueryAdvImages;
					}
				}
				else {
					listAdvImages = (List<AdvertisementImages>) connection.list(AdvertisementImages.QUERY_ALL_BY_USER, param);
					for (AdvertisementImages advImages : listAdvImages) {
						if (advImages.getAdvertisement_id().getUser_id().getId() == idUser && !advImages.getLink().equals("")) {
							countImages++; 
							downloadImage(initialTime, advImages, dataAtual, countImages);						
							if (countImages % cutOff  == 0) {
								connection.getSession().flush();
								connection.getSession().clear();
							}
						}
					}						
				}
				param.clear();
			}
			connection.commit();
			utilities.printDataHora(" - IMPORT IMAGES => ... FIM | QTDA: " + countImportImages + " | TEMPO TOTAL: " + ((System.currentTimeMillis() - initialTime) / 1000) + "s.", isPrint);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			connection.closeTransaction();
			utilities = null;
			listAdvImages = null;
			dataAtual = null;
			param = null;
//			System.gc();
		}
	}
	
	private void downloadImage(long initialTime, AdvertisementImages advImages, Date dataAtual, int countImages) {
		boolean isPrint = false;
		Utilities utilities = new Utilities();
		URL url = null;
		HttpURLConnection httpConnection = null;
		InputStream imputStream = null;
		BufferedImage image = null;
		BufferedImage finalImage = null;
		File imagePath = null;
		File imageFile = null;
		Advertisements advertisement = null;
		int posX = 0;
		int posY = 0;
		boolean addQty = false;
		try {
			if (advImages.getUpdated_at().compareTo(dataAtual) != 0) {
				imageFile = new File(getPathImages() + advImages.getImage_file_name());
				if (!imageFile.exists() || isReImport) {
					if (!imageFile.exists())
						addQty = true;
					url = new URL(advImages.getLink());
					httpConnection = (HttpURLConnection) url.openConnection();
					httpConnection.setRequestMethod("GET");
					httpConnection.setConnectTimeout(0);
					if (httpConnection.getResponseCode() == 200) {
						imputStream = new BufferedInputStream(httpConnection.getInputStream());
						image = ImageIO.read(imputStream);
						// PAISAGEM (640x480)
//						if (image.getWidth() > image.getHeight()) {
						if (image.getWidth() > image.getHeight() && image.getWidth() > 640) {
							image = utilities.resizeImage(image, 640, 480);
							posX = 0;
							posY = 0;
						} else if (image.getWidth() > image.getHeight() && image.getWidth() < 640) {
							posX = (640 - image.getWidth()) / 2;
							posY = (480 - image.getHeight()) / 2;
						}
						// RETRATO (360x480)
//						else if (image.getHeight() > image.getWidth()) {
						else if (image.getHeight() > image.getWidth() && image.getHeight() > 480) {
							image = utilities.resizeImage(image, 360, 480);
							posX = 140;
							posY = 0;
						} else if (image.getHeight() > image.getWidth() && image.getHeight() < 480) {
							posX = (640 - image.getWidth()) / 2;
							posY = (480 - image.getHeight()) / 2;
						}
						// QUADRADA (480x480)
//						else if (image.getWidth() == image.getHeight()) {
						else if (image.getWidth() == image.getHeight() && image.getWidth() > 480) {
							image = utilities.resizeImage(image, 480, 480);
							posX = 80;
							posY = 0;
						} else if (image.getWidth() == image.getHeight() && image.getWidth() < 480) {
							posX = (640 - image.getWidth()) / 2;
							posY = (480 - image.getHeight()) / 2;
						}
						finalImage = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
						Graphics graphics = finalImage.createGraphics();
						graphics.setColor(Color.WHITE);
						graphics.fillRect(0, 0, 640, 480);
						graphics.drawImage(image, posX, posY, null);
				        graphics.dispose();		
						
						imagePath = new File(getPathImages() + "advertisements/" + advImages.getAdvertisement_id().getId() + "/");
						if (!imagePath.exists()) {
							imagePath.mkdirs();
							imagePath.setWritable(true, true);
						}
					
						imageFile.setWritable(true, true);
//						if (uploadFile.length() > 0 && uploadFile.length() <= 2097152) {
							ImageIO.write(finalImage, advImages.getImage_file_type(), imageFile);
							advImages.setUpdated_at(dataAtual);
							connection.update(advImages);
							countImportImages++;
							utilities.printDataHora(" - IMPORT IMAGES => " + countImportImages + " | USER: " + advImages.getAdvertisement_id().getUser_id().getId() + " | ADS: " + advImages.getAdvertisement_id().getId() + " | IMG: " + advImages.getId() + " | NAME: " + advImages.getImage_file_name() + " | IMAGE: " + imageFile.getName() + " | ADDQTY: " + addQty, isPrint);
//						}
						if (addQty) {
							advertisement = advImages.getAdvertisement_id();
							advertisement.setQtda_images(advertisement.getQtda_images() + 1);
							connection.update(advertisement);
						}
					}
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			utilities = new Utilities();
			url = null;
			httpConnection = null;
			imputStream = null;
			image = null; 
			imagePath = null;
			imageFile = null;
			advertisement = null;
//			System.gc();
		}
	}
	
}

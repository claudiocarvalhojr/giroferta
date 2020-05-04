package jobs;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import connection.Connection;
import entity.AdvertisementImages;
import entity.Advertisements;
import utils.Utilities;

public class FixesImages extends TimerTask {

	private Connection connection = null;
	
	private String pathImages = null;
	
	private int countFixedImages = 0;
	
	public String getPathImages() {
		return pathImages;
	}

	public void setPathImages(String pathImages) {
		this.pathImages = pathImages;
	}

	public FixesImages(Connection connection, String pathImages) {
		this.connection = connection;
		this.setPathImages(pathImages);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		int cutOff = 20;
		long initialTime = System.currentTimeMillis();
		boolean isPrint = true;
		boolean isPrintAdjFileNameFolder = false;
		boolean isPrintAdjFileNameTable = false;
		boolean isPrintAdjDeleteImage = false;
		boolean isPrintAdjDeletedFolder = false;
		boolean isPrintAdjQtdaTable = false;
		Utilities utilities = new Utilities();
		Map<String, Object> param = new HashMap<String, Object>();
		File pathImages = null;
		File pathImage = null;
		File renameFile = null;
		File[] listFiles = null;
		Advertisements advertisement = null;
		String[] files = null;
//		BasicFileAttributes attr = null;
//		Path path = Paths.get(pathImages.getAbsolutePath());
		int count = 0;
		int countAdjNameFileFolder = 0;
		int countAdjNameFileTable = 0;
		int countAdjDeleteImage = 0;
		int countAdjDeleteFolder = 0;
		int countAdjQtdaTable = 0;
		try {
			utilities.printDataHora(" - FIXES IMAGES => INICIO ...", isPrint);
			countFixedImages = 0;
			connection.beginTransaction();

			pathImages = new File(getPathImages() + "advertisements/");
			
			// CORRIGE FILE_NAME NO FILE SYSTEM
			for(File dir : pathImages.listFiles()) {
				if (dir.isDirectory()) {
			        if (dir.listFiles().length > 1) {
			        	count = 0;
				        files = dir.list();  
				        for (int i=0; i<files.length; i++) {
				        	count++;
				        	countFixedImages++;
				        	countAdjNameFileFolder++;
				        	renameFile = new File(dir, files[i]);
//				        	path = Paths.get(file.getAbsolutePath());
//				        	attr = Files.readAttributes(path, BasicFileAttributes.class);
//				        	System.out.println(countFixedImages + ") DIR: " + dir.getName() + " - NAME: " + file.getName() + " - CREATED: " + attr.creationTime());
				        	renameFile.renameTo(new File(dir, count + "." + renameFile.getName().substring(renameFile.getName().lastIndexOf(".") + 1)));
							utilities.printDataHora(" - FIXES IMAGES => " + countAdjNameFileFolder + ") FILE NAME CORRIGIDO NA PASTA - ID: " + dir.getName() + " - NAME: " + renameFile.getName(), isPrintAdjFileNameFolder);
				        }
					}
				}
			}
			
			// CORRIGE FILE_NAME NA TABLE
			for (Advertisements adv : (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_ALL)) {
				param.put("advertisement_id", adv.getId());
				count = 0;
				for (AdvertisementImages advImage : (List<AdvertisementImages>) connection.list(AdvertisementImages.QUERY_BY_ADVERTISEMENT, param)) {
					count++;
					countFixedImages++;
					countAdjNameFileTable++;
					advImage.setImage_file_name("advertisements/" + adv.getId() + "/" + count + "." + advImage.getImage_file_type());
					connection.update(advImage);
					utilities.printDataHora(" - FIXES IMAGES => " + countAdjNameFileTable + ") FILE NAME CORRIGIDO NA TABELA - ID: " +adv.getId() + " - NAME: " + advImage.getImage_file_name(), isPrintAdjFileNameTable);
				}
				param.clear();
				if (countAdjNameFileTable % cutOff  == 0) {
					connection.getSession().flush();
					connection.getSession().clear();
				}
			}
			
			// DELETA PASTA SEM IMG OU SEM ADS
			for(File file : pathImages.listFiles()) {
				if (file.listFiles().length == 0) {
					countFixedImages++;
					countAdjDeleteFolder++;
					deleteDir(file);
					utilities.printDataHora(" - FIXES IMAGES => " + countAdjDeleteFolder + ") FOLDER EMPTY, PASTA DELETADA - " + file.getName(), isPrintAdjDeletedFolder);
				}
				else {
					advertisement = (Advertisements) connection.find(Advertisements.class, Integer.parseInt(file.getName()));
					if (advertisement == null) {
						countFixedImages++;
						countAdjDeleteFolder++;
						deleteDir(file);
						utilities.printDataHora(" - FIXES IMAGES => " + countAdjDeleteFolder + ") NOT FOUND ADS, PASTA DELETADA - " + file.getName(), isPrintAdjDeletedFolder);
					}
				}
			}
			
			// DELETA IMG SEM ADS_IMG
			for (File dir : pathImages.listFiles()) {
				if (dir.isDirectory()) {
			        if (dir.listFiles().length > 1) {
			        	listFiles = dir.listFiles();
				        for (int i = 0; i < listFiles.length; i++) {
							param.put("file_name", "advertisements/" + dir.getName() + "/" + listFiles[i].getName());									
							if (connection.find(AdvertisementImages.QUERY_BY_FILE_NAME, param) == null) {
								countFixedImages++;
								countAdjDeleteImage++;
								deleteDir(listFiles[i]);
								utilities.printDataHora(" - FIXES IMAGES => " + countAdjDeleteImage + ") IMG DELETADA: " + dir.getName() + "/" + listFiles[i].getName(), isPrintAdjDeleteImage);
							}
							param.clear();
				        }
			        }
				}
			}

			// CORRIGE QTDA IMG NO ADS
			for (Advertisements adv : (List<Advertisements>) connection.list(Advertisements.QUERY_LIST_ALL)) {
				pathImage = new File(getPathImages() + "advertisements/" + adv.getId() + "/");
				if (pathImage.exists()) {
					if (pathImage.listFiles().length != adv.getQtda_images()) {
						if (pathImage.listFiles().length <= adv.getMax_images()) {
							countFixedImages++;
							countAdjQtdaTable++;
							adv.setQtda_images(pathImage.listFiles().length);
							connection.update(adv);
							utilities.printDataHora(" - FIXES IMAGES => " + countAdjQtdaTable + ") QTDA IMG ADS, CORRIGIDO TABLE - " + adv.getId(), isPrintAdjQtdaTable);
						}
						else {
							countFixedImages++;
							countAdjQtdaTable++;
							adv.setQtda_images(adv.getMax_images());
							connection.update(adv);
							utilities.printDataHora(" - FIXES IMAGES => " + countAdjQtdaTable + ") QTDA IMG ADS, CORRIGIDO TABLE - " + adv.getId(), isPrintAdjQtdaTable);
						}
					}
				}
				else {
					countFixedImages++;
					countAdjQtdaTable++;
					adv.setQtda_images(0);
					connection.update(adv);
					utilities.printDataHora(" - FIXES IMAGES => " + countAdjQtdaTable + ") QTDA IMG ADS, CORRIGIDO TABLE - " + adv.getId(), isPrintAdjQtdaTable);
				}
				if (countAdjQtdaTable % cutOff  == 0) {
					connection.getSession().flush();
					connection.getSession().clear();
				}
			}
			
			connection.commit();
			utilities.printDataHora(" - FIXES IMAGES => ... FIM | TOTAL: " + countFixedImages + " | NAME IN FOLDER: " + countAdjNameFileFolder + " | NAME IN TABLE: " + countAdjNameFileTable + " | DEL FOLDER: " + countAdjDeleteFolder + " | DEL IMG: " + countAdjDeleteImage + " | QTDA IMG IN ADS: " + countAdjQtdaTable + " | TEMPO TOTAL: " + ((System.currentTimeMillis() - initialTime) / 1000) + "s.", isPrint);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			connection.closeTransaction();
			utilities = null;
			utilities = null;
			param = null;
			pathImages = null;
			pathImage = null;
			renameFile = null;
			listFiles = null;
			advertisement = null;
			files = null;
//			attr = null;
//			path = null;
//			System.gc();
		}
	}
	
	public boolean deleteDir(File dir) {  
	    if (dir.isDirectory()) {  
	        String[] children = dir.list();  
	        for (int i=0; i<children.length; i++) {  
	            boolean success = deleteDir(new File(dir, children[i]));  
	            if (!success) {  
	                return false;  
	            }  
	        }  
	    }  
	    // The directory is now empty so delete it  
	    return dir.delete();  
	}  
	
}

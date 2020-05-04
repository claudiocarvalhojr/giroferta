package config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Config {
	
	public void setConfig(HttpServletRequest request, HttpServletResponse response) {
		
		String giroUrl = (String) request.getSession().getAttribute("giroUrl");
		String controller = giroUrl + "Controller";
		String controllerJson = giroUrl + "./Controller";
		String form = "form";
		
		String HOME = "home.jsp";
		String LOGIN = "login.jsp";
		String ERROR = "pageNotFound.jsp";
		
		String admin = "actions.AdminActions";
		String advertisements = "actions.AdvertisementsActions";
		String chat = "actions.ChatActions";
		String newsletterlists = "actions.NewsletterListsActions";
		String pagseguro = "actions.PagSeguroActions";
		String stores = "actions.StoresActions";
		String users = "actions.UsersActions";
		
		request.getSession().setAttribute("HOME", HOME);
		request.getSession().setAttribute("LOGIN", LOGIN);
		request.getSession().setAttribute("ERROR", ERROR);
		
		// HOME
		request.getSession().setAttribute("linkHome", "home");
		
		// LOGIN
		request.getSession().setAttribute("linkLogin", request.getServletContext().getContextPath() + "/login");
		request.getSession().setAttribute("linkCheckLogin", request.getServletContext().getContextPath() + "/checklogin");
		request.getSession().setAttribute("linkLogout", request.getServletContext().getContextPath() + "/logout");
		request.getSession().setAttribute("linkPageNotFound", request.getServletContext().getContextPath() + "/pageNotFound");
		request.getSession().setAttribute("linkPageUnavailable", request.getServletContext().getContextPath() + "/pageUnavailable");
		
//		request.getSession().setAttribute("linkAdvertisement", request.getServletContext().getContextPath() + "/advertisement");
		request.getSession().setAttribute("linkAdvertisementsList", request.getServletContext().getContextPath() + "/meus-anuncios");
		request.getSession().setAttribute("linkAdvertisementsListFirst", request.getServletContext().getContextPath() + "/meus-anuncios/first");
		request.getSession().setAttribute("linkAdvertisementsListPrevious", request.getServletContext().getContextPath() + "/meus-anuncios/previous");
		request.getSession().setAttribute("linkAdvertisementsListNext", request.getServletContext().getContextPath() + "/meus-anuncios/next");
		request.getSession().setAttribute("linkAdvertisementsListLast", request.getServletContext().getContextPath() + "/meus-anuncios/last");
		request.getSession().setAttribute("linkAdvertisementsListSearch", request.getServletContext().getContextPath() + "/meus-anuncios/search");
		request.getSession().setAttribute("linkAdvertisementsPublish", request.getServletContext().getContextPath() + "/meus-anuncios/publish");
		request.getSession().setAttribute("linkAdvertisementsUnpublish", request.getServletContext().getContextPath() + "/meus-anuncios/unpublish");

		request.getSession().setAttribute("linkStoresList", request.getServletContext().getContextPath() + "/minhas-lojas");
		request.getSession().setAttribute("linkStoresListFirst", request.getServletContext().getContextPath() + "/minhas-lojas/first");
		request.getSession().setAttribute("linkStoresListPrevious", request.getServletContext().getContextPath() + "/minhas-lojas/previous");
		request.getSession().setAttribute("linkStoresListNext", request.getServletContext().getContextPath() + "/minhas-lojas/next");
		request.getSession().setAttribute("linkStoresListLast", request.getServletContext().getContextPath() + "/minhas-lojas/last");
		request.getSession().setAttribute("linkStoresListSearch", request.getServletContext().getContextPath() + "/minhas-lojas/search");

//		request.getSession().setAttribute("linkLogin", controller + "?" + form + "=" + users + "&action=login" + "&panel=true");
//		request.getSession().setAttribute("linkCheckLogin", controller + "?" + form + "=" + users + "&action=check" + "&panel=true");
//		request.getSession().setAttribute("linkLogout", controller + "?" + form + "=" + users + "&action=logout" + "&panel=false");
		request.getSession().setAttribute("linkLoginSocial", controller + "?" + form + "=" + users + "&action=loginSocial" + "&panel=true");

		// VENDOR
		request.getSession().setAttribute("linkSales", request.getServletContext().getContextPath() + "/sales");
		request.getSession().setAttribute("linkVendorNewAccount", controller + "?" + form + "=" + users + "&action=form&panel=true&newAccount=true");
		request.getSession().setAttribute("linkVendorMyAccountsList", request.getServletContext().getContextPath() + "/minhas-contas");
		request.getSession().setAttribute("linkVendorMyAccountsListFirst", request.getServletContext().getContextPath() + "/minhas-contas/first");
		request.getSession().setAttribute("linkVendorMyAccountsListPrevious", request.getServletContext().getContextPath() + "/minhas-contas/previous");
		request.getSession().setAttribute("linkVendorMyAccountsListNext", request.getServletContext().getContextPath() + "/minhas-contas/next");
		request.getSession().setAttribute("linkVendorMyAccountsListLast", request.getServletContext().getContextPath() + "/minhas-contas/last");
		request.getSession().setAttribute("linkVendorMyAccountsListSearch", request.getServletContext().getContextPath() + "/minhas-contas/search");
		request.getSession().setAttribute("linkVendorCourtesyForm", controller + "?" + form + "=" + admin + "&action=courtesyForm&panel=true");
		request.getSession().setAttribute("linkVendorCourtesySearch", controller + "?" + form + "=" + admin + "&action=courtesySearch&panel=true");
		request.getSession().setAttribute("linkVendorCourtesyApply", controller + "?" + form + "=" + admin + "&action=courtesyApply&panel=true");
		
		// ADMIN
		request.getSession().setAttribute("linkAdminManager", request.getServletContext().getContextPath() + "/managerSite");
		request.getSession().setAttribute("linkAdminJobs", request.getServletContext().getContextPath() + "/jobs");
		request.getSession().setAttribute("linkAdminReports", request.getServletContext().getContextPath() + "/reports");
		request.getSession().setAttribute("linkAdminManageAccountsForm", controller + "?" + form + "=" + admin + "&action=manageAccountsForm&panel=true");
		request.getSession().setAttribute("linkAdminManageAccountsSearch", controller + "?" + form + "=" + admin + "&action=manageAccountsSearch&panel=true");
		request.getSession().setAttribute("linkAdminManageAccountsApplyActive", controller + "?" + form + "=" + admin + "&action=manageAccountsApplyActive&panel=true");
		request.getSession().setAttribute("linkAdminManageAccountsApplyProfile", controller + "?" + form + "=" + admin + "&action=manageAccountsApplyProfile&panel=true");
		request.getSession().setAttribute("linkAdminManageAccountsApplyPassword", controller + "?" + form + "=" + admin + "&action=manageAccountsApplyPassword&panel=true");
		request.getSession().setAttribute("linkAdminManageAccountsApplyExtraCredit", controller + "?" + form + "=" + admin + "&action=manageAccountsApplyExtraCredit&panel=true");
		request.getSession().setAttribute("linkAdminManageAccountsApplyCurrentClick", controller + "?" + form + "=" + admin + "&action=manageAccountsApplyCurrentClick&panel=true");
		request.getSession().setAttribute("linkAdminManageAccountsApplyImportXml", controller + "?" + form + "=" + admin + "&action=manageAccountsApplyImportXml&panel=true");
		request.getSession().setAttribute("linkAdminManageAccountsApplyTypeXml", controller + "?" + form + "=" + admin + "&action=manageAccountsApplyTypeXml&panel=true");
		request.getSession().setAttribute("linkAdminManageAccountsApplyUrlXml", controller + "?" + form + "=" + admin + "&action=manageAccountsApplyUrlXml&panel=true");
		request.getSession().setAttribute("linkAdminReportByAdv", request.getServletContext().getContextPath() + "/reportAdminByAdv");
		request.getSession().setAttribute("linkAdminReportByAdvFirst", request.getServletContext().getContextPath() + "/reportAdminByAdv/first");
		request.getSession().setAttribute("linkAdminReportByAdvPrevious", request.getServletContext().getContextPath() + "/reportAdminByAdv/previous");
		request.getSession().setAttribute("linkAdminReportByAdvNext", request.getServletContext().getContextPath() + "/reportAdminByAdv/next");
		request.getSession().setAttribute("linkAdminReportByAdvLast", request.getServletContext().getContextPath() + "/reportAdminByAdv/last");
		request.getSession().setAttribute("linkAdminReportByAdvSearch", request.getServletContext().getContextPath() + "/reportAdminByAdv/search");
		request.getSession().setAttribute("linkAdminReportByDate", request.getServletContext().getContextPath() + "/reportAdminByDate");
		request.getSession().setAttribute("linkAdminReportByDateFirst", request.getServletContext().getContextPath() + "/reportAdminByDate/first");
		request.getSession().setAttribute("linkAdminReportByDatePrevious", request.getServletContext().getContextPath() + "/reportAdminByDate/previous");
		request.getSession().setAttribute("linkAdminReportByDateNext", request.getServletContext().getContextPath() + "/reportAdminByDate/next");
		request.getSession().setAttribute("linkAdminReportByDateLast", request.getServletContext().getContextPath() + "/reportAdminByDate/last");
		request.getSession().setAttribute("linkAdminReportByDateSearch", request.getServletContext().getContextPath() + "/reportAdminByDate/search");
		request.getSession().setAttribute("linkAdminFullProccess", controllerJson + "?" + form + "=" + admin + "&action=resource&op=fullproccess&panel=true");
		request.getSession().setAttribute("linkAdminImportXML", controllerJson + "?" + form + "=" + admin + "&action=resource&op=xml&panel=true");
		request.getSession().setAttribute("linkAdminReplyAdsStores", controllerJson + "?" + form + "=" + admin + "&action=resource&op=reply&panel=true");
		request.getSession().setAttribute("linkAdminImportImages", controllerJson + "?" + form + "=" + admin + "&action=resource&op=images&panel=true");
		request.getSession().setAttribute("linkAdminFixesImages", controllerJson + "?" + form + "=" + admin + "&action=resource&op=fixes&panel=true");
		request.getSession().setAttribute("linkAdminUpdateHighLights", controllerJson + "?" + form + "=" + admin + "&action=resource&op=highlights&panel=true");
		request.getSession().setAttribute("linkAdminUpdateSitemap", controllerJson + "?" + form + "=" + admin + "&action=resource&op=sitemap&panel=true");
		request.getSession().setAttribute("linkAdminUpdateURLs", controllerJson + "?" + form + "=" + admin + "&action=resource&op=urls&panel=true");
		
		// ADVERTISEMENTS
//		request.getSession().setAttribute("linkAdvertisement", controller + "?" + form + "=" + advertisements + "&action=advertisement&panel=true");
		request.getSession().setAttribute("linkAdvertisementsForm", controller + "?" + form + "=" + advertisements + "&action=form&panel=true");
		request.getSession().setAttribute("linkAdvertisementsSave", controller + "?" + form + "=" + advertisements + "&action=save&panel=true");
//		request.getSession().setAttribute("linkAdvertisementsPublish", controller + "?" + form + "=" + advertisements + "&action=publish&panel=true");
		request.getSession().setAttribute("linkAdvertisementsDelete", controller + "?" + form + "=" + advertisements + "&action=delete&panel=true");
//		request.getSession().setAttribute("linkAdvertisementsList", controller + "?" + form + "=" + advertisements + "&action=list&panel=true&current=0");
//		request.getSession().setAttribute("linkAdvertisementsListFirst", controller + "?" + form + "=" + advertisements + "&action=list&panel=true");
//		request.getSession().setAttribute("linkAdvertisementsListPrevious", controller + "?" + form + "=" + advertisements + "&action=list&panel=true");
//		request.getSession().setAttribute("linkAdvertisementsListNext", controller + "?" + form + "=" + advertisements + "&action=list&panel=true");
//		request.getSession().setAttribute("linkAdvertisementsListLast", controller + "?" + form + "=" + advertisements + "&action=list&panel=true");
//		request.getSession().setAttribute("linkAdvertisementsListSearch", controller + "?" + form + "=" + advertisements + "&action=list&panel=true");
		request.getSession().setAttribute("linkAdvertisementsImportCsvTxt", controller + "?" + form + "=" + advertisements + "&action=importCsvTxt&panel=true");
		request.getSession().setAttribute("linkAdvertisementsImportXml", controller + "?" + form + "=" + advertisements + "&action=importXml&panel=true");
		request.getSession().setAttribute("linkAdvertisementsImportRunCsvTxt", controller + "?" + form + "=" + advertisements + "&action=importRunCsvTxt&panel=true");
		request.getSession().setAttribute("linkAdvertisementsImportRunXml", controller + "?" + form + "=" + advertisements + "&action=importRunXml&panel=true");
		request.getSession().setAttribute("linkAdvertisementsCount", controller + "?" + form + "=" + advertisements + "&action=resource&op=count");
		request.getSession().setAttribute("linkAdvertisementsJson", controller + "?" + form + "=" + advertisements + "&action=resource&op=json");
		request.getSession().setAttribute("linkAdvertisementsJsonHome", controller + "?" + form + "=" + advertisements + "&action=resource&op=jsonHome");
		request.getSession().setAttribute("linkAdvertisementsJsonAdvertisement", controller + "?" + form + "=" + advertisements + "&action=resource&op=jsonAdvertisement");
		request.getSession().setAttribute("linkAdvertisementsJsonCoord", controller + "?" + form + "=" + advertisements + "&action=resource&op=jsonCoord");
//		request.getSession().setAttribute("linkAdvertisementsJsonImages", controller + "?" + form + "=" + advertisements + "&action=resource&op=jsonImages");
		request.getSession().setAttribute("linkAdvertisementsWarnMe", controller + "?" + form + "=" + advertisements + "&action=resource&op=warnMe");
		request.getSession().setAttribute("linkAdvertisementImagesUpload", controller + "?" + form + "=" + advertisements + "&action=resource&op=uploadImage");
		request.getSession().setAttribute("linkAdvertisementImagesView", controller + "?" + form + "=" + advertisements + "&action=resource&op=viewImage");
		request.getSession().setAttribute("linkAdvertisementImagesDelete", controller + "?" + form + "=" + advertisements + "&action=resource&op=deleteImage");
		request.getSession().setAttribute("linkAdvertisementImagesCategories", controller + "?" + form + "=" + advertisements + "&action=resource&op=categories");
		request.getSession().setAttribute("linkAdvertisementsReportByAdv", request.getServletContext().getContextPath() + "/reportByAdv");
		request.getSession().setAttribute("linkAdvertisementsReportByAdvFirst", request.getServletContext().getContextPath() + "/reportByAdv/first");
		request.getSession().setAttribute("linkAdvertisementsReportByAdvPrevious", request.getServletContext().getContextPath() + "/reportByAdv/previous");
		request.getSession().setAttribute("linkAdvertisementsReportByAdvNext", request.getServletContext().getContextPath() + "/reportByAdv/next");
		request.getSession().setAttribute("linkAdvertisementsReportByAdvLast", request.getServletContext().getContextPath() + "/reportByAdv/last");
		request.getSession().setAttribute("linkAdvertisementsReportByAdvSearch", request.getServletContext().getContextPath() + "/reportByAdv/search");
		request.getSession().setAttribute("linkAdvertisementsReportByDate", request.getServletContext().getContextPath() + "/reportByDate");
		request.getSession().setAttribute("linkAdvertisementsReportByDateFirst", request.getServletContext().getContextPath() + "/reportByDate/first");
		request.getSession().setAttribute("linkAdvertisementsReportByDatePrevious", request.getServletContext().getContextPath() + "/reportByDate/previous");
		request.getSession().setAttribute("linkAdvertisementsReportByDateNext", request.getServletContext().getContextPath() + "/reportByDate/next");
		request.getSession().setAttribute("linkAdvertisementsReportByDateLast", request.getServletContext().getContextPath() + "/reportByDate/last");
		request.getSession().setAttribute("linkAdvertisementsReportByDateSearch", request.getServletContext().getContextPath() + "/reportByDate/search");
		
		//CHAT
		request.getSession().setAttribute("linkChatForm", controller + "?" + form + "=" + chat + "&action=form&panel=true");
		request.getSession().setAttribute("linkChatSave", controller + "?" + form + "=" + chat + "&action=save&panel=true");
		request.getSession().setAttribute("linkChatList", controller + "?" + form + "=" + chat + "&action=list&panel=true&current=0");
		request.getSession().setAttribute("linkChatCreate", controller + "?" + form + "=" + chat + "&action=resource&op=create");
		
		//NEWSLETTERLISTS
		request.getSession().setAttribute("linkNewsletterLists", controller + "?" + form + "=" + newsletterlists + "&action=resource&op=json");
		
		//PAGSEGURO
		request.getSession().setAttribute("linkPagSeguroBuyCredit", controller + "?" + form + "=" + pagseguro + "&action=form&panel=true");
		request.getSession().setAttribute("linkPagSeguroInterval", controller + "?" + form + "=" + pagseguro + "&action=resource&op=interval");
		
		// STORES
//		request.getSession().setAttribute("linkStoresForm", request.getServletContext().getContextPath() + "/store");
		request.getSession().setAttribute("linkStoresForm", controller + "?" + form + "=" + stores + "&action=form&panel=true");
		request.getSession().setAttribute("linkStoresSave", controller + "?" + form + "=" + stores + "&action=save&panel=true");
		request.getSession().setAttribute("linkStoresDelete", controller + "?" + form + "=" + stores + "&action=delete&panel=true");
//		request.getSession().setAttribute("linkStoresList", controller + "?" + form + "=" + stores + "&action=list&panel=true&current=0");
//		request.getSession().setAttribute("linkStoresListNav", controller + "?" + form + "=" + stores + "&action=list&panel=true");
//		request.getSession().setAttribute("linkStoresListSearch", controller + "?" + form + "=" + stores + "&action=list&panel=true");
		request.getSession().setAttribute("linkStoresJson", controller + "?" + form + "=" + stores + "&action=resource&op=json");
		request.getSession().setAttribute("linkStoresJsonImages", controller + "?" + form + "=" + stores + "&action=resource&op=jsonImages");
		request.getSession().setAttribute("linkUserImageLogoUpload", controller + "?" + form + "=" + users + "&action=resource&op=uploadImageLogo");
		request.getSession().setAttribute("linkUserImageLogoDelete", controller + "?" + form + "=" + users + "&action=resource&op=deleteImageLogo");
		request.getSession().setAttribute("linkStoreImagesUpload", controller + "?" + form + "=" + stores + "&action=resource&op=uploadImage");
		request.getSession().setAttribute("linkStoreImagesView", controller + "?" + form + "=" + stores + "&action=resource&op=viewImage");
		request.getSession().setAttribute("linkStoreImagesDelete", controller + "?" + form + "=" + stores + "&action=resource&op=deleteImage");

		// USERS
		request.getSession().setAttribute("linkUsersForm", request.getServletContext().getContextPath() + "/register");
		request.getSession().setAttribute("linkUsersFormEdit", controller + "?" + form + "=" + users + "&action=form&panel=true&newAccount=false");
		request.getSession().setAttribute("linkUsersSave", controller + "?" + form + "=" + users + "&action=save&panel=true");
		request.getSession().setAttribute("linkUsersDelete", controller + "?" + form + "=" + users + "&action=delete&panel=true");
		request.getSession().setAttribute("linkUsersList", controller + "?" + form + "=" + users + "&action=list&panel=true");
		request.getSession().setAttribute("linkUsersFormSendInstructionsResetPassword", request.getServletContext().getContextPath() + "/resetpassword");
//		request.getSession().setAttribute("linkUsersFormSendInstructionsResetPassword", controller + "?" + form + "=" + users + "&action=formSendInstructionsResetPassword&panel=true");
		request.getSession().setAttribute("linkUsersSendInstructionsResetPassword", controller + "?" + form + "=" + users + "&action=sendInstructionsResetPassword&panel=true");
		request.getSession().setAttribute("linkUsersFormResetPassword", controller + "?" + form + "=" + users + "&action=formResetPassword&panel=true");
		request.getSession().setAttribute("linkUsersConfirmResetPassword", controller + "?" + form + "=" + users + "&action=confirmResetPassword&panel=true");
		request.getSession().setAttribute("linkUsersFormUpdatePassword", controller + "?" + form + "=" + users + "&action=formUpdatePassword&panel=true");
		request.getSession().setAttribute("linkUsersConfirmUpdatePassword", controller + "?" + form + "=" + users + "&action=confirmUpdatePassword&panel=true");
		request.getSession().setAttribute("linkUserImagesUpload", controller + "?" + form + "=" + users + "&action=resource&op=uploadImage");
		request.getSession().setAttribute("linkUserImagesView", controller + "?" + form + "=" + users + "&action=resource&op=viewImage");
		request.getSession().setAttribute("linkUserImagesDelete", controller + "?" + form + "=" + users + "&action=resource&op=deleteImage");
		request.getSession().setAttribute("linkUsersPurchasesHistoric", controller + "?" + form + "=" + users + "&action=purchases&panel=true&current=0");
		request.getSession().setAttribute("linkUserTerms", request.getServletContext().getContextPath() + "/terms");
		
//		request.getSession().setAttribute("linkUsersForm", controller + "?" + form + "=" + users + "&action=form&panel=true");
//		request.getSession().setAttribute("linkUsersReport", controller + "?" + form + "=" + users + "&action=report&panel=true&current=0");
//		request.getSession().setAttribute("linkUserTerms", controller + "?" + form + "=" + users + "&action=terms&panel=true");

	}

}

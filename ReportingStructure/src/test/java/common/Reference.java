package common;

//import apppages.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.sql.Driver;


public class Reference {

    private static Reference refSingleton;
    /**
     * @param args
     */
    public WebDriver driver;
    /*public Login login;
    public JQAGatewayLoginPage jqaGatewayLoginPage;
    public JQAGatewayUploadPage jqaGatewayUploadPage;
    public JQAGatewayPackagePage jqaGatewayPackagePage;
    public JQAGatewayArticlesPage jqaGatewayArticlesPage;
    public JQAGatewayHomePage jqaGatewayHomePage;
    public JQACADashboardPage jqacaDashboardPage;
    public JQAAdminPage jqaAdminPage;
    public JQAHomePage jqaHomePage;
    public JQAArticlePreviewPage jqaArticlePreviewPage;
    public JQAPublishedArticlesPage jqaPublishedArticlesPage;
    public JQACSDshboardPage jqacsDshboardPage;
    public JQAActivityLogPage jqaactivitylogpage;
    public MetaDataServicesPage MetaDataServicesPage;
    public JQASADashboardPage JQASADashboardPage;
    public CCHAndDSSPage CCHAndDSSPage;
    public JQAWileyEmail JQAWileyEmail;*/

    public Reference() {
        /*login = PageFactory.initElements(driver, Login.class);
        jqaGatewayLoginPage = PageFactory.initElements(driver, JQAGatewayLoginPage.class);
        jqaGatewayUploadPage = PageFactory.initElements(driver, JQAGatewayUploadPage.class);
        jqaGatewayPackagePage = PageFactory.initElements(driver, JQAGatewayPackagePage.class);
        jqaGatewayArticlesPage = PageFactory.initElements(driver, JQAGatewayArticlesPage.class);
        jqaGatewayHomePage = PageFactory.initElements(driver, JQAGatewayHomePage.class);
        jqacaDashboardPage = PageFactory.initElements(driver, JQACADashboardPage.class);
        jqaAdminPage = PageFactory.initElements(driver, JQAAdminPage.class);
        jqaHomePage = PageFactory.initElements(driver, JQAHomePage.class);
        jqaArticlePreviewPage = PageFactory.initElements(driver, JQAArticlePreviewPage.class);
        jqaPublishedArticlesPage = PageFactory.initElements(driver, JQAPublishedArticlesPage.class);
        jqacsDshboardPage = PageFactory.initElements(driver, JQACSDshboardPage.class);
        jqaactivitylogpage = PageFactory.initElements(driver, JQAActivityLogPage.class);
        MetaDataServicesPage = PageFactory.initElements(driver, MetaDataServicesPage.class);
        JQASADashboardPage = PageFactory.initElements(driver, JQASADashboardPage.class);
        CCHAndDSSPage = PageFactory.initElements(driver, CCHAndDSSPage.class);
        JQAWileyEmail = PageFactory.initElements(driver, JQAWileyEmail.class);*/

    }

    /**
     * Method DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static synchronized Reference instance() {
        if (refSingleton == null) {
            refSingleton = new Reference();
        }

        return refSingleton;
    }

}
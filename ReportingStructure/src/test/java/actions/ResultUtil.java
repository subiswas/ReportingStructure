/*
 * FileName         : ResultUtil 
 * File Description : To get access of HTML Reports
 * Company          : GAVS
 * Author           : UdayBhaskar Donda
 * Manager          : Anand Kumar M C
 * 
 */

package actions;

import intialize.Prerequsite;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

import util.DateUtil;
import util.FileUtil;
import util.PropertyUtil;
import util.StringUtils;
import util.XMLUtil;
import common.ExtendedLibrary;
//import scripts.core.Prerequsite;

	

public class ResultUtil extends ExtendedLibrary{
	public static Map<String,String> reportFilesMap;
	public static String strResultFolder;
	private static String strCurrentReportID;
	private static String strCurrentReportFileName;
	public static FileWriter writer;
	
	public static String strID=null;
	
	private static int inTestCount=0;
	private static int inStepCount=0;
	public static String strCSS=".datagrid table { border-collapse: collapse; text-align: left; width: 100%; }"+ 
	
	".datagrid {font: normal 12px/150% Arial, Helvetica, sans-serif; background: "+
	
	"#fff; overflow: hidden; border: 1px solid #006699; -webkit-border-radius: "+
	
	"3px; -moz-border-radius: 3px; border-radius: 3px; }.datagrid table td, "+
	
	".datagrid table th { padding: 3px 10px; }.datagrid table thead th "+
	
	"{background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, "+
	
	"#006699), color-stop(1, #00557F) );background:-moz-linear-gradient( center "+
	
	"top, #006699 5%, #00557F 100% "+
	
	");filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#006699', "+
	
	"endColorstr='#00557F');background-color:#006699; color:#FFFFFF; font-size: "+
	
	"12px; font-weight: bold; border-left: 1px solid #0070A8; } .datagrid table "+
	
	"thead th:first-child { border: none; }.datagrid table tbody td { color: "+
	
	"#00557F; border-left: 2px solid #E1EEF4;font-size: 12px;font-weight: normal; "+
	
	"}.datagrid table tbody .alt td { background: #E1EEf4; color: #00557F; "+
	
	"}.datagrid table tbody td:first-child { border-left: none; }.datagrid table "+
	
	"tbody tr:last-child td { border-bottom: none; }.datagrid table tfoot td div { "+
	
	"border-top: 1px solid #006699;background: #E1EEf4;} .datagrid table tfoot td "+
	
	"{ padding: 0; font-size: 12px } .datagrid table tfoot td div{ padding: 2px; "+
	
	"}.datagrid table tfoot td ul { margin: 0; padding:0; list-style: none; "+
	
	"text-align: right; }.datagrid table tfoot  li { display: inline; }.datagrid "+
	
	"table tfoot li a { text-decoration: none; display: inline-block;  padding: "+
	
	"2px 8px; margin: 1px;color: #FFFFFF;border: 1px solid "+
	
	"#006699;-webkit-border-radius: 3px; -moz-border-radius: 3px; border-radius: "+
	
	"3px; background:-webkit-gradient( linear, left top, left bottom, "+
	
	"color-stop(0.05, #006699), color-stop(1, #00557F) "+
	
	");background:-moz-linear-gradient( center top, #006699 5%, #00557F 100% "+
	
	");filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#006699', "+
	
	"endColorstr='#00557F');background-color:#006699; }.datagrid table tfoot "+
	
	"ul.active, .datagrid table tfoot ul a:hover { text-decoration:"+
	
	"none;border-color: #00557F; color: #FFFFFF; background: none;"+
	
	"background-color:#006699;}";

/**
 * 
 * @param strStatus: Fail/Pass
 * @param sStep: Desc of TStep
 * @param strExpected: expected result
 * @param strActual: Actual result
 * @param driver:
 */
	
	public synchronized static void report(String strStatus,String sStep,String strExpected,String strActual,WebDriver driver){
		try{
			//String strModuleName=System.getProperty("MODULE");
			String strThreadID=""+Thread.currentThread().getId();
			String strReportFile=reportFilesMap.get(strThreadID);
			Map<String,String> linkMap=new HashMap<String, String>();
			Map<String,String> attrMap=new HashMap<String, String>();
			String strScreenshotFile;	
			
			boolean blResult = false;
			
			Map<String,String> attributeMap=new HashMap<String, String>();
			strID=UUID.randomUUID().toString();
			attributeMap.put("name",strStatus);
			attributeMap.put("id",strID);
			XMLUtil.addNodeWithAttributes(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody", "tr",attributeMap);
			//XMLUtil.addNode(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody", "tr");
			XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td",String.valueOf(getStepCount()));
			//XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td",strStoryID);
			XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td",sStep);
//			XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td",strDesc);
			XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td",strExpected);
			XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td",strActual);
			XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td",DateUtil.getCurrentDate("yyyy/MM/dd HH:mm:sss"));
			XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td",strStatus);
			XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td","");
			
			/*Map<String,String> configMap = PropertyUtil.getPropertyFileAsHashmap("config/execution.properties");
			String strSCREENSHOT= configMap.get("SCREENSHOT");*/
			String strSCREENSHOT= Prerequsite.configMap.get("SCREENSHOT");

			System.out.println(strSCREENSHOT + DateUtil.getCurrentDate("yyyy/MM/dd HH:mm:sss"));
			System.out.println(strStatus + DateUtil.getCurrentDate("yyyy/MM/dd HH:mm:sss"));
			
			
			if(strSCREENSHOT.equalsIgnoreCase("ONFAILURE") && strStatus.equals("FAIL")){
				WebDriver augmentedDriver = new Augmenter().augment(driver);
			
		    	File screenshot = ((TakesScreenshot)augmentedDriver).
		                getScreenshotAs(OutputType.FILE);
		
		    	strScreenshotFile="Screenshot/"+"screen-"+java.util.UUID.randomUUID().toString()+".png";
		    	FileUtils.copyFile(screenshot, new File(strResultFolder+"/"+strScreenshotFile));
		    	linkMap.put("href",strScreenshotFile);
				XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[7]", "a","Link");
				XMLUtil.addAttributes(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[7]/a", linkMap);
			}
			else if(strSCREENSHOT.equalsIgnoreCase("ALWAYS") && !(strStatus.equals("INFO"))){
				WebDriver augmentedDriver = new Augmenter().augment(driver);	
			    File screenshot = ((TakesScreenshot)augmentedDriver).
			                     getScreenshotAs(OutputType.FILE);
			 
			    strScreenshotFile="Screenshot/"+"screen-"+java.util.UUID.randomUUID().toString()+".png";
				FileUtils.copyFile(screenshot, new File(strResultFolder+"/"+strScreenshotFile));
				linkMap.put("href",strScreenshotFile);
				XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[7]", "a","Link");
				XMLUtil.addAttributes(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[7]/a", linkMap);
		        
			}
			if(strStatus.equalsIgnoreCase("PASS")){
	           blResult=true;
	        }
	        else if(strStatus.equalsIgnoreCase("FAIL")){
	        	blResult=false;
	            XMLUtil.addNodeText(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[6]","FAIL");
	            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']","name","FAIL");
	            
	            /******Change FONT COLOR********/
	            
	            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[1]","style","color:red;font-weight:bold;");
	            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[1]", "style","color:red;font-weight:bold;");
	            
	            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[2]","style","color:red;font-weight:bold;");
	            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[2]", "style","color:red;font-weight:bold;");
	            
	            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[3]","style","color:red;font-weight:bold;");
	            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[3]", "style","color:red;font-weight:bold;");
	            
	            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[4]","style","color:red;font-weight:bold;");
	            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[4]", "style","color:red;font-weight:bold;");
	            
	            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[5]","style","color:red;font-weight:bold;");
	            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[5]", "style","color:red;font-weight:bold;");
	            
	            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[6]","style","color:red;font-weight:bold;");
	            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[6]", "style","color:red;font-weight:bold;");
	            
	          //  XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[7]","style","color:red;font-weight:bold;");
//	            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[7]", "style","color:red;font-weight:bold;");
	        }    
	        else if(strStatus.equalsIgnoreCase("INFO")){
				System.out.println("");
				attrMap.put("style", "background-color:#ADD8E6;");
				XMLUtil.addAttributes(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[last()]", attrMap);
	        }
	        else if(strStatus.equalsIgnoreCase("MESSAGE")){
	        	
	        	System.out.println("");
				attrMap.put("style", "background-color:#ADBCE6;");
				XMLUtil.addAttributes(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[last()]", attrMap);
	        	
	        }
			
			/*if(strStatus.equalsIgnoreCase("FAIL")){
				XMLUtil.addText(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strThreadID+"-"+FilenameUtils.getBaseName(strReportFile)+"']/td[5]","FAIL");
				XMLUtil.addText("emailResult/"+strModuleName+"-Emailable-Result.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strThreadID+"-"+FilenameUtils.getBaseName(strReportFile)+"']/td[5]","FAIL");
			}*/
	    }
		catch(Exception e){
			e.printStackTrace();
		}
	}

	

	

	/*public synchronized static void print(String strStatus,String strDesc,String strExpected,String strActual,WebDriver driver){
    System.out.println(strStatus+"-"+strDesc);
    System.out.println();
    boolean blResult=false;
    
   
    try{
        Map<String,String> attributeMap=new HashMap();
        String strID=UUID.randomUUID().toString();
       
        attributeMap.put("name",strStatus);
        attributeMap.put("id",strID);
        XMLUtil.addNodeWithAttributes(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody", "tr",attributeMap);
        XMLUtil.addNodeWithText(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td",strDesc);
        XMLUtil.addNodeWithText(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td",strExpected);
        XMLUtil.addNodeWithText(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td",strActual);
        XMLUtil.addNodeWithText(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td",strStatus);
        XMLUtil.addNodeWithText(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td",DateUtil.getCurrentDate("yyyy_MM_dd_HH_mm_sss"));
        XMLUtil.addNodeWithText(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td","");
       
       
        String strScreenshotPath="/screenshot/"+UUID.randomUUID().toString()+".png";
        takeScreenshot(driver,strResultFolder+strScreenshotPath);
        Map<String,String> linkMap=new HashMap();
        linkMap.put("href","."+strScreenshotPath);
        XMLUtil.addNodeWithText(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[6]", "a","Screenshot");
        XMLUtil.addAttributes(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[6]/a", linkMap);
       
        if(strStatus.equalsIgnoreCase("PASS")){
            blResult=true;
        }
        else if(strStatus.equalsIgnoreCase("FAIL")){
            XMLUtil.addNodeText(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[5]","FAIL");
            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']","name","FAIL");
            
           
            *//****Abdul-Changing background color****//*
           
            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[1]","style","color:red;font-weight:bold;");
            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[1]", "style","color:red;font-weight:bold;");
           
            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[2]","style","color:red;font-weight:bold;");
            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[2]", "style","color:red;font-weight:bold;");
           
            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[3]","style","color:red;font-weight:bold;");
            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[3]", "style","color:red;font-weight:bold;");
           
            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[4]","style","color:red;font-weight:bold;");
            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[4]", "style","color:red;font-weight:bold;");
           
            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[5]","style","color:red;font-weight:bold;");
            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[5]", "style","color:red;font-weight:bold;");
           
            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[6]","style","color:red;font-weight:bold;");
            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[6]", "style","color:red;font-weight:bold;");
           
            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[7]","style","color:red;font-weight:bold;");
            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[7]", "style","color:red;font-weight:bold;");
           
            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[8]","style","color:red;font-weight:bold;");
            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[8]", "style","color:red;font-weight:bold;");
           
            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[9]","style","color:red;font-weight:bold;");
            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[9]", "style","color:red;font-weight:bold;");
          
        }
        else if(strStatus.equalsIgnoreCase("INFO")){
            blResult=true;
        }
    }
    catch(Exception e){
      //  log.error(e.getMessage());
        e.printStackTrace();
    }
   // ScriptUtil.logTestResult(strDesc, blResult);
}*/
/*	@SuppressWarnings("unused")
	private static void takeScreenshot(WebDriver driver,String strPath){
	    try{
	        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	        FileUtils.copyFile(scrFile, new File(strPath));
	    	
	    	WebDriver augmentedDriver = new Augmenter().augment(driver);
	        File scrFile = ((TakesScreenshot)augmentedDriver).
	                            getScreenshotAs(OutputType.FILE);
	        FileUtils.copyFile(scrFile, new File(strPath));
	    }
	    catch(Exception e){
	        e.printStackTrace();
	    }
	}*/

	
	public synchronized static void createReport(String strScriptName, Map<String,String> dataMap) throws Exception{
	
		String strReportFileName=strScriptName+"-"+DateUtil.getCurrentDate("yyyy_MM_dd_HH_mm_sss");
		String strReportFile=strReportFileName+".html";
		String strThreadID=""+Thread.currentThread().getId();
		
		strCurrentReportFileName=strReportFile;
		
		reportFilesMap = new LinkedHashMap<String, String>();
		reportFilesMap.put(strThreadID, strCurrentReportFileName);
		
		String strMachine = dataMap.get("Machine");
		String strBrowser = dataMap.get("Browser");
		
		/*String strStoryID = null; 
		strStoryID = dataMap.get(strStoryId);
		Map<String,String> storyLinkMap = null;*/
		try{
			FileUtils.copyFile(new File("template/Detail.html"), new File(strResultFolder+"/"+strReportFile));
			XMLUtil.addText(strResultFolder+"/"+strReportFile, "//h1[@id='header']", strScriptName);
			XMLUtil.addText(strResultFolder+"/"+strReportFile, "//td[@id='date']", DateUtil.getCurrentDate("dd-MM-YYYY"));
			
			Map<String,String> attributeMap=new HashMap<String, String>();
			strCurrentReportID=DateUtil.getCurrentDate("yyyy_MM_dd_HH_mm_sss");
			attributeMap.put("id",strCurrentReportID);
			attributeMap.put("name","PASS");
			XMLUtil.addNodeWithAttributes(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody", "tr",attributeMap);
			//S.No.
			XMLUtil.addNodeWithText(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']", "td",""+getCount());			
			
			//Browser
			XMLUtil.addNodeWithText(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']", "td",strMachine);
			
			//Browser
			XMLUtil.addNodeWithText(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']", "td",strBrowser);		
			
			//Summary
			XMLUtil.addNodeWithText(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']", "td",strScriptName);
			//TimeStamp
			XMLUtil.addNodeWithText(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']", "td",DateUtil.getCurrentDate("yyyy/MM/dd HH:mm:sss"));
			//Status
			XMLUtil.addNodeWithText(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']", "td","PASS");
			//Link
			XMLUtil.addNodeWithText(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']", "td","");
			
			Map<String,String> linkMap=new HashMap<String, String>();
			linkMap.put("href",strReportFile);
			/*XMLUtil.addNodeWithText(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[2]", "a",strStoryID);
			XMLUtil.addAttributes(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[2]/a", storyLinkMap);*/
			
			XMLUtil.addNodeWithText(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[7]", "a","Link");
			XMLUtil.addAttributes(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[7]/a", linkMap);
			//XMLUtil.addNodeWithAttributes(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody", "br",attributeMap);
			//XMLUtil.addNode(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr", "br");
		}catch(Exception e){
			//log.error(e.getMessage());
		}
	}
	
   private static int getCount(){
		inTestCount++;
		inStepCount = 0;
		return inTestCount;
	}
	
	private static int getStepCount(){
		inStepCount++;
		return inStepCount;
	}
	
	
	
	/*public static void createReportFolder(String strDriverName){
		strResultFolder="result/"+strDriverName+"-"+DateUtil.getCurrentDate("yyyy_MM_dd_HH_mm_ss");
		FileUtil.createFolder(strResultFolder);
		FileUtil.createFolder(strResultFolder+"/xml");
		
		try{
			String strSummaryFile=strResultFolder+"/Summary.html";
			FileUtils.copyDirectory(new File("lib/FCM/template"), new File(strResultFolder));
			//XMLUtil.addText(strSummaryFile, "//h1[@id='header']", strDriverName);
			XMLUtil.addText(strSummaryFile, "//td[@id='date']", DateUtil.getCurrentDate("dd-MM-YYYY"));
			XMLUtil.addText(strSummaryFile, "//td[@id='sTime']", DateUtil.getCurrentDate("HH:mm:ss"));
		}catch(Exception e){
			//log.error(e.getMessage());
		}
	}*/
	
	
	//Log file creation methods
		public static String getCurrentDate(String strFormat) {
			DateFormat dateFormat = new SimpleDateFormat(strFormat);
			Date date = new Date();
			return dateFormat.format(date);
		}

		public static void log(FileWriter writer,String msg1,String status)
		{
			try{
				writer.append(getCurrentDate("yyyy_MM_dd_HH_mm_sss"));
				writer.append(",");
				writer.append(Thread.currentThread().getStackTrace()[2].getClassName());
				writer.append(",");
				writer.append(Thread.currentThread().getStackTrace()[2].getMethodName());
				writer.append(",");
				writer.append(Integer.toString((Thread.currentThread().getStackTrace()[2].getLineNumber())));
				writer.append(",");
				writer.append(msg1);
				writer.append(",");
				writer.append(status);
				writer.append("\n");
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		public static FileWriter createLog(String filepath){
			try{

				//writer = new FileWriter(filepath);
				
				File file = new File(filepath);
				  // System.out.println(file.getAbsolutePath());
				    
				   // if file doesn't exists, then create it
				   if (!file.exists()) {
				    file.createNewFile();
				   }

				   writer = new FileWriter(file.getAbsolutePath(), true);


				writer.append("Date,");
				writer.append("TestScriptName,");
				writer.append("MethodName,");
				writer.append("L.No,");
				writer.append("Log Message,");
				writer.append("Status");
				writer.append('\n');
			}
			catch(IOException e)
			{
				e.printStackTrace();
			} 
			return writer;
		}
		public static FileWriter closeLog(FileWriter writer){
			try{
				writer.flush();
				writer.close();
				Thread.sleep(5000);
			}
			catch(Exception e){
				e.printStackTrace();
			} 
			return writer;
		}
	      public static void endReport() {
	          try {
	              String strSummaryFile = strResultFolder + "\\Summary.html";
	              XMLUtil.addText(strSummaryFile, "//td[@id='eTime']",
	                      DateUtil.getCurrentDate("HH:mm:ss"));
	             /* int total = getCount()-1;
	              System.out.println("Total"+total);
	              String strTotal = Integer.toString(total);
	              XMLUtil.addText(strSummaryFile, "//td[@id='totalCount']", strTotal);*/
	                    String sTime = XMLUtil.getText(strSummaryFile, "//td[@id='sTime']");
	                    String eTime = XMLUtil.getText(strSummaryFile, "//td[@id='eTime']");
	                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
	                    Date date1 = format.parse(sTime);
	                    Date date2 = format.parse(eTime);
	                    long diff = date2.getTime() - date1.getTime();
	                    int diffSeconds = (int) diff / 1000 % 60;
	                    int diffMinutes = (int) diff / (60 * 1000) % 60;
	                    int diffHours = (int) diff / (60 * 60 * 1000) % 24;
	                    String tTime = diffHours+":"+diffMinutes+":"+diffSeconds;
	                    date2 = format.parse(tTime);
	                    tTime = format.format(date2);
	                    XMLUtil.addText(strSummaryFile, "//td[@id='tTime']", tTime);                                    
	          } catch (Exception e) {
	        	  System.out.println(e.getLocalizedMessage());
//	              ResultUtil.report("FAIL","",   "Exception Occurred:" + e.getLocalizedMessage(),"", driver);
	          }
	      }

	      public synchronized static void print(String strStatus,String strDesc,WebDriver driver){
	    	try{
	    		  
	    		String strThreadID=""+Thread.currentThread().getId();
				String strReportFile=reportFilesMap.get(strThreadID);
				Map<String,String> linkMap=new HashMap<String, String>();
				Map<String,String> attrMap=new HashMap<String, String>();
				String strScreenshotFile;	
				
				boolean blResult = false;
				
				Map<String,String> attributeMap=new HashMap<String, String>();
				strID=UUID.randomUUID().toString();
				attributeMap.put("name",strStatus);
				attributeMap.put("id",strID);				
				
				XMLUtil.addNodeWithAttributes(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody", "tr",attributeMap);				
				XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td",String.valueOf(getStepCount()));				
				XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td",strDesc);
				XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td","-");
				XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td","-");
				XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td",DateUtil.getCurrentDate("yyyy/MM/dd HH:mm:sss"));
				XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td",strStatus);
				XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']", "td","");
				
				/*Map<String,String> configMap = PropertyUtil.getPropertyFileAsHashmap("config/execution.properties");
				String strSCREENSHOT= configMap.get("SCREENSHOT");*/
				String strSCREENSHOT= Prerequsite.configMap.get("SCREENSHOT");
				
				
				if(strSCREENSHOT.equalsIgnoreCase("ONFAILURE") && strStatus.equals("FAIL")){
					WebDriver augmentedDriver = new Augmenter().augment(driver);
				
			    	File screenshot = ((TakesScreenshot)augmentedDriver).
			                getScreenshotAs(OutputType.FILE);
			
			    	strScreenshotFile="Screenshot/"+"screen-"+java.util.UUID.randomUUID().toString()+".png";
			    	FileUtils.copyFile(screenshot, new File(strResultFolder+"/"+strScreenshotFile));
			    	linkMap.put("href",strScreenshotFile);
					XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[7]", "a","Link");
					XMLUtil.addAttributes(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[7]/a", linkMap);
				}
				else if(strSCREENSHOT.equalsIgnoreCase("ALWAYS") && !(strStatus.equals("INFO"))){
					WebDriver augmentedDriver = new Augmenter().augment(driver);	
				    File screenshot = ((TakesScreenshot)augmentedDriver).
				                     getScreenshotAs(OutputType.FILE);
				 
				    strScreenshotFile="Screenshot/"+"screen-"+java.util.UUID.randomUUID().toString()+".png";
					FileUtils.copyFile(screenshot, new File(strResultFolder+"/"+strScreenshotFile));
					linkMap.put("href",strScreenshotFile);
					XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[7]", "a","Link");
					XMLUtil.addAttributes(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[7]/a", linkMap);
			        
				}
				if(strStatus.equalsIgnoreCase("PASS")){
		           blResult=true;
		        }
		        else if(strStatus.equalsIgnoreCase("FAIL")){
		        	blResult=false;
		            XMLUtil.addNodeText(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[6]","FAIL");
		            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']","name","FAIL");
		            
		            /******Change FONT COLOR********/
		            
		            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[1]","style","color:red;font-weight:bold;");
		            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[1]", "style","color:red;font-weight:bold;");
		            
		            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[2]","style","color:red;font-weight:bold;");
		            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[2]", "style","color:red;font-weight:bold;");
		            
		            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[3]","style","color:red;font-weight:bold;");
		            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[3]", "style","color:red;font-weight:bold;");
		            
		            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[4]","style","color:red;font-weight:bold;");
		            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[4]", "style","color:red;font-weight:bold;");
		            
		            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[5]","style","color:red;font-weight:bold;");
		            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[5]", "style","color:red;font-weight:bold;");
		            
		            XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[6]","style","color:red;font-weight:bold;");
		            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[6]", "style","color:red;font-weight:bold;");
		            
		          //  XMLUtil.addAttribute(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strCurrentReportID+"']/td[7]","style","color:red;font-weight:bold;");
//		            XMLUtil.addAttribute(strResultFolder+"/"+strCurrentReportFileName, "//table[@id='tblDetail']/tbody/tr[@id='"+strID+"']/td[7]", "style","color:red;font-weight:bold;");
		        }    
		        else if(strStatus.equalsIgnoreCase("INFO")){
					System.out.println("");
					attrMap.put("style", "background-color:#ADD8E6;");
					XMLUtil.addAttributes(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[last()]", attrMap);
		        }
		        else if(strStatus.equalsIgnoreCase("MESSAGE")){
		        	
		        	System.out.println("");
					attrMap.put("style", "background-color:#ADBCE6;");
					XMLUtil.addAttributes(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[last()]", attrMap);
		        	
		        }
				
				/*if(strStatus.equalsIgnoreCase("FAIL")){
					XMLUtil.addText(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strThreadID+"-"+FilenameUtils.getBaseName(strReportFile)+"']/td[5]","FAIL");
					XMLUtil.addText("emailResult/"+strModuleName+"-Emailable-Result.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strThreadID+"-"+FilenameUtils.getBaseName(strReportFile)+"']/td[5]","FAIL");
				}*/
		    }
			catch(Exception e){
				e.printStackTrace();
			}
	    	  
	    }


	    		
	    		
	    		
	    	
	      public synchronized static void reportWithoutScreenshot(String strStatus,String strDesc){
	    	  
	    		String strModuleName=System.getProperty("MODULE");
	    		String strThreadID=""+Thread.currentThread().getId();
	    		String strReportFile=reportFilesMap.get(strThreadID);
	    		Map<String,String> linkMap=new HashMap();
	    		Map<String,String> attrMap=new HashMap();
	    		String strScreenshotFile;
	    		
	    		XMLUtil.addNode(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody", "tr");
	    		//XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[last()]", "td","-");
	    		XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[last()]", "td",strDesc);
	    		XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[last()]", "td",strStatus);
	    		XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[last()]", "td",DateUtil.getCurrentDate("yyyy/MM/dd HH:mm:sss"));
	    		XMLUtil.addNodeWithText(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[last()]", "td","");
	    		
	    		System.out.println(strStatus+"-"+strDesc);
	    		
	    		if(strStatus.equalsIgnoreCase("FAIL")){
	    			XMLUtil.addText(strResultFolder+"/Summary.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strThreadID+"-"+FilenameUtils.getBaseName(strReportFile)+"']/td[5]","FAIL");
	    			XMLUtil.addText("emailResult/"+strModuleName+"-Emailable-Result.html", "//table[@id='tblSummary']/tbody/tr[@id='"+strThreadID+"-"+FilenameUtils.getBaseName(strReportFile)+"']/td[5]","FAIL");
	    		}
	    		
	    		if(strStatus.equalsIgnoreCase("INFO")){
	    			attrMap.put("style", "background-color:#ADD8E6;");
	    			XMLUtil.addAttributes(strResultFolder+"/"+strReportFile, "//table[@id='tblDetail']/tbody/tr[last()]", attrMap);
	    		}
	    		
	    	}
}

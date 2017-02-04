package com.poc.ReportingStructure;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import common.ExtendedLibrary;
import operation.ReadObject;
import operation.UIOperation;
import util.ExcelUtils;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

@Test(dataProvider = "BPA", groups = {"all", "chrome", "firefox", "ie"})
public class BPA_Smoke_Login_TC001 extends ExtendedLibrary{
	WebDriver driver;
	DesiredCapabilities capability = null;
	public static String parent_directory = System.getProperty("user.dir");
	public static String test_data = "BPA_Smoke_Login_TC001.xls";
	public static String test_data_sheet = "BPA";
	public static String test_data_path = parent_directory + "\\Input\\" + test_data;
  
	public void verify_execution(Map<String, String> dataMap) throws Exception {
		try{
			ReadObject object = new ReadObject();
			UIOperation operation = new UIOperation();
			Properties allObject = object.getObjectRepository();
			
			String machine = dataMap.get("Machine");
			String browser = dataMap.get("Browser");
			String port = dataMap.get("Port");
			String tc_id = dataMap.get("TC_ID");
			String keyword = dataMap.get("Keyword");
			String object_name = dataMap.get("Object_Name");
			String object_type = dataMap.get("Object_Type");
			String value = dataMap.get("Value");
			String running_flag = dataMap.get("Running_Flag");
			
			if(running_flag.contentEquals("Y")){
				operation.perform(allObject, keyword, object_name, object_type, value, tc_id, dataMap);
			}
		} catch(NullPointerException e){
			
		}
	}
	
	@DataProvider(name="BPA",parallel=false)
	public static Iterator<Object[]> bpa(){
		Iterator<Object[]> testData=ExcelUtils.getTestData(test_data_path, test_data_sheet);
		return testData;
	}
}

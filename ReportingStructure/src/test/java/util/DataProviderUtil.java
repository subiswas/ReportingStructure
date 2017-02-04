package util;

import java.util.Iterator;

import org.testng.annotations.DataProvider;

public class DataProviderUtil {
	
	public static class staticProviderClass{
			
		@DataProvider(name="JQA",parallel=false)
		public static Iterator<Object[]> jqa(){
			Iterator<Object[]> testData=ExcelUtils.getTestData("Input/TestData.xls", "JQA");
			return testData;
		}
		

	
	}
}



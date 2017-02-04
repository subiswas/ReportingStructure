set var = %cd%
cd..
cd..
cd lib
set var1 = %cd%
cd %var %
java -jar %var1 %\selenium-server-standalone-2.45.0.jar -Dwebdriver.chrome.driver="chromedriver.exe" -Dwebdriver.ie.driver="IEDriverServer32.exe"

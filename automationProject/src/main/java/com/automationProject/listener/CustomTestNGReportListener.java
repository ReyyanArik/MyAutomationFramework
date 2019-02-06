package com.automationProject.listener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.testng.IExecutionListener;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.collections.Lists;
import org.testng.internal.Utils;
import org.testng.xml.XmlSuite;

import com.automationProject.util.SendEmail;
import com.automationProject.util.ZipUtils;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;


public class CustomTestNGReportListener extends BaseTestListener implements IReporter,WebDriverEventListener,IExecutionListener,ITestListener {
		
		protected static final Logger logTest = Logger.getLogger(CustomTestNGReportListener.class);
		
	    String USER_DIR = System.getProperty("user.dir");
	    String outputFolder = USER_DIR+"/test-output/report.rar";
	    String sourceFolder = USER_DIR+"/test-output/report";
	        
	    protected PrintWriter writer;
	    public static String fileName = null;
	    public static List<String> list = new ArrayList();
	    protected final List<SuiteResult> suiteResults = Lists.newArrayList();
	    private final StringBuilder buffer = new StringBuilder();
	    private String ENVIRONMENT= null;

		private String dReportTitle = "AKADEMI TAK - LMS KURUMSAL PORTAL AUTOMATION REPORT";
		//private String dReportFileName = "/report/TCELLakademiReport.html";
		//private String dReportEmbededFileName = "/embededReport.html";
		private String dReportFileName = "../../test-output/report/AkademiKurumsalPortalReport.html";
		private String dReportEmbededFileName = "../../test-output/embededReport.html";
		private String driverName = "ChromeDriver";
		

	    @Override
	    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
	    	//emailable report
	    	try {
	            writer = createWriter(outputDirectory);
	        } catch (IOException e) {
	        	logTest.error("Unable to create output file", e);
	            return;
	        }
	        for (ISuite suite : suites) {
	            suiteResults.add(new SuiteResult(suite));
	        }
	        
	        writeDocumentStart();
	        writeHead();
	        writeBody();
	        writeDocumentEnd();
	        writer.close();
	        
	      //Embeded report
	        try {
	            writer = createEmbededWriter(outputDirectory);
	        } catch (IOException e) {
	        	logTest.error("Unable to create output file", e);
	            return;
	        }
    	
	        
	        writeDocumentStart();
	        writeHead();
	        writer.println("<body>");
	        writeSuiteSummary();
	        writer.println("</body>");
	        writeDocumentEnd();
	        writer.close();
	    }
	    
	    protected PrintWriter createWriter(String outdir) throws IOException {
	    	new File(outdir).mkdirs();
	    	return new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, dReportFileName))));
	    }
	    
	    protected PrintWriter createEmbededWriter(String outdir) throws IOException {
	    	new File(outdir).mkdirs();
	    	return new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, dReportEmbededFileName))));
	    }
	    
	    protected void writeReportTitle(String title) {
			writer.println("<center><h6 style=\"color: #525252;margin-bottom: 15px; margin-top: 15px;\">" + title + " - " + getCurrentDateTime() + "</h6></center>");
		}

	    protected void writeDocumentStart() {
	        writer.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
	        writer.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
	    }

	    protected void writeHead() {
	        writer.println("<head>");
	        writer.println("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>");
	        writer.println("<title>Turkcell Akademi TAK LMS KURUMSAL PORTAL Automation Report</title>");
	        writer.println("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js\"></script>");
	        writer.println("<script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\"></script>");
	        writer.println("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.11.0/umd/popper.min.js\"></script>");
	        writer.println("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/js/bootstrap.min.js\"></script>");
	        writer.println("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/lightbox2/2.7.1/js/lightbox.min.js\"></script>");
	        writer.println("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/ekko-lightbox/5.3.0/ekko-lightbox.js\"></script>");
	        writer.println("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/ekko-lightbox/5.3.0/ekko-lightbox.min.js\"></script>");
	        writer.println("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/anchor-js/3.2.1/anchor.min.js\"></script>");
	        writeStylesheet();
	        writer.println("</head>");
	    }

	    protected void writeStylesheet() {
	    	writer.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"http://netdna.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css\" />");
	    	writer.println("<link rel=\"stylesheet\" href=\"https://use.fontawesome.com/releases/v5.5.0/css/all.css\" integrity=\"sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU\" crossorigin=\"anonymous\"/>");
	    	writer.println("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/lightbox2/2.7.1/css/lightbox.css\" />");
	    	writer.println("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/ekko-lightbox/5.3.0/ekko-lightbox.css\" />");
	        writer.print("<style type=\"text/css\">");
	        writer.print("body{font-size: 12px; background-color:#f7f7f7;}");
	        writer.print("table.table-bordered{border:3px solid #999; background-color:#fff;}");
	        writer.print(".fs{min-width:100px; text-align:center;}");
	        writer.print(".se{max-width:300px;}");
	        writer.print(".num{text-align:center;}");
	        writer.print("table.table-bordered.result{border:3px solid #ffeeba;}");
	        writer.print("tr,th,td{border: 1px solid #dee2e6;}");
	        writer.print(".btn{padding:2px;}");
	        writer.print(".table .thead-dark th {color: #fff;background-color: #999;border-color: #999;}");
	        writer.print("#summary{margin-top:50px;margin-bottom:50px;}");
	        writer.print(".btn-primary {color: #fff;background-color: #e6c96e;border-color: #efd276; font-size: 14px;}");
	        writer.print(".btn-primary:hover {color: #fff;background-color: #c5aa54;border-color: #c5aa54;}");
	        writer.print(".btn-primary.focus, .btn-primary:focus { box-shadow: 0 0 0 0.2rem rgb(206, 181, 103); }");
	        writer.print("h6{color: #525252;margin-bottom: 15px; margin-top: 15px;}");
	        writer.print(".total-num th{font-weight:500;}");
	        writer.print(".table tr.header { font-weight: bold; background-color: #fff; cursor: pointer; -webkit-user-select: none; /* Chrome all / Safari all */ -moz-user-select: none; /* Firefox all */ -ms-user-select: none; /* IE 10+ */ user-select: none; /* Likely future */ }");
	        writer.print("#summary tr:not(.header):not(.detail-result-header) { display: none; }");
            writer.print(".table .header.active td:after { content: \"\\2212\"; }");
	        writer.print(".plus-icon{padding-right: 10px;}");
	        writer.println("</style>");
	    }

	    protected void writeBody() {
	    	writer.println("<body><div class=\"container\"><div class=\"row\"><div class=\"col-md-12\">");
	        writeReportTitle(dReportTitle);
	        writeSuiteSummary();
	        writeScenarioSummary();
	        writeScenarioDetails();
	        writer.println("</div></div></div>");
	        writer.println("<script> $(document).on('click', '[data-toggle=\"lightbox\"]', function(event) { event.preventDefault(); $(this).ekkoLightbox(); });</script>");
	        writer.println("<script> $(document).ready(function() {  var ua = navigator.userAgent,  event =(ua.match(/iPad/i)) ? \"touchstart\" : \"click\"; if ($('.table').length > 0){$('.table .header').on(event, function() {$(this).toggleClass(\"active\",\"\").nextUntil('.header').css('display',function(i, v) {return this.style.display === 'table-row' ? 'none' : 'table-row'; }); }); }});</script>");
	        writer.println("</body>");
	    }

	    protected void writeDocumentEnd() {
	        writer.println("</html>");
	    }

	    protected void writeSuiteSummary() {
	        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
	        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
	        dateFormat.setTimeZone(TimeZone.getTimeZone("Africa/Kampala"));
	        
	        int totalTestsCount = 0;
	        int totalPassedTests = 0;
	        int totalSkippedTests = 0;
	        int totalFailedTests = 0;
	        long totalDuration = 0;

	        writer.println("<div class=\"panel-body\">");
	        writer.println("<table style=\"border: 3px solid #999;background-color: #fff;\" class=\"table table-bordered overall-table\">");
	        writer.print("<thead class=\"thead-dark\"><tr><th colspan=\"9\">OVERALL RESULTS</th></tr></thead>");
	        writer.print("<tr>");
	        writer.print("<th>Suite Name</th>");
	        writer.print("<th>Number of Testcases</th>");
	        writer.print("<th>Passed</th>");
	        writer.print("<th>Skipped</th>");
	        writer.print("<th>Failed</th>");
	        writer.print("<th>Browser</th>");
	        writer.print("<th>Start Time</th>");
	        writer.print("<th>End Time</th>");
	        writer.print("<th>Duration</th>");
	        writer.println("</tr>");

	        int testIndex = 0;
	        for (SuiteResult suiteResult : suiteResults) {

	            for (TestResult testResult : suiteResult.getTestResults()) {
	            	int testsCount = testResult.getTestCount();
	                int passedTests = testResult.getPassedTestCount();
	                int skippedTests = testResult.getSkippedTestCount();
	                int failedTests = testResult.getFailedTestCount();
	                
	               
	                Date startTime = testResult.getTestStartTime();
	                Date endTime = testResult.getTestEndTime();
	                long duration = testResult.getDuration();
	                

	                writer.print("<tr");
	                if ((testIndex % 2) == 1) {
	                    writer.print(" class=\"stripe\"");
	                }
	                writer.print(">");

	                buffer.setLength(0);
	                writeTableData(buffer.append("<a href=\"#t").append(testIndex)
	                        .append("\">")
	                        .append(Utils.escapeHtml(testResult.getTestName()))
	                        .append("</a>").toString());
	                writeTableData(integerFormat.format(testsCount), "num");
	                writeTableData(integerFormat.format(passedTests), "num");
	                writeTableData(integerFormat.format(skippedTests),
	                        (skippedTests > 0 ? "num attn" : "num"));
	                writeTableData(integerFormat.format(failedTests),
	                        (failedTests > 0 ? "num attn" : "num"));
	                writeTableData(driverName,  "num");
	                writeTableData(dateFormat.format(startTime),  "num");
	                writeTableData(dateFormat.format(endTime),  "num");
	                writeTableData(convertTimeToString(duration), "num");
	                writer.println("</tr>");

	                totalTestsCount +=testsCount;
	                totalPassedTests += passedTests;
	                totalSkippedTests += skippedTests;
	                totalFailedTests += failedTests;
	                totalDuration += duration;
	                testIndex++;
	            }
	        }


			
	        // Print totals if there was more than one test
	        if (testIndex > 1) {
	        	
	            writer.print("<tr class=\"total-num\">");
	            writer.print("<th style=\"text-align:left;\">Total</th>");
	            writeTableHeader(integerFormat.format(totalTestsCount), "num");
	            writeTableHeader(integerFormat.format(totalPassedTests), "num");
	            writeTableHeader(integerFormat.format(totalSkippedTests),
	                    (totalSkippedTests > 0 ? "num attn" : "num"));
	            writeTableHeader(integerFormat.format(totalFailedTests),
	                    (totalFailedTests > 0 ? "num attn" : "num"));
	            writer.print("<th colspan=\"3\"></th>");
	            writeTableHeader(convertTimeToString(totalDuration), "num");
	            writer.println("</tr>");
	        }

	        writer.println("</table>");
	        writer.println("</div>");
	    }

	    /**
	     * Writes a summary of all the test scenarios.
	     */
	    protected void writeScenarioSummary() {
	    	writer.print("<div class=\"panel-body\">");
	    	writer.print("<table id='summary' class=\"table table-bordered\">");
	    	writer.print("<thead class=\"thead-dark\"><tr class=\"detail-result-header\"><th colspan=\"7\">DETAILED RESULTS</th></tr></thead>");
	        
	        int testIndex = 0;
	        int scenarioIndex = 0;
	        for (SuiteResult suiteResult : suiteResults) {

	            for (TestResult testResult : suiteResult.getTestResults()) {
	                writer.printf("<tbody id=\"t%d\">", testIndex);

	                String testName = Utils.escapeHtml(testResult.getTestName());
	                int startIndex = scenarioIndex;

	                scenarioIndex += writeScenarioSummary(testName
	                        + " &#8212; failed (configuration methods)",
	                        testResult.getFailedConfigurationResults(), "failed",
	                        scenarioIndex);
	                scenarioIndex += writeScenarioSummary(testName
	                        + " &#8212; failed", testResult.getFailedTestResults(),
	                        "failed", scenarioIndex);
	                scenarioIndex += writeScenarioSummary(testName
	                        + " &#8212; skipped (configuration methods)",
	                        testResult.getSkippedConfigurationResults(), "skipped",
	                        scenarioIndex);
	                scenarioIndex += writeScenarioSummary(testName
	                        + " &#8212; skipped",
	                        testResult.getSkippedTestResults(), "skipped",
	                        scenarioIndex);
	                scenarioIndex += writeScenarioSummary(testName
	                        + " &#8212; passed", testResult.getPassedTestResults(),
	                        "passed", scenarioIndex);

	                if (scenarioIndex == startIndex) {
	                    writer.print("<tr><th colspan=\"4\" class=\"invisible\"/></tr>");
	                }

	                writer.println("</tbody>");

	                testIndex++;
	            }
	        }

	        writer.println("</table>");
	        writer.println("</div>");
	    }

	    /**
	     * Writes the scenario summary for the results of a given state for a single
	     * test.
	     */
	    private int writeScenarioSummary(String description,
	            List<ClassResult> classResults, String cssClassPrefix,
	            int startingScenarioIndex) {
	        int scenarioCount = 0;
	        if (!classResults.isEmpty()) {
	        	if(description.contains("failed")) {
		            writer.print("<tr class=\"table-danger header\"><th colspan=\"7\">");
	        	}else if(description.contains("passed")) {
		            writer.print("<tr class=\"table-success header\"><th colspan=\"7\">");
	        	}else{
		         writer.print("<tr class=\"table-info header\"><th colspan=\"7\">");
	        	}
	        	writer.print("<i class=\"plus-icon fas fa-plus-circle\"></i>");
	        	writer.print(description);
		        writer.print("</th></tr>");
		         	        	
	            int scenarioIndex = startingScenarioIndex;
	            int classIndex = 0;
	            for (ClassResult classResult : classResults) {
	                String cssClass = cssClassPrefix
	                        + ((classIndex % 2) == 0 ? "even" : "odd");

	                buffer.setLength(0);

	                int scenariosPerClass = 0;
	                int methodIndex = 0;
	                for (MethodResult methodResult : classResult.getMethodResults()) {
	                    List<ITestResult> results = methodResult.getResults();
	                    int resultsCount = results.size();
	                    assert resultsCount > 0;

	                    ITestResult firstResult = results.iterator().next();
	                    String methodName = Utils.escapeHtml(firstResult
	                            .getMethod().getMethodName());
	                    long start = firstResult.getStartMillis();
	                    long end = firstResult.getEndMillis();
	                    
	                    
	                    String shortException="";
	                    String failureScreenShot = "";
	                    String fs = "<i style=\"color: #00bd67;font-size: 20px;\" class=\"fas fa-check-circle\"></i>";
	    				
	    					Throwable exception=firstResult.getThrowable();
	    					boolean hasThrowable = exception != null;
	    					if(hasThrowable){

	    						failureScreenShot = list.get(methodIndex);
//	    						String str = ExceptionUtils.getMessage(exception);
//	    						String str = ExceptionUtils.getStackTrace(exception);
	    						String str = Utils.shortStackTrace(exception, true);
	    						Scanner scanner = new Scanner(str);
	    						shortException = scanner.nextLine();
	    						scanner.close();
	    						List<String> msgs = Reporter.getOutput(firstResult);
	    	                    boolean hasReporterOutput = msgs.size() > 0;
	    	                    	if(hasReporterOutput){	    	                    		
	    	                    		for (String info : msgs) {
	    	                            	failureScreenShot+=info+"<br/>";
	    	                            }
	    	                        }
	    	                    	fs = "<a href=\"ErrorScreenshots/"+methodName
	    	                    			+".jpg\" data-toggle=\"lightbox\" data-title=\""+methodName
	    	                    			+"\" data-footer=\" \">"+"<img src=\"ErrorScreenshots/"
	    	                    			+methodName+".jpg\" class=\"img-thumbnail img-responsive img-fluid\"></a>";
	    					}
	    					
	    				DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
	    				formatter.setTimeZone(TimeZone.getTimeZone("Africa/Kampala"));
	    				Calendar startTime = Calendar.getInstance();
	    				startTime.setTimeInMillis(start);
	    				
	    				Calendar endTime = Calendar.getInstance();
	    				endTime.setTimeInMillis(end);
	   				
	    				long dur = TimeUnit.MILLISECONDS.toMillis(Math.abs(end - start));

	                    // The first method per class shares a row with the class
	                    // header
	                    if (methodIndex > 0) {
	                        buffer.append("<tr class=\"")
	                        		.append(cssClass)
	                                .append("\">");

	                    }

	                    // Write the timing information with the first scenario per
	                    // method
	                    buffer.append("<td><a href=\"#m").append(scenarioIndex)
	                            .append("\">")
	                            .append(methodName) 
	                            .append("</a></td>").append("<td class\"se\" rowspan=\"")
	                            .append("\">").append(shortException)
	                            .append("</td>").append("<td class=\"fs\" rowspan=\"").append("\">")
	                            .append(fs)
	                            .append("</td>")
	                            .append("</td>")
	                            .append("<td rowspan=\"")
	                            .append("\">").append(formatter.format(startTime.getTime()))
	                            .append("</td>").append("<td rowspan=\"")
	                            .append("\">").append(formatter.format(endTime.getTime()))
	                            .append("</td>").append("<td rowspan=\"")
	                            .append("\">").append(convertTimeToString(dur)).append("</td>").append("</tr>");
	                    scenarioIndex++;
	                    // Write the remaining scenarios for the method
	                    for (int i = 1; i < resultsCount; i++) {
	                        buffer.append("<tr class=\"").append(cssClass)
	                                .append("\">").append("<td><a href=\"#m")
	                                .append(scenarioIndex).append("\">")
	                                .append(methodName).append("</a></td></tr>");
	                        scenarioIndex++;
	                    }

	                    scenariosPerClass += resultsCount;
	                    methodIndex++;
	                }

	                // Write the test results for the class
	                writer.print("<tr class=\"");
	                writer.print(cssClass);
	                writer.print("\">");
	                writer.print("<td rowspan=\"");
	                writer.print(scenariosPerClass);
	                writer.print("\">");
	                writer.print(Utils.escapeHtml(classResult.getClassName().substring(12)));
	                writer.print("</td>");
	                writer.print(buffer);

	                classIndex++;
	            }
	            scenarioCount = scenarioIndex - startingScenarioIndex;
	        }
	        return scenarioCount;
	    }

	    /**
	     * Writes the details for all test scenarios.
	     */
	    protected void writeScenarioDetails() {
	        int scenarioIndex = 0;
	        for (SuiteResult suiteResult : suiteResults) {
	            for (TestResult testResult : suiteResult.getTestResults()) {

	                scenarioIndex += writeScenarioDetails(
	                        testResult.getFailedConfigurationResults(),
	                        scenarioIndex);
	                scenarioIndex += writeScenarioDetails(
	                        testResult.getFailedTestResults(), scenarioIndex);
	                scenarioIndex += writeScenarioDetails(
	                        testResult.getSkippedConfigurationResults(),
	                        scenarioIndex);
	                scenarioIndex += writeScenarioDetails(
	                        testResult.getSkippedTestResults(), scenarioIndex);
	                scenarioIndex += writeScenarioDetails(
	                        testResult.getPassedTestResults(), scenarioIndex);
	            }
	        }
	    }

	    /**
	     * Writes the scenario details for the results of a given state for a single
	     * test.
	     */
	    private int writeScenarioDetails(List<ClassResult> classResults,
	            int startingScenarioIndex) {
	        int scenarioIndex = startingScenarioIndex;
	        for (ClassResult classResult : classResults) {
	            String className = classResult.getClassName();
	            for (MethodResult methodResult : classResult.getMethodResults()) {
	                List<ITestResult> results = methodResult.getResults();
	                assert !results.isEmpty();

	                String label = Utils
	                		.escapeHtml(results.iterator().next().getMethod()
	                                        .getMethodName());
	                for (ITestResult result : results) {
	                    writeScenario(scenarioIndex, label, result);
	                    scenarioIndex++;
	                }
	            }
	        }

	        return scenarioIndex - startingScenarioIndex;
	    }

	    /**
	     * Writes the details for an individual test scenario.
	     */
	    private void writeScenario(int scenarioIndex, String label,ITestResult result) {
	        writer.print("<div class=\"panel-body\"><table class=\"table table-bordered\"><thead class=\"thead-dark\"><tr><th>EXCEPTION DETAIL</th></tr></thead><tr><td><table style=\"border:3px solid #ffeeba;\" class=\"table table-bordered result\">");
	        writer.print("<tr><td class=\"table-warning\" id=\"m");
	        writer.print(scenarioIndex);
	        writer.print("\">");
	        writer.println("<span style=\"float:left;\">");
	        writer.print(label);
	        writer.print("</span>");
	        writer.println("<span style=\"float:right;\"><a class=\"btn btn-primary\" role=\"button\" href=\"#summary\">back to summary</a></span>");
	        writer.print("</td>");
	        writer.print("</tr>");
	        boolean hasRows = false;

	        // Write test parameters (if any)
	        Object[] parameters = result.getParameters();
	        int parameterCount = (parameters == null ? 0 : parameters.length);
	        if (parameterCount > 0) {
	            writer.print("<tr class=\"param\">");
	            for (int i = 1; i <= parameterCount; i++) {
	                writer.print("<th>Parameter #");
	                writer.print(i);
	                writer.print("</th>");
	            }
	            writer.print("</tr><tr class=\"param\">");
	            for (Object parameter : parameters) {
	                writer.print("<td>");
	                writer.print(Utils.escapeHtml(Utils.toString(parameter)));
	                writer.print("</td>");
	            }
	            writer.print("</tr>");
	            hasRows = true;
	        }

	        // Write reporter messages (if any)
	        List<String> reporterMessages = Reporter.getOutput(result);
	        if (!reporterMessages.isEmpty()) {
	            writer.print("<tr><th");
	            if (parameterCount > 1) {
	                writer.printf(" colspan=\"%d\"", parameterCount);
	            }
	            writer.print(">Messages</th></tr>");

	            writer.print("<tr><td");
	            if (parameterCount > 1) {
	                writer.printf(" colspan=\"%d\"", parameterCount);
	            }
	            writer.print(">");
	            writeReporterMessages(reporterMessages);
	            writer.print("</td></tr>");
	            hasRows = true;
	        }

	        // Write exception (if any)
	        Throwable throwable = result.getThrowable();
	        if (throwable != null) {
	            writer.print("<tr><th");
	            if (parameterCount > 1) {
	                writer.printf(" colspan=\"%d\"", parameterCount);
	            }
	            writer.print(">");
	            writer.print((result.getStatus() == ITestResult.SUCCESS ? "Expected Exception"
	                    : "Exception"));
	            writer.print("</th></tr>");

	            writer.print("<tr><td");
	            if (parameterCount > 1) {
	                writer.printf(" colspan=\"%d\"", parameterCount);
	            }
	            writer.print(">");
	            writeStackTrace(throwable);
	            writer.print("</td></tr>");
	            hasRows = true;
	        }

	        if (!hasRows) {
	            writer.print("<tr><th");
	            if (parameterCount > 1) {
	                writer.printf(" colspan=\"%d\"", parameterCount);
	            }
	            writer.print(" class=\"invisible\"/></tr>");
	        }

	        writer.print("</table></td></tr></table></div>");
	    }

	    protected void writeReporterMessages(List<String> reporterMessages) {
	        writer.print("<div class=\"messages\">");
	        Iterator<String> iterator = reporterMessages.iterator();
	        assert iterator.hasNext();
	        if (Reporter.getEscapeHtml()) {
	        	writer.print(Utils.escapeHtml(iterator.next()));
	        } else {
	        	writer.print(iterator.next());
	        }
	        while (iterator.hasNext()) {
	            writer.print("<br/>");
	            if (Reporter.getEscapeHtml()) {
	            	writer.print(Utils.escapeHtml(iterator.next()));
	            } else {
	            	writer.print(iterator.next());
	            }
	        }
	        writer.print("</div>");
	    }

	    protected void writeStackTrace(Throwable throwable) {
	        writer.print("<div class=\"stacktrace\">");
	        writer.print(Utils.shortStackTrace(throwable, true));
	        writer.print("</div>");
	    }

	    /**
	     * Writes a TH element with the specified contents and CSS class names.
	     * 
	     * @param html
	     *            the HTML contents
	     * @param cssClasses
	     *            the space-delimited CSS classes or null if there are no
	     *            classes to apply
	     */
	    protected void writeTableHeader(String html, String cssClasses) {
	        writeTag("th", html, cssClasses);
	    }

	    /**
	     * Writes a TD element with the specified contents.
	     * 
	     * @param html
	     *            the HTML contents
	     */
	    protected void writeTableData(String html) {
	        writeTableData(html, null);
	    }

	    /**
	     * Writes a TD element with the specified contents and CSS class names.
	     * 
	     * @param html
	     *            the HTML contents
	     * @param cssClasses
	     *            the space-delimited CSS classes or null if there are no
	     *            classes to apply
	     */
	    protected void writeTableData(String html, String cssClasses) {
	        writeTag("td", html, cssClasses);
	    }

	    /**
	     * Writes an arbitrary HTML element with the specified contents and CSS
	     * class names.
	     * 
	     * @param tag
	     *            the tag name
	     * @param html
	     *            the HTML contents
	     * @param cssClasses
	     *            the space-delimited CSS classes or null if there are no
	     *            classes to apply
	     */
	    protected void writeTag(String tag, String html, String cssClasses) {
	        writer.print("<");
	        writer.print(tag);
	        if (cssClasses != null) {
	            writer.print(" class=\"");
	            writer.print(cssClasses);
	            writer.print("\"");
	        }
	        writer.print(">");
	        writer.print(html);
	        writer.print("</");
	        writer.print(tag);
	        writer.print(">");
	    }

	    /**
	     * Groups {@link TestResult}s by suite.
	     */
	    protected static class SuiteResult {
	        private final String suiteName;
	        private final List<TestResult> testResults = Lists.newArrayList();

	        public SuiteResult(ISuite suite) {
	            suiteName = suite.getName();
	            for (ISuiteResult suiteResult : suite.getResults().values()) {
	                testResults.add(new TestResult(suiteResult.getTestContext()));
	            }
	        }

	        public String getSuiteName() {
	            return suiteName;
	        }

	        /**
	         * @return the test results (possibly empty)
	         */
	        public List<TestResult> getTestResults() {
	            return testResults;
	        }
	    }

	    /**
	     * Groups {@link ClassResult}s by test, type (configuration or test), and
	     * status.
	     */
	    protected static class TestResult {
	        /**
	         * Orders test results by class name and then by method name (in
	         * lexicographic order).
	         */
	        protected static final Comparator<ITestResult> RESULT_COMPARATOR = new Comparator<ITestResult>() {
	            @Override
	            public int compare(ITestResult o1, ITestResult o2) {
	                int result = o1.getTestClass().getName()
	                        .compareTo(o2.getTestClass().getName());
	                if (result == 0) {
	                    result = o1.getMethod().getMethodName()
	                            .compareTo(o2.getMethod().getMethodName());
	                }
	                return result;
	            }
	        };

	        private final String testName;
	        private final Date testStartTime;
	        private final Date testEndTime;
	        private final List<ClassResult> failedConfigurationResults;
	        private final List<ClassResult> failedTestResults;
	        private final List<ClassResult> skippedConfigurationResults;
	        private final List<ClassResult> skippedTestResults;
	        private final List<ClassResult> passedTestResults;
	        private final int failedTestCount;
	        private final int skippedTestCount;
	        private final int passedTestCount;
	        private final int testCount;
	        private final long duration;
	        private final String includedGroups;
	        private final String excludedGroups;

	        public TestResult(ITestContext context) {
	            testName = context.getName();

	            Set<ITestResult> failedConfigurations = context
	                    .getFailedConfigurations().getAllResults();
	            Set<ITestResult> failedTests = context.getFailedTests()
	                    .getAllResults();
	            Set<ITestResult> skippedConfigurations = context
	                    .getSkippedConfigurations().getAllResults();
	            Set<ITestResult> skippedTests = context.getSkippedTests()
	                    .getAllResults();
	            Set<ITestResult> passedTests = context.getPassedTests()
	                    .getAllResults();

	            failedConfigurationResults = groupResults(failedConfigurations);
	            failedTestResults = groupResults(failedTests);
	            skippedConfigurationResults = groupResults(skippedConfigurations);
	            skippedTestResults = groupResults(skippedTests);
	            passedTestResults = groupResults(passedTests);
	            
	            testStartTime = context.getStartDate();
	            testEndTime = context.getEndDate();

	            failedTestCount = failedTests.size();
	            skippedTestCount = skippedTests.size();
	            passedTestCount = passedTests.size();
	            testCount = context.getAllTestMethods().length;

	            duration = context.getEndDate().getTime() -context.getStartDate().getTime();
	                   
	            includedGroups = formatGroups(context.getIncludedGroups());
	            excludedGroups = formatGroups(context.getExcludedGroups());
	        }

	        /**
	         * Groups test results by method and then by class.
	         */
	        protected List<ClassResult> groupResults(Set<ITestResult> results) {
	            List<ClassResult> classResults = Lists.newArrayList();
	            if (!results.isEmpty()) {
	                List<MethodResult> resultsPerClass = Lists.newArrayList();
	                List<ITestResult> resultsPerMethod = Lists.newArrayList();

	                List<ITestResult> resultsList = Lists.newArrayList(results);
	                Collections.sort(resultsList, RESULT_COMPARATOR);
	                Iterator<ITestResult> resultsIterator = resultsList.iterator();
	                assert resultsIterator.hasNext();

	                ITestResult result = resultsIterator.next();
	                resultsPerMethod.add(result);

	                String previousClassName = result.getTestClass().getName();
	                String previousMethodName = result.getMethod().getMethodName();
	                while (resultsIterator.hasNext()) {
	                    result = resultsIterator.next();

	                    String className = result.getTestClass().getName();
	                    if (!previousClassName.equals(className)) {
	                        // Different class implies different method
	                        assert !resultsPerMethod.isEmpty();
	                        resultsPerClass.add(new MethodResult(resultsPerMethod));
	                        resultsPerMethod = Lists.newArrayList();

	                        assert !resultsPerClass.isEmpty();
	                        classResults.add(new ClassResult(previousClassName,
	                                resultsPerClass));
	                        resultsPerClass = Lists.newArrayList();

	                        previousClassName = className;
	                        previousMethodName = result.getMethod().getMethodName();
	                    } else {
	                        String methodName = result.getMethod().getMethodName();
	                        if (!previousMethodName.equals(methodName)) {
	                            assert !resultsPerMethod.isEmpty();
	                            resultsPerClass.add(new MethodResult(resultsPerMethod));
	                            resultsPerMethod = Lists.newArrayList();

	                            previousMethodName = methodName;
	                        }
	                    }
	                    resultsPerMethod.add(result);
	                }
	                assert !resultsPerMethod.isEmpty();
	                resultsPerClass.add(new MethodResult(resultsPerMethod));
	                assert !resultsPerClass.isEmpty();
	                classResults.add(new ClassResult(previousClassName,
	                        resultsPerClass));
	            }
	            return classResults;
	        }

	        public String getTestName() {
	            return testName;
	        }
	        
	        public Date getTestStartTime() {
	            return testStartTime;
	          }
	        
	        public Date getTestEndTime() {
	            return testEndTime;
	          }
	        

	        /**
	         * @return the results for failed configurations (possibly empty)
	         */
	        public List<ClassResult> getFailedConfigurationResults() {
	            return failedConfigurationResults;
	        }

	        /**
	         * @return the results for failed tests (possibly empty)
	         */
	        public List<ClassResult> getFailedTestResults() {
	            return failedTestResults;
	        }

	        /**
	         * @return the results for skipped configurations (possibly empty)
	         */
	        public List<ClassResult> getSkippedConfigurationResults() {
	            return skippedConfigurationResults;
	        }

	        /**
	         * @return the results for skipped tests (possibly empty)
	         */
	        public List<ClassResult> getSkippedTestResults() {
	            return skippedTestResults;
	        }

	        /**
	         * @return the results for passed tests (possibly empty)
	         */
	        public List<ClassResult> getPassedTestResults() {
	            return passedTestResults;
	        }

	        public int getFailedTestCount() {
	            return failedTestCount;
	        }

	        public int getSkippedTestCount() {
	            return skippedTestCount;
	        }

	        public int getPassedTestCount() {
	            return passedTestCount;
	        }

	        public long getDuration() {
	            return duration;
	        }

	        public String getIncludedGroups() {
	            return includedGroups;
	        }

	        public String getExcludedGroups() {
	            return excludedGroups;
	        }
	        
	        public int getTestCount() {
	            return testCount;
	        }

	        /**
	         * Formats an array of groups for display.
	         */
	        protected String formatGroups(String[] groups) {
	            if (groups.length == 0) {
	                return "";
	            }

	            StringBuilder builder = new StringBuilder();
	            builder.append(groups[0]);
	            for (int i = 1; i < groups.length; i++) {
	                builder.append(", ").append(groups[i]);
	            }
	            return builder.toString();
	        }
	    }

	    /**
	     * Groups {@link MethodResult}s by class.
	     */
	    protected static class ClassResult {
	        private final String className;
	        private final List<MethodResult> methodResults;

	        /**
	         * @param className
	         *            the class name
	         * @param methodResults
	         *            the non-null, non-empty {@link MethodResult} list
	         */
	        public ClassResult(String className, List<MethodResult> methodResults) {
	            this.className = className;
	            this.methodResults = methodResults;
	        }

	        public String getClassName() {
	            return className;
	        }

	        /**
	         * @return the non-null, non-empty {@link MethodResult} list
	         */
	        public List<MethodResult> getMethodResults() {
	            return methodResults;
	        }
	    }

	    /**
	     * Groups test results by method.
	     */
	    protected static class MethodResult {
	        private final List<ITestResult> results;

	        /**
	         * @param results
	         *            the non-null, non-empty result list
	         */
	        public MethodResult(List<ITestResult> results) {
	            this.results = results;
	        }

	        /**
	         * @return the non-null, non-empty result list
	         */
	        public List<ITestResult> getResults() {
	            return results;
	        }
	    }
	    public static String getCurrentDateTime() {
	    	Date date = new Date();
	    	DateFormat formats = new SimpleDateFormat("dd.MM.yyyy  HH:mm:ss");
	    	formats.setTimeZone(TimeZone.getTimeZone("Africa/Kampala"));    	
			return formats.format(date);
		}
	    
	    public synchronized String takeScreenshot(String methodName) throws IOException {
	    	
	        String path = USER_DIR + "/test-output/report" ;

//	        File folder = new File(path);
//	        if (!folder.exists()) {
//	            folder.mkdir();
//	        }

	        String directory = path + "\\ErrorScreenshots\\";
	        	        	       
	        fileName = methodName + ".jpg";

	        File f = new File(directory);

	        if (!f.isDirectory()) {
	            f.mkdir();
	            logTest.info("Folder was created successfully: " + directory);
	        }

	        String destination = directory + fileName;
	        
	        
	        Screenshot fpScreenshot = new AShot().takeScreenshot(getDriver());
	        ImageIO.write(fpScreenshot.getImage(), "PNG", new File(destination));
	        	        
	        logTest.info("destination is " + destination);
	        
	        return fileName;
	    }
	    
		
		/* Convert long type milliseconds to format hh:mm:ss */
		public String convertTimeToString(long miliSeconds) {
			int hrs = (int) TimeUnit.MILLISECONDS.toHours(miliSeconds) % 24;
			int min = (int) TimeUnit.MILLISECONDS.toMinutes(miliSeconds) % 60;
			int sec = (int) TimeUnit.MILLISECONDS.toSeconds(miliSeconds) % 60;
			return String.format("%02d:%02d:%02d", hrs, min, sec);
		}

		public void beforeAlertAccept(WebDriver driver) {
			// TODO Auto-generated method stub
			
		}

		public void afterAlertAccept(WebDriver driver) {
			// TODO Auto-generated method stub
			
		}

		public void afterAlertDismiss(WebDriver driver) {
			// TODO Auto-generated method stub
			
		}

		public void beforeAlertDismiss(WebDriver driver) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeNavigateTo(String url, WebDriver driver) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterNavigateTo(String url, WebDriver driver) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeNavigateBack(WebDriver driver) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterNavigateBack(WebDriver driver) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeNavigateForward(WebDriver driver) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterNavigateForward(WebDriver driver) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeNavigateRefresh(WebDriver driver) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterNavigateRefresh(WebDriver driver) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeFindBy(By by, WebElement element, WebDriver driver) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterFindBy(By by, WebElement element, WebDriver driver) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeClickOn(WebElement element, WebDriver driver) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterClickOn(WebElement element, WebDriver driver) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void beforeScript(String script, WebDriver driver) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void afterScript(String script, WebDriver driver) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onException(Throwable throwable, WebDriver driver) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onExecutionStart() {
			PropertyConfigurator.configure("properties/log4j.properties");			
			
			File f = new File(sourceFolder);
			deleteDir(f);
			logTest.info("report folder deleted successfully..");
			
			String path = USER_DIR + "/test-output/report" ;
	        File folder = new File(path);
	        if (!folder.exists()) {
	            folder.mkdir();
	        }
	        logTest.info("report folder created successfully..");
			
		}

		static String readFile(String path, Charset encoding) throws IOException 
		{
		  byte[] encoded = Files.readAllBytes(Paths.get(path));
		  return new String(encoded, encoding);
		}
		
		@Override
		public void onExecutionFinish() {
			ZipUtils zipUtil = new ZipUtils();
			String htmlemdededfile;
			try {
				zipUtil.zipIt(outputFolder,sourceFolder);
			} catch (Exception exc) {
				logTest.info("Zipleme işlemi yapılamadı");
				exc.printStackTrace();
			}	
			
			try {
				htmlemdededfile = readFile(USER_DIR+"/test-output/embededReport.html", StandardCharsets.UTF_8);
				SendEmail sendEmail  = new SendEmail();
				try {
					sendEmail.sendMail(htmlemdededfile);
				} catch (EmailException e) {
					logTest.info("E mail gönderiminde bir hata oluştu");
					e.printStackTrace();
				}
			} catch (IOException e1) {
				logTest.info("file okunamadı");
				e1.printStackTrace();
			}
			
				
		}

		@Override
		public void onTestStart(ITestResult result) {
			logTest.info(result.getName() + " method onTestStart");
		}

		@Override
		public void onTestSuccess(ITestResult result) {
			logTest.info(result.getName() + " method onTestSuccess");
			
		}
		

		@Override
		public void onTestFailure(ITestResult result) {
			logTest.info(result.getName() + " method onTestFailure");
			 try {
				
				 String dest = takeScreenshot(result.getName());
				 list.add(dest);		 
				} catch (IOException e) {
					logTest.info(e);
					e.printStackTrace();
				}	
			
		}

		@Override
		public void onTestSkipped(ITestResult result) {
			logTest.info(result.getName() + " method onTestSkipped");
			
		}

		@Override
		public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStart(ITestContext context) {
			logTest.info(context.getName() + " onStart Suite ");
			
		}

		@Override
		public void onFinish(ITestContext context) {
			// TODO Auto-generated method stub
			logTest.info(context.getName() + " onFinish Suite ");
		}
		
		public void deleteDir(File file) {
		    File[] contents = file.listFiles();
		    if (contents != null) {
		        for (File f : contents) {
		            if (! Files.isSymbolicLink(f.toPath())) {
		                deleteDir(f);
		            }
		        }
		    }
		    file.delete();
		}

		
	}
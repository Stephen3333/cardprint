package com.compulynx.iMbank.web.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

/**
 * Servlet implementation class Reports
 */
@Component
public class ReportsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public ReportsServlet() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	private DataSource dataSource;

	public ReportsServlet(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	public Connection getDbConnection() {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return connection;
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;
		JasperDesign jasperDesign = null;
		Map<String, String> parameters = new HashMap();
		ServletOutputStream outstream = null;
		Connection connection = null;
		String reportType = request.getParameter("type");
		String exportType = request.getParameter("eType");
		try {
			InitialContext initialContext = new InitialContext();
			Context context = (Context) initialContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) context.lookup("jdbc/imbankDS");
			connection = ds.getConnection();
			String TOMCAT_HOME = System.getProperty("catalina.base");
			System.out.println("tomcat" + TOMCAT_HOME);

			// Format for output
			DateTimeFormatter dtFrm = DateTimeFormat.forPattern("dd-MMM-yyyy");
			if (reportType.trim().equalsIgnoreCase("D")) {
				String fromDate = request.getParameter("frDate");
				String toDate = request.getParameter("toDate");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");

				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				System.out.println("fromDate: " + fromDate);
				System.out.println("toDate: " + toDate);
				parameters.put("From", fromDate);
				parameters.put("Too", toDate);
				// //"2015-06-29T00:00:00.000"

				/*
				 * parameters.put("dtFrm", dtFrm.print(from).toString());
				 * parameters.put("toFrm", dtFrm.print(to).toString());
				 */
				if (exportType.equalsIgnoreCase("P")) {
					jasperDesign = JRXmlLoader.load(TOMCAT_HOME
							+ "/bin/imbankReports/card_bydate_report.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager
								.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);
					} else {
						System.out.println("No data");
					}
				} else {
					System.out.println("calling servlet## Summary Excel");
					jasperDesign = JRXmlLoader
							.load(TOMCAT_HOME
									+ "/bin/imbankReports/card_bydate_report_xlsx.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(
								JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
								Boolean.FALSE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
								Boolean.FALSE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_IGNORE_GRAPHICS,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.JASPER_PRINT,
								jasperPrint);
						exporter.setParameter(
								JRXlsExporterParameter.OUTPUT_STREAM, baos); // fill
																				// byte
																				// array
																				// output
																				// stream

						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + "CardsPrintedbyDate"
										+ ".xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					} else {
						System.out.println("No data");
					}
				}

			}
			if (reportType.trim().equalsIgnoreCase("B")) {
				String fromDate = request.getParameter("frDate");
				String toDate = request.getParameter("toDate");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");

				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				System.out.println("fromDate: " + fromDate);
				System.out.println("toDate: " + toDate);
				String branchId = request.getParameter("branch");
				System.out.println("BRID: " + branchId);
				parameters.put("From", fromDate);
				parameters.put("Too", toDate);
				parameters.put("branch", branchId);

				/*
				 * parameters.put("dtFrm", dtFrm.print(from).toString());
				 * parameters.put("toFrm", dtFrm.print(to).toString());
				 */
				if (exportType.equalsIgnoreCase("P")) {
					jasperDesign = JRXmlLoader.load(TOMCAT_HOME
							+ "/bin/imbankReports/card_bybranch_report.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager
								.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);
					} else {
						System.out.println("No data");
					}
				} else {

					jasperDesign = JRXmlLoader
							.load(TOMCAT_HOME
									+ "/bin/imbankReports/card_bybranch_report_xlsx.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(
								JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
								Boolean.FALSE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
								Boolean.FALSE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_IGNORE_GRAPHICS,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.JASPER_PRINT,
								jasperPrint);
						exporter.setParameter(
								JRXlsExporterParameter.OUTPUT_STREAM, baos); // fill
																				// byte
																				// array
																				// output
																				// stream

						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + "CardPrintedByBranch"
										+ ".xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					} else {
						System.out.println("No data");
					}
				}
			}
			if (reportType.trim().equalsIgnoreCase("U")) {
				String fromDate = request.getParameter("frDate");
				String toDate = request.getParameter("toDate");
				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");

				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				System.out.println("fromDate: " + fromDate);
				System.out.println("toDate: " + toDate);
				String userId = request.getParameter("user");
				System.out.println("userId: " + userId);
				parameters.put("From", fromDate);
				parameters.put("Too", toDate);
				parameters.put("User", userId);

				/*
				 * parameters.put("dtFrm", dtFrm.print(from).toString());
				 * parameters.put("toFrm", dtFrm.print(to).toString());
				 */
				if (exportType.equalsIgnoreCase("P")) {
					jasperDesign = JRXmlLoader.load(TOMCAT_HOME
							+ "/bin/imbankReports/card_byuser_report.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);
					if (jasperPrint.getPages().size() != 0) {
						byte[] pdfasbytes = JasperExportManager
								.exportReportToPdf(jasperPrint);
						outstream = response.getOutputStream();
						response.setContentType("application/pdf");
						response.setContentLength(pdfasbytes.length);
						outstream.write(pdfasbytes, 0, pdfasbytes.length);
					} else {
						System.out.println("No data");
					}
				}else{
					jasperDesign = JRXmlLoader
							.load(TOMCAT_HOME
									+ "/bin/imbankReports/card_byuser_report_xlsx.jrxml");
					jasperReport = JasperCompileManager
							.compileReport(jasperDesign);
					jasperPrint = JasperFillManager.fillReport(jasperReport,
							parameters, connection);

					if (jasperPrint.getPages().size() != 0) {
						JRXlsxExporter exporter = getCommonXlsxExporter();
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						exporter.setParameter(
								JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
								Boolean.FALSE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
								Boolean.FALSE);
						exporter.setParameter(
								JRXlsExporterParameter.IS_IGNORE_GRAPHICS,
								Boolean.TRUE);
						exporter.setParameter(
								JRXlsExporterParameter.JASPER_PRINT,
								jasperPrint);
						exporter.setParameter(
								JRXlsExporterParameter.OUTPUT_STREAM, baos); // fill
																				// byte
																				// array
																				// output
																				// stream

						exporter.exportReport();

						response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						response.setHeader("Content-disposition",
								"attachment; filename=" + "CardPrintedByUser"
										+ ".xlsx");
						response.setContentLength(baos.size());
						response.getOutputStream().write(baos.toByteArray());

					} else {
						System.out.println("No data");
					}
				}
			}
			if (reportType.equalsIgnoreCase("CB")) {

				// parameters.put("FromDt", fromDate);
				// parameters.put("ToDt", toDate);

				jasperDesign = JRXmlLoader
						.load("imbankReports/card_balance.jrxml");
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				jasperPrint = JasperFillManager.fillReport(jasperReport,
						parameters, connection);

				if (jasperPrint.getPages().size() != 0) {
					byte[] pdfasbytes = JasperExportManager
							.exportReportToPdf(jasperPrint);
					outstream = response.getOutputStream();
					response.setContentType("application/pdf");
					response.setContentLength(pdfasbytes.length);
					outstream.write(pdfasbytes, 0, pdfasbytes.length);

				} else {
					System.out.println("No data");
				}
			}
			if (reportType.equalsIgnoreCase("CR")) {
				String fromDate = request.getParameter("frDate");
				String toDate = request.getParameter("toDate");

				DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");

				// Parsing the date
				DateTime from = dtf.parseDateTime(fromDate);
				DateTime to = dtf.parseDateTime(toDate);
				String branchId = request.getParameter("branch");
				System.out.println("fromDate: " + fromDate);
				System.out.println("toDate: " + toDate);

				parameters.put("From", fromDate);
				parameters.put("To", toDate);
				parameters.put("branch", branchId);

				jasperDesign = JRXmlLoader
						.load("imbankReports/cards_reject.jrxml");
				jasperReport = JasperCompileManager.compileReport(jasperDesign);
				jasperPrint = JasperFillManager.fillReport(jasperReport,
						parameters, connection);

				if (jasperPrint.getPages().size() != 0) {
					byte[] pdfasbytes = JasperExportManager
							.exportReportToPdf(jasperPrint);
					outstream = response.getOutputStream();
					response.setContentType("application/pdf");
					response.setContentLength(pdfasbytes.length);
					outstream.write(pdfasbytes, 0, pdfasbytes.length);

				} else {
					System.out.println("No data");
				}
			}
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	private JRXlsxExporter getCommonXlsxExporter() {
		JRXlsxExporter exporter = new JRXlsxExporter();
		exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS,
				Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
				Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_AUTO_DETECT_CELL_TYPE,
				Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
				Boolean.FALSE);
		exporter.setParameter(
				JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
				Boolean.TRUE);
		exporter.setParameter(JExcelApiExporterParameter.IS_DETECT_CELL_TYPE,
				Boolean.TRUE);

		// exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
		// Boolean.TRUE);

		return exporter;
	}
}

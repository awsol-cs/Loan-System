/**
 *
 */
package org.cs.portfolio.reports.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.sql.DataSource;
import java.sql.Connection;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.naming.NamingException;

import org.cs.portfolio.reports.enums.ExportReportType;
import org.cs.portfolio.reports.util.Converter;
import com.google.common.base.CaseFormat;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.StreamingOutput;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.ws.rs.WebApplicationException;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.engine.JREmptyDataSource;

import javax.annotation.Resource;

import org.apache.fineract.infrastructure.core.service.TomcatJdbcDataSourcePerTenantService;

/**
 * @author michael.delacruz
 *
 */

@Service
public class ReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportService.class);
    private static final String MIFOS_BASE_DIR = System.getProperty("user.home") + File.separator + ".mifosx";
    private static final String imagePath = MIFOS_BASE_DIR + File.separator + "images" + File.separator;

    Connection conn = null;

//    @Resource(name = "jdbc/mifostenant-default")
//    private DataSource dataSource;

//    private TomcatJdbcDataSourcePerTenantService dataSource = null;
//
//    @Autowired
//    public ReportService(final TomcatJdbcDataSourcePerTenantService dataSource) {
//        this.dataSource = dataSource;
//    }

    public ReportService() {
        try {
            Context ctx = ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:/comp/env/jdbc/custom-mifostenant-default");
            conn = ds.getConnection();
        } catch(Exception ne) {
            ne.printStackTrace();
        }
    }

    /**
     * Method for generating simple report
     *
     * @param report - provided data object
     * @return byte array resource ()
     */
    public Response generateSimpleReport(Object report, String fileName, ExportReportType exportReportType) {
        InputStream stream = null;
        Response response = null;

        try {
            LOGGER.info("Entering ReportService....");
            HashMap<String, Object> map = (HashMap<String, Object>)report;
            map.put("imagePath", imagePath);

            // get jasper template
            //ClassPathResource reportResource = new ClassPathResource("reportFilePath/" + fileName + ".jasper");
            File reportLocation = new File(MIFOS_BASE_DIR + File.separator  + "jasperReports" + File.separator + fileName + ".jasper");
            if(reportLocation.exists()) {
                stream = FileUtils.openInputStream(reportLocation);
            }

            // generate parameters
            Map<String, Object> reportParameters = new HashMap<>();
            generateReportParameters(reportParameters, map);

            // generate reports based on export type.
            switch (exportReportType)
            {
                case PDF:
                    response = exportReportToPDF(stream, reportParameters, fileName); break;

                case DOCX:
                    response = exportReportToDOCx(stream, reportParameters, fileName); break;

                case XLSX:
                    response = exportReportToXLSX(stream, reportParameters, fileName); break;

                case JPG:
                    response = exportReportToJPG(stream, reportParameters, fileName); break;

                default:
                    response = null; break;
            }

            stream.close();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            return Response.status(Response.Status.NOT_FOUND).entity("Error in generating report: " + e.getMessage()).build();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return Response.status(Response.Status.NOT_FOUND).entity("Error in generating report: " + e.getMessage()).build();
        }
        return response;

    }

    /**
     * Method for generating report parameters.
     *
     * @param reportParameters - reference to report parameters (passed by reference).
     * @param reportData - report data object
     */
    private void generateReportParameters(Map<String, Object> reportParameters, HashMap<String, Object> map) {
        map.forEach((key, value) -> {
            reportParameters.put(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key), Converter.convert(value));
        });
    }

    /**
     * Method for exporting report to PDF.
     *
     * @param targetStream - target report stream
     * @param parameters - generated parameters
     * @return byte array resource (file content)
     */
    private Response exportReportToPDF(InputStream targetStream, Map<String, Object> parameters, String filename)
    {
        try
        {
            ByteArrayResource byteArrayResource = null;
            JasperPrint jasperPrint = JasperFillManager.fillReport(targetStream, parameters, conn);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            return Response.ok(outputStream.toByteArray(), "application/pdf")
                    .header("Content-Disposition", "attachment; filename=" + filename  + ".pdf")
                    .build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method for exporting report to XLS.
     *
     * @param targetStream - target report stream
     * @param parameters - generated parameters
     * @return byte array resource (file content)
     */
    private Response exportReportToXLSX(InputStream targetStream, Map<String, Object> parameters, String filename)
    {
        try
        {
            ByteArrayResource byteArrayResource = null;
            JasperPrint jasperPrint = JasperFillManager.fillReport(targetStream, parameters, conn);
            JRXlsxExporter xlsxExporter = new JRXlsxExporter();
            final byte[] rawBytes;

            try(ByteArrayOutputStream xlsReport = new ByteArrayOutputStream()){
                xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
                xlsxExporter.exportReport();

                rawBytes = xlsReport.toByteArray();
            }

            byteArrayResource = new ByteArrayResource(rawBytes);

            return Response.ok(rawBytes, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .header("Content-Disposition", "attachment; filename=" + filename + ".xlsx")
                    .build();
        }
        catch (Exception e)
        {
            LOGGER.error("Exporting report to XLS error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Method for exporting report to DOCx format
     *
     * @param targetStream - target report stream
     * @param parameters - generated parameters
     * @return byte array resource (generated report file).
     */
    private Response exportReportToDOCx(InputStream targetStream, Map<String, Object> parameters, String filename)
    {
        try
        {
            ByteArrayResource byteArrayResource = null;
            JasperPrint jasperPrint = JasperFillManager.fillReport(targetStream, parameters, conn);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            JRDocxExporter exporter = new JRDocxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
            exporter.exportReport();

            byte[] reportContent = outputStream.toByteArray();
            byteArrayResource = new ByteArrayResource(reportContent);

            return Response.ok(reportContent, "application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                    .header("Content-Disposition", "attachment; filename=" + filename + ".docx")
                    .build();
        }
        catch (Exception e)
        {
            LOGGER.error("Exporting report to DOCx error: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Method for exporting report to PNG format
     *
     * @param targetStream - target report stream
     * @param parameters - generated parameters
     * @return byte array resource (generated report file).
     */
    private Response exportReportToJPG(InputStream targetStream, Map<String, Object> parameters, String filename)
    {
        try
        {
            ByteArrayResource byteArrayResource = null;
            JasperPrint jasperPrint = JasperFillManager.fillReport(targetStream, parameters, conn);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            DefaultJasperReportsContext.getInstance();
            JasperPrintManager printManager = JasperPrintManager.getInstance(DefaultJasperReportsContext.getInstance());
            BufferedImage rendered_image = null;
            rendered_image = (BufferedImage)printManager.printPageToImage(jasperPrint, 0,1.6f);
            ImageIO.write(rendered_image, "jpg", outputStream);

            byte[] reportContent = outputStream.toByteArray();
            byteArrayResource = new ByteArrayResource(reportContent);

            return Response.ok(reportContent, "image/jpg")
                    .header("Content-Disposition", "attachment; filename=" + ".jpg")
                    .build();
        }
        catch (Exception e)
        {
            LOGGER.error("Exporting report to PNG error: {}", e.getMessage());
            return null;
        }
    }
}

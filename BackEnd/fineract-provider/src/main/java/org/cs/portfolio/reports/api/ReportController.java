/**
 *
 */
package org.cs.portfolio.reports.api;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;

import org.cs.portfolio.reports.enums.ExportReportType;
import org.cs.portfolio.reports.service.ReportService;

import io.swagger.annotations.Api;

/**
 * @author michael.delacruz
 *
 */
@Path("/cs_reports")
@Component
@Scope("singleton")
@Api(value = "Credit Score")
public class ReportController {

    private ReportService reportService;

    @Autowired
    public ReportController(final ReportService reportService) {
        this.reportService = reportService;
    }

    @POST
    @Path("/{type}")
    @Consumes({ MediaType.APPLICATION_JSON })
    public Response generateReport(@PathParam(value = "type") ExportReportType reportExportType, @QueryParam("file") String fileName, Object report) throws IOException {
        return reportService.generateSimpleReport(report, fileName, reportExportType);
    }

}

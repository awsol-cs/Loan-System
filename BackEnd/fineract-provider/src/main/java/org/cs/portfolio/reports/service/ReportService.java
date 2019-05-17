/**
 *
 */
package org.cs.portfolio.reports.service;

import javax.ws.rs.core.Response;

import org.cs.portfolio.reports.enums.ExportReportType;

public interface ReportService {

    public Response generateSimpleReport(Object report, String fileName, ExportReportType exportReportType);

}

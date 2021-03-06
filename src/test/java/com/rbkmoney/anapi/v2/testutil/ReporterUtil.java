package com.rbkmoney.anapi.v2.testutil;

import com.rbkmoney.reporter.Report;
import com.rbkmoney.reporter.StatReportResponse;
import lombok.experimental.UtilityClass;

import static com.rbkmoney.anapi.v2.testutil.DamselUtil.fillRequiredTBaseObject;

@UtilityClass
public class ReporterUtil {

    public static Report createReport(long reportId) {
        return fillRequiredTBaseObject(new Report(), Report.class)
                .setReportId(reportId)
                .setReportType("paymentRegistry");
    }

    public static StatReportResponse createSearchReportsResponse() {
        return fillRequiredTBaseObject(new StatReportResponse(), StatReportResponse.class);
    }
}

package com.rbkmoney.anapi.v2.controller;

import com.rbkmoney.anapi.v2.converter.search.request.ParamsToInvoiceTemplateSearchQueryConverter;
import com.rbkmoney.anapi.v2.security.AccessService;
import com.rbkmoney.anapi.v2.service.SearchService;
import com.rbkmoney.openapi.anapi_v2.api.InvoiceTemplatesApiController;
import com.rbkmoney.openapi.anapi_v2.model.InlineResponse20013;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;

import static com.rbkmoney.anapi.v2.util.DeadlineUtil.checkDeadline;

@Slf4j
@PreAuthorize("hasAuthority('invoices:read')")
@Controller
@SuppressWarnings("ParameterName")
public class InvoiceTemplatesSearchController extends InvoiceTemplatesApiController {

    private final SearchService searchService;
    private final AccessService accessService;
    private final ParamsToInvoiceTemplateSearchQueryConverter invoiceTemplateSearchConverter;

    public InvoiceTemplatesSearchController(
            NativeWebRequest request,
            SearchService searchService,
            AccessService accessService,
            ParamsToInvoiceTemplateSearchQueryConverter invoiceTemplateSearchConverter) {
        super(request);
        this.searchService = searchService;
        this.accessService = accessService;
        this.invoiceTemplateSearchConverter = invoiceTemplateSearchConverter;
    }

    @Override
    public ResponseEntity<InlineResponse20013> searchInvoiceTemplates(
            String xRequestID,
            @NotNull @Size(min = 1, max = 40) @Valid String partyID,
            @NotNull @Valid OffsetDateTime fromTime,
            @NotNull @Valid OffsetDateTime toTime,
            @NotNull @Min(1L) @Max(1000L) @Valid Integer limit,
            String xRequestDeadline,
            @Valid List<String> shopIDs,
            @Valid String paymentInstitutionRealm,
            @Size(min = 1, max = 40) @Valid String invoiceTemplateID,
            @Valid String invoiceTemplateStatus,
            @Size(min = 1, max = 40) @Valid String name,
            @Size(min = 1, max = 40) @Valid String product,
            @Valid OffsetDateTime invoiceValidUntil,
            @Valid String continuationToken) {
        log.info("-> Req: xRequestID={}", xRequestID);
        shopIDs = accessService.getAccessibleShops(
                "searchInvoiceTemplates",
                partyID,
                shopIDs,
                paymentInstitutionRealm);
        checkDeadline(xRequestDeadline, xRequestID);
        var query = invoiceTemplateSearchConverter.convert(
                partyID,
                fromTime,
                toTime,
                limit,
                shopIDs,
                invoiceTemplateStatus,
                invoiceTemplateID,
                continuationToken,
                name,
                product,
                invoiceValidUntil);
        var response = searchService.searchInvoiceTemplates(query);
        log.info("<- Res [200]: xRequestID={}", xRequestID);
        return ResponseEntity.ok(response);
    }
}

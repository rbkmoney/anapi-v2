package com.rbkmoney.anapi.v2.controller;

import com.rbkmoney.anapi.v2.converter.search.request.ParamsToInvoiceSearchQueryConverter;
import com.rbkmoney.anapi.v2.security.AccessService;
import com.rbkmoney.anapi.v2.service.SearchService;
import com.rbkmoney.openapi.anapi_v2.api.InvoicesApiController;
import com.rbkmoney.openapi.anapi_v2.model.InlineResponse2008;
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

import static com.rbkmoney.anapi.v2.util.ConverterUtil.merge;
import static com.rbkmoney.anapi.v2.util.DeadlineUtil.checkDeadline;

@Slf4j
@PreAuthorize("hasAuthority('invoices:read')")
@Controller
@SuppressWarnings("ParameterName")
public class InvoicesSearchController extends InvoicesApiController {

    private final SearchService searchService;
    private final AccessService accessService;
    private final ParamsToInvoiceSearchQueryConverter invoiceSearchConverter;

    public InvoicesSearchController(NativeWebRequest request, SearchService searchService, AccessService accessService, ParamsToInvoiceSearchQueryConverter invoiceSearchConverter) {
        super(request);
        this.searchService = searchService;
        this.accessService = accessService;
        this.invoiceSearchConverter = invoiceSearchConverter;
    }

    @Override
    public ResponseEntity<InlineResponse2008> searchInvoices(
            String xRequestID,
            @NotNull @Size(min = 1, max = 40) @Valid String partyID,
            @NotNull @Valid OffsetDateTime fromTime,
            @NotNull @Valid OffsetDateTime toTime,
            @NotNull @Min(1L) @Max(1000L) @Valid Integer limit,
            String xRequestDeadline,
            @Size(min = 1, max = 40) @Valid String shopID,
            @Valid List<String> shopIDs,
            @Valid String paymentInstitutionRealm,
            @Size(min = 1, max = 40) @Valid String invoiceID,
            @Valid List<String> invoiceIDs,
            @Valid String invoiceStatus,
            @Min(1L) @Valid Long invoiceAmountFrom,
            @Min(1L) @Valid Long invoiceAmountTo,
            @Size(min = 1, max = 40) @Valid String externalID,
            @Valid String continuationToken) {
        log.info("-> Req: xRequestID={}", xRequestID);
        checkDeadline(xRequestDeadline, xRequestID);
        shopIDs = accessService.getAccessibleShops(
                "searchInvoices",
                partyID,
                merge(shopID, shopIDs),
                paymentInstitutionRealm);
        var query = invoiceSearchConverter.convert(
                partyID,
                fromTime,
                toTime,
                limit,
                shopIDs,
                invoiceIDs,
                invoiceStatus,
                invoiceID,
                externalID,
                invoiceAmountFrom,
                invoiceAmountTo,
                continuationToken);
        var response = searchService.searchInvoices(query);
        log.info("<- Res [200]: xRequestID={}", xRequestID);
        return ResponseEntity.ok(response);
    }
}

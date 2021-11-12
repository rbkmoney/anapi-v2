package com.rbkmoney.anapi.v2.controller;

import com.rbkmoney.anapi.v2.converter.search.request.ParamsToRefundSearchQueryConverter;
import com.rbkmoney.anapi.v2.security.AccessService;
import com.rbkmoney.anapi.v2.service.SearchService;
import com.rbkmoney.openapi.anapi_v2.api.RefundsApiController;
import com.rbkmoney.openapi.anapi_v2.model.InlineResponse20010;
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
public class RefundsSearchController extends RefundsApiController {

    private final SearchService searchService;
    private final AccessService accessService;
    private final ParamsToRefundSearchQueryConverter refundSearchConverter;

    public RefundsSearchController(
            NativeWebRequest request,
            SearchService searchService,
            AccessService accessService,
            ParamsToRefundSearchQueryConverter refundSearchConverter) {
        super(request);
        this.searchService = searchService;
        this.accessService = accessService;
        this.refundSearchConverter = refundSearchConverter;
    }

    @Override
    public ResponseEntity<InlineResponse20010> searchRefunds(
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
            @Size(min = 1, max = 40) @Valid String paymentID,
            @Size(min = 1, max = 40) @Valid String refundID,
            @Valid String refundStatus,
            @Size(min = 1, max = 40) @Valid String externalID,
            @Valid String continuationToken) {
        log.info("-> Req: xRequestID={}", xRequestID);
        checkDeadline(xRequestDeadline, xRequestID);
        shopIDs = accessService.getAccessibleShops(
                "searchRefunds",
                partyID,
                merge(shopID, shopIDs),
                paymentInstitutionRealm);
        var query = refundSearchConverter.convert(
                partyID,
                fromTime,
                toTime,
                limit,
                shopIDs,
                invoiceIDs,
                invoiceID,
                paymentID,
                refundID,
                externalID,
                refundStatus,
                continuationToken);
        var response = searchService.searchRefunds(query);
        log.info("<- Res [200]: xRequestID={}", xRequestID);
        return ResponseEntity.ok(response);
    }
}

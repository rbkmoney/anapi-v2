package com.rbkmoney.anapi.v2.controller;

import com.rbkmoney.anapi.v2.converter.search.request.ParamsToPaymentSearchQueryConverter;
import com.rbkmoney.anapi.v2.security.AccessService;
import com.rbkmoney.anapi.v2.service.SearchService;
import com.rbkmoney.openapi.anapi_v2.api.PaymentsApiController;
import com.rbkmoney.openapi.anapi_v2.model.InlineResponse2009;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.OffsetDateTime;
import java.util.List;

import static com.rbkmoney.anapi.v2.util.ConverterUtil.merge;
import static com.rbkmoney.anapi.v2.util.DeadlineUtil.checkDeadline;

@Slf4j
@PreAuthorize("hasAuthority('invoices:read')")
@Controller
@SuppressWarnings("ParameterName")
public class PaymentsSearchController extends PaymentsApiController {

    private final SearchService searchService;
    private final AccessService accessService;
    private final ParamsToPaymentSearchQueryConverter paymentSearchConverter;

    public PaymentsSearchController(
            NativeWebRequest request,
            SearchService searchService,
            AccessService accessService,
            ParamsToPaymentSearchQueryConverter paymentSearchConverter) {
        super(request);
        this.searchService = searchService;
        this.accessService = accessService;
        this.paymentSearchConverter = paymentSearchConverter;
    }

    @Override
    public ResponseEntity<InlineResponse2009> searchPayments(
            String xRequestID,
            @NotNull @Size(min = 1, max = 40) @Valid String partyID,
            @NotNull @Valid OffsetDateTime fromTime,
            @NotNull @Valid OffsetDateTime toTime,
            @NotNull @Min(1L) @Max(1000L) @Valid Integer limit,
            String xRequestDeadline,
            @Size(min = 1, max = 40) @Valid String shopID,
            @Valid List<String> shopIDs,
            @Valid List<String> excludeShopIDs,
            @Valid String paymentInstitutionRealm,
            @Size(min = 1, max = 40) @Valid String invoiceID,
            @Valid List<String> invoiceIDs,
            @Size(min = 1, max = 40) @Valid String paymentID,
            @Valid String paymentStatus,
            @Valid String paymentFlow,
            @Valid String paymentMethod,
            @Valid String paymentTerminalProvider,
            @Size(max = 100) @Email @Valid String payerEmail,
            @Size(max = 45) @Valid String payerIP,
            @Size(max = 1000) @Valid String payerFingerprint,
            @Size(min = 1, max = 40) @Valid String customerID,
            @Pattern(regexp = "^\\d{6}$") @Valid String first6,
            @Pattern(regexp = "^\\d{4}$") @Valid String last4,
            @Pattern(regexp = "^[a-zA-Z0-9]{12}$") @Valid String rrn,
            @Size(min = 1, max = 40) @Valid String approvalCode,
            @Valid String bankCardTokenProvider,
            @Valid String bankCardPaymentSystem,
            @Min(1L) @Valid Long paymentAmountFrom,
            @Min(1L) @Valid Long paymentAmountTo,
            @Size(min = 1, max = 40) @Valid String externalID,
            @Valid String continuationToken) {
        log.info("-> Req: xRequestID={}", xRequestID);
        checkDeadline(xRequestDeadline, xRequestID);
        shopIDs = accessService.getAccessibleShops(
                "searchPayments",
                partyID,
                merge(shopID, shopIDs),
                paymentInstitutionRealm);
        var query = paymentSearchConverter.convert(
                partyID,
                fromTime,
                toTime,
                limit,
                shopIDs,
                invoiceIDs,
                paymentStatus, paymentFlow,
                paymentMethod,
                paymentTerminalProvider,
                invoiceID,
                paymentID,
                externalID,
                payerEmail,
                payerIP,
                payerFingerprint,
                customerID,
                first6,
                last4,
                rrn,
                approvalCode,
                bankCardTokenProvider,
                bankCardPaymentSystem,
                paymentAmountFrom,
                paymentAmountTo,
                excludeShopIDs,
                continuationToken);
        var response = searchService.searchPayments(query);
        log.info("<- Res [200]: xRequestID={}", xRequestID);
        return ResponseEntity.ok(response);
    }
}

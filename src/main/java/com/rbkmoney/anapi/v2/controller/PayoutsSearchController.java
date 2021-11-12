package com.rbkmoney.anapi.v2.controller;

import com.rbkmoney.anapi.v2.converter.search.request.ParamsToPayoutSearchQueryConverter;
import com.rbkmoney.anapi.v2.security.AccessService;
import com.rbkmoney.anapi.v2.service.SearchService;
import com.rbkmoney.openapi.anapi_v2.api.PayoutsApiController;
import com.rbkmoney.openapi.anapi_v2.model.InlineResponse20012;
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
public class PayoutsSearchController extends PayoutsApiController {

    private final SearchService searchService;
    private final AccessService accessService;
    private final ParamsToPayoutSearchQueryConverter payoutSearchConverter;

    public PayoutsSearchController(
            NativeWebRequest request,
            SearchService searchService,
            AccessService accessService,
            ParamsToPayoutSearchQueryConverter payoutSearchConverter) {
        super(request);
        this.searchService = searchService;
        this.accessService = accessService;
        this.payoutSearchConverter = payoutSearchConverter;
    }

    @Override
    public ResponseEntity<InlineResponse20012> searchPayouts(
            String xRequestID,
            @NotNull @Size(min = 1, max = 40) @Valid String partyID,
            @NotNull @Valid OffsetDateTime fromTime,
            @NotNull @Valid OffsetDateTime toTime,
            @NotNull @Min(1L) @Max(1000L) @Valid Integer limit,
            String xRequestDeadline,
            @Size(min = 1, max = 40) @Valid String shopID,
            @Valid List<String> shopIDs,
            @Valid String paymentInstitutionRealm,
            @Size(min = 1, max = 40) @Valid String payoutID,
            @Valid String payoutToolType,
            @Valid String continuationToken) {
        log.info("-> Req: xRequestID={}", xRequestID);
        checkDeadline(xRequestDeadline, xRequestID);
        shopIDs = accessService.getAccessibleShops(
                "searchPayouts",
                partyID,
                merge(shopID, shopIDs),
                paymentInstitutionRealm);
        var query = payoutSearchConverter.convert(
                partyID,
                fromTime,
                toTime,
                limit,
                shopIDs,
                payoutID,
                payoutToolType,
                continuationToken);
        var response = searchService.searchPayouts(query);
        log.info("<- Res [200]: xRequestID={}", xRequestID);
        return ResponseEntity.ok(response);
    }
}

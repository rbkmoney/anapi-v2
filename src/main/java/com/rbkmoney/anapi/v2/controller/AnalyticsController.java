package com.rbkmoney.anapi.v2.controller;

import com.rbkmoney.anapi.v2.security.AccessService;
import com.rbkmoney.anapi.v2.service.AnalyticsService;
import com.rbkmoney.damsel.analytics.SplitUnit;
import com.rbkmoney.damsel.analytics.*;
import com.rbkmoney.openapi.anapi_v2.api.AnalyticsApiController;
import com.rbkmoney.openapi.anapi_v2.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.rbkmoney.anapi.v2.util.DeadlineUtil.checkDeadline;

@Slf4j
@PreAuthorize("hasAuthority('invoices:read')")
@Controller
@SuppressWarnings("ParameterName")
public class AnalyticsController extends AnalyticsApiController {

    private final AccessService accessService;
    private final AnalyticsService analyticsService;

    public AnalyticsController(
            NativeWebRequest request,
            AccessService accessService,
            AnalyticsService analyticsService) {
        super(request);
        this.accessService = accessService;
        this.analyticsService = analyticsService;
    }

    @Override
    public ResponseEntity<InlineResponse200> getAveragePayment(
            String xRequestID,
            @NotNull @Size(min = 1, max = 40) @Valid String partyID,
            @NotNull @Valid OffsetDateTime fromTime,
            @NotNull @Valid OffsetDateTime toTime,
            String xRequestDeadline,
            @Valid List<String> shopIDs,
            @Valid List<String> excludeShopIDs,
            @Valid String paymentInstitutionRealm) {
        log.info("-> Req: xRequestID={}", xRequestID);
        checkDeadline(xRequestDeadline, xRequestID);
        shopIDs = accessService.getAccessibleShops(
                "getAveragePayment",
                partyID,
                shopIDs,
                paymentInstitutionRealm);
        var filterRequest = getFilterRequest(partyID, shopIDs, excludeShopIDs, fromTime, toTime);
        var response = analyticsService.getAveragePayment(filterRequest);
        log.info("<- Res [200]: xRequestID={}", xRequestID);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<InlineResponse200> getCreditingsAmount(
            String xRequestID,
            @NotNull @Size(min = 1, max = 40) @Valid String partyID,
            @NotNull @Valid OffsetDateTime fromTime,
            @NotNull @Valid OffsetDateTime toTime,
            String xRequestDeadline,
            @Valid List<String> shopIDs,
            @Valid List<String> excludeShopIDs,
            @Valid String paymentInstitutionRealm) {
        log.info("-> Req: xRequestID={}", xRequestID);
        checkDeadline(xRequestDeadline, xRequestID);
        shopIDs = accessService.getAccessibleShops(
                "getCreditingsAmount",
                partyID,
                shopIDs,
                paymentInstitutionRealm);
        var filterRequest = getFilterRequest(partyID, shopIDs, excludeShopIDs, fromTime, toTime);
        var response = analyticsService.getCreditingsAmount(filterRequest);
        log.info("<- Res [200]: xRequestID={}", xRequestID);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<InlineResponse200> getCurrentBalances(
            String xRequestID,
            @NotNull @Size(min = 1, max = 40) @Valid String partyID,
            String xRequestDeadline,
            @Valid List<String> shopIDs,
            @Valid List<String> excludeShopIDs,
            @Valid String paymentInstitutionRealm) {
        log.info("-> Req: xRequestID={}", xRequestID);
        checkDeadline(xRequestDeadline, xRequestID);
        shopIDs = accessService.getAccessibleShops(
                "getCurrentBalances",
                partyID,
                shopIDs,
                paymentInstitutionRealm);
        var merchantFilter = getMerchantFilter(partyID, shopIDs, excludeShopIDs);
        var response = analyticsService.getCurrentBalances(merchantFilter);
        log.info("<- Res [200]: xRequestID={}", xRequestID);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<InlineResponse2007> getCurrentShopBalances(
            String xRequestID,
            @NotNull @Size(min = 1, max = 40) @Valid String partyID,
            String xRequestDeadline,
            @Valid List<String> shopIDs,
            @Valid List<String> excludeShopIDs,
            @Valid String paymentInstitutionRealm) {
        log.info("-> Req: xRequestID={}", xRequestID);
        checkDeadline(xRequestDeadline, xRequestID);
        shopIDs = accessService.getAccessibleShops(
                "getCurrentShopBalances",
                partyID,
                shopIDs,
                paymentInstitutionRealm);
        var merchantFilter = getMerchantFilter(partyID, shopIDs, excludeShopIDs);
        var response = analyticsService.getCurrentShopBalances(merchantFilter);
        log.info("<- Res [200]: xRequestID={}", xRequestID);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<InlineResponse200> getPaymentsAmount(
            String xRequestID,
            @NotNull @Size(min = 1, max = 40) @Valid String partyID,
            @NotNull @Valid OffsetDateTime fromTime,
            @NotNull @Valid OffsetDateTime toTime,
            String xRequestDeadline,
            @Valid List<String> shopIDs,
            @Valid List<String> excludeShopIDs,
            @Valid String paymentInstitutionRealm) {
        log.info("-> Req: xRequestID={}", xRequestID);
        checkDeadline(xRequestDeadline, xRequestID);
        shopIDs = accessService.getAccessibleShops(
                "getPaymentsAmount",
                partyID,
                shopIDs,
                paymentInstitutionRealm);
        var filterRequest = getFilterRequest(partyID, shopIDs, excludeShopIDs, fromTime, toTime);
        var response = analyticsService.getPaymentsAmount(filterRequest);
        log.info("<- Res [200]: xRequestID={}", xRequestID);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<InlineResponse2001> getPaymentsCount(
            String xRequestID,
            @NotNull @Size(min = 1, max = 40) @Valid String partyID,
            @NotNull @Valid OffsetDateTime fromTime,
            @NotNull @Valid OffsetDateTime toTime,
            String xRequestDeadline,
            @Valid List<String> shopIDs,
            @Valid List<String> excludeShopIDs,
            @Valid String paymentInstitutionRealm) {
        log.info("-> Req: xRequestID={}", xRequestID);
        checkDeadline(xRequestDeadline, xRequestID);
        shopIDs = accessService.getAccessibleShops(
                "getPaymentsCount",
                partyID,
                shopIDs,
                paymentInstitutionRealm);
        var filterRequest = getFilterRequest(partyID, shopIDs, excludeShopIDs, fromTime, toTime);
        var response = analyticsService.getPaymentsCount(filterRequest);
        log.info("<- Res [200]: xRequestID={}", xRequestID);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<InlineResponse2002> getPaymentsErrorDistribution(
            String xRequestID,
            @NotNull @Size(min = 1, max = 40) @Valid String partyID,
            @NotNull @Valid OffsetDateTime fromTime,
            @NotNull @Valid OffsetDateTime toTime,
            String xRequestDeadline,
            @Valid List<String> shopIDs,
            @Valid List<String> excludeShopIDs,
            @Valid String paymentInstitutionRealm) {
        log.info("-> Req: xRequestID={}", xRequestID);
        checkDeadline(xRequestDeadline, xRequestID);
        shopIDs = accessService.getAccessibleShops(
                "getPaymentsErrorDistribution",
                partyID,
                shopIDs,
                paymentInstitutionRealm);
        var filterRequest = getFilterRequest(partyID, shopIDs, excludeShopIDs, fromTime, toTime);
        var response = analyticsService.getPaymentsErrorDistribution(filterRequest);
        log.info("<- Res [200]: xRequestID={}", xRequestID);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<InlineResponse2003> getPaymentsSplitAmount(
            String xRequestID,
            @NotNull @Size(min = 1, max = 40) @Valid String partyID,
            @NotNull @Valid OffsetDateTime fromTime,
            @NotNull @Valid OffsetDateTime toTime,
            @NotNull @Valid String splitUnit,
            String xRequestDeadline,
            @Valid List<String> shopIDs,
            @Valid List<String> excludeShopIDs,
            @Valid String paymentInstitutionRealm) {
        log.info("-> Req: xRequestID={}", xRequestID);
        checkDeadline(xRequestDeadline, xRequestID);
        shopIDs = accessService.getAccessibleShops(
                "getPaymentsSplitAmount",
                partyID,
                shopIDs,
                paymentInstitutionRealm);
        var splitFilterRequest = getSplitFilterRequest(
                partyID,
                shopIDs, excludeShopIDs, fromTime,
                toTime,
                splitUnit
        );
        var response = analyticsService.getPaymentsSplitAmount(splitFilterRequest);
        log.info("<- Res [200]: xRequestID={}", xRequestID);
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<InlineResponse2004> getPaymentsSplitCount(
            String xRequestID,
            @NotNull @Size(min = 1, max = 40) @Valid String partyID,
            @NotNull @Valid OffsetDateTime fromTime,
            @NotNull @Valid OffsetDateTime toTime,
            @NotNull @Valid String splitUnit,
            String xRequestDeadline,
            @Valid List<String> shopIDs,
            @Valid List<String> excludeShopIDs,
            @Valid String paymentInstitutionRealm) {
        log.info("-> Req: xRequestID={}", xRequestID);
        checkDeadline(xRequestDeadline, xRequestID);
        shopIDs = accessService.getAccessibleShops(
                "getPaymentsSplitCount",
                partyID,
                shopIDs,
                paymentInstitutionRealm);
        var splitFilterRequest = getSplitFilterRequest(
                partyID,
                shopIDs, excludeShopIDs, fromTime,
                toTime,
                splitUnit
        );
        var response = analyticsService.getPaymentsSplitCount(splitFilterRequest);
        log.info("<- Res [200]: xRequestID={}", xRequestID);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<InlineResponse2005> getPaymentsSubErrorDistribution(
            String xRequestID,
            @NotNull @Size(min = 1, max = 40) @Valid String partyID,
            @NotNull @Valid OffsetDateTime fromTime,
            @NotNull @Valid OffsetDateTime toTime,
            String xRequestDeadline,
            @Valid List<String> shopIDs,
            @Valid List<String> excludeShopIDs,
            @Valid String paymentInstitutionRealm) {
        log.info("-> Req: xRequestID={}", xRequestID);
        checkDeadline(xRequestDeadline, xRequestID);
        shopIDs = accessService.getAccessibleShops(
                "getPaymentsSubErrorDistribution",
                partyID,
                shopIDs,
                paymentInstitutionRealm);
        var filterRequest = getFilterRequest(partyID, shopIDs, excludeShopIDs, fromTime, toTime);
        var response = analyticsService.getPaymentsSubErrorDistribution(filterRequest);
        log.info("<- Res [200]: xRequestID={}", xRequestID);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<InlineResponse2006> getPaymentsToolDistribution(
            String xRequestID,
            @NotNull @Size(min = 1, max = 40) @Valid String partyID,
            @NotNull @Valid OffsetDateTime fromTime,
            @NotNull @Valid OffsetDateTime toTime,
            String xRequestDeadline,
            @Valid List<String> shopIDs,
            @Valid List<String> excludeShopIDs,
            @Valid String paymentInstitutionRealm) {
        log.info("-> Req: xRequestID={}", xRequestID);
        checkDeadline(xRequestDeadline, xRequestID);
        shopIDs = accessService.getAccessibleShops(
                "getPaymentsToolDistribution",
                partyID,
                shopIDs,
                paymentInstitutionRealm);
        var filterRequest = getFilterRequest(partyID, shopIDs, excludeShopIDs, fromTime, toTime);
        var response = analyticsService.getPaymentsToolDistribution(filterRequest);
        log.info("<- Res [200]: xRequestID={}", xRequestID);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<InlineResponse200> getRefundsAmount(
            String xRequestID,
            @NotNull @Size(min = 1, max = 40) @Valid String partyID,
            @NotNull @Valid OffsetDateTime fromTime,
            @NotNull @Valid OffsetDateTime toTime,
            String xRequestDeadline,
            @Valid List<String> shopIDs,
            @Valid List<String> excludeShopIDs,
            @Valid String paymentInstitutionRealm) {
        log.info("-> Req: xRequestID={}", xRequestID);
        checkDeadline(xRequestDeadline, xRequestID);
        shopIDs = accessService.getAccessibleShops(
                "getRefundsAmount",
                partyID,
                shopIDs,
                paymentInstitutionRealm);
        var filterRequest = getFilterRequest(partyID, shopIDs, excludeShopIDs, fromTime, toTime);
        var response = analyticsService.getRefundsAmount(filterRequest);
        log.info("<- Res [200]: xRequestID={}", xRequestID);
        return ResponseEntity.ok(response);
    }

    private SplitFilterRequest getSplitFilterRequest(
            String partyID,
            List<String> shopIDs,
            List<String> excludeShopIDs,
            OffsetDateTime fromTime,
            OffsetDateTime toTime,
            String splitUnit) {
        return new SplitFilterRequest()
                .setFilterRequest(getFilterRequest(partyID, shopIDs, excludeShopIDs, fromTime, toTime))
                .setSplitUnit(SplitUnit.valueOf(splitUnit));
    }

    private FilterRequest getFilterRequest(
            String partyID,
            List<String> shopIDs,
            List<String> excludeShopIDs,
            OffsetDateTime fromTime,
            OffsetDateTime toTime) {
        return new FilterRequest()
                .setMerchantFilter(getMerchantFilter(partyID, shopIDs, excludeShopIDs))
                .setTimeFilter(new TimeFilter()
                        .setFromTime(fromTime.format(DateTimeFormatter.ISO_INSTANT))
                        .setToTime(toTime.format(DateTimeFormatter.ISO_INSTANT)));
    }

    private MerchantFilter getMerchantFilter(String partyID, List<String> shopIDs, List<String> excludeShopIDs) {
        return new MerchantFilter()
                .setPartyId(partyID)
                .setShopIds(shopIDs)
                .setExcludeShopIds(excludeShopIDs);
    }
}

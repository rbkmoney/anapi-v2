package com.rbkmoney.anapi.v2.converter.search.response;

import com.rbkmoney.damsel.domain.InvoicePaymentStatus;
import com.rbkmoney.damsel.domain.*;
import com.rbkmoney.geck.common.util.TypeUtil;
import com.rbkmoney.magista.CustomerPayer;
import com.rbkmoney.magista.InvoicePaymentFlow;
import com.rbkmoney.magista.InvoicePaymentFlowHold;
import com.rbkmoney.magista.Payer;
import com.rbkmoney.magista.*;
import com.rbkmoney.openapi.anapi_v2.model.PaymentFlow;
import com.rbkmoney.openapi.anapi_v2.model.PaymentSearchResult;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static com.rbkmoney.anapi.v2.testutil.MagistaUtil.createSearchPaymentAllResponse;
import static com.rbkmoney.anapi.v2.testutil.RandomUtil.randomString;
import static com.rbkmoney.openapi.anapi_v2.model.Payer.PayerTypeEnum.*;
import static com.rbkmoney.openapi.anapi_v2.model.PaymentSearchResult.StatusEnum.*;
import static org.junit.jupiter.api.Assertions.*;

class StatPaymentToPaymentSearchResultConverterTest {

    private static final StatPaymentToPaymentSearchResultConverter converter =
            new StatPaymentToPaymentSearchResultConverter();

    @Test
    void convert() {
        StatPaymentResponse magistaResponse = createSearchPaymentAllResponse();
        StatPayment magistaPayment = magistaResponse.getPayments().get(0);
        magistaPayment.setFlow(InvoicePaymentFlow.hold(new InvoicePaymentFlowHold()));
        magistaPayment.setStatusChangedAt(TypeUtil.temporalToString(OffsetDateTime.now().toLocalDateTime()));
        magistaPayment.setAdditionalTransactionInfo(new AdditionalTransactionInfo()
                .setRrn(randomString(10))
                .setApprovalCode(randomString(10)));
        PaymentSearchResult result = converter.convert(magistaPayment);
        assertAll(
                () -> assertEquals(magistaPayment.getAmount(), result.getAmount()),
                () -> assertEquals(magistaPayment.getCreatedAt(), result.getCreatedAt().toString()),
                () -> assertEquals(magistaPayment.getCurrencySymbolicCode(), result.getCurrency()),
                () -> assertEquals(magistaPayment.getExternalId(), result.getExternalID()),
                () -> assertEquals(magistaPayment.getFee(), result.getFee()),
                () -> assertEquals(PaymentFlow.TypeEnum.PAYMENTFLOWHOLD, result.getFlow().getType()),
                () -> assertEquals(magistaPayment.getLocationInfo().getCityGeoId(),
                        result.getGeoLocationInfo().getCityGeoID()),
                () -> assertEquals(magistaPayment.getLocationInfo().getCountryGeoId(),
                        result.getGeoLocationInfo().getCountryGeoID()),
                () -> assertEquals(magistaPayment.getStatusChangedAt(), result.getStatusChangedAt().toString()),
                () -> assertEquals(magistaPayment.getId(), result.getId()),
                () -> assertEquals(magistaPayment.getInvoiceId(), result.getInvoiceID()),
                () -> assertEquals(magistaPayment.isMakeRecurrent(), result.getMakeRecurrent()),
                () -> assertEquals(magistaPayment.getShopId(), result.getShopID()),
                () -> assertEquals(magistaPayment.getShortId(), result.getShortID()),
                () -> assertEquals(magistaPayment.getAdditionalTransactionInfo().getApprovalCode(),
                        result.getTransactionInfo().getApprovalCode()),
                () -> assertEquals(magistaPayment.getAdditionalTransactionInfo().getRrn(),
                        result.getTransactionInfo().getRrn())
        );
    }

    @Test
    void mapPayer() {
        assertEquals(CUSTOMERPAYER, converter.mapPayer(Payer.customer(new CustomerPayer())).getPayerType());
        assertEquals(PAYMENTRESOURCEPAYER,
                converter.mapPayer(Payer.payment_resource(new PaymentResourcePayer())).getPayerType());
        assertEquals(RECURRENTPAYER, converter.mapPayer(Payer.recurrent(new RecurrentPayer())).getPayerType());
        assertThrows(IllegalArgumentException.class, () -> converter.mapPayer(new Payer()));
    }

    @Test
    void mapStatus() {
        var status = InvoicePaymentStatus.pending(new InvoicePaymentPending());
        assertEquals(PENDING, converter.mapStatus(status));

        status = InvoicePaymentStatus.processed(new InvoicePaymentProcessed());
        assertEquals(PROCESSED, converter.mapStatus(status));

        status = InvoicePaymentStatus.captured(new InvoicePaymentCaptured());
        assertEquals(CAPTURED, converter.mapStatus(status));

        status = InvoicePaymentStatus.cancelled(new InvoicePaymentCancelled());
        assertEquals(CANCELLED, converter.mapStatus(status));

        status = InvoicePaymentStatus.refunded(new InvoicePaymentRefunded());
        assertEquals(REFUNDED, converter.mapStatus(status));

        status = InvoicePaymentStatus.failed(new InvoicePaymentFailed());
        assertEquals(FAILED, converter.mapStatus(status));

        status = InvoicePaymentStatus.charged_back(new InvoicePaymentChargedBack());
        assertEquals(CHARGEDBACK, converter.mapStatus(status));

        assertThrows(IllegalArgumentException.class, () -> converter.mapStatus(new InvoicePaymentStatus()));
    }
}
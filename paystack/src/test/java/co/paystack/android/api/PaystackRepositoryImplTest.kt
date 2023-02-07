package co.paystack.android.api

import co.paystack.android.api.model.ChargeResponse
import co.paystack.android.api.model.TransactionInitResponse
import co.paystack.android.api.request.ChargeParams
import co.paystack.android.api.request.TransactionInitRequestBody
import co.paystack.android.api.service.PaystackApiService
import co.paystack.android.model.Charge
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyMap
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PaystackRepositoryImplTest {
    private val apiService: PaystackApiService = mock()

    @Test
    fun `processCardCharge calls chargeCard on PaystackApiService once`() {
        whenever(apiService.chargeCard(TEST_CHARGE_PARAMS.toRequestMap()))
            .thenReturn(FakeCall.success(TEST_CHARGE_RESPONSE))

        val repository = PaystackRepositoryImpl(apiService)
        val chargeApiCallback = mock<ChargeApiCallback>()

        repository.processCardCharge(TEST_CHARGE_PARAMS, chargeApiCallback)
        verify(apiService, times(1)).chargeCard(TEST_CHARGE_PARAMS.toRequestMap())
    }

    @Test
    fun `initializeTransaction calls PaystackApiService with correct params`() {
        whenever(apiService.initializeTransaction(anyMap()))
            .thenReturn(FakeCall.success(TransactionInitResponse("success", "trans_id")))
        val repository = PaystackRepositoryImpl(apiService)
        val apiCallback = mock<ApiCallback<TransactionInitResponse>>()
        val testMetadata = "internal_tag" to "tag_internal_example"
        val charge = Charge().apply {
            email = testEmail
            amount = testAmount
            currency = testCurrency
            reference = testReference
            subaccount = "subacc_13850248"
            transactionCharge = 1000
            plan = "PLN_123456789"
            bearer = Charge.Bearer.subaccount
            putMetadata(testMetadata.first, testMetadata.second)
        }
        repository.initializeTransaction(testPublicKey, charge, testDeviceId, apiCallback)

        val expectedApiCallMap = mapOf(
            TransactionInitRequestBody.FIELD_KEY to testPublicKey,
            TransactionInitRequestBody.FIELD_EMAIL to charge.email,
            TransactionInitRequestBody.FIELD_AMOUNT to charge.amount,
            TransactionInitRequestBody.FIELD_CURRENCY to charge.currency,
            TransactionInitRequestBody.FIELD_METADATA to charge.metadata,
            TransactionInitRequestBody.FIELD_DEVICE to testDeviceId,
            TransactionInitRequestBody.FIELD_REFERENCE to charge.reference,
            TransactionInitRequestBody.FIELD_SUBACCOUNT to charge.subaccount,
            TransactionInitRequestBody.FIELD_TRANSACTION_CHARGE to charge.transactionCharge,
            TransactionInitRequestBody.FIELD_BEARER to charge.bearer.name,
            TransactionInitRequestBody.FIELD_PLAN to charge.plan,
        )

        verify(apiService).initializeTransaction(expectedApiCallMap)
    }

    companion object {
        const val testPublicKey = "pk_live_123445677555"
        const val testEmail = "michael@paystack.com"
        const val testDeviceId = "test_device_id"
        const val testAmount = 10000
        const val testCurrency = "NGN"
        const val testTransactionId = "123458685949"
        const val testReference = "ref_123458685949"

        val TEST_CHARGE_PARAMS = ChargeParams(
            clientData = "encryptedClientData",
            transactionId = testTransactionId,
            last4 = "5599",
            deviceId = testDeviceId,
            null
        )

        val TEST_CHARGE_RESPONSE = ChargeResponse(
            status = "success",
            transactionId = testTransactionId,
            reference = "ref_42f45g53",
            message = "message",
            otpMessage = "Otpmessage",
            auth = "none",
            countryCode = "NG"
        )
    }
}
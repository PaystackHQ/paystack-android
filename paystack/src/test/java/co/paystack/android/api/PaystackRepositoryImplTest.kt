package co.paystack.android.api

import co.paystack.android.api.model.ChargeResponse
import co.paystack.android.api.request.ChargeParams
import co.paystack.android.api.service.PaystackApiService
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Test

class PaystackRepositoryImplTest {
    private val apiService: PaystackApiService = mock()

    @Test
    fun `processCardCharge calls chargeCard on PaystackApiService once`() {
        whenever(apiService.chargeCard(TEST_CHARGE_PARAMS.toRequestMap()))
            .thenReturn(FakeCall.success(TEST_CHARGE_RESPONSE))

        val repository = PaystackRepositoryImpl(apiService)

        repository.processCardCharge(TEST_CHARGE_PARAMS, mock())
        verify(apiService, times(1)).chargeCard(TEST_CHARGE_PARAMS.toRequestMap())
    }

    companion object {
        const val testPublicKey = "pk_live_123445677555"
        const val testEmail = "michael@paystack.com"
        const val testDeviceId = "test_device_id"
        const val testAmount = 10000
        const val testCurrency = "NGN"
        const val testTransactionId = "123458685949"

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
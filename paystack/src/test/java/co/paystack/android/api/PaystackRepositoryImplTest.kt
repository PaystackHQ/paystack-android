package co.paystack.android.api

import co.paystack.android.api.model.TransactionInitResponse
import co.paystack.android.api.request.TransactionInitRequestBody
import co.paystack.android.api.service.PaystackApiService
import co.paystack.android.model.Charge
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PaystackRepositoryImplTest {
    private val apiService: PaystackApiService = mock()

    @Before
    fun setUp() {
    }

    @Test
    fun `intializeTransaction calls initializeTransaction on PaystackApiService`() {
        whenever(apiService.initializeTransaction(TEST_TRANSACTION_INIT_BODY.toRequestMap()))
            .thenReturn(FakeCall.success(TEST_TRANSACTION_INIT_RESPONSE))

        val repository = PaystackRepositoryImpl(apiService)
        repository.initializeTransaction(testPublicKey, TEST_CHARGE, testDeviceId, mock())

        verify(apiService.initializeTransaction(TEST_TRANSACTION_INIT_BODY.toRequestMap()), times(1))
    }

    companion object {
        const val testPublicKey = "pk_live_123445677555"
        const val testEmail = "michael@paystack.com"
        const val testDeviceId = "test_device_id"
        const val testAmount = 10000
        const val testCurrency = "NGN"

        val TEST_TRANSACTION_INIT_BODY = TransactionInitRequestBody(
            publicKey = testPublicKey,
            email = testEmail,
            amount = testAmount,
            currency = testCurrency,
            device = testDeviceId,
            metadata = ""
        )
        val TEST_TRANSACTION_INIT_RESPONSE = TransactionInitResponse(
            status = "success",
            transactionId = "74927849ID"
        )

        val TEST_CHARGE = Charge().apply {
            email = testEmail
            amount = testAmount
            currency = testCurrency

        }
    }
}
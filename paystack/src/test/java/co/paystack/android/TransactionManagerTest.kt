package co.paystack.android

import android.util.Log
import androidx.test.core.app.ActivityScenario
import co.paystack.android.Paystack.TransactionCallback
import co.paystack.android.api.ApiCallback
import co.paystack.android.api.ChargeApiCallback
import co.paystack.android.api.PaystackRepository
import co.paystack.android.api.model.TransactionInitResponse
import co.paystack.android.api.request.ChargeParams
import co.paystack.android.model.Card
import co.paystack.android.model.Charge
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TransactionManagerTest {
    @Mock
    lateinit var paystackRepository: PaystackRepository

    private val emptyTransactionCallback = object : TransactionCallback {
        override fun onSuccess(transaction: Transaction?) {
        }

        override fun beforeValidate(transaction: Transaction?) {
        }

        override fun showLoading(isProcessing: Boolean?) {
        }

        override fun onError(error: Throwable?, transaction: Transaction?) {
        }
    }

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun chargeCardIsCalled_initiateChargeOnServer() {
        ActivityScenario.launch(TestActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                // Initialize TransactionManager
                val transactionManager = TransactionManager(paystackRepository)
                val charge = Charge().setCard(TEST_CARD)

                val publicKey = "public_key"
                transactionManager.chargeCard(
                    activity,
                    publicKey,
                    charge,
                    mock(Paystack.TransactionCallback::class.java)
                )
                verify(paystackRepository).initializeTransaction(isA(), isA(), isA(), isA())
            }
        }
    }

    @Test
    fun initiateTransactionIsCalled_ChargeAccessCodeIsNull_callInitializeTransactionOnPaystackRepository() {
        val publicKey = "pk_test_key"
        val deviceId = "android_702948084"
        val charge = Charge().apply {
            card = TEST_CARD
            accessCode = null
        }
        whenever(paystackRepository.initializeTransaction(anyString(), any(), anyString(), any()))
            .thenAnswer { Log.i(TAG, "initializeTransaction called") }

        val transactionManager = TransactionManager(paystackRepository)
        transactionManager.setTransactionCallback(emptyTransactionCallback)
        transactionManager.initiateTransaction(publicKey, charge, deviceId)

        verify(paystackRepository).initializeTransaction(
            isA<String>(),
            isA<Charge>(),
            isA<String>(),
            isA<ApiCallback<TransactionInitResponse>>()
        )
    }

    @Test
    fun initiateTransactionIsCalled_ChargeAccessCodeIsNotNull_call_getTransactionWithAccessCode_on_PaystackRepository() {
        val publicKey = "pk_test_key"
        val deviceId = "android_702948084"
        val transAccessCode = "transaction_access_code"
        val charge = Charge().apply {
            card = TEST_CARD
            accessCode = transAccessCode
        }

        whenever(paystackRepository.getTransactionWithAccessCode(anyString(), any()))
            .thenAnswer { Log.i(TAG, "getTransactionWithAccessCode called") }

        val transactionManager = TransactionManager(paystackRepository)
        transactionManager.setTransactionCallback(emptyTransactionCallback)
        transactionManager.initiateTransaction(publicKey, charge, deviceId)

        verify(paystackRepository).getTransactionWithAccessCode(
            isA<String>(),
            isA<ApiCallback<TransactionInitResponse>>()
        )
    }


    @Test
    fun initiateTransactionIsCalled_transactionInitializationSucceeds_call_processCardCharge_OnPaystackRepository() {
        val publicKey = "pk_test_key"
        val deviceId = "android_702948084"
        val charge = Charge().apply {
            card = TEST_CARD
            accessCode = null
        }

        whenever(paystackRepository.initializeTransaction(anyString(), any(), anyString(), any()))
            .thenAnswer {
                val callback = it.arguments[3] as ApiCallback<TransactionInitResponse>
                callback.onSuccess(TransactionInitResponse("success", "trans_id"))
            }

        val transactionManager = TransactionManager(paystackRepository)
        transactionManager.setTransactionCallback(emptyTransactionCallback)
        transactionManager.initiateTransaction(publicKey, charge, deviceId)

        verify(paystackRepository).processCardCharge(
            isA<ChargeParams>(),
            isA<ChargeApiCallback>()
        )
    }


    companion object {
        private const val TAG = "TransactionManagerTest"
        val TEST_CARD = Card("5105105105105100", 2, 2024, "123")
    }
}
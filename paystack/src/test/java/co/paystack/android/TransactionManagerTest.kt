package co.paystack.android

import androidx.test.core.app.ActivityScenario
import co.paystack.android.api.service.ApiService
import co.paystack.android.model.Card
import co.paystack.android.model.Charge
import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class TransactionManagerTest {
    @Mock
    lateinit var apiService: ApiService

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun chargeCardIsCalled_initiateChargeOnServer() {
        ActivityScenario.launch(TestActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                // Initialize TransactionManager
                val transactionManager = TransactionManager(apiService)
                val charge = Charge().setCard(TEST_CARD)

                transactionManager.chargeCard(activity, charge, mock(Paystack.TransactionCallback::class.java))
                verify(apiService).charge(isA())
            }
        }
    }

    companion object {
        val TEST_CARD = Card("5105105105105100", 2, 2024, "123")
    }
}
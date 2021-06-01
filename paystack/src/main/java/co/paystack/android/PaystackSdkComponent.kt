package co.paystack.android

import co.paystack.android.api.di.apiComponent

internal fun sdkComponent(): PaystackSdkComponent = PaystackSdkModule

internal interface PaystackSdkComponent {
    val transactionManagerFactory: Factory<TransactionManager>
}

internal object PaystackSdkModule : PaystackSdkComponent {

    override val transactionManagerFactory: Factory<TransactionManager> = object : Factory<TransactionManager> {
        override fun create(): TransactionManager {
            return TransactionManager(apiComponent().paystackRepository)
        }
    }
}
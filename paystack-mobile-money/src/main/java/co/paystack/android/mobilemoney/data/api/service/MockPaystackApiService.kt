package co.paystack.android.mobilemoney.data.api.service

import co.paystack.android.mobilemoney.data.api.response.MobileMoneyChargeResponse
import co.paystack.android.mobilemoney.data.api.response.TransactionInitResponse

class MockPaystackApiService : PaystackApiService {
    /**
     * checkout/request_inline
     * */
    override suspend fun initiateTransaction(params: Map<String, Any>): TransactionInitResponse {
        return TransactionInitResponse(
            108469,
            "1s2pyr9mvhkdybd",
            params["currency"] as String,
            params["amount"] as Long,
            params["email"] as String,
            listOf("mobile_money", "card"),
            "Paystack",
            100043,
            "abandoned",
            null,
            listOf()
        )
    }

    override suspend fun chargeMobileMoney(params: Map<String, Any>): MobileMoneyChargeResponse {
        if (params.containsKey("registration_token")) {
            return MobileMoneyChargeResponse(
                (params["transaction"] ?: error("")).toString(),
                params["phone"]!! as String,
                params["provider"]!! as String,
                params["channel_name"]!! as String,
                MobileMoneyChargeResponse.DisplayResponse(
                    "pop",
                    "Dial *606# to complete things",
                    180
                )
            )
        }
        return MobileMoneyChargeResponse(
            (params["transaction"] ?: error("")).toString(),
            params["phone"]!! as String,
            params["provider"]!! as String,
            params["channel_name"]!! as String,
            MobileMoneyChargeResponse.DisplayResponse(
                "registration_token",
                "Please enter the registration token sent to your phone to complete this transaction",
                null
            )
        )
    }
}
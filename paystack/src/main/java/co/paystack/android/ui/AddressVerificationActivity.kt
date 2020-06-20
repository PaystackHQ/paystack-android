package co.paystack.android.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ListPopupWindow
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import co.paystack.android.R
import co.paystack.android.mobilemoney.data.api.PaystackApiFactory
import co.paystack.android.model.AvsState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

class AddressVerificationActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    private val addressHolder = AddressHolder.getInstance()
    private val lock = AddressHolder.getLock()


    private val paystackApiService by lazy {
        PaystackApiFactory.createRetrofitService()
    }

    private val etState by lazy { findViewById<EditText>(R.id.etState) }
    private val etStreet by lazy { findViewById<EditText>(R.id.etStreet) }
    private val etCity by lazy { findViewById<EditText>(R.id.etCity) }
    private val etZipCode by lazy { findViewById<EditText>(R.id.etZipCode) }

    private var states: List<AvsState> by Delegates.observable(emptyList()) { _, _, newStates ->
        val names = newStates.map { it.name }
        val listPopupWindow = initPopupMenu(etState, names) { position ->
            selectedState = newStates[position]
        }

        etState.setOnClickListener { listPopupWindow.show() }
    }

    private var selectedState: AvsState? by Delegates.observable<AvsState?>(null) { _, _, selectedState ->
        etState.setText(selectedState?.name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        setContentView(R.layout.co_paystack_android____activity_avs)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setup(intent.getStringExtra(EXTRA_COUNTRY_CODE)!!)


        findViewById<Button>(R.id.btnConfirm).setOnClickListener {
            val address = AddressHolder.Address()
            address.street = etStreet.text.toString()
            address.city = etCity.text.toString()
            address.zipCode = etZipCode.text.toString()
            address.state = selectedState?.name
            submit(address)
        }
    }

    private fun setup(countryCode: String) {
        launch {
            try {
                states = paystackApiService.getAddressVerificationStates(countryCode)
            } catch (e: Throwable) {
                Log.e(TAG, e.message, e)
            }
        }
    }

    private fun submit(address: AddressHolder.Address?) {
        synchronized(lock) {
            addressHolder.address = address
            (lock as Object).notify()
        }
        finish()
    }

    public override fun onDestroy() {
        super.onDestroy()
        submit(null)
        job.cancel()
    }


    private fun initPopupMenu(anchorView: View, items: List<String>, onItemClickListener: (Int) -> Unit): ListPopupWindow {
        val listPopupWindow = ListPopupWindow(this, null, R.attr.listPopupWindowStyle)
        val adapter = ArrayAdapter<CharSequence>(this, R.layout.support_simple_spinner_dropdown_item, items)
        listPopupWindow.setAdapter(adapter)
        listPopupWindow.anchorView = anchorView
        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            onItemClickListener(position)
            listPopupWindow.dismiss()
        }
        return listPopupWindow
    }

    companion object {
        private const val TAG = "AddressVerificationActi"
        const val EXTRA_COUNTRY_CODE = "country_code"
    }
}
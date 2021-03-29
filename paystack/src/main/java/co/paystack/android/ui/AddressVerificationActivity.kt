package co.paystack.android.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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
    private val tvError by lazy { findViewById<TextView>(R.id.tvError) }
    private val btnRetry by lazy { findViewById<Button>(R.id.btnRetry) }
    private val btnConfirm by lazy { findViewById<Button>(R.id.btnConfirm) }
    private val errorContainer by lazy { findViewById<LinearLayout>(R.id.errorContainer) }
    private val avsForm by lazy { findViewById<ScrollView>(R.id.avsForm) }
    private val pbLoadingStates by lazy { findViewById<ProgressBar>(R.id.pbLoadingStates) }

    private var states: List<AvsState> by Delegates.observable(emptyList()) { _, _, newStates ->
        val names = newStates.map { it.name }
        val listPopupWindow = initPopupMenu(etState, names) { position ->
            selectedState = newStates[position]
        }

        etState.setOnClickListener { listPopupWindow.show() }
    }

    private var selectedState: AvsState? by Delegates.observable<AvsState?>(null) { _, _, selectedState ->
        etState.setText(selectedState?.name)
        validateForm()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        setContentView(R.layout.co_paystack_android____activity_avs)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setupForm()
    }

    private fun setupForm() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateForm()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        etStreet.addTextChangedListener(textWatcher)
        etCity.addTextChangedListener(textWatcher)
        etZipCode.addTextChangedListener(textWatcher)


        val countryCode = intent.getStringExtra(EXTRA_COUNTRY_CODE)!!
        btnRetry.setOnClickListener {
            loadStates(countryCode)
        }
        loadStates(countryCode)

        btnConfirm.setOnClickListener {
            val address = AddressHolder.Address()
            address.street = etStreet.text.toString()
            address.city = etCity.text.toString()
            address.zipCode = etZipCode.text.toString()
            address.state = selectedState?.name
            submit(address)
        }
    }

    private fun validateForm() {
        val isValid = etStreet.text.isNotBlank() && etCity.text.toString().isNotBlank() &&
                etZipCode.text.toString().isNotBlank() && selectedState != null

        btnConfirm.isEnabled = isValid
    }

    private fun loadStates(countryCode: String) {
        errorContainer.visibility = View.GONE
        avsForm.visibility = View.GONE
        pbLoadingStates.visibility = View.VISIBLE

        launch(coroutineContext) {
            try {
                states = paystackApiService.getAddressVerificationStates(countryCode)
                errorContainer.visibility = View.GONE
                avsForm.visibility = View.VISIBLE
                pbLoadingStates.visibility = View.GONE
            } catch (e: Throwable) {
                Log.e(TAG, e.message, e)
                tvError.text = getString(R.string.pstk__avs_state_loading_error)
                errorContainer.visibility = View.VISIBLE
                avsForm.visibility = View.GONE
                pbLoadingStates.visibility = View.GONE
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
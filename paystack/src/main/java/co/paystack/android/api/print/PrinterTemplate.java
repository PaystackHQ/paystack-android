package co.paystack.android.api.print;

import android.content.Context;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import co.paystack.android.exceptions.PrinterException;
import co.paystack.android.model.Purchase;
import co.paystack.android.model.Receipt;
import co.paystack.android.model.ReceiptHeader;

import static android.os.Build.VERSION_CODES.KITKAT;
import static android.os.Build.VERSION_CODES.LOLLIPOP;
import static co.paystack.android.utils.Utils.formatCurrency;

/**
 * Created by Oluwagbenga on 29,November,2018
 */
public class PrinterTemplate {

    private StringBuilder templateBuilder;
    private WebView mWebView;
    private Context mContext;
    private int defaultTotal = 0;
    public static final String TAG = PrinterTemplate.class.getSimpleName();
    private int transactionCharge;
    private String cardName;
    private String cardNumber;
    private String transactionStatus;
    private String receiptMsg;

   // @RestrictTo(RestrictTo.Scope.LIBRARY)
    public PrinterTemplate(Context mContext){
        templateBuilder = new StringBuilder();
        mWebView = new WebView(mContext);
        this.mContext =mContext;
    }


    private StringBuilder getPurchases(List<Purchase> purchases){
        StringBuilder sBuilder = new StringBuilder();
        for (Purchase purchase : purchases){
            this.defaultTotal += purchase.getAmount();
            this.transactionCharge += purchase.getTransactionCharge();
            this.cardName = purchase.getCardName();
            this.cardNumber = purchase.getCardNumber();
            this.transactionStatus = purchase.getTransactionStatus();
            this.receiptMsg = purchase.getTransactionMessage();
            sBuilder.append("\t<tr class=\"service\">\n" +
                    "\t<td class=\"tableitem\"><p class=\"itemtext\">"+purchase.getItem()+"</p></td>\n" +
                    "\t<td class=\"tableitem\"><p class=\"itemtext\">"+purchase.getQuantity()+"</p></td>\n" +
                    "\t<td class=\"tableitem\"><p class=\"itemtext\">"
                    +formatCurrency(purchase.getAmount() >0?purchase.getAmount() :defaultTotal)+"</p></td>\n" +
                    "\t</tr>\n");
        }

        return sBuilder;
    }


    public static PrinterTemplate with(Context context){
        return new PrinterTemplate(context);
    }

    @RequiresApi(19)
    public void print(ReceiptHeader header, List<Purchase> purchases) {
        if (purchases == null || purchases.isEmpty()){
            //Throw an exception if the purchase is empty
            throw new PrinterException("Purchase cannot be empty. Add some parameter to " +
                    "the Purchase model.");
        }
        Receipt receipt = new Receipt();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yy", Locale.US);
        String date =sdf.format(new Date(System.currentTimeMillis()));
        receipt.addTransactionDate(date)
                .addReceiptHeader(header)
                .addPurchases(purchases);
        printPayslip( receipt);
    }


    private String getHeader(Receipt receipt){
        return "   <div id=\"mid\">\n" +
                "      <div class=\"info\">\n" +
                "        <h2 class=\"tocenter\">Marchant Info</h2>\n" +
                "        <p> \n" +
                "            Address : "+receipt.getReceiptHeader().getAddress()+"</br>\n" +
                "            Email   : "+receipt.getReceiptHeader().getEmail()+"</br>\n" +
                "            Phone   : "+receipt.getReceiptHeader().getPhone()+"</br>\n" +
                "        </p>\n" +
                "      </div>\n";
    }

    private String getAsHtml(int value){
        return "<td class=\"payment\"><h2>"+formatCurrency(value)+"</h2></td>";
    }

    /**
     *
     * @param receipt is the model that carries every item needed to print out the slip
     * @return returns an html string with the receipt's value
     */

    private String getFormattedReceipt(Receipt receipt){
        return  "<html>\n" +
                "<head>\n" +
                "\t<style>\n" +
                "\n" +
                "\tbody{\n" +
                "    font-family: Arial, Helvetica, sans-serif;\n" +
                "    width: -moz-max-content;\n" +
                "\t}\n" +
                "\n" +
                "#invoice-POS {\n" +
                "  box-shadow: 0 0 1in -0.25in rgba(0, 0, 0, 0.5);\n" +
                "  padding: 2mm;\n" +
                "  margin: 0 auto;\n" +
                "  width: 44mm;\n" +
                "  background: #FFF;\n" +
                "}\n" +
                "\n" +
                "::selection {\n" +
                "  background: #f31544;\n" +
                "  color: #FFF;\n" +
                "}\n" +
                "\n" +
                "::moz-selection {\n" +
                "  background: #f31544;\n" +
                "  color: #FFF;\n" +
                "}\n" +
                "\n" +
                "h1 {\n" +
                "  font-size: 1.5em;\n" +
                "  color: #222;\n" +
                "}\n" +
                "\n" +
                "h2 {\n" +
                "  font-size: .9em;\n" +
                "}\n" +
                "\n" +
                "h3 {\n" +
                "  font-size: 1.2em;\n" +
                "  font-weight: 300;\n" +
                "  line-height: 2em;\n" +
                "}\n" +
                "\n" +
                "p {\n" +
                "  font-size: .7em;\n" +
                "  color: #666;\n" +
                "  line-height: 1.2em;\n" +
                "}\n" +
                "\n" +
                "#top, #mid, #bot {\n" +
                "  /* Targets all id with 'col-' */\n" +
                "  border-bottom: 1px solid #EEE;\n" +
                "}\n" +
                "\n" +
                "#top {\n" +
                "  min-height: 40px;\n" +
                "}\n" +
                "\n" +
                "#mid {\n" +
                "  min-height: 80px;\n" +
                "}\n" +
                "\n" +
                "#bot {\n" +
                "  min-height: 50px;\n" +
                "}\n" +
                "\n" +
                "#top .logo {\n" +
                " margin-top: 20px; " +
                "height: 30px;\n" +
                "  width: 100px;" +
                "  background: url(file:///android_asset/logo/paystack_logo.png) no-repeat;\n" +
                "  background-size: 100px 25px;\n" +
                "}\n" +
                "\n" +
                ".clientlogo {\n" +
                "  float: left;\n" +
                "  height: 30px;\n" +
                "  width: 100px;\n" +
                "  background: url(file:///android_asset/logo/paystack_logo.png) no-repeat;\n" +
                "  background-size: 100px 15px;\n" +
                "  border-radius: 50px;\n" +
                "}\n" +
                "\n" +
                ".info {\n" +
                "  display: block;\n" +
                "  margin-left: 0;\n" +
                "}\n" +
                "\n" +
                ".title {\n" +
                "  float: right;\n" +
                "}\n" +
                "\n" +
                ".title p {\n" +
                "  text-align: right;\n" +
                "}\n" +
                "\n" +
                "table {\n" +
                "  width: 100%;\n" +
                "  border-collapse: collapse;\n" +
                "}\n" +
                "\n" +
                ".tabletitle {\n" +
                "  font-size: .5em;\n" +
                "  background: #EEE;\n" +
                "}\n" +
                "\n" +
                ".service {\n" +
                "  border-bottom: 1px solid #EEE;\n" +
                "}\n" +
                "\n" +
                ".item {\n" +
                "  width: 24mm;\n" +
                "}\n" +
                "\n" +
                ".itemtext {\n" +
                "  font-size: .5em;\n" +
                "}\n" +
                "\n" +
                ".cardno {\n" +
                "  font-size: .9em;\n" +
                "\ttext-align: center;\n" +
                "    font-weight: bold;\n" +
                "}\n" +
                "\n" +
                ".cardtype{\n" +
                "\tfont-size: 10px; \n" +
                "\tmargin-top: 15px;\n" +
                "\tmargin-bottom: 2px;\n" +
                "}\n" +
                "\n" +
                "#legalcopy {\n" +
                "  margin-top: 5mm;\n" +
                "}\n" +
                "\n" +
                "h2.tocenter{\n" +
                "    text-align: center;\n" +
                "}\n" +
                "\n" +
                "\t</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "  <div id=\"invoice-POS\">\n" +
                "    \n" +
                "    <center id=\"top\">\n" +
                "      <div class=\"logo\"></div>\n" +
                "      <div class=\"info\"> \n" +
                "        <h2>"+receipt.getReceiptHeader().getBusinessName()+"</h2>\n" +
                "      </div><!--End Info-->\n" +
                "    </center><!--End InvoiceTop-->\n" +
                "    \n" + getHeader(receipt)+
                "    </div><!--End Invoice Mid-->\n" +
                "\n" +
                "    <div\n" +
                "    \n" +
                "    <div id=\"bot\">\n" +
                "        <h2 class=\"tocenter\">PURCHASE</h2>\n" +
                "\n" +
                "\t\t\t\t\t<div id=\"table\">\n" +
                "\t\t\t\t\t\t<table>\n" +
                "\t\t\t\t\t\t\t<tr class=\"tabletitle\">\n" +
                "\t\t\t\t\t\t\t\t<td class=\"item\"><h2>Item</h2></td>\n" +
                "\t\t\t\t\t\t\t\t<td class=\"Hours\"><h2>Qty</h2></td>\n" +
                "\t\t\t\t\t\t\t\t<td class=\"Rate\"><h2>Sub Total</h2></td>\n" +
                "\t\t\t\t\t\t\t</tr>\n" +
                "\n" +  getPurchases(receipt.getPurchases())+
                "\t\t\t\t\t\t\t<tr class=\"tabletitle\">\n" +
                "\t\t\t\t\t\t\t\t<td></td>\n" +
                "\t\t\t\t\t\t\t\t<td class=\"Rate\"><h2>tax</h2></td>\n" + getAsHtml(transactionCharge)+
                "\t\t\t\t\t\t\t</tr>\n" +
                "\n" +
                "\t\t\t\t\t\t\t<tr class=\"tabletitle\">\n" +
                "\t\t\t\t\t\t\t\t<td></td>\n" +
                "\t\t\t\t\t\t\t\t<td class=\"Rate\"><h2>Total</h2></td>\n" +getAsHtml(defaultTotal +transactionCharge) +
                "\t\t\t\t\t\t\t</tr>\n" +
                "\n" +
                "\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t</div>" +
                "\n" +
                "\n" +
                "      <div class=\"info\">\n" +
                "        <div class=\"cardtype\">"+cardName+"</div>\n" +
                "        <div class=\"cardno\"> \n" +cardNumber +
                "        </div> <p> \n" +
                "            Transaction date : "+receipt.getTransactionDate()+"</br>\n" +
                "            Reference code   : 12345</br>\n" +
                "        </p>\n" +
                "      </div>\n" +
                "\n" +
                "    \n" +
                "      <div class=\"info\">\n" +
                "       \n" +
                "        <h2 class=\"tocenter\">"+transactionStatus+"</h2>\n" +
                "      </div>\n" +
                "  \n" +
                "<div id=\"legalcopy\"><p class=\"legal\">\n" +receiptMsg+ "\t</p>\n" +
                "\t</div>" +
                "</div><!--End InvoiceBot-->\n" +
                "  </div><!--End Invoice-->\n" +
                "</body>\n" +
                "</html>";
    }


    @RequiresApi(19)
    private void printPayslip(Receipt receipt) {

        // Generate an HTML document on the fly:;
        mWebView.loadDataWithBaseURL(null, getFormattedReceipt(receipt)
                , "text/HTML", "UTF-8", null);

        mWebView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //Log.i(TAG, "Setting up " + url);
                SimpleDateFormat sdf = new SimpleDateFormat("ss_mm_h", Locale.US);
                createWebPrintJob(view, mContext, "Paystack_Receipt_"
                        .concat(sdf.format(new Date(System.currentTimeMillis()))));
                mWebView = null;
            }

        });


    }


    @RequiresApi(19)
    private void createWebPrintJob(WebView webView, Context mContext, String paperName) {

        // Get a PrintManager instance
        PrintManager printManager = (PrintManager) mContext
                .getSystemService(Context.PRINT_SERVICE);

        String jobName =paperName  + " Document";

        // Get a print adapter instance
        PrintDocumentAdapter printAdapter = null;

        if (Build.VERSION.SDK_INT <= KITKAT){
            printAdapter = webView.createPrintDocumentAdapter();
        }else if (Build.VERSION.SDK_INT >= LOLLIPOP){
            printAdapter =  webView.createPrintDocumentAdapter(jobName);
        }

        if (printAdapter == null && null == printManager){
            Toast.makeText(mContext, "Your device does not support document print out!", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create a print job with name and adapter instance
        printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());

        // Save the job object for later status checking
        //mPrintJobs.add(printJob);
    }

}

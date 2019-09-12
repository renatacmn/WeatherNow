package br.com.weathernow

import android.app.AlertDialog
import android.content.DialogInterface
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    // Protected methods

    protected fun showAlertDialog(
        title: String?,
        message: String?,
        cancelable: Boolean = true,
        positiveButtonLabel: String = getString(android.R.string.ok),
        positiveButtonAction: () -> Unit? = {},
        negativeButtonLabel: String? = null,
        negativeButtonAction: () -> Unit? = {}
    ) {
        val builder = AlertDialog.Builder(this)
            .setCancelable(cancelable)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonLabel) { dialog: DialogInterface?, _ ->
                positiveButtonAction()
                dialog?.dismiss()
            }

        negativeButtonLabel?.let {
            builder.setNegativeButton(negativeButtonLabel) { dialog: DialogInterface?, _ ->
                negativeButtonAction()
                dialog?.dismiss()
            }
        }

        builder.create()
        builder.show()
    }

    protected fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

}

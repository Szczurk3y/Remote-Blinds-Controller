package AsyncTask

import Retrofit.ServiceBuilder
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import com.szczurk3y.blindsanimation.Handler
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicBoolean

class SendShouldBe(val context: Context): AsyncTask<String, Boolean, Boolean>() {
    private var progressDialog = ProgressDialog(context)
    private val isCanceled = AtomicBoolean(false)

    override fun onPreExecute() {
        super.onPreExecute()
        progressDialog.setMessage("Setting blinds...")
        progressDialog.setCancelable(true)
        progressDialog.setOnCancelListener {
            isCanceled.set(true)
        }
        progressDialog.show()
    }

    override fun doInBackground(vararg p0: String?): Boolean {
        Thread.sleep(1000) // Loading... (｡◕‿‿◕｡)
        for (blind in Handler.blindsList) {
            if (!isCanceled.get()) {
                val call = ServiceBuilder(blind.ip!!).getService().shouldBe(blind.blindCoverPercentage.toString())
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(context, "failure", Toast.LENGTH_LONG).show()
                    }

                    override fun onResponse(
                        call: retrofit2.Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        Toast.makeText(context, "success!", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
        return isCanceled.get()
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }
}

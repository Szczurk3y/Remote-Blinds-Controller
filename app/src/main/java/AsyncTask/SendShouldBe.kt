package AsyncTask

import Retrofit.ServiceBuilder
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.widget.Toast
import com.szczurk3y.blindsanimation.Handler
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response

class SendShouldBe(val context: Context): AsyncTask<String, Unit, Unit>() {
    private var progressDialog = ProgressDialog(context)

    override fun onPreExecute() {
        super.onPreExecute()
        progressDialog.setTitle("Setting blinds...")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    override fun doInBackground(vararg p0: String?) {
        Thread.sleep(1000) // Loading... (｡◕‿‿◕｡)
        for (blind in Handler.blindsList) {
            val call = ServiceBuilder(blind.ip!!).getService().shouldBe(blind.blindCoverPercentage.toString())
            call.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
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

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }
}

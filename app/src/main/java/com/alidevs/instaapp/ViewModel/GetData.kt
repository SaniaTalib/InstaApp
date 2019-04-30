package com.alidevs.instaapp.ViewModel

import android.os.AsyncTask
import android.util.Log
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.conn.DefaultClientConnection
import org.apache.http.util.EntityUtils
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import kotlin.math.log

class GetData(listner: AuthenticationListner) {


    private var listner = listner
    fun getUserinfo(accessToken: String) {
        val infowebsite = "https://api.instagram.com/v1/users/self/?access_token=$accessToken"
        /*val infowebsite = ""*/
        var getinfo = RequestInstagramApi(infowebsite, listner).execute()

        Log.d("#Token",accessToken)
    }

    private class RequestInstagramApi(infowebsite: String, listner: AuthenticationListner) : AsyncTask<Void, String, String>() {

        private var listner = listner
        private val infowebsite = infowebsite

        override fun doInBackground(vararg params: Void?): String? {

            val httpclient: HttpClient = DefaultHttpClient()
            val httpget: HttpGet = HttpGet(infowebsite)
            try {
                val response: HttpResponse = httpclient.execute(httpget)
                val httpEntity: HttpEntity = response.entity
                val json: String = EntityUtils.toString(httpEntity)
                return json
            } catch (exception: ClientProtocolException) {
                Log.d("ClientProtocolException", exception.toString())
            } catch (exception: IOException) {
                Log.d("IOException", exception.toString())
            }
            return null


        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var data = mutableMapOf<String, String>()
            if (result != null) {
                try {
                    var jsonobject = JSONObject(result)
                    jsonobject = jsonobject.getJSONObject("data")

                    if (jsonobject.has("id"))
                        data["id"] = jsonobject.getString("id")

                    if (jsonobject.has("username")) {
                        data["username"] = jsonobject.getString("username")
                    }

                    if (jsonobject.has("profile_picture"))
                        data["profile_picture"] = jsonobject.getString("profile_picture")


                    if (jsonobject.has("full_name"))
                        data["full_name"] = jsonobject.getString("full_name")

                    if (jsonobject.has("bio"))
                        data["bio"] = jsonobject.getString("bio")

                    if (jsonobject.has("website"))
                        data["website"] = jsonobject.getString("website")

                    if (jsonobject.has("is_business"))
                        data["is_business"] = jsonobject.getString("is_business")

                    if (jsonobject.has("counts")){
                        jsonobject = jsonobject.getJSONObject("counts")


                        if (jsonobject.has("media"))
                            data["media"] = jsonobject.getInt("media").toString()

                        if (jsonobject.has("follows"))
                            data["follows"] = jsonobject.getInt("follows").toString()

                        if (jsonobject.has("followed_by"))
                            data["followed_by"] = jsonobject.getInt("followed_by").toString()
                    }




                    listner.DataRecieved(data)


                } catch (exception: JSONException) {
                    Log.d("JSONException", exception.toString())
                }
            }
        }
    }

}
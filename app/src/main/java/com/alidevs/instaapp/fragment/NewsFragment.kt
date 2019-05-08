package com.alidevs.instaapp.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alidevs.instaapp.R
import com.alidevs.instaapp.adapter.NewsAdapter
import com.alidevs.instaapp.model.Item
import com.alidevs.instaapp.model.RSSObject
import com.alidevs.instaapp.utils.HttpDataHandler
import com.google.gson.Gson

class NewsFragment : Fragment(), NewsAdapter.ItemClickListener {


    private val RSS_link="http://www.hodinkee.com%2Farticles%2Frss.xml"
    private val RSS_to_JSON_API="https://api.rss2json.com/v1/api.json?rss_url="
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_news, container, false)
        recyclerView = root.findViewById(R.id.recyclerview) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        loadRSS()
        return root
    }

    private fun loadRSS()
    {
        try{
            val loadRSSAsync=object: AsyncTask<String, String, String>(){
                internal  var mDialog = ProgressDialog(context)

                override fun onPreExecute() {
                    mDialog.setMessage("Please wait...")
                    mDialog.setCancelable(false)
                    mDialog.show()
                }

                override fun doInBackground(vararg params: String): String {
                    val result:String
                    val http = HttpDataHandler()
                    result=http.getHttpDataHandler(params[0])
                    return result
                }override fun onPostExecute(result: String?) {
                    mDialog.dismiss()
                    var rssObject: RSSObject = Gson().fromJson<RSSObject>(result,RSSObject::class.java!!)
                    val adapter=NewsAdapter(rssObject,context!!,this@NewsFragment)
                    recyclerView.adapter=adapter
                    adapter.notifyDataSetChanged()
                }
            }

            val url_get_data=StringBuilder(RSS_to_JSON_API)  //call async to get data
            url_get_data.append(RSS_link)
            loadRSSAsync.execute(url_get_data.toString())
        }catch(e: Exception){
            Toast.makeText(context, "Error->$e", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onItemClicked(item: Item, position: Int) {
        val intent = Intent(context,NewsDetailActivity::class.java)
        intent.putExtra("url", item.link)
        startActivity(intent)
        /*addFragment(NewsDetailActivity(), true,"NewsDetailActivity", item.link)*/
    }

    private fun addFragment(fragment: Fragment, addToBackStack: Boolean, tag: String, item: String) {
        val manager = activity?.supportFragmentManager
        val ft = manager!!.beginTransaction()

        if (addToBackStack) {
            ft.addToBackStack(tag)
        }
        val ldf = fragment
        val args = Bundle()
        args.putString("url", item)
        ldf.arguments = args
        ft.replace(R.id.content_main, fragment, tag)
        ft.commit()
    }

}

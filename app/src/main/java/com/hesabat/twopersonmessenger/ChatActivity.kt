package com.hesabat.twopersonmessenger
import android.content.Intent
import android.net.Uri
import android.os.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.hesabat.twopersonmessenger.databinding.ActivityChatBinding
import java.net.URLEncoder

class ChatActivity:AppCompatActivity(){
    private lateinit var b:ActivityChatBinding; private lateinit var a:MessageAdapter
    private val h=Handler(Looper.getMainLooper()); private var last=0
    private val poll=object:Runnable{override fun run(){load();heartbeat();h.postDelayed(this,2500)}}
    override fun onCreate(s:Bundle?){
        super.onCreate(s); b=ActivityChatBinding.inflate(layoutInflater); setContentView(b.root)
        window.setFlags(android.view.WindowManager.LayoutParams.FLAG_SECURE,android.view.WindowManager.LayoutParams.FLAG_SECURE)
        a=MessageAdapter(Session.uid(this)){deleteDialog(it)}
        b.list.layoutManager=LinearLayoutManager(this); b.list.adapter=a
        b.sendBtn.setOnClickListener{send()}
        b.settingsBtn.setOnClickListener{startActivity(Intent(this,SettingsActivity::class.java))}
        b.callBtn.setOnClickListener{Toast.makeText(this,"Səsli zəng WebRTC mərhələsində aktivləşdiriləcək",Toast.LENGTH_SHORT).show()}
        b.videoBtn.setOnClickListener{Toast.makeText(this,"Video zəng WebRTC mərhələsində aktivləşdiriləcək",Toast.LENGTH_SHORT).show()}
        h.post(poll)
    }
    override fun onDestroy(){h.removeCallbacks(poll);super.onDestroy()}
    private fun load(){
        val cut=getSharedPreferences("security",0).getString("cleared_before","")?:""
        val q=if(cut.isBlank())"messages.php?after_id=$last&limit=100"
              else "messages.php?after_id=$last&limit=100&after_time="+URLEncoder.encode(cut,"UTF-8")
        Api.get(q,Session.token(this)){ok,raw->if(ok){
            val r=Gson().fromJson(raw,MessageResponse::class.java)
            if(r.messages.isNotEmpty())runOnUiThread{
                a.items.addAll(r.messages);last=r.messages.maxOf{it.id}
                a.notifyItemRangeInserted(a.items.size-r.messages.size,r.messages.size)
                b.list.scrollToPosition(a.items.size-1);markRead()
            }
        }}
    }
    private fun send(){
        val t=b.message.text.toString().trim();if(t.isEmpty())return;b.message.setText("")
        Api.post("send_message.php",mapOf("type" to "text","text" to t),Session.token(this)){ok,raw->
            if(ok){val r=Gson().fromJson(raw,SendResponse::class.java);r.message?.let{runOnUiThread{
                a.items.add(it);last=maxOf(last,it.id);a.notifyItemInserted(a.items.size-1);b.list.scrollToPosition(a.items.size-1)
            }}} else runOnUiThread{Toast.makeText(this,"Mesaj göndərilmədi",Toast.LENGTH_SHORT).show()}
        }
    }
    private fun markRead(){Api.post("read_messages.php", emptyMap<String,String>(),Session.token(this)){_,_->}}
    private fun heartbeat(){Api.post("heartbeat.php", emptyMap<String,String>(),Session.token(this)){_,_->}}
    private fun deleteDialog(m:Message){AlertDialog.Builder(this).setItems(arrayOf("Məndən sil","Hər iki tərəfdən sil")){_,w->
        Api.post("delete_message.php",mapOf("message_id" to m.id,"mode" to if(w==0)"me" else "everyone"),Session.token(this)){ok,_->
            if(ok)runOnUiThread{a.items.remove(m);a.notifyDataSetChanged()}
        }}.show()}
}
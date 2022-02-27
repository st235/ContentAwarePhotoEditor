package st235.com.github.sampleapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FeedActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    private val feedAdapter = FeedAdapter {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = feedAdapter

        feedAdapter.setItems(FEED_ITEMS)
    }

}
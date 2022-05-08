package st235.com.github.sampleapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import github.com.st235.lib_samurai.Harakiri
import github.com.st235.lib_samurai.SamuraiView

class FeedActivity : AppCompatActivity() {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var samuraiView: SamuraiView
    private lateinit var samuraiNextButton: Button
    private lateinit var recyclerView: RecyclerView

    private val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    private val feedAdapter = FeedAdapter(
        onViewBindListener = { view, item, position ->
            if (!onboardingManager.shouldShow || position != 0) {
                return@FeedAdapter
            }

            val imageView = view.findViewById<View>(R.id.images_layout)

            Harakiri(into = samuraiView)
                .overlayColorRes(R.color.colorWhiteOp95)
                .withTooltip(R.layout.onboarding_tooltip_hint, width = ViewGroup.LayoutParams.MATCH_PARENT)
                .capture(imageView)
        },
        onItemClickListener = {
            // empty on purpose
        }
    )

    private lateinit var onboardingManager: OnboardingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        onboardingManager = OnboardingManager(this)

        toolbar = findViewById(R.id.top_app_bar)
        setSupportActionBar(toolbar)

        samuraiView = findViewById(R.id.samurai_view)
        samuraiNextButton = findViewById(R.id.samurai_next_view)
        samuraiView.visibility = View.GONE

        samuraiNextButton.setOnClickListener {
            onboardingManager.updateWasShown()
            samuraiView.visibility = View.GONE
        }

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = feedAdapter

        feedAdapter.setItems(FEED_ITEMS)
    }

}
package jp.techacademy.tomoya.apiapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.internal.bind.DateTypeAdapter
import kotlinx.android.synthetic.main.activity_web_view.*
import java.net.URL

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        webView.loadUrl((if (intent.getSerializableExtra(KEY_URL) is Shop) {
            val shop = intent.getSerializableExtra(KEY_URL) as Shop
            if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc
        } else if (intent.getSerializableExtra(KEY_URL) is FavoriteShop) {
            val favoriteShop = intent.getSerializableExtra(KEY_URL) as FavoriteShop
            if (favoriteShop.url.isNotEmpty()) favoriteShop.url else null
        } else {
            null
        }).toString())

        if (intent.getSerializableExtra(KEY_URL) is Shop) {
            val shop = intent.getSerializableExtra(KEY_URL) as Shop
            val isFavorites = FavoriteShop.findBy(shop.id) != null

            fab.apply {
                setImageResource(if (isFavorites) R.drawable.ic_star else R.drawable.ic_star_border)
                setOnClickListener {
                    if (isFavorites) {
                        FavoriteShop.delete(shop.id)
                        setImageResource(R.drawable.ic_star_border)
                    } else {
                        FavoriteShop.insert(FavoriteShop().apply {
                            id = shop.id
                            name = shop.name
                            imageUrl = shop.logoImage
                            address = shop.address
                            couponUrls = shop.couponUrls.toString()
                            url =
                                if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc
                        })
                        setImageResource(R.drawable.ic_star)
                    }

                }
            }

        } else if (intent.getSerializableExtra(KEY_URL) is FavoriteShop) {
            val favoriteShop = intent.getSerializableExtra(KEY_URL) as FavoriteShop
            val isFavorite = FavoriteShop.findBy(favoriteShop.id) != null

            fab.apply {
                setImageResource(if (isFavorite) R.drawable.ic_star else R.drawable.ic_star_border)
                setOnClickListener {
                    if (isFavorite) {
                        FavoriteShop.delete(favoriteShop.id)
                        setImageResource(R.drawable.ic_star_border)
                    } else {
                        FavoriteShop.insert(FavoriteShop().apply {
                            id = favoriteShop.id
                            name = favoriteShop.name
                            address = favoriteShop.address
                            couponUrls = favoriteShop.couponUrls.toString()
                            url =
                                if (favoriteShop.url.isNotEmpty()) favoriteShop.url else favoriteShop.url

                        }
                        )
                        setImageResource(R.drawable.ic_star)
                    }
                }
            }
        } else {
            null
        }
    }

    companion object {
        private const val KEY_URL = "key_url"
        fun start(activity: Activity, shop: Shop) {
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtra(KEY_URL,
                shop))
        }

        fun startFavorite(activity: Activity, favoriteShop: FavoriteShop) {
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtra(KEY_URL,
                favoriteShop))
        }
    }
}


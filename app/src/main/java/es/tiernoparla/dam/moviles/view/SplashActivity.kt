package es.tiernoparla.dam.moviles.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import es.tiernoparla.dam.moviles.R
import es.tiernoparla.dam.moviles.view.utils.ViewUtil

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val SPLASH_TIME = 7000L

        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_view)

        Handler(Looper.getMainLooper()).postDelayed({
            try {
                startActivity(Intent(this, LoginActivity::class.java))
                overridePendingTransition(R.anim.scale_in, R.anim.scale_out)
                finish()
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }, SPLASH_TIME)
    }
}
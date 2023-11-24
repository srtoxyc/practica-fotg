package es.tiernoparla.dam.moviles.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import es.tiernoparla.dam.moviles.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
        }, 7000)
    }
}
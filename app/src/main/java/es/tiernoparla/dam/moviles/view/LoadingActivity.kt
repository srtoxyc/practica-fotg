package es.tiernoparla.dam.moviles.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import es.tiernoparla.dam.moviles.R
import es.tiernoparla.dam.moviles.view.utils.ViewUtil

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val LOADING_TIME = 2400L

        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_view)

        Handler(Looper.getMainLooper()).postDelayed({
            try {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                overridePendingTransition(R.anim.scale_in, R.anim.scale_out)
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }, LOADING_TIME)
    }
}
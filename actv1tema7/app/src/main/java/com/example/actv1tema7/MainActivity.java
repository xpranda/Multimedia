package com.example.actv1tema7;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private TextView tvCurrentTime, tvTotalTime, tvSongName;
    private Button btnPlay, btnPause;
    private ImageView imageView;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas
        seekBar = findViewById(R.id.seekBar2);
        tvCurrentTime = findViewById(R.id.textView10);
        tvTotalTime = findViewById(R.id.textView11);
        tvSongName = findViewById(R.id.textView9);
        btnPlay = findViewById(R.id.button4);
        btnPause = findViewById(R.id.button5);
        imageView = findViewById(R.id.imageView);

        // Configurar valores iniciales
        tvCurrentTime.setText("0:00");
        tvTotalTime.setText("0:00");
        tvSongName.setText("COMERNOS");

        // Cargar imagen
        try {
            imageView.setImageResource(R.drawable.images);
        } catch (Exception e) {
            Toast.makeText(this, "No se encontró la imagen", Toast.LENGTH_SHORT).show();
        }

        // Inicializar MediaPlayer
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.musica);

            if (mediaPlayer != null) {
                // Configurar duración total
                int duration = mediaPlayer.getDuration();
                tvTotalTime.setText(formatTime(duration));
                seekBar.setMax(duration);

                // Configuración de botones
                setupButtons();

                // Configuración de SeekBar
                setupSeekBar();

                // Listener para cuando termine la canción
                mediaPlayer.setOnCompletionListener(mp -> {
                    btnPlay.setEnabled(true);
                    btnPause.setEnabled(false);
                    seekBar.setProgress(0);
                    tvCurrentTime.setText("0:00");
                    if (updateSeekBar != null) {
                        handler.removeCallbacks(updateSeekBar);
                    }
                });
            } else {
                Toast.makeText(this, "Error al cargar el audio", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "No se encontró musica.mp3 en res/raw/", Toast.LENGTH_LONG).show();
        }
    }

    private void setupButtons() {
        btnPause.setEnabled(false);

        // Botón Play
        btnPlay.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.start();
                btnPlay.setEnabled(false);
                btnPause.setEnabled(true);
                updateSeekBarProgress();
            }
        });

        // Botón Pause
        btnPause.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPlay.setEnabled(true);
                btnPause.setEnabled(false);
                if (updateSeekBar != null) {
                    handler.removeCallbacks(updateSeekBar);
                }
            }
        });
    }

    private void setupSeekBar() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    tvCurrentTime.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (updateSeekBar != null) {
                    handler.removeCallbacks(updateSeekBar);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    updateSeekBarProgress();
                }
            }
        });
    }

    private void updateSeekBarProgress() {
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                    tvCurrentTime.setText(formatTime(currentPosition));
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(updateSeekBar);
    }

    private String formatTime(int milliseconds) {
        int seconds = (milliseconds / 1000) % 60;
        int minutes = (milliseconds / (1000 * 60)) % 60;
        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (updateSeekBar != null) {
            handler.removeCallbacks(updateSeekBar);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlay.setEnabled(true);
            btnPause.setEnabled(false);
            if (updateSeekBar != null) {
                handler.removeCallbacks(updateSeekBar);
            }
        }
    }
}
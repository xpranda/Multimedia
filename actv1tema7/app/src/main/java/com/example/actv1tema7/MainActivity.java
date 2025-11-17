package com.example.actv1tema7;

import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private TextView tvCurrentTime, tvTotalTime, tvSongName;
    private Button btnPlay, btnPause;
    private ImageView imageView;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable updateSeekBar;

    // SoundPool para efectos
    private SoundPool soundPool;
    private int sound1 = -1, sound2 = -1, sound3 = -1, sound4 = -1;
    private Button btnSound1, btnSound2, btnSound3, btnSound4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- Bind UI ---
        seekBar = findViewById(R.id.seekBar2);
        tvCurrentTime = findViewById(R.id.textView10);
        tvTotalTime = findViewById(R.id.textView11);
        tvSongName = findViewById(R.id.textView9);
        btnPlay = findViewById(R.id.button4);
        btnPause = findViewById(R.id.button5);
        imageView = findViewById(R.id.imageView);

        btnSound1 = findViewById(R.id.btnSound1);
        btnSound2 = findViewById(R.id.btnSound2);
        btnSound3 = findViewById(R.id.btnSound3);
        btnSound4 = findViewById(R.id.btnSound4);

        // Valores iniciales
        tvSongName.setText("COMERNOS");
        tvCurrentTime.setText("0:00");
        tvTotalTime.setText("0:00");

        try {
            imageView.setImageResource(R.drawable.images);
        } catch (Exception e) {}

        // Reproductor principal con musica.mp3
        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.musica);
        } catch (Exception e) {
            mediaPlayer = null;
        }

        if (mediaPlayer == null) {
            Toast.makeText(this, "Error: musica.mp3 no encontrado en res/raw", Toast.LENGTH_LONG).show();
        } else {
            int duration = mediaPlayer.getDuration();
            tvTotalTime.setText(formatTime(duration));
            seekBar.setMax(duration);
            setupButtons();
            setupSeekBar();

            mediaPlayer.setOnCompletionListener(mp -> {
                btnPlay.setEnabled(true);
                btnPause.setEnabled(false);
                seekBar.setProgress(0);
                tvCurrentTime.setText("0:00");
                if (updateSeekBar != null) {
                    handler.removeCallbacks(updateSeekBar);
                }
            });
        }

        setupSoundPool();
    }

    private void setupSoundPool() {
        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .build();

        try {
            sound1 = soundPool.load(this, R.raw.sound1, 1);
            sound2 = soundPool.load(this, R.raw.sound2, 1);
            sound3 = soundPool.load(this, R.raw.sound3, 1);
            sound4 = soundPool.load(this, R.raw.sound4, 1);
        } catch (Exception e) {
            Toast.makeText(this, "No se pudieron cargar los efectos (comprueba nombres en res/raw)", Toast.LENGTH_LONG).show();
        }

        if (btnSound1 != null) {
            btnSound1.setOnClickListener(v -> {
                if (soundPool != null && sound1 != -1) soundPool.play(sound1, 1f, 1f, 1, 0, 1f);
            });
        }

        if (btnSound2 != null) {
            btnSound2.setOnClickListener(v -> {
                if (soundPool != null && sound2 != -1) soundPool.play(sound2, 1f, 1f, 1, 0, 1f);
            });
        }

        if (btnSound3 != null) {
            btnSound3.setOnClickListener(v -> {
                if (soundPool != null && sound3 != -1) soundPool.play(sound3, 1f, 1f, 1, 0, 1f);
            });
        }

        if (btnSound4 != null) {
            btnSound4.setOnClickListener(v -> {
                if (soundPool != null && sound4 != -1) soundPool.play(sound4, 1f, 1f, 1, 0, 1f);
            });
        }
    }

    private void setupButtons() {
        btnPause.setEnabled(false);

        btnPlay.setOnClickListener(v -> {
            if (mediaPlayer != null) {
                mediaPlayer.start();
                btnPlay.setEnabled(false);
                btnPause.setEnabled(true);
                updateSeekBarProgress();
            }
        });

        btnPause.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPlay.setEnabled(true);
                btnPause.setEnabled(false);
                if (updateSeekBar != null) handler.removeCallbacks(updateSeekBar);
            }
        });
    }

    private void setupSeekBar() {
        if (seekBar == null) return;

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    tvCurrentTime.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar sb) {
                if (updateSeekBar != null) handler.removeCallbacks(updateSeekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar sb) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) updateSeekBarProgress();
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
                    handler.postDelayed(this, 200);
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
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlay.setEnabled(true);
            btnPause.setEnabled(false);
            if (updateSeekBar != null) handler.removeCallbacks(updateSeekBar);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception ignored) { }
            mediaPlayer = null;
        }

        if (soundPool != null) {
            try {
                soundPool.release();
            } catch (Exception ignored) { }
            soundPool = null;
        }

        if (updateSeekBar != null) {
            handler.removeCallbacks(updateSeekBar);
        }
    }
}

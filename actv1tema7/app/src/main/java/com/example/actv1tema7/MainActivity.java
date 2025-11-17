package com.example.actv1tema7;

import android.media.AudioAttributes;
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

    private SoundPool soundPool;
    private int sound1, sound2, sound3, sound4;
    private Button btnSound1, btnSound2, btnSound3, btnSound4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        seekBar = findViewById(R.id.seekBar2);
        tvCurrentTime = findViewById(R.id.textView10);
        tvTotalTime = findViewById(R.id.textView11);
        tvSongName = findViewById(R.id.textView9);
        btnPlay = findViewById(R.id.button4);
        btnPause = findViewById(R.id.button5);
        imageView = findViewById(R.id.imageView);

        try {
            btnSound1 = findViewById(R.id.btnSound1);
            btnSound2 = findViewById(R.id.btnSound2);
            btnSound3 = findViewById(R.id.btnSound3);
            btnSound4 = findViewById(R.id.btnSound4);
        } catch (Exception e) {
            Toast.makeText(this, "Error al inicializar botones de efectos", Toast.LENGTH_SHORT).show();
        }

        tvCurrentTime.setText("0:00");
        tvTotalTime.setText("0:00");
        tvSongName.setText("COMERNOS");

        try {
            imageView.setImageResource(R.drawable.images);
        } catch (Exception e) {
            Toast.makeText(this, "No se encontró la imagen", Toast.LENGTH_SHORT).show();
        }

        try {
            mediaPlayer = MediaPlayer.create(this, R.raw.musica);

            if (mediaPlayer != null) {
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
            } else {
                Toast.makeText(this, "Error al cargar el audio", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "No se encontró musica.mp3 en res/raw/", Toast.LENGTH_LONG).show();
        }

        setupSoundPool();
    }

    private void setupSoundPool() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(audioAttributes)
                .build();

        try {
            sound1 = generateTone(440, 300); // La (A4) - 300ms
            sound2 = generateTone(523, 400); // Do (C5) - 400ms
            sound3 = generateTone(659, 200); // Mi (E5) - 200ms
            sound4 = generateTone(784, 500); // Sol (G5) - 500ms

            Toast.makeText(this, "Efectos de sonido generados", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error al generar efectos de sonido", Toast.LENGTH_SHORT).show();
        }

        if (btnSound1 != null) {
            btnSound1.setOnClickListener(v -> {
                if (soundPool != null) {
                    soundPool.play(sound1, 1.0f, 1.0f, 1, 0, 1.0f);
                }
            });
        }

        if (btnSound2 != null) {
            btnSound2.setOnClickListener(v -> {
                if (soundPool != null) {
                    soundPool.play(sound2, 1.0f, 1.0f, 1, 0, 1.0f);
                }
            });
        }

        if (btnSound3 != null) {
            btnSound3.setOnClickListener(v -> {
                if (soundPool != null) {
                    soundPool.play(sound3, 1.0f, 1.0f, 1, 0, 1.0f);
                }
            });
        }

        if (btnSound4 != null) {
            btnSound4.setOnClickListener(v -> {
                if (soundPool != null) {
                    soundPool.play(sound4, 1.0f, 1.0f, 1, 0, 1.0f);
                }
            });
        }
    }

    private int generateTone(int frequency, int duration) {
        try {
            int sampleRate = 8000;
            int numSamples = duration * sampleRate / 1000;
            double[] sample = new double[numSamples];
            byte[] generatedSound = new byte[2 * numSamples];

            for (int i = 0; i < numSamples; ++i) {
                sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / frequency));
            }

            int idx = 0;
            for (double dVal : sample) {
                short val = (short) ((dVal * 32767));
                generatedSound[idx++] = (byte) (val & 0x00ff);
                generatedSound[idx++] = (byte) ((val & 0xff00) >>> 8);
            }

            String fileName = "tone_" + frequency + ".wav";
            java.io.File tempFile = new java.io.File(getCacheDir(), fileName);
            java.io.FileOutputStream fos = new java.io.FileOutputStream(tempFile);

            writeWavHeader(fos, generatedSound.length, sampleRate);
            fos.write(generatedSound);
            fos.close();

            return soundPool.load(tempFile.getAbsolutePath(), 1);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void writeWavHeader(java.io.FileOutputStream out, int dataLength, int sampleRate) throws java.io.IOException {
        long totalDataLen = dataLength + 36;
        long bitrate = 16 * sampleRate * 1 / 8;
        byte[] header = new byte[44];

        header[0] = 'R';
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f';
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16;
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1;
        header[21] = 0;
        header[22] = 1;
        header[23] = 0;
        header[24] = (byte) (sampleRate & 0xff);
        header[25] = (byte) ((sampleRate >> 8) & 0xff);
        header[26] = (byte) ((sampleRate >> 16) & 0xff);
        header[27] = (byte) ((sampleRate >> 24) & 0xff);
        header[28] = (byte) (bitrate & 0xff);
        header[29] = (byte) ((bitrate >> 8) & 0xff);
        header[30] = (byte) ((bitrate >> 16) & 0xff);
        header[31] = (byte) ((bitrate >> 24) & 0xff);
        header[32] = 2;
        header[33] = 0;
        header[34] = 16;
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (dataLength & 0xff);
        header[41] = (byte) ((dataLength >> 8) & 0xff);
        header[42] = (byte) ((dataLength >> 16) & 0xff);
        header[43] = (byte) ((dataLength >> 24) & 0xff);

        out.write(header, 0, 44);
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
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
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
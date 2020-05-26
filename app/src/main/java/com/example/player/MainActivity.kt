package com.example.player

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private   lateinit var  mp:MediaPlayer

    fun playBtnClick(v: View){
        if(mp.isPlaying) {
            // stop
            mp.pause()
            //  mp.isLooping = false

            playBtn.setBackgroundResource(R.drawable.play)
        }
        else{
            // start
            mp.start()
            // mp.isLooping = true
            playBtn.setBackgroundResource(R.drawable.stop)

        }
    }
    private var totalTime: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mp = MediaPlayer.create(this,R.raw.music)

        mp.isLooping = true
        mp.setVolume(0.5f , 0.5f)

        totalTime = mp.duration

        volumeBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if(fromUser){
                        var volumeNum = progress/100.0f
                        mp.setVolume(volumeNum,volumeNum)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    Toast.makeText(applicationContext,"on Start Trscing Touch",Toast.LENGTH_SHORT).show()

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    Toast.makeText(applicationContext,"on Stop Trscing Touch " ,Toast.LENGTH_SHORT).show()

                }
            }
        )
        // positionBar
        positionbar.max = totalTime
        positionbar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if(fromUser){
                   //     Toast.makeText(applicationContext,"from User",Toast.LENGTH_SHORT).show()

                        mp.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    Toast.makeText(applicationContext,"on Start Trscing Touch Second",Toast.LENGTH_SHORT).show()

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    Toast.makeText(applicationContext,"on Stop Trscing Touch Second",Toast.LENGTH_SHORT).show()

                }
            }
        )

        fun createTimeLabel(time : Int):String{
            var timeLabel = ""
            var min = time/1000/60
            var sec = (time / 1000 )  % 60

            timeLabel = "$min:"
            if(sec < 10){
                timeLabel += "00000000" /// ,,,,
            }
            timeLabel += sec
            return timeLabel
        }

        // Thread

        @SuppressLint("HandlerLeak")
        var handler = object: Handler(){
            override fun handleMessage(msg: Message) {
                var currentPosition = msg.what

                // update position bar
                positionbar.progress = currentPosition

                // Update Lables
                var elapsedTime = createTimeLabel(currentPosition)
                elapsedTimeLabel.text = elapsedTime

                var remainingTime = createTimeLabel(totalTime - currentPosition)
                remainingTimeLevel.text = remainingTime

            }
        }


        Thread(Runnable {
            while(mp!=null){
                try{
                    var msg =  Message()

                    msg.what = mp.currentPosition

//                    Toast.makeText(applicationContext,msg.what,Toast.LENGTH_SHORT).show()

                    handler.sendMessage(msg)
                    Thread.sleep(1000)

                }catch(e : InterruptedException){

                }
            }
        }).start()

    }

}

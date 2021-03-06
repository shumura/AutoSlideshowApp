package jp.techacademy.shuu.autoslideshowapp

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import android.database.Cursor
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {






    private val PERMISSIONS_REQUEST_CODE = 100
    var mcursor : Cursor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                getContentsInfo()
            } else {
                // 許可されていないので許可ダイアログを表示する
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_CODE
                )

            }
        } else {
            getContentsInfo()
        }

        button1.setOnClickListener {
            if (mcursor != null) {
                if (mcursor!!.moveToNext()) {
                    // indexからIDを取得し、そのIDから画像のURIを取得する
                    val fieldIndex = mcursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = mcursor!!.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    imageView.setImageURI(imageUri)

                } else {
                    mcursor!!.moveToFirst()

                    val fieldIndex = mcursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = mcursor!!.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    imageView.setImageURI(imageUri)
                }
                mcursor!!.close()
            }
        }

        button2.setOnClickListener {
            if (mcursor != null) {
                if (mcursor!!.moveToPrevious()) {
                    // indexからIDを取得し、そのIDから画像のURIを取得する
                    val fieldIndex = mcursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = mcursor!!.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    imageView.setImageURI(imageUri)

                } else {
                    mcursor!!.moveToLast()

                    val fieldIndex = mcursor!!.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = mcursor!!.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    imageView.setImageURI(imageUri)
                }
                mcursor!!.close()
            }
        }




        button3.setOnClickListener {

            var mTimer: Timer? = null
            var mTimerSec = 0.0

            if (mTimer == null){
                mTimer = Timer()
                mTimer!!.schedule(object : TimerTask() {
                    override fun run() {
                        mTimerSec += 0.1

                        mcursor!!.moveToNext()


                    }
                }, 2000, 2000) // 最初に始動させるまで 100ミリ秒、ループの間隔を 100ミリ秒 に設定



                button1.isClickable = false
                button2.isClickable = false

                Log.d("UI_PARTS", "停止")}
             else{
                mTimer!!.cancel()

                button1.isClickable = true
                button2.isClickable = true


                Log.d("UI_PARTS", "再生")}
            }








    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo()
                }
        }
    }
        private fun getContentsInfo() {
            // 画像の情報を取得する
            val resolver = contentResolver
            val cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                null, // 項目(null = 全項目)
                null, // フィルタ条件(null = フィルタなし)
                null, // フィルタ用パラメータ
                null // ソート (null ソートなし)
            )


            if (cursor!!.moveToFirst()) {

                // indexからIDを取得し、そのIDから画像のURIを取得する
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                imageView.setImageURI(imageUri)
            }
            mcursor = cursor!!

            fun onDestroy() {
                cursor.close()
            }

        }











}

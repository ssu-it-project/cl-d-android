package com.seumulseumul.cld.util

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.util.Log

class VideoThumbnailUtil {
    //영상의 1초 시간의 사진을 가져옴
    val thumbnailTime = 1

    fun getWebVideoThumbnail(uri : Uri) : Bitmap? {
        val retriever = MediaMetadataRetriever()

        Log.d("TESTLOG", "[getWebVideoThumbnail] uri: $uri")
        try {
            retriever.setDataSource(uri.toString(), HashMap<String,String>())
            val original = retriever.getFrameAtTime((thumbnailTime * 1000000).toLong(), MediaMetadataRetriever.OPTION_CLOSEST)
            return ThumbnailUtils.extractThumbnail(original, 720 , 720)
        } catch (e : IllegalArgumentException){
            e.printStackTrace()
        } catch (e : RuntimeException){
            e.printStackTrace()
        } finally {
            try {
                retriever.release()
            } catch (e : RuntimeException){
                e.printStackTrace()
            }
        }
        return null
    }
}
package com.arriyam.whatareyoudrawing.dataclass

object ObjectConverter {
    fun convertPaintCoordinates(str:PaintCoordinates):PaintCoordinates{
//        var hen=str
//        hen=hen.replace("{", "")
//        hen=hen.replace("}", "")
//        hen=hen.replace(":",",")
//        hen=hen.replace("\"", "")
//
//        val strsArray = hen.split(",").toTypedArray()
//
//
//        val x=strsArray[1].toFloat()
//        val y=strsArray[3].toFloat()
//        val px=strsArray[5].toFloat()
//        val py=strsArray[7].toFloat()
//        val windowX=strsArray[9].toFloat()
//        val windowY=strsArray[11].toFloat()

        val displayHeight= CanvasSize.getCanvasHeight()
        val displayWidth= CanvasSize.getCanvasWidth()

        val scaledX = str.x * displayWidth / str.windowX

        val scaledY = str.y * displayHeight / str.windowY

        return PaintCoordinates(scaledX,scaledY,scaledX,scaledY,scaledX,scaledY)
    }


}
package com.example.twixtapp

public class SetupCoords {

     fun getXCoords(size: Int) : Array<FloatArray> {
        var xCoords = Array(24) { FloatArray(24) }

        xCoords[0] = arrayOf<Float>(size*.025f,size*.067f,size*.107f,size*.148f,size*.190f,size*.231f
            ,size*.272f,size*.312f,size*.354f,size*.395f,size*.436f,size*.478f
            ,size*.520f,size*.560f,size*.602f,size*.643f,size*.685f,size*.727f
            ,size*.769f,size*.810f,size*.852f,size*.893f,size*.936f,size*.979f).toFloatArray()
        xCoords[1] = arrayOf<Float>(size*.025f,size*.067f,size*.107f,size*.148f,size*.190f,size*.231f
            ,size*.272f,size*.312f,size*.354f,size*.395f,size*.436f,size*.477f
            ,size*.519f,size*.560f,size*.602f,size*.643f,size*.685f,size*.725f
            ,size*.767f,size*.809f,size*.850f,size*.892f,size*.934f,size*.976f).toFloatArray()
        xCoords[2] = arrayOf<Float>(size*.025f,size*.067f,size*.107f,size*.148f,size*.190f,size*.231f
            ,size*.272f,size*.312f,size*.354f,size*.395f,size*.436f,size*.477f
            ,size*.519f,size*.560f,size*.602f,size*.643f,size*.684f,size*.725f
            ,size*.767f,size*.808f,size*.850f,size*.891f,size*.933f,size*.976f).toFloatArray()
        xCoords[3] = arrayOf<Float>(size*.0255f,size*.067f,size*.109f,size*.150f,size*.190f,size*.231f
            ,size*.272f,size*.312f,size*.354f,size*.395f,size*.436f,size*.477f
            ,size*.519f,size*.560f,size*.600f,size*.642f,size*.683f,size*.724f
            ,size*.766f,size*.807f,size*.849f,size*.891f,size*.933f,size*.975f).toFloatArray()
        xCoords[4] = arrayOf<Float>(size*.0256f,size*.067f,size*.109f,size*.150f,size*.190f,size*.231f
            ,size*.272f,size*.314f,size*.354f,size*.395f,size*.436f,size*.477f
            ,size*.517f,size*.559f,size*.600f,size*.642f,size*.683f,size*.724f
            ,size*.766f,size*.807f,size*.848f,size*.890f,size*.932f,size*.975f).toFloatArray()
        xCoords[5] = arrayOf<Float>(size*.027f,size*.067f,size*.109f,size*.150f,size*.190f,size*.231f
            ,size*.272f,size*.314f,size*.354f,size*.395f,size*.436f,size*.477f
            ,size*.517f,size*.557f,size*.598f,size*.641f,size*.682f,size*.724f
            ,size*.766f,size*.805f,size*.848f,size*.890f,size*.932f,size*.971f).toFloatArray()
        xCoords[6] = arrayOf<Float>(size*.027f,size*.067f,size*.109f,size*.150f,size*.190f,size*.231f
            ,size*.272f,size*.314f,size*.354f,size*.395f,size*.436f,size*.477f
            ,size*.516f,size*.557f,size*.598f,size*.639f,size*.680f,size*.721f
            ,size*.763f,size*.806f,size*.848f,size*.888f,size*.930f,size*.971f).toFloatArray()
        xCoords[7] =  arrayOf<Float>(size*.027f,size*.067f,size*.109f,size*.150f,size*.190f,size*.231f
            ,size*.273f,size*.314f,size*.354f,size*.395f,size*.436f,size*.477f
            ,size*.516f,size*.557f,size*.598f,size*.639f,size*.680f,size*.721f
            ,size*.763f,size*.804f,size*.846f,size*.888f,size*.930f,size*.971f).toFloatArray()
        xCoords[8] = arrayOf<Float>(size*.028f,size*.068f,size*.109f,size*.150f,size*.191f,size*.232f
            ,size*.272f,size*.314f,size*.354f,size*.395f,size*.436f,size*.477f
            ,size*.516f,size*.557f,size*.598f,size*.639f,size*.680f,size*.721f
            ,size*.763f,size*.804f,size*.846f,size*.888f,size*.930f,size*.971f).toFloatArray()
        xCoords[9] = arrayOf<Float>(size*.028f,size*.068f,size*.109f,size*.150f,size*.191f,size*.232f
            ,size*.273f,size*.314f,size*.354f,size*.395f,size*.436f,size*.477f
            ,size*.516f,size*.557f,size*.598f,size*.639f,size*.680f,size*.721f
            ,size*.763f,size*.804f,size*.846f,size*.888f,size*.929f,size*.971f).toFloatArray()
        xCoords[10] = arrayOf<Float>(size*.028f,size*.069f,size*.110f,size*.151f,size*.192f,size*.233f
            ,size*.273f,size*.314f,size*.354f,size*.395f,size*.436f,size*.476f
            ,size*.516f,size*.557f,size*.597f,size*.638f,size*.679f,size*.720f
            ,size*.761f,size*.802f,size*.844f,size*.886f,size*.927f,size*.969f).toFloatArray()
        xCoords[11] = arrayOf<Float>(size*.029f,size*.070f,size*.110f,size*.151f,size*.192f,size*.233f
            ,size*.274f,size*.314f,size*.355f,size*.395f,size*.435f,size*.476f
            ,size*.516f,size*.556f,size*.597f,size*.638f,size*.679f,size*.720f
            ,size*.761f,size*.802f,size*.844f,size*.885f,size*.926f,size*.968f).toFloatArray()
        xCoords[12] = arrayOf<Float>(size*.027f,size*.068f,size*.108f,size*.149f,size*.190f,size*.231f
            ,size*.271f,size*.312f,size*.352f,size*.393f,size*.433f,size*.473f
            ,size*.515f,size*.555f,size*.596f,size*.637f,size*.678f,size*.719f
            ,size*.760f,size*.801f,size*.843f,size*.884f,size*.925f,size*.967f).toFloatArray()
        xCoords[13] = arrayOf<Float>(size*.026f,size*.066f,size*.107f,size*.148f,size*.189f,size*.230f
            ,size*.271f,size*.311f,size*.352f,size*.392f,size*.433f,size*.473f
            ,size*.515f,size*.555f,size*.596f,size*.636f,size*.677f,size*.718f
            ,size*.760f,size*.801f,size*.843f,size*.884f,size*.925f,size*.967f).toFloatArray()
        xCoords[14] = arrayOf<Float>(size*.026f,size*.066f,size*.107f,size*.148f,size*.189f,size*.230f
            ,size*.270f,size*.311f,size*.352f,size*.392f,size*.432f,size*.473f
            ,size*.514f,size*.555f,size*.595f,size*.636f,size*.677f,size*.718f
            ,size*.759f,size*.801f,size*.842f,size*.884f,size*.925f,size*.967f).toFloatArray()
        xCoords[15] = arrayOf<Float>(size*.024f,size*.066f,size*.107f,size*.148f,size*.189f,size*.230f
            ,size*.270f,size*.311f,size*.351f,size*.392f,size*.432f,size*.473f
            ,size*.514f,size*.555f,size*.595f,size*.636f,size*.677f,size*.718f
            ,size*.759f,size*.801f,size*.842f,size*.884f,size*.925f,size*.967f).toFloatArray()
        xCoords[16] = arrayOf<Float>(size*.024f,size*.066f,size*.107f,size*.148f,size*.188f,size*.229f
            ,size*.270f,size*.311f,size*.351f,size*.392f,size*.432f,size*.473f
            ,size*.514f,size*.554f,size*.595f,size*.636f,size*.677f,size*.719f
            ,size*.759f,size*.801f,size*.842f,size*.884f,size*.926f,size*.967f).toFloatArray()
        xCoords[17] =  arrayOf<Float>(size*.024f,size*.065f,size*.106f,size*.147f,size*.187f,size*.228f
            ,size*.269f,size*.310f,size*.351f,size*.392f,size*.432f,size*.473f
            ,size*.514f,size*.554f,size*.595f,size*.636f,size*.677f,size*.718f
            ,size*.759f,size*.801f,size*.842f,size*.884f,size*.926f,size*.967f).toFloatArray()
        xCoords[18] = arrayOf<Float>(size*.024f,size*.065f,size*.106f,size*.147f,size*.187f,size*.228f
            ,size*.269f,size*.310f,size*.351f,size*.391f,size*.432f,size*.473f
            ,size*.513f,size*.554f,size*.595f,size*.636f,size*.677f,size*.718f
            ,size*.759f,size*.801f,size*.842f,size*.884f,size*.926f,size*.968f).toFloatArray()
        xCoords[19] = arrayOf<Float>(size*.023f,size*.064f,size*.106f,size*.147f,size*.187f,size*.228f
            ,size*.269f,size*.309f,size*.350f,size*.391f,size*.432f,size*.473f
            ,size*.513f,size*.554f,size*.595f,size*.636f,size*.677f,size*.718f
            ,size*.760f,size*.801f,size*.842f,size*.884f,size*.926f,size*.968f).toFloatArray()
        xCoords[20] =  arrayOf<Float>(size*.023f,size*.064f,size*.105f,size*.146f,size*.187f,size*.228f
            ,size*.269f,size*.309f,size*.350f,size*.391f,size*.431f,size*.472f
            ,size*.513f,size*.554f,size*.595f,size*.636f,size*.677f,size*.718f
            ,size*.760f,size*.801f,size*.842f,size*.884f,size*.926f,size*.968f).toFloatArray()
        xCoords[21] =  arrayOf<Float>(size*.023f,size*.064f,size*.105f,size*.146f,size*.187f,size*.228f
            ,size*.268f,size*.309f,size*.350f,size*.391f,size*.431f,size*.472f
            ,size*.513f,size*.554f,size*.595f,size*.636f,size*.677f,size*.718f
            ,size*.760f,size*.801f,size*.843f,size*.885f,size*.927f,size*.969f).toFloatArray()
        xCoords[22] =  arrayOf<Float>(size*.022f,size*.063f,size*.104f,size*.145f,size*.186f,size*.227f
            ,size*.268f,size*.309f,size*.350f,size*.391f,size*.431f,size*.472f
            ,size*.513f,size*.554f,size*.595f,size*.636f,size*.677f,size*.718f
            ,size*.760f,size*.801f,size*.843f,size*.885f,size*.927f,size*.969f).toFloatArray()
        xCoords[23] =  arrayOf<Float>(size*.022f,size*.063f,size*.104f,size*.145f,size*.186f,size*.227f
            ,size*.268f,size*.309f,size*.350f,size*.391f,size*.431f,size*.472f
            ,size*.513f,size*.554f,size*.595f,size*.636f,size*.677f,size*.718f
            ,size*.760f,size*.801f,size*.843f,size*.885f,size*.927f,size*.969f).toFloatArray()

        return xCoords
    }

    fun getYCoords(size: Int) : Array<FloatArray> {
        var yCoords = Array(24) { FloatArray(24) }

        yCoords[0] = arrayOf<Float>(size*.022f,size*.022f,size*.022f,size*.022f,size*.022f,size*.022f
            ,size*.022f,size*.022f,size*.023f,size*.023f,size*.023f,size*.023f
            ,size*.023f,size*.023f,size*.023f,size*.023f,size*.023f,size*.023f
            ,size*.023f,size*.023f,size*.023f,size*.023f,size*.0225f,size*.023f).toFloatArray()
        yCoords[1] = arrayOf<Float>(size*.0635f,size*.0635f,size*.0635f,size*.0635f,size*.0635f,size*.0635f
            ,size*.0643f,size*.0643f,size*.0643f,size*.0643f,size*.0643f,size*.0643f
            ,size*.0645f,size*.0645f,size*.0645f,size*.0645f,size*.0645f,size*.0645f
            ,size*.0645f,size*.0645f,size*.0645f,size*.0645f,size*.0645f,size*.0645f).toFloatArray()
        yCoords[2] = arrayOf<Float>(size*.1050f,size*.1050f,size*.1050f,size*.1050f,size*.1050f,size*.1050f
            ,size*.1055f,size*.1055f,size*.1055f,size*.1055f,size*.1055f,size*.1055f
            ,size*.1063f,size*.1063f,size*.1063f,size*.1063f,size*.1063f,size*.1063f
            ,size*.1063f,size*.1063f,size*.1063f,size*.1060f,size*.1060f,size*.1060f).toFloatArray()
        yCoords[3] = arrayOf<Float>(size*.1470f,size*.1470f,size*.1470f,size*.1470f,size*.1470f,size*.1470f
            ,size*.1475f,size*.1475f,size*.1475f,size*.1475f,size*.1475f,size*.1475f
            ,size*.1483f,size*.1483f,size*.1483f,size*.1483f,size*.1483f,size*.1483f
            ,size*.1483f,size*.1483f,size*.1483f,size*.1483f,size*.1483f,size*.1483f).toFloatArray()
        yCoords[4] = arrayOf<Float>(size*.1880f,size*.1880f,size*.1880f,size*.1880f,size*.1880f,size*.1883f
            ,size*.1888f,size*.1889f,size*.1889f,size*.1889f,size*.1889f,size*.1889f
            ,size*.1900f,size*.1900f,size*.1900f,size*.1900f,size*.1900f,size*.1900f
            ,size*.1900f,size*.1900f,size*.1900f,size*.1900f,size*.1900f,size*.1900f).toFloatArray()
        yCoords[5] = arrayOf<Float>(size*.2290f,size*.2290f,size*.2290f,size*.2290f,size*.2300f,size*.2300f
            ,size*.2320f,size*.2322f,size*.2322f,size*.2322f,size*.2322f,size*.2322f
            ,size*.2325f,size*.2325f,size*.2325f,size*.2325f,size*.2325f,size*.2325f
            ,size*.2325f,size*.2325f,size*.2325f,size*.2325f,size*.2325f,size*.2325f).toFloatArray()
        yCoords[6] = arrayOf<Float>(size*.2719f,size*.2719f,size*.2721f,size*.2721f,size*.2721f,size*.2721f
            ,size*.2720f,size*.2725f,size*.2725f,size*.2725f,size*.2725f,size*.2725f
            ,size*.2730f,size*.2730f,size*.2730f,size*.2730f,size*.2730f,size*.2730f
            ,size*.2730f,size*.2730f,size*.2730f,size*.2730f,size*.2730f,size*.2730f).toFloatArray()
        yCoords[7] = arrayOf<Float>(size*.3129f,size*.3129f,size*.3129f,size*.3129f,size*.3129f,size*.3129f
            ,size*.3134f,size*.3134f,size*.3134f,size*.3134f,size*.3134f,size*.3134f
            ,size*.3140f,size*.3140f,size*.3140f,size*.3140f,size*.3140f,size*.3140f
            ,size*.3140f,size*.3140f,size*.3140f,size*.3140f,size*.3140f,size*.3140f).toFloatArray()
        yCoords[8] = arrayOf<Float>(size*.3539f,size*.3539f,size*.3539f,size*.3539f,size*.3539f,size*.3539f
            ,size*.3544f,size*.3544f,size*.3544f,size*.3544f,size*.3544f,size*.3544f
            ,size*.3550f,size*.3550f,size*.3550f,size*.3550f,size*.3550f,size*.3550f
            ,size*.3550f,size*.3550f,size*.3550f,size*.3550f,size*.3550f,size*.3550f).toFloatArray()
        yCoords[9] = arrayOf<Float>(size*.3949f,size*.3949f,size*.3949f,size*.3949f,size*.3949f,size*.3949f
            ,size*.3954f,size*.3954f,size*.3954f,size*.3954f,size*.3954f,size*.3954f
            ,size*.3960f,size*.3960f,size*.3960f,size*.3960f,size*.3960f,size*.3960f
            ,size*.3960f,size*.3960f,size*.3960f,size*.398f,size*.398f,size*.398f).toFloatArray()
        yCoords[10] = arrayOf<Float>(size*.434f,size*.434f,size*.434f,size*.434f,size*.435f,size*.435f
            ,size*.435f,size*.435f,size*.436f,size*.436f,size*.436f,size*.436f
            ,size*.437f,size*.437f,size*.437f,size*.437f,size*.438f,size*.438f
            ,size*.438f,size*.438f,size*.438f,size*.439f,size*.439f,size*.439f).toFloatArray()
        yCoords[11] = arrayOf<Float>(size*.475f,size*.475f,size*.475f,size*.475f,size*.475f,size*.476f
            ,size*.476f,size*.476f,size*.476f,size*.477f,size*.477f,size*.477f
            ,size*.477f,size*.478f,size*.478f,size*.478f,size*.479f,size*.479f
            ,size*.479f,size*.479f,size*.479f,size*.480f,size*.480f,size*.480f).toFloatArray()
        yCoords[12] = arrayOf<Float>(size*.516f,size*.516f,size*.516f,size*.516f,size*.516f,size*.516f
            ,size*.516f,size*.516f,size*.516f,size*.517f,size*.517f,size*.517f
            ,size*.517f,size*.518f,size*.518f,size*.518f,size*.519f,size*.519f
            ,size*.519f,size*.520f,size*.520f,size*.521f,size*.521f,size*.521f).toFloatArray()
        yCoords[13] = arrayOf<Float>(size*.556f,size*.556f,size*.556f,size*.557f,size*.557f,size*.557f
            ,size*.557f,size*.557f,size*.557f,size*.557f,size*.557f,size*.557f
            ,size*.558f,size*.558f,size*.559f,size*.559f,size*.559f,size*.560f
            ,size*.560f,size*.560f,size*.561f,size*.561f,size*.562f,size*.562f).toFloatArray()
        yCoords[14] = arrayOf<Float>(size*.597f,size*.597f,size*.597f,size*.597f,size*.597f,size*.597f
            ,size*.597f,size*.597f,size*.597f,size*.597f,size*.597f,size*.597f
            ,size*.598f,size*.599f,size*.599f,size*.599f,size*.600f,size*.600f
            ,size*.601f,size*.601f,size*.602f,size*.602f,size*.603f,size*.603f).toFloatArray()
        yCoords[15] = arrayOf<Float>(size*.638f,size*.638f,size*.638f,size*.638f,size*.638f,size*.638f
            ,size*.638f,size*.638f,size*.638f,size*.638f,size*.638f,size*.638f
            ,size*.639f,size*.639f,size*.640f,size*.640f,size*.641f,size*.641f
            ,size*.642f,size*.642f,size*.643f,size*.643f,size*.644f,size*.644f).toFloatArray()
        yCoords[16] = arrayOf<Float>(size*.679f,size*.679f,size*.679f,size*.679f,size*.679f,size*.679f
            ,size*.679f,size*.679f,size*.679f,size*.679f,size*.679f,size*.679f
            ,size*.680f,size*.680f,size*.681f,size*.681f,size*.682f,size*.682f
            ,size*.683f,size*.683f,size*.684f,size*.685f,size*.685f,size*.686f).toFloatArray()
        yCoords[17] = arrayOf<Float>(size*.719f,size*.719f,size*.719f,size*.719f,size*.719f,size*.719f
            ,size*.719f,size*.719f,size*.719f,size*.719f,size*.719f,size*.719f
            ,size*.720f,size*.721f,size*.721f,size*.722f,size*.723f,size*.723f
            ,size*.724f,size*.725f,size*.725f,size*.726f,size*.727f,size*.727f).toFloatArray()
        yCoords[18] = arrayOf<Float>(size*.760f,size*.760f,size*.760f,size*.760f,size*.760f,size*.760f
            ,size*.760f,size*.760f,size*.760f,size*.760f,size*.760f,size*.760f
            ,size*.761f,size*.762f,size*.762f,size*.763f,size*.764f,size*.764f
            ,size*.765f,size*.766f,size*.766f,size*.767f,size*.768f,size*.768f).toFloatArray()
        yCoords[19] = arrayOf<Float>(size*.801f,size*.801f,size*.801f,size*.801f,size*.801f,size*.801f
            ,size*.801f,size*.801f,size*.801f,size*.801f,size*.801f,size*.801f
            ,size*.802f,size*.803f,size*.804f,size*.804f,size*.805f,size*.806f
            ,size*.807f,size*.807f,size*.808f,size*.809f,size*.809f,size*.810f).toFloatArray()
        yCoords[20] = arrayOf<Float>(size*.842f,size*.842f,size*.842f,size*.842f,size*.842f,size*.842f
            ,size*.843f,size*.843f,size*.843f,size*.843f,size*.843f,size*.843f
            ,size*.844f,size*.845f,size*.845f,size*.846f,size*.846f,size*.847f
            ,size*.848f,size*.848f,size*.849f,size*.850f,size*.851f,size*.852f).toFloatArray()
        yCoords[21] = arrayOf<Float>(size*.884f,size*.884f,size*.884f,size*.884f,size*.884f,size*.884f
            ,size*.884f,size*.884f,size*.884f,size*.884f,size*.884f,size*.884f
            ,size*.886f,size*.886f,size*.887f,size*.887f,size*.888f,size*.889f
            ,size*.889f,size*.890f,size*.891f,size*.892f,size*.893f,size*.894f).toFloatArray()
        yCoords[22] = arrayOf<Float>(size*.925f,size*.925f,size*.925f,size*.925f,size*.925f,size*.925f
            ,size*.925f,size*.925f,size*.925f,size*.925f,size*.925f,size*.926f
            ,size*.927f,size*.928f,size*.928f,size*.929f,size*.930f,size*.931f
            ,size*.931f,size*.932f,size*.933f,size*.934f,size*.935f,size*.935f).toFloatArray()
        yCoords[23] = arrayOf<Float>(size*.966f,size*.966f,size*.966f,size*.966f,size*.966f,size*.966f
            ,size*.966f,size*.966f,size*.966f,size*.966f,size*.966f,size*.967f
            ,size*.968f,size*.969f,size*.970f,size*.970f,size*.971f,size*.972f
            ,size*.973f,size*.974f,size*.975f,size*.975f,size*.976f,size*.976f).toFloatArray()

        return yCoords
    }
}
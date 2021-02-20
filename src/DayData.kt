const val DELIMITER = ':'

class DayData (
    private var timeStamp: String,
    private var weight: String,
    private var bodyWater: String,
    private var bodyFat: String,
    private var bone: String,
    private var bmi: String, // TODO: Interpret to Float,
    private var visceralFat: String, // TODO: Interpret to 0.0F
    private var bmr: String,
    private var muscleMass: String
    ) {

    init {
        // TODO: Does Kotlin pass by reference?
        formatTimeStamp()
        weight = stripLabel(weight)
        bodyWater = stripLabel(bodyWater)
        bodyFat = stripLabel(bodyFat)
        bone = stripLabel(bone)
        bmi = stripLabel(bmi)
        visceralFat = stripLabel(visceralFat)
        bmr = stripLabel(bmr)
        muscleMass = stripLabel(muscleMass)
    }

    override fun toString(): String {
        return "DayData(" +
                "timeStamp=$timeStamp; " +
                "weight=$weight; " +
                "bodyWater=$bodyWater; " +
                "bodyFat=$bodyFat; " +
                "bone=$bone; " +
                "bmi=$bmi; " +
                "visceralFat=$visceralFat; " +
                "bmr=$bmr; " +
                "muscleMass=$muscleMass"
    }

    // Format each measurement.
    /**
     * Strips the label name from the measurement value.
     * @param aPair Name-value pair of label-measurement.
     * @return Measurement value.
     */
    private fun stripLabel(aPair: String): String {
        return aPair.split(DELIMITER)[1]
    }

    private fun formatTimeStamp() {
        // Since time data contains colons and colon is the delimiter,
        // drop the prefix "Time:" from the value.
        // timestamp = Time:21:07:42, Tues,1/ 9/2018
        val arrTs = timeStamp.split(DELIMITER).filterNot { it == "Time" } // Filtered String = [21, 07, 42, Tues,1/ 9/2018]
        // arrTs[0] = 21
        // arrTs[1] = 07
        // arrTs[2] = 42, Tues,1/ 9/2018
        // Separate minutes from date in arrTs[2].
        val date = arrTs[2].split(',')[2]
        val minutes = arrTs[2].split(',')[0]
        timeStamp = "$date ${arrTs[0]}:${arrTs[1]}:${minutes}"
    }

    private fun formatWeight() {
    }
}
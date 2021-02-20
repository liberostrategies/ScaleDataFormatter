const val INPUT_DATA_DELIMITER = ':'

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

    companion object {
        fun headerToCsv() =
            // TODO: Populate "Metabolic Age" column?
            "Time of Measurement,Weight(lb),BMI,Body Fat(%),Body Type,Visceral Fat,Body Water(%),Muscle Mass(lb),Bone Mass(lb),BMR(kcal),Metabolic Age\n"
    }
    init {
        // TODO: Does Kotlin pass by reference?
        formatTimeStamp()
        weight = stripUnit("lb", stripLabel(weight))
        bodyWater = stripUnit("%", stripLabel(bodyWater))
        bodyFat = stripUnit("%", stripLabel(bodyFat))
        bone = stripUnit("lb", stripLabel(bone))
        bmi = stripLabel(bmi)
        visceralFat = stripLabel(visceralFat)
        bmr = stripUnit(" kcal", stripLabel(bmr))
        muscleMass = stripUnit("lb", stripLabel(muscleMass))
    }

    override fun toString(): String {
        return "DayData(" +
                "timeStamp=$timeStamp, " +
                "weight=$weight, " +
                "bodyWater=$bodyWater, " +
                "bodyFat=$bodyFat, " +
                "bone=$bone, " +
                "bmi=$bmi, " +
                "visceralFat=$visceralFat, " +
                "bmr=$bmr, " +
                "muscleMass=$muscleMass"
    }

    fun toCsv() = "$timeStamp\"," + // TODO: Match Arboleaf format = "Feb 9, 2021 05:56:21"
            "$weight," +
            "$bmi," +
            "$bodyFat," +
            "Fit," + // Manually add Body Type to match Arboleaf data.
            "$visceralFat," +
            "$bodyWater," +
            "$muscleMass," +
            "$bone," +
            "$bmr\n"

    // Format each measurement.
    /**
     * Strips the label name from the measurement value.
     * @param aPair Name-value pair of label-measurement.
     * @return Measurement value.
     */
    private fun stripLabel(aPair: String) = aPair.split(INPUT_DATA_DELIMITER)[1]

    private fun stripUnit(aUnit: String, aString: String): String {
        return aString.removeSuffix(aUnit)
    }

    private fun formatTimeStamp() {
        // Since time data contains colons and colon is the INPUT_DATA_DELIMITER,
        // drop the prefix "Time:" from the value.
        // timestamp = Time:21:07:42, Tues,1/ 9/2018
        val arrTs = timeStamp.split(INPUT_DATA_DELIMITER).filterNot { it == "Time" } // Filtered String = [21, 07, 42, Tues,1/ 9/2018]
        // arrTs[0] = 21
        // arrTs[1] = 07
        // arrTs[2] = 42, Tues,1/ 9/2018
        // Separate minutes from date in arrTs[2].
        println("${arrTs[0]}, ${arrTs[1]}, ${arrTs[2]}")
        val date = arrTs[2].split(',')[2]
        val minutes = arrTs[2].split(',')[0]
        timeStamp = "\"$date ${arrTs[0]}:${arrTs[1]}:${minutes}"
    }

    private fun formatWeight() {
    }
}
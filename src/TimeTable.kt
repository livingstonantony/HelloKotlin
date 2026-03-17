typealias Teacher = Pair<String, Int>

typealias Subject = Pair<String, Int>

typealias TeacherSubject = Pair<String, String>

typealias Slot<T, K> = Pair<T, K>
typealias Schedule<T, K> = Pair<T, K>

val teachers = arrayListOf<Teacher>(
    Teacher("Livin", 5),
    Teacher("Mercy", 5),
    Teacher("Jency", 5),
    Teacher("Akash", 5),
    Teacher("Jecil", 5),
)

val subjects = arrayListOf(
    Subject("C", 5),
    Subject("CPP", 5),
    Subject("MYSQL", 5),
    Subject("JAVA", 5),
    Subject("Python", 5),
)

val days = arrayListOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
val slots = arrayListOf(1, 2, 3, 4, 5)

fun <T, K> createSlots(days: List<T>, slots: List<K>): List<Slot<T, K>> {
    // The compiler now knows what T and K are
    val schedules: List<Slot<T, K>> = days.flatMap { day ->
        slots.map { slot ->
            Slot(day, slot) // Equivalent to Pair(day, slot)
        }
    }
    return schedules
}

fun <T, K> randomMapping(primaryList: List<T>, secondaryList: List<K>): List<Pair<T, K>> {
    if (secondaryList.isEmpty()) return emptyList()

    // Shuffle once to ensure randomness
    val shuffledSecondary = secondaryList.shuffled()
    val secondarySize = shuffledSecondary.size

    return primaryList.mapIndexed { index, primary ->
        // Use modulo (%) to cycle through the secondary list
        // This ensures every item is used before any item is repeated
        val secondaryItem = shuffledSecondary[index % secondarySize]
        primary to secondaryItem
    }
}

fun <T, K> verifyTeachersAvailability(
    teachers: List<Teacher>,
    subjects: List<Subject>,
    days: List<T>,
    slots: List<K>
): Boolean {
    val teachersTotalAvailableHours = teachers.sumOf { teacher -> teacher.second }
    val subjectsRequiredHours = subjects.sumOf { subject -> subject.second }

    if (teachersTotalAvailableHours < subjectsRequiredHours) throw Exception("Teachers Available $teachersTotalAvailableHours hours must be in $subjectsRequiredHours hours")

    val availableSlots = createSlots(days, slots).size

    if (subjectsRequiredHours > availableSlots) throw Exception(
        "Subject required hours $subjectsRequiredHours  exceeded the available slot hours $availableSlots"
    )

    return true

}

fun <T, K, L> mapSubjectsWithSchedule(
    slotItems: List<L>,
    slots: List<Slot<T, K>>
): List<Schedule<L, Slot<T, K>>> {

    val schedules: List<Schedule<L, Slot<T, K>>> = slotItems.flatMap { slotItem ->
        slots.map { slot -> Schedule(slotItem, slot) }
    }

    return schedules
}

fun <T, K> prepareTimeTable(
    subjectRepeatMinPerDay: Int = 1,
    subjectRepeatMaxPerDay: Int = 1,
    subjectRepeatMinPerWeek: Int = 5,
    subjectRepeatMaxPerWeek: Int = 5,
    days: List<T>,
    slots: List<K>,
    subjects: List<Subject>,
    teachers: List<Teacher>,

    ) {


    verifyTeachersAvailability(teachers, subjects, days, slots)

    if (subjectRepeatMaxPerDay > slots.size) {
        throw Exception("Subject Max Count: $subjectRepeatMaxPerDay cannot exceed slots: $slots")
    }
}

fun main() {
    println("Hello World!")

    val teacherSubjectMapped: List<TeacherSubject> = randomMapping(teachers.map { it.first }, subjects.map { it.first })

    verifyTeachersAvailability(teachers, subjects, days, slots)

    println("\n=========\n")
    val slots = createSlots(days, slots)

    mapSubjectsWithSchedule(subjects.map { it.first }, slots)

}
package uz.gita.customview

import kotlin.random.Random

class AppRepository private constructor() {

    companion object {
        private var instance: AppRepository? = null

        fun getInstance(): AppRepository {
            if (instance == null) {
                instance = AppRepository()
            }
            return instance!!
        }
    }

    var matrix = arrayOf(arrayOf(0))
    fun newMatrix(x: Int, y: Int): Array<Array<Int>> {
        matrix = Array(x) { Array(y) { 0 } }
        for (i in 0 until x) {
            for (j in 0 until y) {
                matrix[i][j] = Random.nextInt(2)
            }
        }
        return matrix
    }
}

package ds.learning.SplitIt

class MinCashFlow{
    public var list: MutableList<MutableList<Int>> = mutableListOf()

    fun getList(amount: MutableList<Int>, n: Int) {
        val miniEl = amount.indexOf(amount.minOrNull())
        val maxiEl = amount.indexOf(amount.maxOrNull())

        if (amount[miniEl] == 0 && amount[maxiEl] == 0) return

        val minFlow = minOf(-amount[miniEl], amount[maxiEl])

        amount[maxiEl] -= minFlow
        amount[miniEl] += minFlow

        list[miniEl][maxiEl] = minFlow

        getList(amount, n)
    }

    public fun minCashFlow(transaction: MutableList<MutableList<Int>>, n: Int): MutableList<MutableList<Int>> {
        list = MutableList(n) { MutableList(n) { 0 } }

        val amount = MutableList(n) { 0 }
        for (i in 0 until n) {
            for (j in 0 until n) {
                amount[i] += (transaction[j][i] - transaction[i][j])
            }
        }

        getList(amount, n)

        return list
    }


}
package com.spikeyduyphan.sidecryptobar

class Coin {
    var name : String = ""
    var ticker : String = ""
    var priceUSD : String = ""

    constructor() {}

    constructor(name: String, ticker : String, priceUSD : String) {
        this.name = name
        this.ticker = ticker
        this.priceUSD = priceUSD
    }

    override fun toString(): String {
        return "Coin(name='$name', ticker='$ticker', priceUSD='$priceUSD')"
    }


}
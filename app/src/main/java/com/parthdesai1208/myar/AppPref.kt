package com.parthdesai1208.myar

import com.chibatching.kotpref.KotprefModel

object AppPref : KotprefModel(){

    var UserTokenId by stringPref("")

}
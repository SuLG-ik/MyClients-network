package beauty.shafran.network.api

import at.favre.lib.crypto.bcrypt.BCrypt

class BCryptPasswordEncoder(
    private val hasher: BCrypt.Hasher,
    private val verifier: BCrypt.Verifyer,
) : AuthPasswordEncoder {
    override fun encode(rawPassword: String): String {
        return hasher.hashToString(4, rawPassword.toCharArray())
    }

    override fun match(rawPassword: String, encodedPassword: String): Boolean {
        return verifier.verify(rawPassword.toCharArray(), encodedPassword).verified
    }

}


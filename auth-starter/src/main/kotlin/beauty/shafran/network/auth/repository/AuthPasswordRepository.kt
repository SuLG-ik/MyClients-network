package beauty.shafran.network.auth.repository

import beauty.shafran.network.accounts.repositories.AccountRepository
import beauty.shafran.network.auth.encoder.PasswordEncoder
import beauty.shafran.network.auth.entities.AccountPasswordEntity
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional


@Component
class AuthPasswordRepository(
    private val passwordEncoder: PasswordEncoder,
    private val accountPasswordRepository: AccountPasswordRepository,
    private val accountRepository: AccountRepository,
) {

    @Transactional
    fun matchPassword(accountId: Long, rawPassword: String): Boolean {
        val passwordEntity =
            accountPasswordRepository.findByAccount(accountRepository.getReferenceById(accountId))
                ?: TODO("UNAVAILABLE SITUATION")
        return passwordEncoder.matches(rawPassword = rawPassword, passwordHash = passwordEntity.passwordHash)
    }

    @Transactional
    fun setPassword(accountId: Long, rawPassword: String) {
        val passwordEntity =
            accountPasswordRepository.findByAccount(accountRepository.getReferenceById(accountId))
        val passwordHash = passwordEncoder.hashPassword(rawPassword)
        val account = accountRepository.getReferenceById(accountId)
        if (passwordEntity == null) {
            accountPasswordRepository.save(AccountPasswordEntity(account = account, passwordHash = passwordHash))
        } else {
            passwordEntity.passwordHash = passwordHash
            accountPasswordRepository.save(passwordEntity)
        }
    }


}
package com.example.citadelpoc

import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.security.KeyPairGenerator
import java.security.KeyStore
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val TAG = "CitadelPoC"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        triggerStrongBoxDos()
    }

    private fun triggerStrongBoxDos() {
        thread(start = true) {
            try {
                Log.i(TAG, "Iniciando saturación de KeyMint StrongBox...")
                val keyPairGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore"
                )
                val parameterSpec = KeyGenParameterSpec.Builder(
                    "StrongBoxDosKey",
                    KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
                )
                    .setAlgorithmParameterSpec(java.security.spec.RSAKeyGenParameterSpec(2048, java.security.spec.RSAKeyGenParameterSpec.F4))
                    .setDigests(KeyProperties.DIGEST_SHA256)
                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                    .setIsStrongBoxBacked(true)
                    .setUserAuthenticationRequired(true)
                    .build()

                keyPairGenerator.initialize(parameterSpec)

                thread {
                    try {
                        keyPairGenerator.generateKeyPair()
                    } catch (e: Exception) {
                        Log.e(TAG, "Excepción controlada: ${e.message}")
                    }
                }

                Thread.sleep(50)

                val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
                val startTime = System.currentTimeMillis()
                keyStore.getKey("StrongBoxDosKey", null)
                val elapsed = System.currentTimeMillis() - startTime
                Log.w(TAG, "Tiempo de bloqueo en Keystore2: $elapsed ms")

            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.message}", e)
            } finally {
                android.os.Process.killProcess(android.os.Process.myPid())
            }
        }
    }
}
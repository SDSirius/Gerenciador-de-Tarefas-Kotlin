package br.com.devaria.kotlin.projects.gerenciador_tarefas.extensions

import java.security.MessageDigest

fun md5(string : String) : ByteArray = MessageDigest.getInstance("MD5").digest(string.toByteArray(Charsets.UTF_8))

fun ByteArray.toHex() = joinToString("") { byte -> "%02x".format(byte)}
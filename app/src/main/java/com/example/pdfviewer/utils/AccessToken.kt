package com.example.pdfviewer.utils


import com.google.auth.oauth2.GoogleCredentials

import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets

object AccessToken {

    private const val firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging"

    fun getToken(): String? {

        try {
            val jsonString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"my-application-697fb\",\n" +
                    "  \"private_key_id\": \"700429a2bfa775ab7e65de272c4fd828c96f6af3\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEuwIBADANBgkqhkiG9w0BAQEFAASCBKUwggShAgEAAoIBAQCiSLlLDtH4bBQo\\ntH8pX6K8tQGMJHgP4tik9c3VIIgWQVw0mikzmcIa8fnoZhHF+8H2CeSZZY62sxJX\\nNTFb+K+xkLVDhXuBIqvsrXEy/J13lJGv60V5qDEB0Ehg9L6uzNW/XQ0kdC4xEtT+\\nNjhFiYz0qAAVNSOAKQJWSOPxSH7goK/e9YNkodIp/mcZeFZcaFsKQwGuSC0jv802\\nODKfdZk4kraBKYHQCGuJrtx1cLkOtwWbSmF9ONm+85kuekcwuRqwmrDbH6n3Ul0o\\nhIPoFMKbha+8m5MaehnfVS1GjnC/SBOsIKNh9zjGqcm7K5dVNdd8yWRxnb9iDX7c\\ndgsZiZ93AgMBAAECgf9wAyiu8tiUhgT9rTGx3QQp1fkh26PQIBBo/0CTWvqTlSMp\\nK6V33sLO1mmOsn6SD4BRbE92xO88kiowOqCKWs+3DN4QcCuCQ4XN8LYX+0UbkYow\\nlecRkbkOoQG6tzOzQEivTT4R0bjdJm9D1MJo1m7BPM4FCI74Zu+C0jNQCqBVakdN\\n/iqumPVBYqiweDx6tZ8tLFGz1xQvPzB/XwRdk4Q+hOS4DWE9xz8zSf4b8DZ6cSdo\\nsBa1KHihIi4Yu0WLuPdtL4vGfVLAX6TuFSqOf/avdv4Kj5l1S3fs+gB6VVU3zyYw\\nspvUhUQfaCuzkF3Kzw5QqstQtMjn0Snyzxo4CE0CgYEA0UatUmvblYVB8PTnkDnT\\nLVzYHigbn9wSZWOBp3IQLxGe+48ffCN8cw8NgLBKB32lGOj87U0Le3nve4rqr/oS\\nXlakwbVSNPnzp86eYf9eIkavZuu5Spc5Au7wila4GwFovoa8eXSLZeU3wIfj9xwt\\n1e+yDPbDpfFM2uChh6Ea0bsCgYEAxoQvdWQJQe1m9mWTDhGQ1Jj/QCL9yH/aWwJv\\nYyNDrxWo6yKB1pkMfqrSoJcI9jcFaxn5kp6Bt9KOK8sYi/Ih3VAIhLfu7QjR5JOF\\ngQmBSB8w3ZjiOWrsTBRnDzob3Gd2xyqJ+oMJvx1RvNZc0yN6+D6In4KHqd389EvX\\noIz0f3UCgYBe0GKeQPePWnxxqsNu+IPQvYu8vfcmChLblKWdM3RZnuYkmsds+rbb\\neipn73baxfYSvxw4dvuIycnSczG44NeJnj+u83N3RPc7Q8VGKOiBJv045/te6/yM\\nc6zPQFLRCWZnS6wAaujtFViZBfgmcMHjd2F3tiUV/akQUU1doYQBGQKBgEeWCUsq\\nzM/SXUvDKzlqijCwD3PAaoXv3Pzurt4wLEsp42UPZ4TMYh4XWacBcADhrHmHv2Do\\n+dwg+JXAQlTM9vtIzhmV/nnJsW7c58q1c8JfRQ7T/9g2v4lb3iI2kS90u/dsVp9Y\\nu54IY55WKtkUtVZprnje6C8QVxR9eP9zdQO1AoGBANDpSE1w+UFHoiZbIYGqvRtV\\n5uvhTemuuNNXa3ZQJ8IV+Fo8oeKWOpIgmUEzoHsF5JIT/R8dLRfpsvjKdf+dEPP5\\nbmpH3r1h491me6uvDtEH6p1rxE1c2CKNnFcTj84AuHS8Bkahnug9YYzCXM5ehbEP\\ngqyLcWpP1w3wlhMA1BfG\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-ceb2o@my-application-697fb.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"111320660474748972527\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-ceb2o%40my-application-697fb.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}"


            val inputStream = ByteArrayInputStream(jsonString.toByteArray(StandardCharsets.UTF_8))
            val googleCredentials= GoogleCredentials.fromStream(inputStream)
                .createScoped(arrayListOf(firebaseMessagingScope))

            googleCredentials.refresh()

            return googleCredentials.accessToken.tokenValue

        }catch (e: IOException){
            return null
        }

    }
}

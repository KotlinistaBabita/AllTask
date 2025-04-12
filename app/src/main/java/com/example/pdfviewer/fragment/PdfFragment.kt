package com.example.pdfviewer.fragment

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.example.pdfviewer.R
import com.shockwave.pdfium.PdfiumCore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.graphics.createBitmap
import androidx.lifecycle.lifecycleScope
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class PdfFragment : Fragment() {

    private lateinit var imagePdf: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pdf, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imagePdf = view.findViewById(R.id.pdfPageImage)

        val pdfUrl = "https://fssservices.bookxpert.co/GeneratedPDF/Companies/nadc/2024-2025/BalanceSheet.pdf"
        lifecycleScope.launch {
            val pdfFile = downloadPdf(pdfUrl)
            pdfFile?.let { file ->
                renderPdfPage(file, 0, imagePdf)  // Render the first page
            }
        }
    }
    private suspend fun downloadPdf(pdfUrl: String): File? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(pdfUrl)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.inputStream.use { inputStream ->
                    val file = File(requireContext().cacheDir, "downloaded_pdf.pdf")
                    val outputStream = FileOutputStream(file)
                    inputStream.copyTo(outputStream)
                    outputStream.flush()
                    file
                }
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }

    private fun renderPdfPage(file: File, pageIndex: Int, imageView: ImageView) {
        val parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfRenderer = PdfRenderer(parcelFileDescriptor)

        if (pageIndex < pdfRenderer.pageCount) {
            val page = pdfRenderer.openPage(pageIndex)
            val bitmap = createBitmap(page.width, page.height)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            imageView.setImageBitmap(bitmap)
            page.close()
        }

        pdfRenderer.close()
    }
}
package com.github.krxwl.codecat

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.krxwl.codecat.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.MultiBrowseCarouselStrategy
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


private val API_KEY_GOOGLE_BOOKS = "AIzaSyBATmMX8JWe5S2HnyM_A09g46aQzx503xI"
private const val TAG = "BottomSheet"

class BottomSheet(course: Course) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetBinding
    private val currentCourse = course
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetBinding.inflate(layoutInflater)
        binding.languageImageview.setImageBitmap(currentCourse.image)
        binding.languageName.text = currentCourse.name
        binding.languageDescription.text = currentCourse.description
        // TODO("НОВАЯ КОЛОНКА В ТАБЛИЦЕ С ПРОГРЕССОМ")
        binding.progressTextview.text = "${currentCourse.progress}/15"

        getBooksId(currentCourse.name.toString())

        val carouselLayoutManager = CarouselLayoutManager()
        carouselLayoutManager.setCarouselStrategy(MultiBrowseCarouselStrategy())
        binding.carouselRecyclerView.layoutManager = carouselLayoutManager

        return binding.root
    }

    fun getBooksId(languageName: String) {
        val client = OkHttpClient()
        val url =
            "https://www.googleapis.com/books/v1/volumes?q=${languageName}книги+intitle:${languageName}&maxResults=10&startIndex=0&key=${API_KEY_GOOGLE_BOOKS}"
        val request: Request = Request.Builder().url(url).build()
        val handler = Handler(Looper.getMainLooper())

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i(TAG, "exception ${e}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                handler.post {
                    val books = ArrayList<Book>()
                    Log.i(TAG, "получил ${response.body}")
                }
            }
        })

    }


    inner class CarouselAdapter(private var books: ArrayList<Book>) : ListAdapter<Book, CarouselAdapter.BookHolder>(BookDiffCallback()) {
        override fun onBindViewHolder(holder: CarouselAdapter.BookHolder, position: Int) {
            val book = books[position]
            holder.bind(book)
        }

        override fun getItemCount(): Int = books.size

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CarouselAdapter.BookHolder {
            return BookHolder(layoutInflater.inflate(R.layout.carousel_item, parent, false))
        }

        inner class BookHolder(view: View?) : RecyclerView.ViewHolder(view!!), View.OnClickListener {

            private lateinit var book: Book

            private val nameTextView: TextView = itemView.findViewById(R.id.book_name)
            private val imageButton: ImageView = itemView.findViewById(R.id.book_picture)
            private val authorTextView: TextView = itemView.findViewById(R.id.author_textview)

            init {
                itemView.setOnClickListener(this)
            }

            fun bind(book: Book) {
                this.book = book
                nameTextView.text = this.book.name
                authorTextView.text = this.book.author
                nameTextView.visibility = View.GONE
                authorTextView.visibility = View.GONE
                imageButton.setImageBitmap(this.book.picture)
            }

            override fun onClick(p0: View?) {
                //callbacks?.onCourseSelected(this.book)
            }
        }
    }

    class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            val (author, name, _) = oldItem
            val (author1, name1, _) = newItem

            return author == author1 && name == name1
        }

        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.name == newItem.name
        }
    }

    companion object {
        const val TAG = "BottomSheet"
    }
}

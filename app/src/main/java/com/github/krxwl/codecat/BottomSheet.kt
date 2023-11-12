package com.github.krxwl.codecat

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.github.krxwl.codecat.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.MultiBrowseCarouselStrategy
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


private const val API_KEY_GOOGLE_BOOKS = "AIzaSyBATmMX8JWe5S2HnyM_A09g46aQzx503xI"
private const val TAG = "BottomSheet"

class BottomSheet(course: Course) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetBinding
    private val currentCourse = course
    private val client = OkHttpClient()
    val id = ArrayList<String>()
    val handler = Handler(Looper.getMainLooper())
    var books: MutableLiveData<ArrayList<Book>> = MutableLiveData<ArrayList<Book>>()
    var booksArrayList = ArrayList<Book>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetBinding.inflate(layoutInflater, container, false)
        binding.languageImageview.setImageBitmap(currentCourse.image)
        binding.languageName.text = currentCourse.name
        binding.languageDescription.text = currentCourse.description
        // TODO("НОВАЯ КОЛОНКА В ТАБЛИЦЕ С ПРОГРЕССОМ")
        binding.progressTextview.text = "${currentCourse.progress}/15"

        getBooksId(currentCourse.name.toString())

        books.observe(this) { books ->
            val adapter = CarouselAdapter(ArrayList(books))
            adapter.submitList(ArrayList(books))
            binding.carouselRecyclerView.adapter = adapter
           // binding.loading.visibility = View.GONE
        }

        val carouselLayoutManager = CarouselLayoutManager()
        carouselLayoutManager.setCarouselStrategy(MultiBrowseCarouselStrategy())
        binding.carouselRecyclerView.layoutManager = carouselLayoutManager

        return binding.root
    }

    private fun getBooksId(languageName: String) {
        val url =
            "https://www.googleapis.com/books/v1/volumes?q=${languageName}книги+intitle:${languageName}&maxResults=10&startIndex=0&key=${API_KEY_GOOGLE_BOOKS}"
        val request: Request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i(TAG, "exception $e")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                handler.post {
                    val gson = Gson()
                    Log.i(TAG, "пометка, ${responseBody}")
                    val map = gson.fromJson(responseBody, Map::class.java)["items"] as List<*>

                    for (i in map) {
                        for ((key, value) in (i as Map<*, *>)) {
                            if (key.toString() == "id") {
                                id.add(value.toString())
                            }
                        }
                    }
                    for (i in id) {
                        getBook(i)
                    }
                    Log.i(TAG, "АДАПТЕР УСТАНОВЛЕН")
                }
            }
        })
    }

    fun getBook(id: String) {
        val url = "https://www.googleapis.com/books/v1/volumes/${id}?key=${API_KEY_GOOGLE_BOOKS}"
        val request: Request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i(TAG, "exception $e")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                handler.post {
                    val gson = Gson()

                    var map = gson.fromJson(responseBody, Map::class.java) as Map<*, *>
                    map = map["volumeInfo"] as Map<*, *>
                    try {
                        val author = (map["authors"] as List<*>)[0] as String
                        val title = map["title"] as String
                        val image = (map["imageLinks"] as Map<*, *>)["medium"]
                        Glide.with(context!!)
                            .asBitmap()
                            .load(image)
                            .into(object : CustomTarget<Bitmap>() {
                                override fun onLoadFailed(errorDrawable: Drawable?) {
                                    Log.i(TAG, "ошибка загрузки")
                                }

                                override fun onResourceReady(resource: Bitmap,
                                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                                ) {
                                    booksArrayList.add(Book(author = author, name = title, picture = resource))
                                    books.value = booksArrayList
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {}
                            })

                        Log.i(TAG, "что мы получили $author $title $image")
                    } catch (ex: NullPointerException) {
                        Log.i(TAG, "нет картинки, скипаем")
                    }
                    //books.add(Book())
                }
            }
        })
    }


    inner class CarouselAdapter(private var books: ArrayList<Book>) : ListAdapter<Book, CarouselAdapter.BookHolder>(BookDiffCallback()) {
        override fun onBindViewHolder(holder: CarouselAdapter.BookHolder, position: Int) {
            Log.i(TAG, "заполняем")
            val book = books[position]
            holder.bind(book)
        }

        override fun getItemCount(): Int = books.size

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BookHolder {
            Log.i(TAG, "заполняем")
            return BookHolder(layoutInflater.inflate(R.layout.book_carousel_item, parent, false))
        }

        inner class BookHolder(view: View?) : RecyclerView.ViewHolder(view!!), View.OnClickListener {

            private lateinit var book: Book

            private val darkeningBlock: View = itemView.findViewById(R.id.darkening_block)
            private val nameTextView: TextView = itemView.findViewById(R.id.book_name)
            private val imageView: ImageView = itemView.findViewById(R.id.book_picture)
            private val authorTextView: TextView = itemView.findViewById(R.id.author_textview)

            init {
                itemView.setOnClickListener(this)
            }

            fun bind(book: Book) {
                this.book = book
                nameTextView.text = this.book.name
                authorTextView.text = this.book.author
                nameTextView.visibility = View.INVISIBLE
                authorTextView.visibility = View.INVISIBLE
                darkeningBlock.visibility = View.INVISIBLE
                darkeningBlock.alpha = 0.8f
                imageView.setImageBitmap(this.book.picture)
            }

            override fun onClick(p0: View?) {
                val fadeIn = AlphaAnimation(0f, 2.5f)
                fadeIn.interpolator = DecelerateInterpolator()
                fadeIn.duration = 7000

                val fadeOut = AlphaAnimation(2.5f, 0f)
                fadeOut.interpolator = AccelerateInterpolator()
                fadeOut.duration = 3000

                val animation = AnimationSet(false)
                animation.addAnimation(fadeIn)
                animation.addAnimation(fadeOut)

                darkeningBlock.visibility = View.VISIBLE
                nameTextView.visibility = View.VISIBLE
                authorTextView.visibility = View.VISIBLE
                darkeningBlock.startAnimation(animation)
                nameTextView.startAnimation(animation)
                authorTextView.startAnimation(animation)

                darkeningBlock.visibility = View.INVISIBLE
                nameTextView.visibility = View.INVISIBLE
                authorTextView.visibility = View.INVISIBLE

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

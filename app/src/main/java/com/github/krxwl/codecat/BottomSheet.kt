package com.github.krxwl.codecat

import android.os.Bundle
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
import com.mikhaellopez.circularprogressbar.CircularProgressBar

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

        var carouselLayoutManager = CarouselLayoutManager()
        carouselLayoutManager.setCarouselStrategy(MultiBrowseCarouselStrategy())
        binding.carouselRecyclerView.layoutManager = carouselLayoutManager

        return binding.root
    }

    inner class CarouselAdapter(var books: ArrayList<Book>) : ListAdapter<Book, CarouselAdapter.BookHolder>(BookDiffCallback()) {
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


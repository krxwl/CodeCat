package com.github.krxwl.codecat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.krxwl.codecat.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.MultiBrowseCarouselStrategy

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

    companion object {
        const val TAG = "BottomSheet"
    }
}


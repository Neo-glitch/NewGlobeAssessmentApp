package com.bridge.androidtechnicaltest.core.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bridge.androidtechnicaltest.core.BaseBottomSheetFragment
import com.bridge.androidtechnicaltest.core.presentation.RecyclerViewItemSpaceDecorator
import com.bridge.androidtechnicaltest.core.presentation.model.ImageSourceType
import com.bridge.androidtechnicaltest.core.presentation.adapter.ImageSourceAdapter
import com.bridge.androidtechnicaltest.core.utils.toPx
import com.bridge.androidtechnicaltest.databinding.BottomsheetImageSourceBinding

class ImageSourceBottomSheet: BaseBottomSheetFragment<BottomsheetImageSourceBinding>() {

    private var onImageSourceSelected: ((ImageSourceType) -> Unit)? = null
    private lateinit var imageSourceAdapter: ImageSourceAdapter

    companion object {
        fun newInstance(onImageSourceSelected: (ImageSourceType) -> Unit): ImageSourceBottomSheet {
            val fragment = ImageSourceBottomSheet()
            fragment.onImageSourceSelected = onImageSourceSelected
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return initBinding(
            BottomsheetImageSourceBinding.inflate(inflater, container, false),
            this
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        requireBinding().apply {
            setBehaviour(isCancellable = true)
            closeIcon.setOnClickListener {
                dismiss()
            }

            imageSourceAdapter = ImageSourceAdapter(
                onItemClicked = {
                    onImageSourceSelected?.invoke(it)
                    dismiss()
                }
            )

            imageSourceList.adapter = imageSourceAdapter
            imageSourceList.addItemDecoration(RecyclerViewItemSpaceDecorator(verticalSpacing = 16.toPx))
        }
    }
}


package com.bridge.androidtechnicaltest.pupil.presentation.pupils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bridge.androidtechnicaltest.core.BaseFragment
import com.bridge.androidtechnicaltest.core.presentation.RecyclerViewItemSpaceDecorator
import com.bridge.androidtechnicaltest.core.utils.launchScope
import com.bridge.androidtechnicaltest.core.utils.toPx
import com.bridge.androidtechnicaltest.databinding.FragmentPupillistBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PupilListFragment : BaseFragment<FragmentPupillistBinding>() {

    private val viewModel: PupilListViewModel by viewModel()
    private lateinit var pupilsAdapter: PupilsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initBinding(
            FragmentPupillistBinding.inflate(inflater, container, false),
            this
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeUi()
    }

    private fun observeUi() {
        launchScope {
            launch {
                viewModel.uiState.collect { state ->

                }
            }

        }
    }

    private fun initView() {
        requireBinding().apply {
            pupilsAdapter = PupilsAdapter(
                onItemClick = { pupil ->
                    navigate(
                        PupilListFragmentDirections.actionPupilListFragmentToPupilDetailFragment(
//                            pupil.pupilId
                        )
                    )
                }
            )
            pupilList.adapter = pupilsAdapter
            pupilList.addItemDecoration(RecyclerViewItemSpaceDecorator(verticalSpacing = 4.toPx))

            addPupilBtn.setOnClickListener {
                navigate(
                    PupilListFragmentDirections.actionPupilListFragmentToAddEditPupilDetailFragment()
                )
            }
        }
    }
}
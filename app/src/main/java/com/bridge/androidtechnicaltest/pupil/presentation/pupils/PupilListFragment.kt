package com.bridge.androidtechnicaltest.pupil.presentation.pupils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bridge.androidtechnicaltest.R
import com.bridge.androidtechnicaltest.core.BaseFragment
import com.bridge.androidtechnicaltest.core.presentation.RecyclerViewItemSpaceDecorator
import com.bridge.androidtechnicaltest.core.utils.launchScope
import com.bridge.androidtechnicaltest.core.utils.showWarningToastMessage
import com.bridge.androidtechnicaltest.core.utils.toPx
import com.bridge.androidtechnicaltest.databinding.FragmentPupillistBinding
import com.bridge.androidtechnicaltest.pupil.util.PupilSyncWorkManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PupilListFragment : BaseFragment<FragmentPupillistBinding>() {

    private val viewModel: PupilListViewModel by viewModel()
    private lateinit var pupilsAdapter: PupilsAdapter
    private var isSwipeToRefreshLoading = false

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
       if (!isGranted) {
           showWarningToastMessage("Notification permission is required to show notifications")
       }
    }

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
                viewModel.pupils.collectLatest {
                    pupilsAdapter.submitData(pagingData = it)
                }
            }

            launch {
                viewModel.uiState.collectLatest { state ->
//                    state.data.collectLatest {
//                        pupilsAdapter.submitData(lifecycle = lifecycle, pagingData = it)
//                    }


                    when (state.loadState) {
                        PupilsLoadState.Empty -> handleEmptyState()
                        is PupilsLoadState.Error -> handleErrorState(state.loadState.message)
                        PupilsLoadState.Idle -> handleIdleState()
                        PupilsLoadState.InitialLoading -> handleInitialLoadState()
                        PupilsLoadState.Refreshing -> handleRefreshingState()
                        PupilsLoadState.Success -> handleSuccessState()
                    }
                }
            }

            launch {
                pupilsAdapter.loadStateFlow.collectLatest { loadStates ->
                    viewModel.onLoadStateChanged(loadStates = loadStates, itemCount = pupilsAdapter.itemCount)
                }
            }

            launch {
                viewModel.pupils.collectLatest {
                    pupilsAdapter.submitData(lifecycle = lifecycle, pagingData = it)
                }
            }
        }
    }

    private fun handleEmptyState() {
        requireBinding().apply {
            swipeRefreshLayout.isEnabled = false
            swipeRefreshLayout.isRefreshing = false
            isSwipeToRefreshLoading = false

            emptyErrorView.visibility = View.VISIBLE
            pupilList.visibility = View.GONE
            progressLoader.visibility = View.GONE
            retryBtn.visibility = View.GONE
            errorTitle.text = getString(R.string.empty_pupils_title_label)
            errorDescription.text = getString(R.string.empty_pupils_description_label)
        }
    }

    private fun handleErrorState(message: String) {
        requireBinding().apply {
            swipeRefreshLayout.isEnabled = false
            swipeRefreshLayout.isRefreshing = false
            isSwipeToRefreshLoading = false

            emptyErrorView.visibility = View.VISIBLE
            pupilList.visibility = View.GONE
            progressLoader.visibility = View.GONE
            retryBtn.visibility = View.VISIBLE
            errorTitle.text = getString(R.string.error_title_label)
            errorDescription.text = message
            retryBtn.setOnClickListener {
                pupilsAdapter.retry()
            }
        }
    }

    private fun handleRefreshingState() {
        requireBinding().apply {
            swipeRefreshLayout.isEnabled = false
            swipeRefreshLayout.isRefreshing = true
            isSwipeToRefreshLoading = true

            emptyErrorView.visibility = View.GONE
            pupilList.visibility = View.VISIBLE
            progressLoader.visibility = View.GONE
            retryBtn.visibility = View.GONE
        }
    }

    private fun handleIdleState() {
        requireBinding().apply {
            swipeRefreshLayout.isEnabled = true
            swipeRefreshLayout.isRefreshing = false
            isSwipeToRefreshLoading = false
            emptyErrorView.visibility = View.GONE
            retryBtn.visibility = View.GONE
            pupilList.visibility = View.VISIBLE
            progressLoader.visibility = View.GONE
        }
    }

    private fun handleInitialLoadState() {
        requireBinding().apply {
            swipeRefreshLayout.isEnabled = isSwipeToRefreshLoading
            swipeRefreshLayout.isRefreshing = false
            isSwipeToRefreshLoading = false

            retryBtn.visibility = View.GONE
            emptyErrorView.visibility = View.GONE
            pupilList.visibility = View.GONE
            progressLoader.visibility = View.VISIBLE
        }
    }

    private fun handleSuccessState() {
        requireBinding().apply {
            swipeRefreshLayout.isEnabled = true
            swipeRefreshLayout.isRefreshing = false
            isSwipeToRefreshLoading = false

            retryBtn.visibility = View.GONE
            emptyErrorView.visibility = View.GONE
            pupilList.visibility = View.VISIBLE
            progressLoader.visibility = View.GONE
        }
    }

    private fun initView() {
        requireBinding().apply {
            checkAndRequestNotificationPermission()
            pupilsAdapter = PupilsAdapter(
                onItemClick = { pupil ->
                    navigate(
                        PupilListFragmentDirections.actionPupilListFragmentToPupilDetailFragment(
                            pupil.id
                        )
                    )
                }
            )
            val loadStateAdapter = PupilsLoadStateAdapter { pupilsAdapter.retry() }
            pupilList.adapter = pupilsAdapter.withLoadStateFooter(
                footer = loadStateAdapter
            )
            pupilList.addItemDecoration(RecyclerViewItemSpaceDecorator(verticalSpacing = 4.toPx))

            addPupilBtn.setOnClickListener {
                navigate(
                    PupilListFragmentDirections.actionPupilListFragmentToAddEditPupilDetailFragment()
                )
            }

            showMenuIv.setOnClickListener {
                displayPopupMenu(it)
            }

            swipeRefreshLayout.setOnRefreshListener {
                if (viewModel.uiState.value.loadState != PupilsLoadState.InitialLoading){
                    swipeRefreshLayout.isRefreshing = true
                    pupilsAdapter.refresh()
                }
            }
        }
    }

    private fun displayPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.main_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_sync -> {
                    PupilSyncWorkManager(requireContext().applicationContext).enqueOneTimePupilSyncWork()
                    true
                }
                else -> false
            }
        }
        popupMenu.gravity = Gravity.BOTTOM
        popupMenu.show()
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isNotificationPermissionGranted = ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!isNotificationPermissionGranted) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
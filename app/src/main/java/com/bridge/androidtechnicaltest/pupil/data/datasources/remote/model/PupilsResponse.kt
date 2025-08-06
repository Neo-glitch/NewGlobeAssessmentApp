package com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model

data class PupilsResponse(
	val pageNumber: Int,
	val totalPages: Int,
	val items: List<RemotePupil>? = null,
	val itemCount: Int
)




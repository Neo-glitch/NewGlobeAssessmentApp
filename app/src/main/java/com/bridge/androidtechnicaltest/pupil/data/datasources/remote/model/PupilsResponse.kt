package com.bridge.androidtechnicaltest.pupil.data.datasources.remote.model

data class PupilsResponse(
	val pageNumber: Int? = null,
	val totalPages: Int? = null,
	val items: List<RemotePupil>? = null,
	val itemCount: Int? = null
)




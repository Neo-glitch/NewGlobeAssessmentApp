package com.bridge.androidtechnicaltest

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class MyAppGlideModule : AppGlideModule() {
    override fun applyOptions(
        context: Context,
        builder: GlideBuilder
    ) {
        val cacheSizeBytes = 1024 * 1024 * 50
        builder.setDiskCache(
            InternalCacheDiskCacheFactory(context, cacheSizeBytes.toLong())
        )
    }
}


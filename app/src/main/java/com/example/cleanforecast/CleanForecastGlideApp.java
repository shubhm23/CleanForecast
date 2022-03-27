package com.example.cleanforecast;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

@GlideModule
public class CleanForecastGlideApp extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
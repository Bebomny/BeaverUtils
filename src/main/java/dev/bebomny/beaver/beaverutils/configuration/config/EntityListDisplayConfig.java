package dev.bebomny.beaver.beaverutils.configuration.config;

import com.google.gson.annotations.Expose;
import dev.bebomny.beaver.beaverutils.features.features.EntityListDisplay;

public class EntityListDisplayConfig extends EnableConfigOption{

    @Expose
    public int searchRadius = 128;

    @Expose
    public int onScreenPosX = 30;

    @Expose
    public int onScreenPosY = 80;

    @Expose
    public int maxDisplayedExpandableEntityInfoEntryAmount = 5;

    @Expose
    public int minNameColumnWidth = 100;

    @Expose
    public int minCountColumnWidth = 40;

    @Expose
    public int minViewportCountColumnWidth = 40;

    @Expose
    public int minDistanceColumnWidth = 40;

    @Expose
    public EntityListDisplay.EntrySortBy entrySortBy = EntityListDisplay.EntrySortBy.COUNT;

    public EntityListDisplayConfig() {

    }
}

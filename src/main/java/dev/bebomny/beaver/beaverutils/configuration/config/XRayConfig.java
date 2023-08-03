package dev.bebomny.beaver.beaverutils.configuration.config;

import com.google.gson.annotations.Expose;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.Collection;

public class XRayConfig {

    @Expose
    public boolean enable = false;

    @Expose
    public Collection<String> interestingBlocksAsCollection = new ArrayList<>();


    public XRayConfig() {

    }
}
